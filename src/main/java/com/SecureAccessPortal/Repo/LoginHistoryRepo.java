package com.SecureAccessPortal.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.SecureAccessPortal.Entity.LoginHistory;

public interface LoginHistoryRepo extends JpaRepository<LoginHistory, Long> {

	@Query("SELECT u FROM LoginHistory u WHERE u.security.email = :email AND u.userCode = :userCode AND u.deletedFlag = :deletedFlag ORDER BY u.id DESC")
	List<LoginHistory> findByEmailAndDeletedFlag(@Param("email") String email, @Param("userCode") String userCode,
			@Param("deletedFlag") String deletedFlag);

	@Query(value = "SELECT * FROM login_history u WHERE u.email = :email AND u.userCode = :userCode AND u.deletedFlag = :deletedFlag ORDER BY u.id DESC LIMIT 1", nativeQuery = true)
	LoginHistory findByEmailAndDeletedFlagLatest(@Param("email") String email, @Param("userCode") String userCode,
			@Param("deletedFlag") String deletedFlag);

}
