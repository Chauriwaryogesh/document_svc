package com.SecureAccessPortal.Modal;

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
