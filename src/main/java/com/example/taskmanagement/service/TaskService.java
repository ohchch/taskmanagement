package com.example.taskmanagement.service;

import com.example.taskmanagement.dto.TaskDTO;
import com.example.taskmanagement.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    TaskDTO createTask(Task task);
    List<TaskDTO> findTasksByTitle(String title);
    List<TaskDTO> findTasksByCategory(String category);
    List<TaskDTO> findTasksByPriority(String priority);
    Optional<TaskDTO> findById(Long id);
    TaskDTO updateTask(TaskDTO taskDTO);
    void deleteTask(Long id);
}
