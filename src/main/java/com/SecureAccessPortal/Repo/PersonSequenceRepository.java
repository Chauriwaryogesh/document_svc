package com.SecureAccessPortal.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SecureAccessPortal.Entity.PersonSequence;

@Repository
public interface PersonSequenceRepository extends JpaRepository<PersonSequence, Long> {

}

