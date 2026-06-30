package com.system_design.splitwise.Controllers;

import com.system_design.splitwise.DTO.SettlementRequest;
import com.system_design.splitwise.DTO.SettlementResponse;
import com.system_design.splitwise.DTO.TransactionResponse;
import com.system_design.splitwise.DTO.UserBalanceResponse;
import com.system_design.splitwise.Entities.Balance;
import com.system_design.splitwise.services.BalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(
        name = "Balance Management",
        description = """
        APIs responsible for computing and maintaining balances within an expense group.

        Business Responsibilities:
        • Track who owes whom after every expense.
        • Calculate a user's total receivable and payable amount.
        • Retrieve all outstanding balances for a group.
        • Simplify debts to minimize settlement transactions.
        • Record new balance entries after expenses.
        • Settle outstanding debts between two users.
        """
)
@AllArgsConstructor
@RequestMapping("/balances")
public class BalanceController {
    private final BalanceService balanceService;

    @Operation(
            summary = "Retrieve all balances for a group",
            description = """
            Returns all outstanding balances maintained for an expense group.

            Business Logic:
            1. Fetches all balance records belonging to the group.
            2. Each balance represents the amount a debtor owes a creditor.
            3. Used by reporting and settlement APIs.
            """
    )
    @GetMapping("/group/{groupId}")
    public List<Balance> getBalances(@PathVariable("groupId") UUID groupId) {
        return balanceService.getGroupBalances(groupId);
    }

    @Operation(
            summary = "Get balance summary for a user",
            description = """
            Computes the complete financial position of a user inside an expense group.

            Business Logic:
            1. Reads every balance entry for the group.
            2. Calculates:
               • Total amount owed by the user.
               • Total amount receivable by the user.
            3. Consolidates balances against the same user.
            4. Returns only the effective payable/receivable transactions.
            """
    )
    @GetMapping("/group/{groupId}/user/{userId}")
    public UserBalanceResponse getUserBalance(@PathVariable("groupId") UUID groupId, @PathVariable("userId") UUID userId) {
        return balanceService.getUserBalance(groupId, userId);
    }

    @Operation(
            summary = "Simplify debts within a group",
            description = """
            Generates the minimum number of transactions required to settle all outstanding debts.

            Business Logic:
            1. Calculates the net balance of every member.
            2. Separates creditors and debtors.
            3. Uses a greedy algorithm to match the largest creditor with the largest debtor.
            4. Produces an optimized settlement plan with the fewest possible transactions.
            """
    )
    @GetMapping("/group/{groupId}/simplify")
    public List<TransactionResponse> simplify(@PathVariable("groupId") UUID groupId) {
        return balanceService.simplifyDebts(groupId);
    }

    @Operation(
            summary = "Record a settlement between two users",
            description = """
            Records a payment made by a debtor to a creditor and updates the outstanding balance.

            Business Logic:
            1. Validates the settlement amount.
            2. Verifies an outstanding balance exists.
            3. Reduces the outstanding debt.
            4. Removes the balance entry if the debt becomes zero.
            5. Returns settlement confirmation.
            """
    )
    @PostMapping("/settlement")
    public SettlementResponse settle(@RequestBody SettlementRequest request) {
        return balanceService.settle(request);
    }
}
