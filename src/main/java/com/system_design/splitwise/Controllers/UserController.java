package com.system_design.splitwise.Controllers;

import com.system_design.splitwise.DTO.CreateUserRequest;
import com.system_design.splitwise.Entities.User;
import com.system_design.splitwise.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(
        name = "User Management",
        description = """
        APIs responsible for managing users participating in expense sharing.

        Business Responsibilities:
        • Register new users.
        • Retrieve user profile information.
        • List all registered users.
        • Users created through these APIs can participate in groups,
          add expenses, pay for expenses, and settle balances.
        """
)
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Register a new user",
            description = """
            Creates a new user in the SplitWise system.

            Business Logic:
            1. Validates the user information.
            2. Ensures the email address is unique.
            3. Creates a new user profile.
            4. Makes the user eligible to join groups, record expenses,
               and participate in settlements.
            """
    )
    @PostMapping
    public User createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @Operation(
            summary = "Retrieve a user",
            description = """
            Retrieves the profile information of a registered user.

            Business Logic:
            1. Searches for the user using the unique user identifier.
            2. Returns the user's profile details.
            """
    )
    @GetMapping("/{id}")
    public User getUser(@PathVariable("id")UUID id) {
        return userService.getUser(id);
    }

    @Operation(
            summary = "Retrieve all users",
            description = """
            Returns all registered users in the system.

            Business Logic:
            1. Fetches every registered user.
            2. Returns basic profile information.
            3. Used while creating groups and adding group members.
            """
    )
    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }
}
