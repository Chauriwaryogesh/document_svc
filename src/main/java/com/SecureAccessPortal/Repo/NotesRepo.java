package com.SecureAccessPortal.Repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SecureAccessPortal.Entity.Note;

public interface NotesRepo extends JpaRepository<Note, String> {

	Page<Note> findAll(Specification<Note> spec, Pageable pageable);

	@Query("SELECT n.id FROM Note n WHERE n.id LIKE 'NOTE/%' ORDER BY n.id DESC LIMIT 1")
	String findLatestByNotesId();

	@Query("SELECT u FROM Note u WHERE u.id= :id AND u.deleted= :deleted")
	Optional<Note> findById(String id, boolean deleted);
	@Query("SELECT u FROM Note u WHERE u.id= :id")
	Optional<Note> updateStatus(String id);

}
