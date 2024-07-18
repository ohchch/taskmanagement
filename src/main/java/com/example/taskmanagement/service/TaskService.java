package com.example.taskmanagement.service;

import com.example.taskmanagement.dto.TaskDTO;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    TaskDTO createTask(TaskDTO taskDTO, Long userId);
    List<TaskDTO> findTasksByTitle(String title);
    List<TaskDTO> findTasksByCategory(String category);
    List<TaskDTO> findTasksByPriority(String priority);
    List<TaskDTO> findTasksByUserId(Long userId);
    List<TaskDTO> findTasksByTitleAndUserId(String title, Long userId);
    List<TaskDTO> findTasksByCategoryAndUserId(String category, Long userId);
    List<TaskDTO> findTasksByPriorityAndUserId(String priority, Long userId);
    List<TaskDTO> findTasksByQueryAndUserId(String query, Long userId);
    Optional<TaskDTO> findById(Long id);
    TaskDTO updateTask(TaskDTO taskDTO);
    void deleteTask(Long id);
}
