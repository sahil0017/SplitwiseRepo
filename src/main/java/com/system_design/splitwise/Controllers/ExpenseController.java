package com.system_design.splitwise.Controllers;

import com.system_design.splitwise.DTO.AddExpenseRequest;
import com.system_design.splitwise.Entities.Expense;
import com.system_design.splitwise.services.ExpenseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Expense APIs", description = "APIs for expense management")
@AllArgsConstructor
@RequestMapping("/expense")
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping
    public Expense addExpense(@RequestBody AddExpenseRequest request) {
        return  expenseService.addExpense(request);
    }

    @GetMapping("/group/{groupId}")
    public List<Expense> getExpenses(@PathVariable("groupId") UUID groupId) {
        return expenseService.getGroupExpenses(groupId);
    }
}
