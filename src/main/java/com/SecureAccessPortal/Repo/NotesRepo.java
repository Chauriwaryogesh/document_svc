package com.SecureAccessPortal.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SecureAccessPortal.Entity.Note;

public interface NotesRepo extends JpaRepository<Note, Long> {

}
