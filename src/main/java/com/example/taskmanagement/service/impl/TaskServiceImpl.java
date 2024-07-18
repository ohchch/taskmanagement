package com.example.taskmanagement.service.impl;

import com.example.taskmanagement.dto.TaskDTO;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public TaskDTO createTask(TaskDTO taskDTO, Long userId) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setCategory(taskDTO.getCategory());
        task.setPriority(taskDTO.getPriority());
        LocalDateTime createdAt = LocalDateTime.now(); // 获取当前时间
        task.setCreatedAt(createdAt); // 设置创建时间
        task.setUpdatedAt(createdAt); // 设置更新时间
        
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new IllegalArgumentException("User not found"));
        task.setUser(user);
    
        Task savedTask = taskRepository.save(task);
        TaskDTO savedTaskDTO = toDTO(savedTask);
        log.info("Created task: {}", savedTaskDTO);
        return savedTaskDTO;
    }
    
    

    @Override
    public List<TaskDTO> findTasksByTitle(String title) {
        List<Task> tasks = taskRepository.findByTitleContaining(title);
        return toDTOs(tasks);
    }

    @Override
    public List<TaskDTO> findTasksByCategory(String category) {
        List<Task> tasks = taskRepository.findByCategory(category);
        return toDTOs(tasks);
    }

    @Override
    public List<TaskDTO> findTasksByPriority(String priority) {
        List<Task> tasks = taskRepository.findByPriority(priority);
        return toDTOs(tasks);
    }

    @Override
    public List<TaskDTO> findTasksByUserId(Long userId) {
        List<Task> tasks = taskRepository.findByUserId(userId);
        return toDTOs(tasks);
    }

    @Override
    public List<TaskDTO> findTasksByTitleAndUserId(String title, Long userId) {
        List<Task> tasks = taskRepository.findByTitleContainingAndUserId(title, userId);
        return toDTOs(tasks);
    }

    @Override
    public List<TaskDTO> findTasksByCategoryAndUserId(String category, Long userId) {
        List<Task> tasks = taskRepository.findByCategoryAndUserId(category, userId);
        return toDTOs(tasks);
    }

    @Override
    public List<TaskDTO> findTasksByPriorityAndUserId(String priority, Long userId) {
        List<Task> tasks = taskRepository.findByPriorityAndUserId(priority, userId);
        return toDTOs(tasks);
    }

    @Override
    public List<TaskDTO> findTasksByQueryAndUserId(String query, Long userId) {
        List<Task> tasks = taskRepository.findByQueryAndUserId(query, userId);
        return toDTOs(tasks);
    }

    @Override
    public Optional<TaskDTO> findById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.map(this::toDTO);
    }

    @Override
    public TaskDTO updateTask(TaskDTO taskDTO) {
        Task task = taskRepository.findById(taskDTO.getId())
                                  .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setCategory(taskDTO.getCategory());
        task.setPriority(taskDTO.getPriority());
        task.setUpdatedAt(LocalDateTime.now()); // 更新时间更新为当前时间
    
        Task updatedTask = taskRepository.save(task);
        return toDTO(updatedTask);
    }
    

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    private TaskDTO toDTO(Task task) {
        return task.toDTO();
    }

    private List<TaskDTO> toDTOs(List<Task> tasks) {
        return tasks.stream().map(Task::toDTO).collect(Collectors.toList());
    }
}
