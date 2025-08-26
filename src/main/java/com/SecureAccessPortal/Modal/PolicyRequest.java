package com.SecureAccessPortal.Modal;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PolicyRequest {
	private String policyNumber; // maps to entity: policyNumber
	private String policyName; // maps to entity: policyName
	private String productCode; // maps to entity: productCode
	private String polCompanyName; // maps to entity: polCompanyName

	private String createdBy; // maps to entity: createdBy
	private String updatedBy; // maps to entity: updatedBy
	private String deletedFlag; // maps to entity: deletedFlag
	private String policyType; // maps to entity: policyType
	private String policyStatus;

	private LocalDateTime createdDate; // maps to entity: createdBy
	private LocalDateTime updatedDate;

	private LocalDateTime premiumDueDate; // maps to entity: premiumDueDate
	private String beneficiaryName; // maps to entity: beneficiaryName
	private String beneficiaryRelationship; // maps to entity: beneficiaryRelationship
	private String policyTerm; // maps to entity: policyTerm
	private String smokerStatus; // maps to entity: smokerStatus

	private int totalInstallments; // maps to entity: totalInstallments
	private BigDecimal totalAmount; // maps to entity: totalAmount
	private BigDecimal monthlyInstallment; // maps to entity: monthlyInstallmen
	private BigDecimal totalPaidAmount; // maps to entity: totalAmount
	private BigDecimal totalUnPaidAmount;
	private int  totalPaidInstallments; // maps to entity: totalAmount
	private int  totalUnPaidInstallments;

	private LocalDateTime policyStartDate; // maps to entity: policyStartDate
	private LocalDateTime policyEndDate; // maps to entity: policyEndDate

	private String beneficiaryIdentityNumber; // maps to entity: beneficiaryIdentityNumber
	private String beneficiaryStatus; // maps to entity: beneficiaryStatus
	private String beneficiaryContactNumber; // maps to entity: beneficiaryContactNumber

	private String reason; // maps to entity: reson

	private String userCode;
	private String customerNo;
	// ---------------- Extra Fields (Not in Entity) ----------------
	private String fcuStatus; // extra field
	private String associatedpolicyCount; // extra field
	// extra field
	private String customerName; // extra field
	private String phoneNumber; // extra field
	private String email; // extra field
	private String dateOfBirth; // extra field
	private String gender;

	public BigDecimal getTotalPaidAmount() {
		return totalPaidAmount;
	}

	public void setTotalPaidAmount(BigDecimal totalPaidAmount) {
		this.totalPaidAmount = totalPaidAmount;
	}

	public BigDecimal getTotalUnPaidAmount() {
		return totalUnPaidAmount;
	}

	public void setTotalUnPaidAmount(BigDecimal totalUnPaidAmount) {
		this.totalUnPaidAmount = totalUnPaidAmount;
	}

	public int getTotalPaidInstallments() {
		return totalPaidInstallments;
	}

	public void setTotalPaidInstallments(int totalPaidInstallments) {
		this.totalPaidInstallments = totalPaidInstallments;
	}

	public int getTotalUnPaidInstallments() {
		return totalUnPaidInstallments;
	}

	public void setTotalUnPaidInstallments(int totalUnPaidInstallments) {
		this.totalUnPaidInstallments = totalUnPaidInstallments;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getPolCompanyName() {
		return polCompanyName;
	}

	public void setPolCompanyName(String polCompanyName) {
		this.polCompanyName = polCompanyName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public String getPolicyStatus() {
		return policyStatus;
	}

	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}

	public LocalDateTime getPremiumDueDate() {
		return premiumDueDate;
	}

	public void setPremiumDueDate(LocalDateTime premiumDueDate) {
		this.premiumDueDate = premiumDueDate;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getBeneficiaryRelationship() {
		return beneficiaryRelationship;
	}

	public void setBeneficiaryRelationship(String beneficiaryRelationship) {
		this.beneficiaryRelationship = beneficiaryRelationship;
	}

	public String getPolicyTerm() {
		return policyTerm;
	}

	public void setPolicyTerm(String policyTerm) {
		this.policyTerm = policyTerm;
	}

	public String getSmokerStatus() {
		return smokerStatus;
	}

	public void setSmokerStatus(String smokerStatus) {
		this.smokerStatus = smokerStatus;
	}

	public int getTotalInstallments() {
		return totalInstallments;
	}

	public void setTotalInstallments(int totalInstallments) {
		this.totalInstallments = totalInstallments;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getMonthlyInstallment() {
		return monthlyInstallment;
	}

	public void setMonthlyInstallment(BigDecimal monthlyInstallment) {
		this.monthlyInstallment = monthlyInstallment;
	}

	public String getBeneficiaryIdentityNumber() {
		return beneficiaryIdentityNumber;
	}

	public void setBeneficiaryIdentityNumber(String beneficiaryIdentityNumber) {
		this.beneficiaryIdentityNumber = beneficiaryIdentityNumber;
	}

	public String getBeneficiaryStatus() {
		return beneficiaryStatus;
	}

	public void setBeneficiaryStatus(String beneficiaryStatus) {
		this.beneficiaryStatus = beneficiaryStatus;
	}

	public String getBeneficiaryContactNumber() {
		return beneficiaryContactNumber;
	}

	public void setBeneficiaryContactNumber(String beneficiaryContactNumber) {
		this.beneficiaryContactNumber = beneficiaryContactNumber;
	}

	public LocalDateTime getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(LocalDateTime policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public LocalDateTime getPolicyEndDate() {
		return policyEndDate;
	}

	public void setPolicyEndDate(LocalDateTime policyEndDate) {
		this.policyEndDate = policyEndDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getFcuStatus() {
		return fcuStatus;
	}

	public void setFcuStatus(String fcuStatus) {
		this.fcuStatus = fcuStatus;
	}

	public String getAssociatedpolicyCount() {
		return associatedpolicyCount;
	}

	public void setAssociatedpolicyCount(String associatedpolicyCount) {
		this.associatedpolicyCount = associatedpolicyCount;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public boolean isBlank() {
		return (policyNumber == null || policyNumber.isEmpty()) && (customerNo == null || customerNo.isEmpty());
		// add other relevant field checks as needed
	}

}
