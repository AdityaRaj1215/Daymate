package com.daymate.backend.controllers;

import com.daymate.backend.dto.NotesRequest;
import com.daymate.backend.models.Notes;
import com.daymate.backend.service.NotesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NotesController {
    private final NotesService noteService;

    public NotesController(NotesService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<Notes> createNote(Authentication authentication,
                                            @RequestBody NotesRequest req) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        Notes n = noteService.createNote(email, req);
        return ResponseEntity.ok(n);
    }

    @GetMapping
    public ResponseEntity<List<Notes>> getNotes(Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return ResponseEntity.ok(noteService.getNotesByUser(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notes> updateNote(Authentication authentication,
                                           @PathVariable Long id,
                                           @RequestBody NotesRequest req) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return ResponseEntity.ok(noteService.updateNote(id, req, email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(Authentication authentication,
                                           @PathVariable Long id) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        noteService.deleteNote(id, email);
        return ResponseEntity.noContent().build();
    }
}

