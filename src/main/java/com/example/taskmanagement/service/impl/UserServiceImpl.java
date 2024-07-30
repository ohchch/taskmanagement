package com.example.taskmanagement.service.impl;

import com.example.taskmanagement.model.User;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        // Encode the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public User updateUser(User user) {
        // Encode the password before updating the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public User findOrCreateUser(String githubUserId, String email) throws Exception {
        Optional<User> existingUserOptional = userRepository.findByGithubUserId(githubUserId);

        if (existingUserOptional.isPresent()) {
            return existingUserOptional.get();
        } else {
            // Create a new user if not found
            User newUser = fetchUserFromGithub(githubUserId, email);
            return userRepository.save(newUser);
        }
    }

    // Fetch user from GitHub based on GitHub user ID and email
    private User fetchUserFromGithub(String githubUserId, String email) throws Exception {
        User user = new User();
        user.setGithubUserId(githubUserId);
        user.setUsername(email); // Use email as username
        user.setEmail(email);

        // Set a default password (encoded)
        user.setPassword(passwordEncoder.encode("default_password"));
        return user;
    }
}
