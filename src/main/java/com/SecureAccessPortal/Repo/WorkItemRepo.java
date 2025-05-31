package com.SecureAccessPortal.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SecureAccessPortal.Entity.Workitem;

public interface WorkItemRepo extends JpaRepository<Workitem, String> {

	@Query(value="Select * from WorkItem w where w.workItemRefNumber=?1 ", nativeQuery = true)
	Optional<Workitem> findByWiRefNum(String wiRefNum);

//	@Modifying
//	@Query(value = "INSERT INTO WorkItem () VALUES ()", nativeQuery = true)
//	void insertSequenceRow();
//
//	@Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
//	Long getLastInsertedId();

}
