package com.example.service;

import org.springframework.stereotype.Service;

import com.example.dto.DashboardStats;


	@Service
	public class DashboardService {

	    public DashboardStats getDashboardStats(String userId) {
	        // Fetch stats for the dashboard based on the userId
	        DashboardStats stats = new DashboardStats();
	        // Example data
	        stats.setTotalEmails(100);
	        stats.setSlaCrossed(10);
	        return stats;
	    }
	}


