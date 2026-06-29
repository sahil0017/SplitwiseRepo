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
public class SettlementResponse {

    private UUID settlementId;

    private UUID groupId;

    private UUID fromUserId;

    private UUID toUserId;

    private BigDecimal amount;

    private String status;
}
