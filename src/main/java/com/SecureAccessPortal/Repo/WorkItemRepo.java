package com.SecureAccessPortal.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.SecureAccessPortal.Entity.Workitem;

public interface WorkItemRepo extends JpaRepository<Workitem, String> {

	@Query(value="Select * from WorkItem w where w.workItemRefNumber=?1 ", nativeQuery = true)
	Optional<Workitem> findByWiRefNum(String wiRefNum);
	
	@Query(value="Select * from WorkItem w where w.workItemRefNumber=?1 ", nativeQuery = true)
	Page<Workitem> findByWiRefNum(String wiRefNum, Pageable pageable);
	
    Page<Workitem> findAll(Specification<Workitem> spec, Pageable pageable);
    
    @Query("SELECT COUNT(w) FROM Workitem w WHERE w.status = :status")
    long countByStatus(@Param("status") String status);

    @Query("SELECT COUNT(w) FROM Workitem w WHERE w.queue = :queue")
    long countByQueue(@Param("queue") String queue);

    @Query("SELECT COUNT(w) FROM Workitem w")
    long countAll();

    // If userCode filtering is needed
    @Query("SELECT COUNT(w) FROM Workitem w WHERE w.status = :status AND w.userCode = :userCode")
    long countByStatusAndUserCode(@Param("status") String status, @Param("userCode") String userCode);

    @Query("SELECT COUNT(w) FROM Workitem w WHERE w.queue = :queue AND w.userCode = :userCode")
    long countByQueueAndUserCode(@Param("queue") String queue, @Param("userCode") String userCode);

    @Query("SELECT COUNT(w) FROM Workitem w WHERE w.userCode = :userCode")
    long countAllByUserCode(@Param("userCode") String userCode);


    @Query("SELECT w FROM Workitem w WHERE w.policy.policyNumber = (SELECT w2.policy.policyNumber FROM Workitem w2 WHERE w2.workItemRefNumber = :workItemRefNumber) ORDER BY w.createdTime DESC")
    Page<Workitem> findWorkItemsByRefNumberPolicy(@Param("workItemRefNumber") String workItemRefNumber, Pageable pageable);

    @Query("SELECT w FROM Workitem w WHERE w.customer.customerNo = (SELECT w2.customer.customerNo FROM Workitem w2 WHERE w2.workItemRefNumber = :workItemRefNumber) ORDER BY w.createdTime DESC")
    Page<Workitem> findWorkItemsByRefNumberCustomerNo(@Param("workItemRefNumber") String workItemRefNumber, Pageable pageable);

    @Query("SELECT COUNT(w) FROM Workitem w WHERE w.userCode = :userCode")
	long findAllWorkItems(String userCode);

    @Query("SELECT w FROM Workitem w WHERE w.customer.customerNo =:customerNo")
	List<Workitem> findByCustomerNo(String customerNo);
}
