package com.system_design.splitwise.Repository;

import com.system_design.splitwise.Entities.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BalanceRepository extends JpaRepository<Balance, UUID> {

    List<Balance> findByGroupId(UUID groupId);

    List<Balance> findByGroup_Id(UUID groupId);

    List<Balance> findByCreditorId(UUID creditorId);

    List<Balance> findByDebtorId(UUID debtorId);

    List<Balance> findByGroupIdAndCreditorId(UUID groupId,
                                             UUID creditorId);


    List<Balance> findByGroupIdAndDebtorId(UUID groupId,
                                           UUID debtorId);

    Optional<Balance> findByGroupIdAndCreditorIdAndDebtorId(
            UUID groupId,
            UUID creditorId,
            UUID debtorId);

    boolean existsByGroupIdAndCreditorIdAndDebtorId(
            UUID groupId,
            UUID creditorId,
            UUID debtorId);

}
