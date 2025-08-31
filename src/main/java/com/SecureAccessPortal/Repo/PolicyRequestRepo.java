package com.SecureAccessPortal.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SecureAccessPortal.Entity.PolicyRequestEntity;

public interface PolicyRequestRepo extends JpaRepository<PolicyRequestEntity, String> {

	@Query(value = "SELECT p.requestNumber " + "FROM policyrequest p "
			+ "WHERE RIGHT(p.requestNumber, 4) = :currentYear "
			+ "ORDER BY CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(p.requestNumber, '/', 2), '/', -1) AS UNSIGNED) DESC "
			+ "LIMIT 1", nativeQuery = true)
	String findTopRequestNumberForCurrentYear(String currentYear);

	@Query(value = "Select * from policyrequest p where p.customerNo =?1 and  p.deletedFlag=?2", nativeQuery = true)
	List<PolicyRequestEntity> findByCustomerNo(String customerNo, String deletedFlag);

}
