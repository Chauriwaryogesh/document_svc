package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.WorkItem;

public interface IWorkItemService extends JpaRepository<WorkItem, String> {

//	@Modifying
//	@Query(value = "INSERT INTO WorkItem () VALUES ()", nativeQuery = true)
//	void insertSequenceRow();
//
//	@Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
//	Long getLastInsertedId();

}
