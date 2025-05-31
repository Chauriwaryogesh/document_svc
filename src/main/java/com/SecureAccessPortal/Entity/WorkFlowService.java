package com.SecureAccessPortal.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "T_WORKFLOW_SERVICE")
public class WorkFlowService {

	@Id
	@Column
	private String journeyId;

	@Lob
	@Column
	private String journeyDetails;
	
	@Column
	private String customerNo;
	
	@Column
	private String workItemNo;
	
	@Column
	private String createdBy;
	
	@Column
	private String createdTime;
	
	@Column
	private String isVerified;
	
	@Column
	private String emailId;
	
	@Column
	private String email;
	
	@Column
	private String phoneNumber;

	public String getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(String journeyId) {
		this.journeyId = journeyId;
	}

	public String getJourneyDetails() {
		return journeyDetails;
	}

	public void setJourneyDetails(String journeyDetails) {
		this.journeyDetails = journeyDetails;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getWorkItemNo() {
		return workItemNo;
	}

	public void setWorkItemNo(String workItemNo) {
		this.workItemNo = workItemNo;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(String isVerified) {
		this.isVerified = isVerified;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	

}
