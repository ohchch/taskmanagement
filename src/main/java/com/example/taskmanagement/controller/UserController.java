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
    public ResponseEntity<UserDTO> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        UserDTO userDTO = toDTO(registeredUser); // Convert User to UserDTO
        if (userDTO != null) {
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody User user) {
        Optional<UserDTO> existingUser = Optional.ofNullable(userService.findByUsername(user.getUsername()))
                .map(this::toDTO); // Convert User to UserDTO
        if (existingUser.isPresent() && userService.checkPassword(user.getPassword(), userService.findByUsername(user.getUsername()).getPassword())) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful!");
            response.put("userId", existingUser.get().getId());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(Collections.singletonMap("message", "Invalid username or password"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User user) {
        Optional<User> existingUser = userService.findById(id);
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setUsername(user.getUsername());
            updatedUser.setPassword(user.getPassword());
            updatedUser.setEmail(user.getEmail());
            User savedUser = userService.updateUser(updatedUser);
            UserDTO updatedUserDTO = toDTO(savedUser);
            return ResponseEntity.ok(updatedUserDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<UserDTO> user = userService.findById(id)
                .map(this::toDTO); // Convert User to UserDTO
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Helper method to convert User to UserDTO
    private UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setTasks(user.getTasks().stream().map(TaskDTO::new).collect(Collectors.toSet()));
        return userDTO;
    }
}
