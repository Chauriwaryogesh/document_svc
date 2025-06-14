package com.SecureAccessPortal.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SecureAccessPortal.Entity.VerificationRecord;

public interface VerificationRecordRepo extends JpaRepository<VerificationRecord, Long> {
	Page<VerificationRecord> findByBankAccountAccountNo(String accountNo, Pageable pageable);

	@Query("SELECT v FROM VerificationRecord v WHERE v.bankAccount.accountNo = :accountNo "
			+ "AND ((:action = 'SANCTIONS' AND v.sanctions IS NOT NULL) OR "
			+ "(:action = 'ID' AND v.identity IS NOT NULL) OR " + "(:action = 'DEATH' AND v.death IS NOT NULL))")
	Page<VerificationRecord> findByBankAccountAccountNoAndAction(String accountNo, String action, Pageable pageable);

	@Query("SELECT v FROM VerificationRecord v WHERE v.bankAccount.accountNo = :accountNo "
			+ "AND ((:status = v.sancStatus AND v.sanctions IS NOT NULL) OR "
			+ "(:status = v.identityStatus AND v.identity IS NOT NULL) OR "
			+ "(:status = v.deathStatus AND v.death IS NOT NULL))")
	Page<VerificationRecord> findByBankAccountAccountNoAndStatus(String accountNo, String status, Pageable pageable);

	@Query("SELECT v FROM VerificationRecord v WHERE v.bankAccount.accountNo = :accountNo "
			+ "AND ((:action = 'SANCTIONS' AND v.sanctions IS NOT NULL AND v.sancStatus = :status) OR "
			+ "(:action = 'ID' AND v.identity IS NOT NULL AND v.identityStatus = :status) OR "
			+ "(:action = 'DEATH' AND v.death IS NOT NULL AND v.deathStatus = :status))")
	Page<VerificationRecord> findByBankAccountAccountNoAndActionAndStatus(String accountNo, String action,
			String status, Pageable pageable);

}
