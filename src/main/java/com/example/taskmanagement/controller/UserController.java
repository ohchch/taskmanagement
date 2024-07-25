package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.UserDTO;
import com.example.taskmanagement.dto.TaskDTO;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        if (userService.usernameExists(user.getUsername())) {
            response.put("message", "Username already exists.");
            return ResponseEntity.badRequest().body(response);
        }

        if (userService.emailExists(user.getEmail())) {
            response.put("message", "Email already exists.");
            return ResponseEntity.badRequest().body(response);
        }

        if (user.getTasks() == null) {
            user.setTasks(new HashSet<>());
        }

        try {
            User registeredUser = userService.registerUser(user);
            UserDTO userDTO = toDTO(registeredUser);
            response.put("message", "Registration successful!");
            response.put("user", userDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> existingUserOptional = Optional.ofNullable(userService.findByUsername(user.getUsername()));

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            if (userService.checkPassword(user.getPassword(), existingUser.getPassword())) {
                UserDTO userDTO = toDTO(existingUser);
                response.put("message", "Login successful!");
                response.put("userId", userDTO.getId());
                response.put("user", userDTO);  
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Invalid username or password");
                return ResponseEntity.status(401).body(response);
            }
        } else {
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(401).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> existingUser = userService.findById(id);

        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setUsername(user.getUsername());
            updatedUser.setPassword(user.getPassword());
            updatedUser.setEmail(user.getEmail());
            try {
                User savedUser = userService.updateUser(updatedUser);
                UserDTO updatedUserDTO = toDTO(savedUser);
                response.put("message", "Update successful!");
                response.put("user", updatedUserDTO);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                response.put("message", "Update failed: " + e.getMessage());
                return ResponseEntity.status(500).body(response);
            }
        } else {
            response.put("message", "User not found");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isPresent()) {
            UserDTO userDTO = toDTO(userOptional.get());
            response.put("user", userDTO);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "User not found");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/oauth/login")
    public ResponseEntity<Map<String, Object>> handleOAuthLogin(@RequestBody Map<String, String> payload) {
        String githubUserId = payload.get("userId");
        String email = payload.get("email");
    
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.findOrCreateUser(githubUserId, email);
            UserDTO userDTO = toDTO(user);
            response.put("message", "OAuth login successful!");
            response.put("userId", userDTO.getId());
            response.put("user", userDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "OAuth login failed: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setTasks(user.getTasks() != null ? user.getTasks().stream().map(TaskDTO::new).collect(Collectors.toSet()) : Collections.emptySet());
        return dto;
    }
}
