package com.example.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.BusinessDocument;

public interface DocumentRepo extends JpaRepository<BusinessDocument, Long> {
	List<BusinessDocument> findByUserId(String userId);
}
