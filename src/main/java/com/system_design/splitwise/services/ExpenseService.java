package com.system_design.splitwise.services;

import com.system_design.splitwise.DTO.AddExpenseRequest;
import com.system_design.splitwise.Entities.Expense;

import java.util.List;
import java.util.UUID;

public interface ExpenseService {

    Expense addExpense(AddExpenseRequest request);

    Expense getExpense(UUID expenseId);

    List<Expense> getGroupExpenses(UUID groupId);

}
