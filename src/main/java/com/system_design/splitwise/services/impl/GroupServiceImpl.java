package com.system_design.splitwise.services.impl;

import com.system_design.splitwise.DTO.AddMemberRequest;
import com.system_design.splitwise.DTO.CreateGroupRequest;
import com.system_design.splitwise.Entities.Balance;
import com.system_design.splitwise.Entities.ExpenseGroup;
import com.system_design.splitwise.Entities.GroupMember;
import com.system_design.splitwise.Entities.User;
import com.system_design.splitwise.Repository.ExpenseGroupRepository;
import com.system_design.splitwise.Repository.GroupMemberRepository;
import com.system_design.splitwise.Repository.UserRepository;
import com.system_design.splitwise.services.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final ExpenseGroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository memberRepository;

    @Override
    public ExpenseGroup createGroup(CreateGroupRequest request) {

        User creator = userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ExpenseGroup group = new ExpenseGroup();
        group.setId(UUID.randomUUID());
        group.setName(request.getName());
        group.setCreatedBy(creator);
        group.setCreatedAt(LocalDateTime.now());

        groupRepository.save(group);

        GroupMember member = new GroupMember();
        member.setId(UUID.randomUUID());
        member.setGroup(group);
        member.setUser(creator);
        member.setJoinedAt(LocalDateTime.now());

        memberRepository.save(member);

        return group;
    }

    @Override
    public ExpenseGroup getGroup(UUID id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));
    }

    @Override
    public List<ExpenseGroup> getAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public void addMember(AddMemberRequest request) {

        if (memberRepository.existsByGroupIdAndUserId(
                request.getGroupId(),
                request.getUserId())) {
            throw new RuntimeException("Already member");
        }

        ExpenseGroup group = groupRepository.findById(request.getGroupId())
                .orElseThrow();

        User user = userRepository.findById(request.getUserId())
                .orElseThrow();

        GroupMember member = new GroupMember();
        member.setId(UUID.randomUUID());
        member.setGroup(group);
        member.setUser(user);
        member.setJoinedAt(LocalDateTime.now());

        memberRepository.save(member);
    }

    @Override
    public List<Balance> getGroupBalances(UUID groupId) {
        return List.of();
    }
}
