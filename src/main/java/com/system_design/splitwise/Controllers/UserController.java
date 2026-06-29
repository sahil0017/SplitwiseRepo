package com.system_design.splitwise.Controllers;

import com.system_design.splitwise.DTO.CreateUserRequest;
import com.system_design.splitwise.Entities.User;
import com.system_design.splitwise.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "User APIs", description = "APIs for user management")
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id")UUID id) {
        return userService.getUser(id);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }
}
