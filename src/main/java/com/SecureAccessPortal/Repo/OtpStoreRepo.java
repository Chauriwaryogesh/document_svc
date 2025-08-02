package com.SecureAccessPortal.Repo;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.SecureAccessPortal.Entity.OtpStore;

@Repository
public interface OtpStoreRepo extends JpaRepository<OtpStore, String> {

	@Query("SELECT o FROM OtpStore o WHERE o.email = :email AND o.otp = :otp AND o.deletedFlag = :deletedFlag")
	Optional<OtpStore> findByEmailAndOtpAndDeletedFlag(String email, String otp, String deletedFlag);

	@Modifying
	@Query("UPDATE OtpStore o SET o.deletedFlag = 'Y', o.updatedTime = :updatedTime WHERE o.email = :email AND o.userCode = :userCode AND o.deletedFlag = 'N'")
	void markAsDeletedByEmail(@Param("email") String email, @Param("userCode") String userCode,
			@Param("updatedTime") LocalDateTime updatedTime);
}
