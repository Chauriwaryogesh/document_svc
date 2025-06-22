package com.SecureAccessPortal.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SecureAccessPortal.Entity.Policy_Info;

public interface PolicyInfoRepo extends JpaRepository<Policy_Info, String> {

	@Query("SELECT p FROM Policy_Info p WHERE p.product_code = :productCode")
    List<Policy_Info> findByProductCode(String productCode);

    @Query("SELECT p FROM Policy_Info p ORDER BY p.product_code")
    List<Policy_Info> findAllByOrderByProductCode();

}
