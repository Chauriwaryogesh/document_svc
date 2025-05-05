package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.DashboardStats;
import com.example.entity.Document;
import com.example.repo.IDocumentRepo;


	@Service
	public class DashboardService {
		
		@Autowired
		private IDocumentRepo docRepo;

	    public DashboardStats getDashboardStats(String userId) {
	        // Fetch stats for the dashboard based on the userId
	    	List<Document> documents = docRepo.findAll();
	    	 long docsCount=documents.stream().count();
	    	
	        DashboardStats stats = new DashboardStats();
	        // Example data
	        stats.setTotalEmails(100);
	        stats.setSlaCrossed(10);
	        stats.setDocumentUpload((int) docsCount);
	        stats.setEmailsInDb(20);
	        stats.setEmailsSent(20);
	        
	        return stats;
	    }
	}


