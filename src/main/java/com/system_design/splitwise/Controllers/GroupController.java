package com.system_design.splitwise.Controllers;

import com.system_design.splitwise.DTO.AddMemberRequest;
import com.system_design.splitwise.DTO.CreateGroupRequest;
import com.system_design.splitwise.Entities.ExpenseGroup;
import com.system_design.splitwise.services.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(
        name = "Group Management",
        description = """
        APIs responsible for managing expense groups and their members.

        Business Responsibilities:
        • Create expense groups.
        • Assign a group owner.
        • Add members to existing groups.
        • Retrieve group information and members.
        • List all expense groups available in the system.
        """
)
@RequestMapping("/groups")
@AllArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @Operation(
            summary = "Create a new expense group",
            description = """
            Creates a new expense group.

            Business Logic:
            1. Validates the group creation request.
            2. Verifies that the creator exists.
            3. Creates a new expense group.
            4. Automatically assigns the creator as the group owner.
            5. Adds the creator as the first member of the group.
            6. Returns the created group.
            """
    )
    @PostMapping
    public ExpenseGroup createGroup(@RequestBody CreateGroupRequest request) {
        return groupService.createGroup(request);
    }

    @Operation(
            summary = "Add a member to a group",
            description = """
            Adds an existing user to an expense group.

            Business Logic:
            1. Validates that the group exists.
            2. Validates that the user exists.
            3. Ensures the user is not already a member.
            4. Creates a group membership entry.
            5. Makes the user eligible to participate in future expenses.
            """
    )
    @PutMapping("/{groupId}/members/{userId}")
    public void addMember(@PathVariable("groupId") UUID groupId,@PathVariable("userId") UUID userId) {
        groupService.addMember(new AddMemberRequest(userId, groupId));
    }

    @Operation(
            summary = "Retrieve group details",
            description = """
            Returns complete information about an expense group.

            Business Logic:
            1. Retrieves the group information.
            2. Includes owner and member details.
            3. Returns metadata required for expense management.
            """
    )
    @GetMapping("/{groupId}")
    public ExpenseGroup getGroup(@PathVariable("groupId") UUID groupId) {
        return groupService.getGroup(groupId);
    }

    @Operation(
            summary = "Retrieve all expense groups",
            description = """
            Returns every expense group available in the system.

            Business Logic:
            1. Retrieves all groups.
            2. Returns group metadata.
            3. Used for dashboards and group selection.
            """
    )
    @GetMapping
    public List<ExpenseGroup> getGroups() {
        return groupService.getAllGroups();
    }
}
