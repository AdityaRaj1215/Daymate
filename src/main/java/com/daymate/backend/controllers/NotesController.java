package com.daymate.backend.controllers;



import com.daymate.backend.dto.NotesRequest;
import com.daymate.backend.models.Notes;

import com.daymate.backend.service.NotesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NotesController {
    private final NotesService noteService;
    public NotesController(NotesService noteService) { this.noteService = noteService; }

    // Create note
    @PostMapping
    public ResponseEntity<Notes> createNote(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                           @RequestBody NotesRequest req) {
        // For now we accept X-User-Id header; replace with authenticated principal later.
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        Notes n = noteService.createNote(userId, req);
        return ResponseEntity.ok(n);
    }

    // Get all notes for user
    @GetMapping
    public ResponseEntity<List<Notes>> getNotes(@RequestHeader(value = "X-User-Id", required = false) String userId) {
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(noteService.getNotesByUser(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notes> updateNote(@PathVariable Long id,
                                           @RequestBody NotesRequest req) {
        return ResponseEntity.ok(noteService.updateNote(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}

