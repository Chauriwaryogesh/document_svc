package com.SecureAccessPortal.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SecureAccessPortal.Entity.CapturePhoto;
import com.SecureAccessPortal.Entity.Security;
import com.SecureAccessPortal.Modal.DashboardStats;
import com.SecureAccessPortal.Repo.ICapturePhtoRepo;
import com.SecureAccessPortal.Repo.ISecurityRepo;


	@Service
	public class DashboardService {
		
		@Autowired
		private ICapturePhtoRepo docRepo;
		
		@Autowired
		private ISecurityRepo securityRepo;

	    public DashboardStats getDashboardStats(String userCode) {
	        // Fetch stats for the dashboard based on the userCode
	    	List<CapturePhoto> documents = docRepo.findAll();
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
	    	 long totalEmails = security.stream().map(mail -> mail.getEmail()).count();
	        DashboardStats stats = new DashboardStats();
	        // Example data
	        stats.setTotalEmails((int)totalEmails);
	        stats.setSlaCrossed((int)expiryDaysCount);
	        stats.setDocumentUpload((int) docsCount);
	        stats.setEmailsInDb(38);
	        stats.setEmailsSent(44);
	        
	        return stats;
	    }

		public DashboardStats getEmailCountStats(String userCode) {
			DashboardStats stats = new DashboardStats();
		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			List<Security> security = securityRepo.findAll();
			long expiryDaysCount = security.stream()
			        .filter(sec -> sec.getEndTime() != null) // Filter out null endTime
			        .filter(sec -> {
			            try {
			                // Parse endTime as LocalDateTime
			                LocalDateTime endTimeDateTime = LocalDateTime.parse(sec.getEndTime(), formatter);
			                // Convert to LocalDate for comparison
			                LocalDate endTimeDate = endTimeDateTime.toLocalDate();
			                // Compare with threshold (10 days from now)
			                LocalDate thresholdDate = LocalDate.now().plusDays(10);
			                return endTimeDate.isAfter(thresholdDate);
			            } catch (DateTimeParseException e) {
			                System.err.println("Error parsing endTime: " + sec.getEndTime() + " for user " + sec.getUserCode() + " - " + e.getMessage());
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


