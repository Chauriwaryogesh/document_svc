package com.example.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.ActivityDtls;

public interface ActivityDtlsRepo extends JpaRepository<ActivityDtls, Long> {

	@Query(value = "Select * from Activity_Monitor t where t.userId=?1", nativeQuery = true)
	List<ActivityDtls> findByUserId(String userId);

}
