package com.daymate.backend.service;

import com.daymate.backend.models.Task;
import com.daymate.backend.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendTaskReminder(Task task, User user) {
        // Skip if email is not configured
        if (fromEmail == null || fromEmail.isEmpty()) {
            System.out.println("Email not configured. Skipping reminder for task " + task.getId());
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(user.getEmail());
            message.setSubject("Reminder: " + task.getTitle());
            message.setText(buildReminderEmailBody(task, user));
            
            mailSender.send(message);
            System.out.println("Reminder email sent successfully for task " + task.getId() + " to " + user.getEmail());
        } catch (Exception e) {
            // Log error but don't throw - scheduler should continue
            System.err.println("Failed to send reminder email for task " + task.getId() + ": " + e.getMessage());
        }
    }

    private String buildReminderEmailBody(Task task, User user) {
        StringBuilder body = new StringBuilder();
        body.append("Hello ").append(user.getName()).append(",\n\n");
        body.append("This is a reminder about your upcoming task:\n\n");
        body.append("Task: ").append(task.getTitle()).append("\n");
        
        if (task.getDescription() != null && !task.getDescription().isEmpty()) {
            body.append("Description: ").append(task.getDescription()).append("\n");
        }
        
        if (task.getDueDate() != null) {
            body.append("Due Date: ").append(task.getDueDate()).append("\n");
        }
        
        if (task.getUrgency() != null) {
            body.append("Urgency: ").append(task.getUrgency()).append("\n");
        }
        
        body.append("\n");
        body.append("Please make sure to complete this task on time!\n\n");
        body.append("Best regards,\n");
        body.append("DayMate Team");
        
        return body.toString();
    }
}

