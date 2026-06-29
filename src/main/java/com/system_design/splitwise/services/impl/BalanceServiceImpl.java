package com.system_design.splitwise.services.impl;

import com.system_design.splitwise.DTO.SettlementRequest;
import com.system_design.splitwise.DTO.SettlementResponse;
import com.system_design.splitwise.DTO.TransactionResponse;
import com.system_design.splitwise.DTO.UserBalanceResponse;
import com.system_design.splitwise.Entities.Balance;
import com.system_design.splitwise.Entities.ExpenseGroup;
import com.system_design.splitwise.Entities.User;
import com.system_design.splitwise.Repository.BalanceRepository;
import com.system_design.splitwise.Repository.ExpenseGroupRepository;
import com.system_design.splitwise.Repository.UserRepository;
import com.system_design.splitwise.services.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;
    private final UserRepository userRepository;
    private final ExpenseGroupRepository expenseGroupRepository;

    @Override
    public UserBalanceResponse getUserBalance(UUID groupId, UUID userId) {

        List<Balance> balances = balanceRepository.findByGroup_Id(groupId);

        BigDecimal totalOwed = BigDecimal.ZERO;
        BigDecimal totalReceivable = BigDecimal.ZERO;

        Map<UUID, BigDecimal> netMap = new HashMap<>();

        // =========================
        // 1. COMPUTE NET BALANCE
        // =========================
        for (Balance b : balances) {

            UUID creditorId = b.getCreditor().getId();
            UUID debtorId = b.getDebtor().getId();
            BigDecimal amount = b.getAmount();

            if (debtorId.equals(userId)) {
                totalOwed = totalOwed.add(amount);

                netMap.merge(creditorId,
                        amount.negate(),
                        BigDecimal::add);
            }

            if (creditorId.equals(userId)) {
                totalReceivable = totalReceivable.add(amount);

                netMap.merge(debtorId,
                        amount,
                        BigDecimal::add);
            }
        }

        // =========================
        // 2. BUILD TRANSACTIONS
        // =========================
        List<TransactionResponse> transactions = new ArrayList<>();

        for (Map.Entry<UUID, BigDecimal> entry : netMap.entrySet()) {

            UUID otherUserId = entry.getKey();
            BigDecimal amount = entry.getValue();

            TransactionResponse tr = new TransactionResponse();

            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                // user owes otherUser
                tr.setFrom(userId.toString());
                tr.setTo(otherUserId.toString());
                tr.setAmount(amount.abs());
            } else if (amount.compareTo(BigDecimal.ZERO) > 0) {
                // otherUser owes user
                tr.setFrom(otherUserId.toString());
                tr.setTo(userId.toString());
                tr.setAmount(amount);
            }

            // ignore zero balances
            if (amount.compareTo(BigDecimal.ZERO) != 0) {
                transactions.add(tr);
            }
        }

        // =========================
        // 3. RESPONSE
        // =========================
        return UserBalanceResponse.builder()
                .userId(userId)
                .totalOwed(totalOwed)
                .totalReceivable(totalReceivable)
                .transactions(transactions)
                .build();
    }


    @Override
    public List<TransactionResponse> simplifyDebts(UUID groupId) {

        List<Balance> balances = balanceRepository.findByGroup_Id(groupId);

        Map<UUID, BigDecimal> net = new HashMap<>();

        // =========================
        // 1. BUILD NET BALANCE
        // =========================
        for (Balance b : balances) {

            UUID creditorId = b.getCreditor().getId();
            UUID debtorId = b.getDebtor().getId();
            BigDecimal amount = b.getAmount();

            net.merge(debtorId, amount.negate(), BigDecimal::add);
            net.merge(creditorId, amount, BigDecimal::add);
        }

        // =========================
        // 2. SPLIT USERS
        // =========================
        List<Map.Entry<UUID, BigDecimal>> creditors = new ArrayList<>();
        List<Map.Entry<UUID, BigDecimal>> debtors = new ArrayList<>();

        for (Map.Entry<UUID, BigDecimal> entry : net.entrySet()) {

            if (entry.getValue().compareTo(BigDecimal.ZERO) > 0) {
                creditors.add(entry);
            } else if (entry.getValue().compareTo(BigDecimal.ZERO) < 0) {
                debtors.add(entry);
            }
        }

        // sort (greedy optimization)
        creditors.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        debtors.sort((a, b) -> a.getValue().compareTo(b.getValue()));

        // =========================
        // 3. GREEDY SETTLEMENT
        // =========================
        List<TransactionResponse> result = new ArrayList<>();

        int i = 0, j = 0;

        while (i < debtors.size() && j < creditors.size()) {

            Map.Entry<UUID, BigDecimal> debtor = debtors.get(i);
            Map.Entry<UUID, BigDecimal> creditor = creditors.get(j);

            BigDecimal debt = debtor.getValue().abs();
            BigDecimal credit = creditor.getValue();

            BigDecimal settled = debt.min(credit);

            TransactionResponse tr = TransactionResponse.builder()
                    .from(debtor.getKey().toString())
                    .to(creditor.getKey().toString())
                    .amount(settled)
                    .build();

            result.add(tr);

            // update remaining balances
            debtor.setValue(debtor.getValue().add(settled));
            creditor.setValue(creditor.getValue().subtract(settled));

            if (debtor.getValue().compareTo(BigDecimal.ZERO) == 0) i++;
            if (creditor.getValue().compareTo(BigDecimal.ZERO) == 0) j++;
        }

        return result;
    }

    @Override
    public void recordTransaction(UUID debtorId,
                                  UUID creditorId,
                                  BigDecimal amount,
                                  UUID groupId) {

        // =========================
        // 1. FETCH ENTITIES
        // =========================
        User debtor = userRepository.findById(debtorId)
                .orElseThrow(() -> new RuntimeException("Debtor not found"));

        User creditor = userRepository.findById(creditorId)
                .orElseThrow(() -> new RuntimeException("Creditor not found"));

        ExpenseGroup group = expenseGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        // =========================
        // 2. CHECK EXISTING BALANCE
        // =========================
        Optional<Balance> existing =
                balanceRepository.findByGroupIdAndCreditorIdAndDebtorId(
                        group.getId(),
                        creditorId,
                        debtorId
                );

        if (existing.isPresent()) {

            Balance balance = existing.get();

            // increase existing debt
            balance.setAmount(balance.getAmount().add(amount));

            balanceRepository.save(balance);

        } else {

            // =========================
            // 3. CREATE NEW BALANCE
            // =========================
            Balance balance = Balance.builder()
                    .id(UUID.randomUUID())
                    .group(group)
                    .debtor(debtor)
                    .creditor(creditor)
                    .amount(amount)
                    .build();

            balanceRepository.save(balance);
        }
    }

    @Override
    public List<Balance> getGroupBalances(UUID groupId) {
        return balanceRepository.findByGroup_Id(groupId);
    }

    @Override
    public SettlementResponse settle(SettlementRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Invalid settlement amount");
        }

        // =========================
        // 1. FIND EXISTING BALANCE
        // =========================
        Balance balance = balanceRepository
                .findByGroupIdAndCreditorIdAndDebtorId(
                        request.getGroupId(),
                        request.getToUserId(),
                        request.getFromUserId()
                )
                .orElse(null);

        if (balance == null) {
            throw new RuntimeException("No outstanding balance found");
        }

        // =========================
        // 2. UPDATE BALANCE
        // =========================
        if (balance.getAmount().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Settlement exceeds owed amount");
        }

        balance.setAmount(balance.getAmount().subtract(request.getAmount()));

        if (balance.getAmount().compareTo(BigDecimal.ZERO) == 0) {
            balanceRepository.delete(balance);
        } else {
            balanceRepository.save(balance);
        }

        // =========================
        // 3. RESPONSE
        // =========================
        return SettlementResponse.builder()
                .settlementId(UUID.randomUUID())
                .groupId(request.getGroupId())
                .fromUserId(request.getFromUserId())
                .toUserId(request.getToUserId())
                .amount(request.getAmount())
                .status("SETTLED")
                .build();
    }
}