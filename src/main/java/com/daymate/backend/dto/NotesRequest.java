package com.daymate.backend.dto;



import lombok.Data;

@Data
public class NotesRequest {
    private String title;
    private String content;
    private String tags;
    private Long linkedTaskId;
}
