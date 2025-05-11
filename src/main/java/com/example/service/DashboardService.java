package com.example.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.DashboardStats;
import com.example.entity.Document;
import com.example.entity.Security;
import com.example.repo.IDocumentRepo;
import com.example.repo.ISecurityRepo;


	@Service
	public class DashboardService {
		
		@Autowired
		private IDocumentRepo docRepo;
		
		@Autowired
		private ISecurityRepo securityRepo;

	    public DashboardStats getDashboardStats(String userId) {
	        // Fetch stats for the dashboard based on the userId
	    	List<Document> documents = docRepo.findAll();
	    	 long docsCount=documents.stream().count();
	    	
	    	 List<Security> security = securityRepo.findAll();
	    	 
	    	 long expiryDaysCount = security.stream()
	                 .filter(sec -> sec.getEndTime() != null)
	                 .filter(sec -> {
	                     try {
	                         LocalDate endTimeDate = LocalDate.parse(sec.getEndTime());
	                         LocalDate now = LocalDate.now();
	                         return endTimeDate.isAfter(now);
	                     } catch (Exception e) {
	                         System.err.println("Error parsing date: " + sec.getEndTime() + " - " + e.getMessage());
	                         return false;
	                     }
	                 })
	                 .count();
	        DashboardStats stats = new DashboardStats();
	        // Example data
	        stats.setTotalEmails(100);
	        stats.setSlaCrossed((int)expiryDaysCount);
	        stats.setDocumentUpload((int) docsCount);
	        stats.setEmailsInDb(20);
	        stats.setEmailsSent(20);
	        
	        return stats;
	    }

		public DashboardStats getEmailCountStats(String userId) {
			DashboardStats stats = new DashboardStats();
			List<Security> security = securityRepo.findAll();

			long expiryDaysCount = security.stream()
				    .filter(sec -> sec.getEndTime() != null)
				    .filter(sec -> {
				        try {
				            LocalDate endTimeDate = LocalDate.parse(sec.getEndTime());
				            LocalDate thresholdDate = LocalDate.now().plusDays(10);
				            return endTimeDate.isAfter(thresholdDate); // strictly more than 10 days
				        } catch (Exception e) {
				            System.err.println("Error parsing date: " + sec.getEndTime() + " - " + e.getMessage());
				            return false;
				        }
				    })
				    .count();


			long totalEmails = security.stream().map(mail -> mail.getEmail()).count();
			long totalClosed = security.stream().filter(sec -> sec.getIsEmailVerified().equalsIgnoreCase("Y")
					&& sec.getIsUserCodeVerified().equalsIgnoreCase("Y")).count();
			long verPending = security.stream().filter(sec -> !sec.getIsEmailVerified().equalsIgnoreCase("Y")
					|| !sec.getIsUserCodeVerified().equalsIgnoreCase("Y")).count();
		
			stats.setTotalEmails((int) totalEmails);
			stats.setCorrospodnace((int) expiryDaysCount);
			stats.setTotalClosed((int) totalClosed);
			stats.setVerPending((int) verPending);
			return stats;

		}
	}


