package com.example.taskmanagement.service;

import com.example.taskmanagement.model.User;

import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    User findByUsername(String username);
    Optional<User> findById(Long id);
    boolean checkPassword(String rawPassword, String encodedPassword);
    User updateUser(User user);
    boolean usernameExists(String username);
    boolean emailExists(String email);
    User findOrCreateUser(String githubUserId, String email) throws Exception;
}
