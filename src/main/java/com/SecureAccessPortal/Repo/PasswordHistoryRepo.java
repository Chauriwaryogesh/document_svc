package com.SecureAccessPortal.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SecureAccessPortal.Entity.PasswordHistory;

public interface PasswordHistoryRepo extends JpaRepository<PasswordHistory, Long> {

	@Query("SELECT p FROM PasswordHistory p WHERE p.security.email = :email AND p.isCurrent = true AND p.deletedFlag = :deletedFlag")
	Optional<PasswordHistory> findByEmailAndIsCurrentTrueAndDeletedFlag(String email, String deletedFlag);

	@Query("SELECT p FROM PasswordHistory p WHERE p.security.email = :email AND p.isCurrent = false AND p.deletedFlag = :deletedFlag ORDER BY p.createdTime DESC")
	List<PasswordHistory> findTop5ByEmailAndIsCurrentFalseAndDeletedFlagOrderByCreatedTimeDesc(String email,
			String deletedFlag);

	@Query("SELECT p FROM PasswordHistory p WHERE p.security.email = :email AND p.isCurrent = false AND p.deletedFlag = :deletedFlag ORDER BY p.createdTime ASC")
	List<PasswordHistory> findByEmailAndIsCurrentFalseAndDeletedFlagOrderByCreatedTimeAsc(String email,
			String deletedFlag);

}
