package com.example.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.WorkItem;

public interface IWorkItemService extends JpaRepository<WorkItem, String> {

	@Query(value="Select * from WorkItem w where w.workItemRefNumber=?1 ", nativeQuery = true)
	Optional<WorkItem> findByWiRefNum(String wiRefNum);

//	@Modifying
//	@Query(value = "INSERT INTO WorkItem () VALUES ()", nativeQuery = true)
//	void insertSequenceRow();
//
//	@Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
//	Long getLastInsertedId();

}
