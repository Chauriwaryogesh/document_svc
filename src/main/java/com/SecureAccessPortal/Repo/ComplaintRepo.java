package com.SecureAccessPortal.Repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
			+"AND (:type IS NULL OR c.status = :type)"
			+ "AND c.deletedFlag= :deletedFlag")
	List<Complaint> findByCriteria(@Param("complaintId") String complaintId,
			@Param("complaintNumber") String complaintNumber, @Param("customerNo") String customerNo,
			@Param("policyNumber") String policyNumber, @Param("workitemNumber") String workitemNumber,
			@Param("type") String type,String deletedFlag);

	
	@Query("SELECT c FROM Complaint c WHERE c.complaintNumber = :complaintNumber AND c.deletedFlag= :deletedFlag")
	List<Complaint> findByComplaintNumber(String complaintNumber, String deletedFlag);


	@Query("SELECT c.complaintNumber FROM Complaint c WHERE c.complaintNumber LIKE 'CMPLT/%' ORDER BY c.complaintNumber DESC LIMIT 1")
	String findLatestComplaintNumber();


	Page<Complaint> findAll(Specification<Complaint> spec, Pageable pageable);

    @Query("SELECT c FROM Complaint c where c.customer.customerNo= :customerNo")
	List<Complaint> findByCustomerNo(String customerNo);

}
