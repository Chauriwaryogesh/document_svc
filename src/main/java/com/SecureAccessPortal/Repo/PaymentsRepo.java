package com.SecureAccessPortal.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SecureAccessPortal.Entity.Payments;

public interface PaymentsRepo  extends JpaRepository<Payments, String>{

	@Query(value="select * from Payments u where u.customerNo=?1",nativeQuery = true)
	List<Payments> findByCustomerNumber(String customerNumber);
	
	@Query(value ="select * from Payments u where u.policyNumber=?1",nativeQuery = true)
	List<Payments> findByPolicyPolicyNumber(String policyNumber);

	@Query(value ="select * from Payments u where u.transaction_id=?1",nativeQuery = true)
	Optional<Payments> findByTxnId(String transactionId);

}
