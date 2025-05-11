package com.example.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.Email;

public interface IEmailRepo  extends JpaRepository<Email, Long>{
	
	@Query(value ="Select * from  Email_Service  t where t.email= ?1",nativeQuery = true)
	List<Email> findByEmail(String email);

}
