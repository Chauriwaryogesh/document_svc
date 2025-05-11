package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Note;

public interface NotesRepo extends JpaRepository<Note, Long>{

}
