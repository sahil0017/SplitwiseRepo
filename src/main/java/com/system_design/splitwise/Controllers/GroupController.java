package com.system_design.splitwise.Controllers;

import com.system_design.splitwise.DTO.AddMemberRequest;
import com.system_design.splitwise.DTO.CreateGroupRequest;
import com.system_design.splitwise.Entities.ExpenseGroup;
import com.system_design.splitwise.services.GroupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Group APIs", description = "APIs for group management")
@RequestMapping("/groups")
@AllArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping
    public ExpenseGroup createGroup(@RequestBody CreateGroupRequest request) {
        return groupService.createGroup(request);
    }

    @PutMapping("/{groupId}/members/{userId}")
    public void addMember(@PathVariable("groupId") UUID groupId,@PathVariable("userId") UUID userId) {
        groupService.addMember(new AddMemberRequest(userId, groupId));
    }

    @GetMapping("/{groupId}")
    public ExpenseGroup getGroup(@PathVariable("groupId") UUID groupId) {
        return groupService.getGroup(groupId);
    }

    @GetMapping
    public List<ExpenseGroup> getGroups() {
        return groupService.getAllGroups();
    }
}
