package com.SecureAccessPortal.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.SecureAccessPortal.Entity.ClaimEntity;

public interface ClaimRepo  extends JpaRepository<ClaimEntity, String> {

	Page<ClaimEntity> findAll(Specification<ClaimEntity> spec, Pageable pagable);

}
