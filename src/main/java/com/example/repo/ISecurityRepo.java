package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Security;

@Repository
public interface ISecurityRepo  extends JpaRepository<Security, Long>{

}
