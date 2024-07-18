package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.TaskDTO;
import com.example.taskmanagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;


    @PostMapping
    public TaskDTO createTask(@RequestBody TaskDTO taskDTO, @RequestParam Long userId) {
        return taskService.createTask(taskDTO, userId);
    }

    @GetMapping
    public List<TaskDTO> getTasks(@RequestParam Long userId,
                                  @RequestParam(required = false) String query) {
        System.out.println("Received request with userId: " + userId + " and query: " + query);
        if (query != null) {
            return taskService.findTasksByQueryAndUserId(query, userId);
        } else {
            return taskService.findTasksByUserId(userId);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        Optional<TaskDTO> task = taskService.findById(id);
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDetails) {
        Optional<TaskDTO> taskOptional = taskService.findById(id);
        if (taskOptional.isPresent()) {
            TaskDTO taskDTO = taskOptional.get();
            taskDTO.setTitle(taskDetails.getTitle());
            taskDTO.setDescription(taskDetails.getDescription());
            taskDTO.setCategory(taskDetails.getCategory());
            taskDTO.setPriority(taskDetails.getPriority());
            taskDTO.setUpdatedAt(LocalDateTime.now());
            TaskDTO updatedTask = taskService.updateTask(taskDTO);
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskService.findById(id).isPresent()) {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/priority")
    public ResponseEntity<TaskDTO> updateTaskPriority(@PathVariable Long id, @RequestBody String priority) {
        Optional<TaskDTO> taskOptional = taskService.findById(id);
        if (taskOptional.isPresent()) {
            TaskDTO taskDTO = taskOptional.get();
            taskDTO.setPriority(priority);
            TaskDTO updatedTask = taskService.updateTask(taskDTO);
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
