package com.SecureAccessPortal.Modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardStats {
	private int totalEmails;
	private int emailsSent;
	private int emailsInDb;
	private int slaCrossed;
	private int documentUpload;

	// newly added for New email Counts tab.
	private int verPending;
	private int totalClosed;
	private int corrospodnace;

	// new ly added for Usercount
	private int activeUsers;
	private int inactiveUsers;
	private int emailVerified;
	private int userCodeVerified;
	private int adminAccess;

	// new fields
	private long totalWorkItems;
	private long totalPolicy;
	private long totalUsers;
	private long totalBankAccounts;
	private long totalVerificationRecords;
	
	//complaints
	private long complaintsTeam;
	private long adminTeam;
	private long escalationTeam;
	private long bancsTeam;
	
	
	//bank account
	private long totalAccounts;
	private long verifiedAccounts;
	private long pendingVerifications;
	private long rejectedBankAccount;
	
	//Payments;
	private long totalPolicies;
	private long totalCustomers;
	private long totalPayments;
	private long paid;
	private long unpaid;
	private long failed;
	private long cancelled;
	private long pending;
	public long getTotalPolicies() {
		return totalPolicies;
	}

	public void setTotalPolicies(long totalPolicies) {
		this.totalPolicies = totalPolicies;
	}

	public long getTotalCustomers() {
		return totalCustomers;
	}

	public void setTotalCustomers(long totalCustomers) {
		this.totalCustomers = totalCustomers;
	}

	public long getTotalPayments() {
		return totalPayments;
	}

	public void setTotalPayments(long totalPayments) {
		this.totalPayments = totalPayments;
	}

	public long getPaid() {
		return paid;
	}

	public void setPaid(long paid) {
		this.paid = paid;
	}

	public long getUnpaid() {
		return unpaid;
	}

	public void setUnpaid(long unpaid) {
		this.unpaid = unpaid;
	}

	public long getFailed() {
		return failed;
	}

	public void setFailed(long failed) {
		this.failed = failed;
	}

	public long getCancelled() {
		return cancelled;
	}

	public void setCancelled(long cancelled) {
		this.cancelled = cancelled;
	}

	public long getPending() {
		return pending;
	}

	public void setPending(long pending) {
		this.pending = pending;
	}

	public long getTotalAccounts() {
		return totalAccounts;
	}

	public void setTotalAccounts(long totalAccounts) {
		this.totalAccounts = totalAccounts;
	}

	public long getVerifiedAccounts() {
		return verifiedAccounts;
	}

	public void setVerifiedAccounts(long verifiedAccounts) {
		this.verifiedAccounts = verifiedAccounts;
	}

	public long getPendingVerifications() {
		return pendingVerifications;
	}

	public void setPendingVerifications(long pendingVerifications) {
		this.pendingVerifications = pendingVerifications;
	}

	public long getRejectedBankAccount() {
		return rejectedBankAccount;
	}

	public void setRejectedBankAccount(long rejectedBankAccount) {
		this.rejectedBankAccount = rejectedBankAccount;
	}

	public long getComplaintsTeam() {
		return complaintsTeam;
	}

	public void setComplaintsTeam(long complaintsTeam) {
		this.complaintsTeam = complaintsTeam;
	}

	public long getAdminTeam() {
		return adminTeam;
	}

	public void setAdminTeam(long adminTeam) {
		this.adminTeam = adminTeam;
	}

	public long getEscalationTeam() {
		return escalationTeam;
	}

	public void setEscalationTeam(long escalationTeam) {
		this.escalationTeam = escalationTeam;
	}

	public long getBancsTeam() {
		return bancsTeam;
	}

	public void setBancsTeam(long bancsTeam) {
		this.bancsTeam = bancsTeam;
	}

	public long getTotalWorkItems() {
		return totalWorkItems;
	}

	public void setTotalWorkItems(long totalWorkItems) {
		this.totalWorkItems = totalWorkItems;
	}

	public long getTotalPolicy() {
		return totalPolicy;
	}

	public void setTotalPolicy(long totalPolicy) {
		this.totalPolicy = totalPolicy;
	}

	public long getTotalUsers() {
		return totalUsers;
	}

	public void setTotalUsers(long totalUsers) {
		this.totalUsers = totalUsers;
	}

	public long getTotalBankAccounts() {
		return totalBankAccounts;
	}

	public void setTotalBankAccounts(long totalBankAccounts) {
		this.totalBankAccounts = totalBankAccounts;
	}

	public long getTotalVerificationRecords() {
		return totalVerificationRecords;
	}

	public void setTotalVerificationRecords(long totalVerificationRecords) {
		this.totalVerificationRecords = totalVerificationRecords;
	}

	public int getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(int activeUsers) {
		this.activeUsers = activeUsers;
	}

	public int getInactiveUsers() {
		return inactiveUsers;
	}

	public void setInactiveUsers(int inactiveUsers) {
		this.inactiveUsers = inactiveUsers;
	}

	public int getEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(int emailVerified) {
		this.emailVerified = emailVerified;
	}

	public int getUserCodeVerified() {
		return userCodeVerified;
	}

	public void setUserCodeVerified(int userCodeVerified) {
		this.userCodeVerified = userCodeVerified;
	}

	public int getAdminAccess() {
		return adminAccess;
	}

	public void setAdminAccess(int adminAccess) {
		this.adminAccess = adminAccess;
	}

	public int getVerPending() {
		return verPending;
	}

	public void setVerPending(int verPending) {
		this.verPending = verPending;
	}

	public int getTotalClosed() {
		return totalClosed;
	}

	public void setTotalClosed(int totalClosed) {
		this.totalClosed = totalClosed;
	}

	public int getCorrospodnace() {
		return corrospodnace;
	}

	public void setCorrospodnace(int corrospodnace) {
		this.corrospodnace = corrospodnace;
	}

	public int getTotalEmails() {
		return totalEmails;
	}

	public void setTotalEmails(int totalEmails) {
		this.totalEmails = totalEmails;
	}

	public int getEmailsSent() {
		return emailsSent;
	}

	public void setEmailsSent(int emailsSent) {
		this.emailsSent = emailsSent;
	}

	public int getEmailsInDb() {
		return emailsInDb;
	}

	public void setEmailsInDb(int emailsInDb) {
		this.emailsInDb = emailsInDb;
	}

	public int getSlaCrossed() {
		return slaCrossed;
	}

	public void setSlaCrossed(int slaCrossed) {
		this.slaCrossed = slaCrossed;
	}

	public int getDocumentUpload() {
		return documentUpload;
	}

	public void setDocumentUpload(int documentUpload) {
		this.documentUpload = documentUpload;
	}

}
