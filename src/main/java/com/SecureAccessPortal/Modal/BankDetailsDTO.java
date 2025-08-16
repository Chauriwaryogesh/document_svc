package com.SecureAccessPortal.Modal;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BankDetailsDTO {
	private Long id;
	private String action;
	private String accountNumber;
	private String ifscCode;
	private String bankName;
	private String accountType;
	private String status;
	private String customerNumber; // From Customer entity
	private String customerName;
	private String holderName; // Derived from Customer's name
	private String policyNumber; // From Policy entity
	private String policyStatus; // From Policy entity
	private LocalDateTime lastVerificationDate;
	private String createdBy;
	private LocalDateTime createdDate;
	private String notes;
	private String accountHolderType;
	private String branchCode;
	private String swiftCode;
	private String paymentMethodStatus;
	private LocalDateTime lastPaymentDate;
	private String amlStatus;
	private BigDecimal accountBalance;
	private String linkedPaymentMethod;
	private Integer verificationAttempts;
	private String comment;
	private String verificationStatus;
	
	private String updatedBy;

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	private String accountHolderName;

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the ifscCode
	 */
	public String getIfscCode() {
		return ifscCode;
	}

	/**
	 * @param ifscCode the ifscCode to set
	 */
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
	 * @return the accountType
	 */
	public String getAccountType() {
		return accountType;
	}

	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the customerNumber
	 */
	public String getCustomerNumber() {
		return customerNumber;
	}

	/**
	 * @param customerNumber the customerNumber to set
	 */
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	/**
	 * @return the holderName
	 */
	public String getHolderName() {
		return holderName;
	}

	/**
	 * @param holderName the holderName to set
	 */
	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	/**
	 * @return the policyNumber
	 */
	public String getPolicyNumber() {
		return policyNumber;
	}

	/**
	 * @param policyNumber the policyNumber to set
	 */
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	/**
	 * @return the policyStatus
	 */
	public String getPolicyStatus() {
		return policyStatus;
	}

	/**
	 * @param policyStatus the policyStatus to set
	 */
	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}

	/**
	 * @return the lastVerificationDate
	 */
	public LocalDateTime getLastVerificationDate() {
		return lastVerificationDate;
	}

	/**
	 * @param lastVerificationDate the lastVerificationDate to set
	 */
	public void setLastVerificationDate(LocalDateTime lastVerificationDate) {
		this.lastVerificationDate = lastVerificationDate;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdDate
	 */
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return the accountHolderType
	 */
	public String getAccountHolderType() {
		return accountHolderType;
	}

	/**
	 * @param accountHolderType the accountHolderType to set
	 */
	public void setAccountHolderType(String accountHolderType) {
		this.accountHolderType = accountHolderType;
	}

	/**
	 * @return the branchCode
	 */
	public String getBranchCode() {
		return branchCode;
	}

	/**
	 * @param branchCode the branchCode to set
	 */
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	/**
	 * @return the swiftCode
	 */
	public String getSwiftCode() {
		return swiftCode;
	}

	/**
	 * @param swiftCode the swiftCode to set
	 */
	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	/**
	 * @return the paymentMethodStatus
	 */
	public String getPaymentMethodStatus() {
		return paymentMethodStatus;
	}

	/**
	 * @param paymentMethodStatus the paymentMethodStatus to set
	 */
	public void setPaymentMethodStatus(String paymentMethodStatus) {
		this.paymentMethodStatus = paymentMethodStatus;
	}

	/**
	 * @return the lastPaymentDate
	 */
	public LocalDateTime getLastPaymentDate() {
		return lastPaymentDate;
	}

	/**
	 * @param lastPaymentDate the lastPaymentDate to set
	 */
	public void setLastPaymentDate(LocalDateTime lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
	}

	/**
	 * @return the amlStatus
	 */
	public String getAmlStatus() {
		return amlStatus;
	}

	/**
	 * @param amlStatus the amlStatus to set
	 */
	public void setAmlStatus(String amlStatus) {
		this.amlStatus = amlStatus;
	}

	/**
	 * @return the accountBalance
	 */
	public BigDecimal getAccountBalance() {
		return accountBalance;
	}

	/**
	 * @param accountBalance the accountBalance to set
	 */
	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}

	/**
	 * @return the linkedPaymentMethod
	 */
	public String getLinkedPaymentMethod() {
		return linkedPaymentMethod;
	}

	/**
	 * @param linkedPaymentMethod the linkedPaymentMethod to set
	 */
	public void setLinkedPaymentMethod(String linkedPaymentMethod) {
		this.linkedPaymentMethod = linkedPaymentMethod;
	}

	/**
	 * @return the verificationAttempts
	 */
	public Integer getVerificationAttempts() {
		return verificationAttempts;
	}

	/**
	 * @param verificationAttempts the verificationAttempts to set
	 */
	public void setVerificationAttempts(Integer verificationAttempts) {
		this.verificationAttempts = verificationAttempts;
	}
}