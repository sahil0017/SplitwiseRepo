package com.system_design.splitwise.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name="expense_split")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseSplit {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name="expense_id")
    private Expense expense;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private BigDecimal amount;

}
