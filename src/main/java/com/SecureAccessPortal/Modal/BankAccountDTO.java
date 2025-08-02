package com.SecureAccessPortal.Modal;

import java.math.BigDecimal;

public class BankAccountDTO {
	private Long id;
	private String accountNumber;
	private String ifscCode;
	private String bankName;
	private String accountType;
	private String status;
	private String customerNumber;
	private String holderName;
	private String policyNumber;
	private String policyStatus;
	private String lastVerificationDate; // ISO string
	private String createdBy;
	private String createdDate; // ISO string
	private String notes;
	private String accountHolderType;
	private String branchCode;
	private String swiftCode;
	private String paymentMethodStatus;
	private String lastPaymentDate; // ISO string
	private String amlStatus;
	private BigDecimal accountBalance;
	private String linkedPaymentMethod;
	private Integer verificationAttempts;

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
	public String getLastVerificationDate() {
		return lastVerificationDate;
	}

	/**
	 * @param lastVerificationDate the lastVerificationDate to set
	 */
	public void setLastVerificationDate(String lastVerificationDate) {
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
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
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
	 * @return the lastPaymentDate
	 */
	public String getLastPaymentDate() {
		return lastPaymentDate;
	}

	/**
	 * @param lastPaymentDate the lastPaymentDate to set
	 */
	public void setLastPaymentDate(String lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
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