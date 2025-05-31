package com.example.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Policy;

public interface IPolicyRepo extends JpaRepository<Policy, String> {
	
	@Query("SELECT p.policyNumber FROM Policy p " +
	           "WHERE SUBSTRING(p.policyNumber, LENGTH(p.policyNumber) - 1, 2) = :currentTwoDigitYear " + // Extracts 'YY' part
	           "ORDER BY CAST(SUBSTRING(p.policyNumber, 5, 5) AS INTEGER) DESC " + // Extracts 'XXXXX' part and converts to integer for correct numeric sort
	           "LIMIT 1")
	    String findTopPolicyNumberForCurrentYear(@Param("currentTwoDigitYear") String currentTwoDigitYear);

	@Query(value="Select * from policy p where p.policyNumber=?1", nativeQuery = true)
	Policy findByPolicyNum(String policyNumber);

	@Query(value="Select * from policy p where p.customerNo=?1", nativeQuery = true)
	List<Policy> findByCustomerNo(String customerNo);
	
	@Query(value="Select * from policy p where p.work_item_ref_no=?1", nativeQuery = true)
	List<Policy> findByWorkItemRefNum(String workItemRefNum);
}
