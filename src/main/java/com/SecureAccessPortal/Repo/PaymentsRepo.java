package com.SecureAccessPortal.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SecureAccessPortal.Entity.Payments;

public interface PaymentsRepo extends JpaRepository<Payments, String> {

//	@Query(value="select * from Payments u where u.customerNo=?1",nativeQuery = true)
//	List<Payments> findByCustomerNumber(String customerNumber);
//	
//	@Query(value ="select * from Payments u where u.policyNumber=?1",nativeQuery = true)
//	List<Payments> findByPolicyPolicyNumber(String policyNumber);
//
//	@Query(value ="select * from Payments u where u.transaction_id=?1",nativeQuery = true)
//	Optional<Payments> findByTxnId(String transactionId);

	Page<Payments> findByPolicyPolicyNumber(String policyNumber, Pageable pageable);

	Page<Payments> findByCustomerCustomerNo(String customerNumber, Pageable pageable);

	Optional<Payments> findById(String paymentId);

	Optional<Payments> findByTransactionId(String transaction_id);

	List<Payments> findAll(Specification<Payments> spec, Sort sort);

	@Query("SELECT p FROM Payments  p WHERE p.status= :status ORDER BY p.createdTime DESC")
	List<Payments> findByStatus(String status);
	
	@Query("SELECT p FROM Payments  p WHERE p.paymentId= :paymentId")
	Payments findByPaymentId(String pymentId);

	@Query("SELECT p FROM Payments  p WHERE p.customer.customerNo= :number")
	List<Payments> findByCustomerNo(String number);
	
	@Query("SELECT p FROM Payments  p WHERE p.paymentId= :paymentId")
	Optional< Payments> findByPaymentIs(String pymentId);

}
