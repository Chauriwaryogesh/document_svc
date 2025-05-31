package com.SecureAccessPortal.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SecureAccessPortal.Entity.OtpStore;

@Repository
public interface IOtpServiceDB extends JpaRepository<OtpStore, String>  {

}
