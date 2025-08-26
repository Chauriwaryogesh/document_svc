package com.SecureAccessPortal.Repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SecureAccessPortal.Entity.Customer;

public interface CustomerRepo extends JpaRepository<Customer, String> {

	@Query(value = "Select * from  customer c where c.email=?1 and c.deletedFlag=?2", nativeQuery = true)
	Optional<Customer> findByEmail(String email, String deletedFlag);

	@Query("SELECT c.customerNo FROM Customer c ORDER BY c.customerNo DESC LIMIT 1")
	String findTopCustomerNo();

	@Query(value = "Select * from  customer c where c.customerNo=?1", nativeQuery = true)
	Optional<Customer> findByCustomerNo(String customerNo);

	@Query(value = "Select * from  customer c where c.customerNo=?1 AND c.deletedFlag=?2", nativeQuery = true)
	Customer findByCustomerNoNew(String customerNo, String deletedFlag);

	@Query("SELECT c FROM Customer c WHERE c.userCode = :userCode")
	Optional<Customer> findByUserCode(String userCode);

	@Query("SELECT c FROM Customer c WHERE c.userCode = :userCode AND c.deletedFlag= :deletedFlag")
	Customer findByUserCodeAndDeletedFlagN(String userCode, String deletedFlag);

	@Query("SELECT COUNT(c) FROM Customer c WHERE c.userCode = :userCode")
	long findAllUsers(String userCode);

	Page<Customer> findAll(Specification<Customer> customerSpec, Pageable pageable);

}
