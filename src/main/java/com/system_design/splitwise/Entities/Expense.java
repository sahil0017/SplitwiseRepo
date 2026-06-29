package com.system_design.splitwise.Entities;

import com.system_design.splitwise.Enum.SplitType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="expense")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name="group_id")
    private ExpenseGroup group;

    private String description;

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name="paid_by")
    private User paidBy;

    @Enumerated(EnumType.STRING)
    private SplitType splitType;

    private LocalDateTime createdAt;

}