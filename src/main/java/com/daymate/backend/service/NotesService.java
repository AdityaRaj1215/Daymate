package com.daymate.backend.service;

import com.daymate.backend.dto.NotesRequest;
import com.daymate.backend.exception.ResourceNotFoundException;
import com.daymate.backend.models.Notes;
import com.daymate.backend.models.Task;
import com.daymate.backend.models.User;
import com.daymate.backend.repository.NotesRepository;
import com.daymate.backend.repository.TaskRepository;
import com.daymate.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotesService {
    private final NotesRepository noteRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public NotesService(NotesRepository noteRepository,
                       UserRepository userRepository,
                       TaskRepository taskRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public Notes createNote(String email, NotesRequest req) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Notes note = new Notes();
        note.setTitle(req.getTitle());
        note.setContent(req.getContent());
        note.setTags(req.getTags());
        note.setUser(user);
        if (req.getLinkedTaskId() != null) {
            Task t = taskRepository.findById(req.getLinkedTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
            // Verify task belongs to user
            if (!t.getUser().getEmail().equals(email)) {
                throw new ResourceNotFoundException("Task not found");
            }
            note.setLinkedTask(t);
        }
        return noteRepository.save(note);
    }

    public List<Notes> getNotesByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return noteRepository.findByUserId(user.getId());
    }

    public Notes updateNote(Long id, NotesRequest req, String email) {
        Notes n = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        
        // Verify note belongs to user
        if (!n.getUser().getEmail().equals(email)) {
            throw new ResourceNotFoundException("Note not found");
        }
        
        n.setTitle(req.getTitle());
        n.setContent(req.getContent());
        n.setTags(req.getTags());
        if (req.getLinkedTaskId() != null) {
            Task t = taskRepository.findById(req.getLinkedTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
            // Verify task belongs to user
            if (!t.getUser().getEmail().equals(email)) {
                throw new ResourceNotFoundException("Task not found");
            }
            n.setLinkedTask(t);
        } else {
            n.setLinkedTask(null);
        }
        return noteRepository.save(n);
    }

    public void deleteNote(Long id, String email) {
        Notes note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        
        // Verify note belongs to user
        if (!note.getUser().getEmail().equals(email)) {
            throw new ResourceNotFoundException("Note not found");
        }
        
        noteRepository.deleteById(id);
    }
}
