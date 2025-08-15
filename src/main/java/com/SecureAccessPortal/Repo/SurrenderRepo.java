package com.SecureAccessPortal.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.SecureAccessPortal.Entity.SurrenderEntity;

public interface SurrenderRepo  extends JpaRepository<SurrenderEntity, String>{

	Page<SurrenderEntity> findAll(Specification<SurrenderEntity> spec, Pageable pagable);

}
