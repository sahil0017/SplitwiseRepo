package com.system_design.splitwise.services;

import com.system_design.splitwise.DTO.*;
import com.system_design.splitwise.Entities.Balance;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface BalanceService {

    UserBalanceResponse getUserBalance(UUID groupId, UUID userId);

    List<TransactionResponse> simplifyDebts(UUID groupId);

    public void recordTransaction(UUID debtorId,
                                  UUID creditorId,
                                  BigDecimal amount,
                                  UUID groupId);

   List<Balance> getGroupBalances(UUID groupId);

    SettlementResponse settle(SettlementRequest request);

}
