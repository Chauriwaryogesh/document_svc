package com.SecureAccessPortal.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SecureAccessPortal.Entity.LoginHistory;

public interface LoginHistoryRepo extends JpaRepository<LoginHistory, Long> {

}
