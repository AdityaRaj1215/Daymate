package com.daymate.backend.service;


import com.daymate.backend.dto.NotesRequest;
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

    public Notes createNote(String userId, NotesRequest req) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Notes note = new Notes();
        note.setTitle(req.getTitle());
        note.setContent(req.getContent());
        note.setTags(req.getTags());
        note.setUser(user);
        if (req.getLinkedTaskId() != null) {
            Task t = taskRepository.findById(req.getLinkedTaskId()).orElse(null);
            note.setLinkedTask(t);
        }
        return noteRepository.save(note);
    }

    public List<Notes> getNotesByUser(String userId) {
        return noteRepository.findByUserId(userId);
    }

    public Notes updateNote(Long id, NotesRequest req) {
        Notes n = noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Note not found"));
        n.setTitle(req.getTitle());
        n.setContent(req.getContent());
        n.setTags(req.getTags());
        if (req.getLinkedTaskId() != null) {
            Task t = taskRepository.findById(req.getLinkedTaskId()).orElse(null);
            n.setLinkedTask(t);
        } else {
            n.setLinkedTask(null);
        }
        return noteRepository.save(n);
    }

    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }
}
