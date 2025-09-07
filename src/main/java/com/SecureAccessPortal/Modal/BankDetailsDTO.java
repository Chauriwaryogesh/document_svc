package com.SecureAccessPortal.Modal;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankDetailsDTO {

	private String bankId;
	private String action;
	// Relations
	private String customerNumber;
	private String customerName;
	private String policyNumber;
	private String policyStatus;

	// Core Account Details
	private String accountNumber;
	private String accountHolderName;
	private String accountHolderType;
	private String bankName;
	private String branchCode;
	private String ifscCode;
	private String swiftCode;
	private String accountType;
	private String currency; // e.g. INR, USD
	private BigDecimal accountBalance;
	private Boolean isDefaultAccount;
	private LocalDateTime accountOpeningDate;
	private LocalDateTime accountClosingDate;

	// KYC & Compliance
	private String kycDocumentStatus; // PENDING, VERIFIED, REJECTED
	private String kycStatus; // "Y" or "N"
	private String kycDocument; // uploaded KYC document
	private String amlStatus;

	// Payment & Verification
	private String paymentMethodStatus;
	private String linkedPaymentMethod;
	private LocalDateTime lastPaymentDate;
	private LocalDateTime lastVerificationDate;
	private Integer verificationAttempts;
	private String verifierComment;
	private String customerComment;

	// Status Flags
	private String status;
	private String deletedFlag;

	// Audit
	private String createdBy;
	private LocalDateTime createdDate;
	private String updatedBy;
	private LocalDateTime updatedDate;

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getPolicyStatus() {
		return policyStatus;
	}

	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}

	

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

	public String getAccountHolderType() {
		return accountHolderType;
	}

	public void setAccountHolderType(String accountHolderType) {
		this.accountHolderType = accountHolderType;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}

	public Boolean getIsDefaultAccount() {
		return isDefaultAccount;
	}

	public void setIsDefaultAccount(Boolean isDefaultAccount) {
		this.isDefaultAccount = isDefaultAccount;
	}

	public LocalDateTime getAccountOpeningDate() {
		return accountOpeningDate;
	}

	public void setAccountOpeningDate(LocalDateTime accountOpeningDate) {
		this.accountOpeningDate = accountOpeningDate;
	}

	public LocalDateTime getAccountClosingDate() {
		return accountClosingDate;
	}

	public void setAccountClosingDate(LocalDateTime accountClosingDate) {
		this.accountClosingDate = accountClosingDate;
	}

	public String getKycDocumentStatus() {
		return kycDocumentStatus;
	}

	public void setKycDocumentStatus(String kycDocumentStatus) {
		this.kycDocumentStatus = kycDocumentStatus;
	}

	public String getKycStatus() {
		return kycStatus;
	}

	public void setKycStatus(String kycStatus) {
		this.kycStatus = kycStatus;
	}

	public String getKycDocument() {
		return kycDocument;
	}

	public void setKycDocument(String kycDocument) {
		this.kycDocument = kycDocument;
	}

	public String getAmlStatus() {
		return amlStatus;
	}

	public void setAmlStatus(String amlStatus) {
		this.amlStatus = amlStatus;
	}

	public String getPaymentMethodStatus() {
		return paymentMethodStatus;
	}

	public void setPaymentMethodStatus(String paymentMethodStatus) {
		this.paymentMethodStatus = paymentMethodStatus;
	}

	public String getLinkedPaymentMethod() {
		return linkedPaymentMethod;
	}

	public void setLinkedPaymentMethod(String linkedPaymentMethod) {
		this.linkedPaymentMethod = linkedPaymentMethod;
	}

	public LocalDateTime getLastPaymentDate() {
		return lastPaymentDate;
	}

	public void setLastPaymentDate(LocalDateTime lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
	}

	public LocalDateTime getLastVerificationDate() {
		return lastVerificationDate;
	}

	public void setLastVerificationDate(LocalDateTime lastVerificationDate) {
		this.lastVerificationDate = lastVerificationDate;
	}

	public Integer getVerificationAttempts() {
		return verificationAttempts;
	}

	public void setVerificationAttempts(Integer verificationAttempts) {
		this.verificationAttempts = verificationAttempts;
	}

	public String getVerifierComment() {
		return verifierComment;
	}

	public void setVerifierComment(String verifierComment) {
		this.verifierComment = verifierComment;
	}

	public String getCustomerComment() {
		return customerComment;
	}

	public void setCustomerComment(String customerComment) {
		this.customerComment = customerComment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

}