package com.system_design.splitwise.DTO;

import com.system_design.splitwise.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SplitRequest {

    private User user;

    private BigDecimal amount;

    private Double percentage;

}
