package com.SecureAccessPortal.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.SecureAccessPortal.Entity.DocumentEntity;

import jakarta.transaction.Transactional;

@Repository
public interface DocumentDtlsRepo extends JpaRepository<DocumentEntity, Long> {

	Page<DocumentEntity> findAll(Specification<DocumentEntity> spec, Pageable pageable);
	
	@Query("SELECT d.documentId FROM DocumentEntity d WHERE d.documentId LIKE 'DOC/%' ORDER BY d.documentId DESC LIMIT 1")
	String findLatestDocumentId();

	@Query("SELECT d FROM DocumentEntity d WHERE d.documentId =:documentId")
	DocumentEntity findbyDocumentId(String documentId);

	
	@Modifying
	@Transactional
	@Query("DELETE FROM DocumentEntity d where d.documentId=:documentId")
	void deleteByDocumentId(String documentId);
}
