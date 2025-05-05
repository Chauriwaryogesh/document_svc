package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.WorkFlowService;

public interface IWorkflowService extends JpaRepository<WorkFlowService, String> {

}
