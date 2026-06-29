package com.system_design.splitwise.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettlementRequest {

    private UUID groupId;

    private UUID fromUserId;   // debtor (pays money)

    private UUID toUserId;     // creditor (receives money)

    private BigDecimal amount;
}