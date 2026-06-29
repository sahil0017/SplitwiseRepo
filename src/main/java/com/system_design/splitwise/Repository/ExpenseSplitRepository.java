package com.system_design.splitwise.Repository;

import com.system_design.splitwise.Entities.ExpenseSplit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExpenseSplitRepository extends JpaRepository<ExpenseSplit, UUID> {

    List<ExpenseSplit> findByExpenseId(UUID expenseId);

    List<ExpenseSplit> findByUserId(UUID userId);

    List<ExpenseSplit> findByExpenseGroupId(UUID groupId);

    void deleteByExpenseId(UUID expenseId);

}
