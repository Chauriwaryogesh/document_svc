package com.example.dto;

public class DashboardStats {
	private int totalEmails;
	private int emailsSent;
	private int emailsInDb;
	private int slaCrossed;
	private int workQueue;

	// Constructors
	public DashboardStats() {
	}

	public DashboardStats(int totalEmails, int emailsSent, int emailsInDb, int slaCrossed, int workQueue) {
		this.totalEmails = totalEmails;
		this.emailsSent = emailsSent;
		this.emailsInDb = emailsInDb;
		this.slaCrossed = slaCrossed;
		this.workQueue = workQueue;
	}

	// Getters and setters
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

	public int getWorkQueue() {
		return workQueue;
	}

	public void setWorkQueue(int workQueue) {
		this.workQueue = workQueue;
	}
}
