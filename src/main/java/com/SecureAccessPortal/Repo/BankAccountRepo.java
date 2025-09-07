package com.SecureAccessPortal.Repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.SecureAccessPortal.Entity.Bank;

public interface BankAccountRepo extends JpaRepository<Bank, String> {

	@Query("SELECT ba FROM Bank ba WHERE ba.policy.policyNumber = :policyNumber AND ba.deletedFlag =:deletedFlag")
	List<Bank> findByPolicyNumber(@Param("policyNumber") String policyNumber, String deletedFlag);

	@Query("SELECT ba FROM Bank ba WHERE ba.customer.customerNo = :customerNo AND ba.deletedFlag= :deletedFlag")
	List<Bank> findByCustomerNo(String customerNo, String deletedFlag);

	@Query("SELECT ba FROM Bank ba WHERE ba.accountNumber = ?1")
	List<Bank> findByBankAccNo(String accountNo);

	@Query("SELECT ba FROM Bank ba WHERE ba.accountNumber = ?1 AND ba.deletedFlag= ?2")
	Bank findByAccountNo(String accountNo, String deletedFlag);

	Page<Bank> findAll(Specification<Bank> spec, Pageable pageable);

	@Query("SELECT COUNT(DISTINCT b) FROM Bank b ")
	long findAllBankAcc(String userCode);

	@Query("SELECT b FROM Bank b WHERE b.customer.userCode= :userCode")
	List<String> findBankAccountByUserCode(String userCode);

	
	@Query(value = "SELECT p.bankId " + "FROM Bank p " + "WHERE RIGHT(p.bankId, 4) = :currentYear "
			+ "ORDER BY CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(p.bankId, '/', 2), '/', -1) AS UNSIGNED) DESC "
			+ "LIMIT 1", nativeQuery = true)
	String findTopBankForCurrentYear(String currentYear);	
}
