package com.daymate.backend.repository;

import com.daymate.backend.models.Task;
import com.daymate.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
    List<Task> findByDueDateBetweenAndReminderSentFalse(LocalDateTime start, LocalDateTime end);
}
