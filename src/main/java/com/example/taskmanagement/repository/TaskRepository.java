package com.example.taskmanagement.repository;

import com.example.taskmanagement.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTitleContaining(String title);
    List<Task> findByCategory(String category);
    List<Task> findByPriority(String priority);
    List<Task> findByUserId(Long userId);
    List<Task> findByTitleContainingAndUserId(String title, Long userId);
    List<Task> findByCategoryAndUserId(String category, Long userId);
    List<Task> findByPriorityAndUserId(String priority, Long userId);
}
