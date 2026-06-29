package com.system_design.splitwise.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name="balance",
        uniqueConstraints=@UniqueConstraint(columnNames={
                "group_id",
                "creditor_id",
                "debtor_id"
        }))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Balance {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name="group_id")
    private ExpenseGroup group;

    @ManyToOne
    @JoinColumn(name="creditor_id")
    private User creditor;

    @ManyToOne
    @JoinColumn(name="debtor_id")
    private User debtor;

    private BigDecimal amount;

}
