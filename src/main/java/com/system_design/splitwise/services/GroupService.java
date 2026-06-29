package com.system_design.splitwise.services;

import com.system_design.splitwise.DTO.AddMemberRequest;
import com.system_design.splitwise.DTO.CreateGroupRequest;
import com.system_design.splitwise.Entities.Balance;
import com.system_design.splitwise.Entities.ExpenseGroup;

import java.util.List;
import java.util.UUID;

public interface GroupService {

    ExpenseGroup createGroup(CreateGroupRequest request);

    ExpenseGroup getGroup(UUID groupId);

    List<ExpenseGroup> getAllGroups();

    void addMember(AddMemberRequest request);

    List<Balance> getGroupBalances(UUID groupId);

}
