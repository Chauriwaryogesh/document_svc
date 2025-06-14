package com.SecureAccessPortal.Modal;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class VerificationRecordDTO {
	private Long id;
	private String accountNo;
	private String customerNo;
	private String policyNumber;
	private String action;
	private String details;
	private String status;
	private String userCode;
	private LocalDateTime createdBy;
	private String deathStatus;
	private String identityStatus;
	private String sancStatus;

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public LocalDateTime getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(LocalDateTime createdBy) {
		this.createdBy = createdBy;
	}

	public String getDeathStatus() {
		return deathStatus;
	}

	public void setDeathStatus(String deathStatus) {
		this.deathStatus = deathStatus;
	}

	public String getIdentityStatus() {
		return identityStatus;
	}

	public void setIdentityStatus(String identityStatus) {
		this.identityStatus = identityStatus;
	}

	public String getSancStatus() {
		return sancStatus;
	}

	public void setSancStatus(String sancStatus) {
		this.sancStatus = sancStatus;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
}