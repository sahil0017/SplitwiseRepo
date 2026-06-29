package com.system_design.splitwise.Repository;

import com.system_design.splitwise.Entities.ExpenseGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExpenseGroupRepository extends JpaRepository<ExpenseGroup, UUID> {

    List<ExpenseGroup> findByCreatedById(UUID createdBy);

}
