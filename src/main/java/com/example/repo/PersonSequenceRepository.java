package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.PersonSequence;

@Repository
public interface PersonSequenceRepository extends JpaRepository<PersonSequence, Long> {

}

