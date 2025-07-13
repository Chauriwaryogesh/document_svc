package com.SecureAccessPortal.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SecureAccessPortal.Entity.Customer;
import com.SecureAccessPortal.Entity.Security;
import com.SecureAccessPortal.Modal.DashboardStats;
import com.SecureAccessPortal.Repo.BankAccountRepo;
import com.SecureAccessPortal.Repo.CustomerRepo;
import com.SecureAccessPortal.Repo.IPolicyRepo;
import com.SecureAccessPortal.Repo.ISecurityRepo;
import com.SecureAccessPortal.Repo.VerificationRecordRepo;
import com.SecureAccessPortal.Repo.WorkItemRepo;


	@Service
	public class DashboardService {
		@Autowired
		private ISecurityRepo securityRepo;
		
		@Autowired
		private WorkItemRepo workitemRepository;
		
		@Autowired
		private VerificationRecordRepo verificationRecordRepository;
		
		@Autowired
		private BankAccountRepo bankAccountRepository;
		
		@Autowired
		private IPolicyRepo policyRepository;
		
		@Autowired
		private CustomerRepo customerRepository;
		
		
		@Autowired
		private CustomerRepo customerRepo;

		public DashboardStats getDashboardStats(String userCode) {
	        DashboardStats stats = new DashboardStats();

	        stats.setTotalUsers(customerRepository.findAllUsers(userCode));
	        stats.setTotalWorkItems(workitemRepository.findAllWorkItems(userCode));
	        stats.setTotalPolicy(policyRepository.findAllPolicies(userCode));
	        stats.setTotalBankAccounts(bankAccountRepository.findAllBankAcc(userCode));
	        stats.setTotalVerificationRecords(verificationRecordRepository.findAllRecords(userCode));

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
			                LocalDate thresholdDate = LocalDate.now().plusDays(10);
			                return sec.getEndTime().isAfter(thresholdDate);
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

		public DashboardStats getUsersCountStats(String userCode) {
			DashboardStats stats = new DashboardStats();
			try {
			List<Security> security = securityRepo.findAll();
			List<Customer> customer = customerRepo.findAll();
			long activeUsers = security.stream().count();
			long inactiveUsers = security.stream().filter(sec -> sec.getIsEmailVerified().equalsIgnoreCase("N")
					&& sec.getIsUserCodeVerified().equalsIgnoreCase("N")).count();
			long emailVerified = security.stream().filter(sec -> sec.getIsEmailVerified().equalsIgnoreCase("Y"))
					.count();
			long userCodeVerified = security.stream().filter(sec -> sec.getIsUserCodeVerified().equalsIgnoreCase("Y"))
					.count();
			long adminAccess = customer.stream().filter(sec -> sec.getAdminAccess() != null && sec.getAdminAccess().equalsIgnoreCase("Y")).count();
			stats.setActiveUsers((int) activeUsers);
			stats.setInactiveUsers((int) inactiveUsers);
			stats.setEmailVerified((int) emailVerified);
			stats.setUserCodeVerified((int) userCodeVerified);
			stats.setAdminAccess((int) adminAccess);
			}catch(Exception e) {
				e.printStackTrace();
			}
			return stats;
		}
	}


