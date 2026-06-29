package com.system_design.splitwise.Repository;

import com.system_design.splitwise.Entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    List<Expense> findByGroupId(UUID groupId);

    List<Expense> findByPaidById(UUID paidBy);

    List<Expense> findByGroupIdOrderByCreatedAtDesc(UUID groupId);

}
