package com.system_design.splitwise.DTO;

import com.system_design.splitwise.Enum.SplitType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class AddExpenseRequest {

    private UUID groupId;

    private UUID paidBy;

    private String description;

    private BigDecimal amount;

    private SplitType splitType;

    private List<SplitRequest> splits;

}
