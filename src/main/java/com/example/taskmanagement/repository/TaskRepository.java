package com.example.taskmanagement.repository;

import com.example.taskmanagement.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTitleContaining(String title);
    List<Task> findByCategory(String category);
    List<Task> findByPriority(String priority);
    List<Task> findByUserId(Long userId);
    List<Task> findByTitleContainingAndUserId(String title, Long userId);
    List<Task> findByCategoryAndUserId(String category, Long userId);
    List<Task> findByPriorityAndUserId(String priority, Long userId);

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND " +
           "(LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(t.category) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(t.priority) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Task> findByQueryAndUserId(@Param("query") String query, @Param("userId") Long userId);
}
