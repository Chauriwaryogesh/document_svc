package com.SecureAccessPortal.Modal;

public class DashboardStats {
	private int totalEmails;
	private int emailsSent;
	private int emailsInDb;
	private int slaCrossed;
	private int documentUpload;

	//newly added for New email Counts tab.
	private int verPending;
	private int totalClosed;
	private int corrospodnace;

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
