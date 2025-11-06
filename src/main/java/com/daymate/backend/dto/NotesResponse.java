package com.daymate.backend.dto;



import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotesResponse {
    private Long id;
    private String title;
    private String content;
    private String tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long linkedTaskId;
}
