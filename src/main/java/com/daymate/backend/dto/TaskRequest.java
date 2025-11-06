package com.daymate.backend.dto;

import com.daymate.backend.models.Urgency;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequest {
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Urgency urgency;
}
