package com.system_design.splitwise.services;

import com.system_design.splitwise.DTO.CreateUserRequest;
import com.system_design.splitwise.Entities.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User createUser(CreateUserRequest request);

    User getUser(UUID userId);

    List<User> getAllUsers();

    void deleteUser(UUID userId);

}
