package com.SecureAccessPortal.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SecureAccessPortal.Entity.BusinessDocument;

public interface DocumentRepo extends JpaRepository<BusinessDocument, Long> {
	List<BusinessDocument> findByuserCode(String userCode);
}
