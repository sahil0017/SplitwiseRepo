package com.system_design.splitwise.Controllers;

import com.system_design.splitwise.DTO.AddExpenseRequest;
import com.system_design.splitwise.Entities.Expense;
import com.system_design.splitwise.services.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(
        name = "Expense Management",
        description = """
        APIs responsible for creating and retrieving expenses within an expense group.

        Business Responsibilities:
        • Add expenses paid by a group member.
        • Support multiple split strategies (Equal, Exact, Percentage).
        • Calculate each participant's share.
        • Update balances between creditors and debtors.
        • Retrieve all expenses belonging to a group.
        """
)
@AllArgsConstructor
@RequestMapping("/expense")
public class ExpenseController {
    private final ExpenseService expenseService;

    @Operation(
            summary = "Add a new expense",
            description = """
            Creates a new expense and updates group balances.

            Business Logic:
            1. Validates the expense request.
            2. Ensures the payer and all participants belong to the group.
            3. Selects the appropriate split strategy:
               • EQUAL
               • EXACT
               • PERCENTAGE
            4. Calculates each participant's share.
            5. Persists the expense and expense splits.
            6. Updates creditor-debtor balances for the group.
            7. Returns the created expense.
            """
    )
    @PostMapping
    public Expense addExpense(@RequestBody AddExpenseRequest request) {
        return  expenseService.addExpense(request);
    }

    @Operation(
            summary = "Retrieve all expenses for a group",
            description = """
            Returns every expense recorded for an expense group.

            Business Logic:
            1. Retrieves all expenses associated with the specified group.
            2. Includes payer information and split details.
            3. Returns expenses in chronological order.
            """
    )
    @GetMapping("/group/{groupId}")
    public List<Expense> getExpenses(@PathVariable("groupId") UUID groupId) {
        return expenseService.getGroupExpenses(groupId);
    }
}
