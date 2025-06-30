package com.SecureAccessPortal.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SecureAccessPortal.Entity.BankAccount;
import com.SecureAccessPortal.Entity.Customer;
import com.SecureAccessPortal.Entity.Policy;
import com.SecureAccessPortal.Entity.Security;
import com.SecureAccessPortal.Entity.VerificationRecord;
import com.SecureAccessPortal.Entity.Workitem;
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
		private WorkItemRepo workItem;
		
		@Autowired
		private VerificationRecordRepo verfRec;
		
		@Autowired
		private BankAccountRepo bankAcc;
		
		@Autowired
		private IPolicyRepo policy;
		
		
		@Autowired
		private CustomerRepo customerRepo;

		public DashboardStats getDashboardStats(String userCode) {
			DashboardStats stats = new DashboardStats();
			List<Security> security = securityRepo.findAll();
			long users = security.stream().count();

			List<Workitem> workitem = workItem.findAll();
			long workItem = workitem.stream().count();

			List<Policy> pol = policy.findAll();
			long polCnt = pol.stream().count();

			List<BankAccount> bankAccount = bankAcc.findAll();
			long bank = bankAccount.stream().count();

			List<VerificationRecord> verf = verfRec.findAll();
			long ver = verf.stream().count();

			stats.setTotalBankAccounts(bank);
			stats.setTotalPolicy(polCnt);
			stats.setTotalUsers(users);
			stats.setTotalWorkItems(workItem);
			stats.setTotalVerificationRecords(ver);						
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
			               // LocalDateTime endTimeDateTime = LocalDateTime.parse(sec.getEndTime(), formatter);
			                // Convert to LocalDate for comparison
			               // LocalDate endTimeDate = endTimeDateTime.toLocalDate();
			                // Compare with threshold (10 days from now)
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


