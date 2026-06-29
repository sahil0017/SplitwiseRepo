package com.system_design.splitwise.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBalanceResponse {

    private UUID userId;

    private BigDecimal totalOwed;

    private BigDecimal totalReceivable;

    private List<TransactionResponse> transactions;

}