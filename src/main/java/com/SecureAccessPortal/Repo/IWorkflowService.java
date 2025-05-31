package com.SecureAccessPortal.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SecureAccessPortal.Entity.WorkFlowService;

public interface IWorkflowService extends JpaRepository<WorkFlowService, String> {

}
