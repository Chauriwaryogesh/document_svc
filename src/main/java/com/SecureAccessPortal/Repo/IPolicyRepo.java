package com.SecureAccessPortal.Repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.SecureAccessPortal.Entity.Policy;

public interface IPolicyRepo extends JpaRepository<Policy, String> {

	@Query(value = "SELECT p.policyNumber " + "FROM Policy p " + "WHERE RIGHT(p.policyNumber, 4) = :currentYear "
			+ "ORDER BY CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(p.policyNumber, '/', 2), '/', -1) AS UNSIGNED) DESC "
			+ "LIMIT 1", nativeQuery = true)
	String findTopPolicyNumberForCurrentYear(@Param("currentYear") String currentYear);

	@Query(value = "Select * from policy p where p.policyNumber=?1 and p.deleted_flag=?2", nativeQuery = true)
	Policy findByPolicyNum(String policyNumber, String deletedFlag);

	@Query(value = "Select * from policy p where p.customerNo=?1 and p.deleted_flag=?2", nativeQuery = true)
	List<Policy> findByCustomerNoNew(String customerNo, String deletedFlag);

	@Query(value = "Select * from policy p where p.work_item_ref_no=?1 and p.deleted_flag=?2", nativeQuery = true)
	List<Policy> findByWorkItemRefNum(String workItemRefNum, String deletedFlag);

	@Query(value = "Select * from policy p where p.deleted_flag=?1", nativeQuery = true)
	List<Policy> findAllDeletedflagN(String deletedFlag);

	@Query("SELECT COUNT(DISTINCT p) FROM Policy p WHERE p.userCode = :userCode")
	long findAllPolicies(String userCode);

	@Query("SELECT p FROM Policy p WHERE p.userCode = :userCode")
	List<String> findByPoliciesByUserCode(String userId);

	@Query("SELECT p FROM Policy p JOIN p.customer c WHERE c.customerNo = :customerNo AND p.deletedFlag = :flag")
	Page<Policy> findByCustomerNo(@Param("customerNo") String customerNo, @Param("flag") String flag,
			Pageable pageable);

	@Query(value = "Select * from policy p where p.policyNumber=?1 and p.deleted_flag=?2", nativeQuery = true)
	List<Policy> findByPolicyNumber(String number, String deletedFlag);

	List<Policy> findAll(Specification<Policy> spec);

	@Query("SELECT p FROM Policy p WHERE p.customer.customerNo IN :customerNos AND p.deletedFlag=:deletedFlag")
	List<Policy> findByCustomerCustomerNoIn(List<String> customerNos, String deletedFlag);

	Page<Policy> findAll(Specification<Policy> spec, Pageable pageable);

	//Page<Policy> findAllPolicyForCustomer(Specification<Policy> spec, Pageable pageable);
}
