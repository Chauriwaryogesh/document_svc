package com.example.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.BankAccount;

public interface BankAccountRepo extends JpaRepository<BankAccount, Long> {

	@Query("SELECT ba FROM BankAccount ba WHERE ba.policy.policyNumber = :policyNumber")
	List<BankAccount> findByPolicyNumber(@Param("policyNumber") String policyNumber);
}
