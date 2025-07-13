package com.SecureAccessPortal.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SecureAccessPortal.Entity.VerificationRecord;

public interface VerificationRecordRepo extends JpaRepository<VerificationRecord, Long> {
	Page<VerificationRecord> findByBankAccountAccountNo(String accountNo, Pageable pageable);

	@Query("SELECT v FROM VerificationRecord v WHERE v.bankAccount.accountNo = :accountNo "
            + "AND ((:action = 'SANCTIONS' AND v.sanctions IS NOT NULL) OR "
            + "(:action = 'ID' AND v.identity IS NOT NULL) OR "
            + "(:action = 'DEATH' AND v.death IS NOT NULL))")
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

    @Query(value = "SELECT * FROM Verification_Records v WHERE v.account_number = ?1", nativeQuery = true)
    List<VerificationRecord> findByAccountNo(String accountNo);

    @Query("SELECT v.verId FROM VerificationRecord v WHERE v.verId LIKE 'REC_%' ORDER BY CAST(SUBSTRING(v.verId, 5) AS INTEGER) DESC LIMIT 1")
    String findTopVerId();

	 @Query("Select u from VerificationRecord u where u.verId=?1 ")
	Optional<VerificationRecord> findByVerId(String id);

	 
	 @Query("SELECT COUNT(DISTINCT v) FROM VerificationRecord v  WHERE v.userCode = :userCode")
	 long findAllRecords(String userCode);

}
