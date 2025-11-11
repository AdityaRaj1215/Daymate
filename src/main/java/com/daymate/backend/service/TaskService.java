package com.daymate.backend.service;

import com.daymate.backend.dto.TaskRequest;
import com.daymate.backend.exception.ResourceNotFoundException;
import com.daymate.backend.models.Task;
import com.daymate.backend.models.User;
import com.daymate.backend.repository.TaskRepository;
import com.daymate.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(TaskRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        task.setUrgency(request.getUrgency());
        task.setUser(user);
        return taskRepository.save(task);
    }

    public List<Task> getTasks(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return taskRepository.findByUser(user);
    }

    public Task updateTask(Long id, TaskRequest request, String email) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        
        // Verify task belongs to user
        if (!task.getUser().getEmail().equals(email)) {
            throw new ResourceNotFoundException("Task not found");
        }
        
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        task.setUrgency(request.getUrgency());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id, String email) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        
        // Verify task belongs to user
        if (!task.getUser().getEmail().equals(email)) {
            throw new ResourceNotFoundException("Task not found");
        }
        
        taskRepository.deleteById(id);
    }

    public Task markComplete(Long id, String email) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        
        // Verify task belongs to user
        if (!task.getUser().getEmail().equals(email)) {
            throw new ResourceNotFoundException("Task not found");
        }
        
        task.setCompleted(true);
        return taskRepository.save(task);
    }
}
