package com.SecureAccessPortal.Modal;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class VerificationRecordDTO {
	private String id;
	private String accountNo;
	private String customerNo;
	private String policyNumber;
	private String action;
	private String details;
	private String status;
	private String userCode;
	private String createdBy;
	private LocalDateTime createdTime;
	private String updatedBy;
	private LocalDateTime updatedTime;
	private String deathCertNo;
	private String deathStatus;
	private byte[] deathCertificate;
	private String idCertNo;
	private String identityStatus;
	private byte[] idCertificate;
	private String sanctionsCertNo;
	private String sancStatus;
	private byte[] sanctionsCertificate;

	public String getDeathCertNo() {
		return deathCertNo;
	}

	public void setDeathCertNo(String deathCertNo) {
		this.deathCertNo = deathCertNo;
	}

	public String getIdCertNo() {
		return idCertNo;
	}

	public void setIdCertNo(String idCertNo) {
		this.idCertNo = idCertNo;
	}

	public String getSanctionsCertNo() {
		return sanctionsCertNo;
	}

	public void setSanctionsCertNo(String sanctionsCertNo) {
		this.sanctionsCertNo = sanctionsCertNo;
	}

	public byte[] getDeathCertificate() {
		return deathCertificate;
	}

	public void setDeathCertificate(byte[] deathCertificate) {
		this.deathCertificate = deathCertificate;
	}

	public byte[] getIdCertificate() {
		return idCertificate;
	}

	public void setIdCertificate(byte[] idCertificate) {
		this.idCertificate = idCertificate;
	}

	public byte[] getSanctionsCertificate() {
		return sanctionsCertificate;
	}

	public void setSanctionsCertificate(byte[] sanctionsCertificate) {
		this.sanctionsCertificate = sanctionsCertificate;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public LocalDateTime getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(LocalDateTime updatedTime) {
		this.updatedTime = updatedTime;
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