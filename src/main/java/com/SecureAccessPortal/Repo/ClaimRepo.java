package com.SecureAccessPortal.Repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SecureAccessPortal.Entity.ClaimEntity;

public interface ClaimRepo  extends JpaRepository<ClaimEntity, String> {

	Page<ClaimEntity> findAll(Specification<ClaimEntity> spec, Pageable pagable);
	
	@Query("SELECT c.claimRefNo FROM ClaimEntity c WHERE c.claimRefNo LIKE 'CLAIM/%' ORDER BY c.claimRefNo DESC LIMIT 1")
	String findLatestByNotesId();
	@Query("Select s from ClaimEntity s WHERE s.customer.customerNo =:customerNo AND s.deletedFlag=:deletedFlag")
	List<ClaimEntity> findBuCustomerNo(String customerNo, String deletedFlag);

	@Query("SELECT c FROM ClaimEntity c WHERE c.claimRefNo =:claimRefNo AND c.deletedFlag=:deletedFlag")
	ClaimEntity findByClaimRefNo(String claimRefNo, String deletedFlag);

}
