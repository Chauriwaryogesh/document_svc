package com.SecureAccessPortal.Repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.SecureAccessPortal.Entity.Policy;

public interface IPolicyRepo extends JpaRepository<Policy, String> {
	
	@Query("SELECT p.policyNumber FROM Policy p " +
	           "WHERE SUBSTRING(p.policyNumber, LENGTH(p.policyNumber) - 1, 2) = :currentTwoDigitYear " + // Extracts 'YY' part
	           "ORDER BY CAST(SUBSTRING(p.policyNumber, 5, 5) AS INTEGER) DESC " + // Extracts 'XXXXX' part and converts to integer for correct numeric sort
	           "LIMIT 1")
	    String findTopPolicyNumberForCurrentYear(@Param("currentTwoDigitYear") String currentTwoDigitYear);

	@Query(value="Select * from policy p where p.policyNumber=?1 and p.deleted_flag=?2", nativeQuery = true)
	Policy findByPolicyNum(String policyNumber, String deletedFlag);

	@Query(value="Select * from policy p where p.customerNo=?1 and p.deleted_flag=?2", nativeQuery = true)
	List<Policy> findByCustomerNoNew(String customerNo, String deletedFlag);
	
	@Query(value="Select * from policy p where p.work_item_ref_no=?1 and p.deleted_flag=?2", nativeQuery = true)
	List<Policy> findByWorkItemRefNum(String workItemRefNum, String deletedFlag);

	@Query(value="Select * from policy p where p.deleted_flag=?1", nativeQuery = true)
	List<Policy> findAllDeletedflagN(String deletedFlag);

	@Query("SELECT COUNT(DISTINCT p) FROM Policy p WHERE p.userCode = :userCode")
	long findAllPolicies(String userCode);

	@Query("SELECT p FROM Policy p WHERE p.userCode = :userCode")
	List<String> findByPoliciesByUserCode(String userId);

	 @Query("SELECT p FROM Policy p JOIN p.customer c WHERE c.customerNo = :customerNo AND p.deletedFlag = :flag")
	    Page<Policy> findByCustomerNo(@Param("customerNo") String customerNo, @Param("flag") String flag, Pageable pageable);}
