package com.system_design.splitwise.Repository;

import com.system_design.splitwise.Entities.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupMemberRepository extends JpaRepository<GroupMember, UUID> {

    List<GroupMember> findByGroupId(UUID groupId);

    List<GroupMember> findByUserId(UUID userId);

    Optional<GroupMember> findByGroupIdAndUserId(UUID groupId,
                                                 UUID userId);

    boolean existsByGroupIdAndUserId(UUID groupId,
                                     UUID userId);

    void deleteByGroupIdAndUserId(UUID groupId,
                                  UUID userId);

}
