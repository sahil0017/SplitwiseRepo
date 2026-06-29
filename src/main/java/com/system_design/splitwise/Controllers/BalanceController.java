package com.system_design.splitwise.Controllers;

import com.system_design.splitwise.DTO.SettlementRequest;
import com.system_design.splitwise.DTO.SettlementResponse;
import com.system_design.splitwise.DTO.TransactionResponse;
import com.system_design.splitwise.DTO.UserBalanceResponse;
import com.system_design.splitwise.Entities.Balance;
import com.system_design.splitwise.services.BalanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Balance APIs", description = "APIs for balance management")
@AllArgsConstructor
@RequestMapping("/balances")
public class BalanceController {
    private final BalanceService balanceService;

    @GetMapping("/group/{groupId}")
    public List<Balance> getBalances(@PathVariable("groupId") UUID groupId) {
        return balanceService.getGroupBalances(groupId);
    }

    @GetMapping("/group/{groupId}/user/{userId}")
    public UserBalanceResponse getUserBalance(@PathVariable("groupId") UUID groupId, @PathVariable("userId") UUID userId) {
        return balanceService.getUserBalance(groupId, userId);
    }

    @GetMapping("/group/{groupId}/simplify")
    public List<TransactionResponse> simplify(@PathVariable("groupId") UUID groupId) {
        return balanceService.simplifyDebts(groupId);
    }

    @PostMapping("/settlement")
    public SettlementResponse settle(@RequestBody SettlementRequest request) {
        return balanceService.settle(request);
    }
}
