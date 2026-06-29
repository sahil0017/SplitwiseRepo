package com.system_design.splitwise.services.impl;

import com.system_design.splitwise.DTO.CreateUserRequest;
import com.system_design.splitwise.Entities.User;
import com.system_design.splitwise.Repository.UserRepository;
import com.system_design.splitwise.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Override
    public User getUser(UUID userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

    }

    @Override
    public List<User> getAllUsers() {

        return userRepository.findAll();

    }

    @Override
    public void deleteUser(UUID userId) {

        userRepository.deleteById(userId);

    }
}
