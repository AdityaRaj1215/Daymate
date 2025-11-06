package com.daymate.backend.repository;


import com.daymate.backend.models.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotesRepository extends JpaRepository<Notes, Long> {
    List<Notes> findByUserId(String userId);
    List<Notes> findByUserIdAndTagsContaining(String userId, String tag);
}
