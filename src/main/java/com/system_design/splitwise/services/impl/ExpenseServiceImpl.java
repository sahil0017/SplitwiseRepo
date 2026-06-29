package com.system_design.splitwise.services.impl;

import com.system_design.splitwise.DTO.AddExpenseRequest;
import com.system_design.splitwise.DTO.SplitRequest;
import com.system_design.splitwise.Entities.Expense;
import com.system_design.splitwise.Entities.ExpenseGroup;
import com.system_design.splitwise.Entities.ExpenseSplit;
import com.system_design.splitwise.Entities.User;
import com.system_design.splitwise.Repository.ExpenseGroupRepository;
import com.system_design.splitwise.Repository.ExpenseRepository;
import com.system_design.splitwise.Repository.ExpenseSplitRepository;
import com.system_design.splitwise.Repository.UserRepository;
import com.system_design.splitwise.services.BalanceService;
import com.system_design.splitwise.services.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseSplitRepository expenseSplitRepository;
    private final UserRepository userRepository;
    private final ExpenseGroupRepository groupRepository;
    private final BalanceService balanceService;

    @Override
    public Expense addExpense(AddExpenseRequest request) {

        validateRequest(request);
        validateUsers(request);

        // =========================
        // 1. FETCH ENTITIES
        // =========================
        ExpenseGroup group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User paidBy = userRepository.findById(request.getPaidBy())
                .orElseThrow(() -> new RuntimeException("PaidBy user not found"));

        // =========================
        // 2. CREATE EXPENSE
        // =========================
        Expense expense = Expense.builder()
                .id(UUID.randomUUID())
                .group(group)
                .paidBy(paidBy)
                .description(request.getDescription())
                .amount(request.getAmount())
                .splitType(request.getSplitType())
                .createdAt(LocalDateTime.now())
                .build();

        expenseRepository.save(expense);

        // =========================
        // 3. CALCULATE SPLITS
        // =========================
        List<ExpenseSplit> splits = new ArrayList<>();

        switch (request.getSplitType()) {

            case EQUAL -> {

                BigDecimal splitAmount = request.getAmount()
                        .divide(BigDecimal.valueOf(request.getSplits().size()),
                                2,
                                RoundingMode.HALF_UP);

                BigDecimal distributed = BigDecimal.ZERO;
                int size = request.getSplits().size();

                for (int i = 0; i < size; i++) {

                    SplitRequest s = request.getSplits().get(i);

                    BigDecimal amount = (i == size - 1)
                            ? request.getAmount().subtract(distributed)
                            : splitAmount;

                    distributed = distributed.add(amount);

                    splits.add(buildSplit(expense, s.getUser(), amount));
                }
            }

            case EXACT -> {

                BigDecimal sum = request.getSplits().stream()
                        .map(SplitRequest::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                if (sum.compareTo(request.getAmount()) != 0) {
                    throw new RuntimeException("Exact split must match total amount");
                }

                for (SplitRequest s : request.getSplits()) {
                    splits.add(buildSplit(expense, s.getUser(), s.getAmount()));
                }
            }

            case PERCENTAGE -> {

                BigDecimal totalPercent = request.getSplits().stream()
                        .map(SplitRequest::getPercentage)
                        .map(BigDecimal::valueOf)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                if (totalPercent.compareTo(BigDecimal.valueOf(100)) != 0) {
                    throw new RuntimeException("Percent must equal 100");
                }

                for (SplitRequest s : request.getSplits()) {

                    BigDecimal amount = request.getAmount()
                            .multiply(BigDecimal.valueOf(s.getPercentage()))
                            .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);

                    splits.add(buildSplit(expense, s.getUser(), amount));
                }
            }
        }
        // =========================
        // 4. SAVE SPLITS
        // =========================
        expenseSplitRepository.saveAll(splits);

        // =========================
        // 5. UPDATE LEDGER
        // =========================
        updateBalances(expense, splits);

        return expense;
    }

    @Override
    public Expense getExpense(UUID expenseId) {
        return expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
    }

    @Override
    public List<Expense> getGroupExpenses(UUID groupId) {
        return expenseRepository.findByGroupId(groupId);
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private void validateRequest(AddExpenseRequest request) {

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        if (request.getSplits() == null || request.getSplits().isEmpty()) {
            throw new RuntimeException("Splits cannot be empty");
        }
    }

    private void validateUsers(AddExpenseRequest request) {

        Set<UUID> userIds = new HashSet<>();

        // paidBy is User
        userIds.add(request.getPaidBy());

        for (SplitRequest s : request.getSplits()) {
            userIds.add(s.getUser().getId());
        }

        for (UUID userId : userIds) {
            if (!userRepository.existsById(userId)) {
                throw new RuntimeException("User not found: " + userId);
            }
        }
    }

    private ExpenseSplit buildSplit(Expense expense, User user, BigDecimal amount) {

        return ExpenseSplit.builder()
                .id(UUID.randomUUID())
                .expense(expense)
                .user(user)
                .amount(amount)
                .build();
    }

    private void updateBalances(Expense expense, List<ExpenseSplit> splits) {

        UUID paidById = expense.getPaidBy().getId();
        UUID groupId = expense.getGroup().getId();

        for (ExpenseSplit split : splits) {

            UUID userId = split.getUser().getId();

            if (!userId.equals(paidById)) {

                balanceService.recordTransaction(
                        userId,            // debtor
                        paidById,          // creditor
                        split.getAmount(),
                        groupId
                );
            }
        }
    }
}