package com.daymate.backend.controller;

import com.daymate.backend.dto.TaskRequest;
import com.daymate.backend.models.Task;
import com.daymate.backend.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    public TaskController(TaskService taskService) { this.taskService = taskService; }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestHeader("X-User-Id") String userId,
                                           @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.createTask(request, userId));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(taskService.getTasks(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Task> markComplete(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.markComplete(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
