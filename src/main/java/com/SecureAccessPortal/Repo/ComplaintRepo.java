package com.SecureAccessPortal.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.SecureAccessPortal.Entity.Complaint;

@Repository
public interface ComplaintRepo extends JpaRepository<Complaint, String> {

	@Query("SELECT c FROM Complaint c " + "LEFT JOIN c.customer cust " + "LEFT JOIN c.policy pol "
			+ "LEFT JOIN c.workitem wi " + "WHERE (:complaintId IS NULL OR c.complaintId = :complaintId) "
			+ "AND (:complaintNumber IS NULL OR c.complaintNumber = :complaintNumber) "
			+ "AND (:customerNo IS NULL OR cust.customerNo = :customerNo) "
			+ "AND (:policyNumber IS NULL OR pol.policyNumber = :policyNumber) "
			+ "AND (:workitemNumber IS NULL OR wi.workItemRefNumber = :workitemNumber)"
			+ "AND c.deletedFlag= :deletedFlag")
	List<Complaint> findByCriteria(@Param("complaintId") String complaintId,
			@Param("complaintNumber") String complaintNumber, @Param("customerNo") String customerNo,
			@Param("policyNumber") String policyNumber, @Param("workitemNumber") String workitemNumber,
			String deletedFlag);

	
	@Query("SELECT c FROM Complaint c WHERE c.complaintNumber = :complaintNumber AND c.deletedFlag= :deletedFlag")
	List<Complaint> findByComplaintNumber(String complaintNumber, String deletedFlag);


	@Query("SELECT c.complaintNumber FROM Complaint c WHERE c.complaintNumber LIKE 'CMPLT/%' ORDER BY c.complaintNumber DESC LIMIT 1")
	String findLatestComplaintNumber();

}
