package com.SecureAccessPortal.Repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SecureAccessPortal.Entity.SurrenderEntity;

public interface SurrenderRepo  extends JpaRepository<SurrenderEntity, String>{

	Page<SurrenderEntity> findAll(Specification<SurrenderEntity> spec, Pageable pagable);

	@Query("SELECT s.surrRefNo FROM SurrenderEntity s WHERE s.surrRefNo LIKE 'SURR/%' ORDER BY s.surrRefNo DESC LIMIT 1")
	String findLatestByNotesId();

	@Query("SELECT s from  SurrenderEntity s WHERE s.surrRefNo =:surrenderRefNo AND s.deletedFlag =:deletedFlag")
	SurrenderEntity findBySurrenderRefNo(String surrenderRefNo, String deletedFlag);

	@Query("Select s from SurrenderEntity s WHERE s.customer.customerNo =:customerNo AND s.deletedFlag=:deletedFlag")
	List<SurrenderEntity> findBuCustomerNo(String customerNo,String deletedFlag);

}
