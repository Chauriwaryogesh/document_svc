package com.SecureAccessPortal.Repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.SecureAccessPortal.Entity.BankAccount;

public interface BankAccountRepo extends JpaRepository<BankAccount, Long> {

	@Query("SELECT ba FROM BankAccount ba WHERE ba.policy.policyNumber = :policyNumber")
	List<BankAccount> findByPolicyNumber(@Param("policyNumber") String policyNumber);

	@Query("SELECT ba FROM BankAccount ba WHERE ba.customer.customerNo = ?1")
	List<BankAccount> findByCustomerNo(String customerNo);

	@Query("SELECT ba FROM BankAccount ba WHERE ba.accountNo = ?1")
	List<BankAccount> findByBankAccNo(String accountNo);

	@Query("SELECT ba FROM BankAccount ba WHERE ba.accountNo = ?1 AND ba.deletedFlag= ?2")
	BankAccount findByAccountNo(String accountNo, String deletedFlag);

	Page<BankAccount> findAll(Specification<BankAccount> spec, Pageable pageable);
}
