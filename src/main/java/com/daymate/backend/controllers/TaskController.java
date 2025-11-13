package com.daymate.backend.controllers;

import com.daymate.backend.dto.TaskRequest;
import com.daymate.backend.models.Task;
import com.daymate.backend.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(Authentication authentication,
                                           @Valid @RequestBody TaskRequest request) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return ResponseEntity.ok(taskService.createTask(request, email));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return ResponseEntity.ok(taskService.getTasks(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(Authentication authentication,
                                          @PathVariable Long id,
                                          @Valid @RequestBody TaskRequest request) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return ResponseEntity.ok(taskService.updateTask(id, request, email));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Task> markComplete(Authentication authentication,
                                            @PathVariable Long id) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return ResponseEntity.ok(taskService.markComplete(id, email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(Authentication authentication,
                                          @PathVariable Long id) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        taskService.deleteTask(id, email);
        return ResponseEntity.noContent().build();
    }
}
