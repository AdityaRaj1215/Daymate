package com.daymate.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NotesRequest {
    @NotBlank(message = "Note title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @Size(max = 10000, message = "Content must not exceed 10000 characters")
    private String content;

    @Size(max = 500, message = "Tags must not exceed 500 characters")
    private String tags;

    private Long linkedTaskId;
}
