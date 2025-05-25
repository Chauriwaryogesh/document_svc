package com.example.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.Customer;

public interface CustomerRepo extends JpaRepository<Customer, String> {

	@Query(value= "Select * from  customer c where c.email=?1", nativeQuery = true)
	Optional<Customer> findByEmail(String email);

		@Query("SELECT c.customerNo FROM Customer c ORDER BY c.customerNo DESC LIMIT 1")
		String findTopCustomerNo();

		@Query(value= "Select * from  customer c where c.customerNo=?1", nativeQuery = true)
		Optional<Customer> findByCustomerNo(String customerNo);


}
