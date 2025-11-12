package com.daymate.backend.scheduler;

import com.daymate.backend.models.Task;
import com.daymate.backend.repository.TaskRepository;
import com.daymate.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReminderScheduler {

    private final TaskRepository taskRepository;
    private final EmailService emailService;

    @Value("${reminder.hours.before:24}")
    private int hoursBefore;

    public ReminderScheduler(TaskRepository taskRepository, EmailService emailService) {
        this.taskRepository = taskRepository;
        this.emailService = emailService;
    }

    @Scheduled(fixedDelayString = "${reminder.check.interval:900000}")
    public void checkAndSendReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderWindowEnd = now.plusHours(hoursBefore);

        // Find tasks with due dates within the reminder window that haven't been sent reminders
        List<Task> tasksToRemind = taskRepository.findByDueDateBetweenAndReminderSentFalse(now, reminderWindowEnd);

        for (Task task : tasksToRemind) {
            // Only send reminders for incomplete tasks
            if (!task.isCompleted() && task.getUser() != null) {
                try {
                    emailService.sendTaskReminder(task, task.getUser());
                    task.setReminderSent(true);
                    taskRepository.save(task);
                } catch (Exception e) {
                    // Log error but continue with other tasks
                    System.err.println("Error processing reminder for task " + task.getId() + ": " + e.getMessage());
                }
            }
        }

        if (!tasksToRemind.isEmpty()) {
            System.out.println("Processed " + tasksToRemind.size() + " task reminder(s) at " + now);
        }
    }
}

