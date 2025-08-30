package com.SecureAccessPortal.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Bank")
public class Bank {

	/* ---------------- Primary Key ---------------- */
	@Id
	@Column(name = "bankId")
	private String bankId;

	/* ---------------- Relations ---------------- */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customerNo", referencedColumnName = "customerNo")
	private Customer customer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "policyNumber", referencedColumnName = "policyNumber")
	private Policy policy;

	/* ---------------- Core Account Details ---------------- */
	@Column(name = "accountNumber")
	private String accountNumber;

	@Column(name = "accountHolderName")
	private String accountHolderName;

	@Column(name = "accountHolderType")
	private String accountHolderType;

	@Column(name = "bankName")
	private String bankName;

	@Column(name = "branchCode")
	private String branchCode;

	@Column(name = "ifscCode")
	private String ifscCode;

	@Column(name = "swiftCode")
	private String swiftCode;

	@Column(name = "accountType")
	private String accountType;

	@Column(name = "currency", length = 3) // e.g. INR, USD
	private String currency;

	@Column(name = "accountBalance")
	private BigDecimal accountBalance;

	@Column(name = "isDefaultAccount")
	private Boolean isDefaultAccount = Boolean.FALSE;

	@Column(name = "accountOpeningDate")
	private LocalDateTime accountOpeningDate;

	@Column(name = "accountClosingDate")
	private LocalDateTime accountClosingDate;

	/* ---------------- KYC & Compliance ---------------- */
	@Column(name = "kycDocumentStatus")
	private String kycDocumentStatus; // PENDING, VERIFIED, REJECTED
	@Column(name = "kycStatus")
	private String kycStatus;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "kycDocument", columnDefinition = "BLOB")
	private byte[] kycDocument; // uploaded KYC document

	@Column(name = "amlStatus")
	private String amlStatus; // AML compliance check

	/* ---------------- Payment & Verification ---------------- */
	@Column(name = "paymentMethodStatus")
	private String paymentMethodStatus;

	@Column(name = "linkedPaymentMethod")
	private String linkedPaymentMethod;

	@Column(name = "lastPaymentDate")
	private LocalDateTime lastPaymentDate;

	@Column(name = "lastVerificationDate")
	private LocalDateTime lastVerificationDate;

	@Column(name = "verificationAttempts")
	private Integer verificationAttempts;

	@Column(name = "verifierComment")
	private String verifierComment;

	@Column(name = "customerComment", columnDefinition = "TEXT")
	private String customerComment;

	/* ---------------- Status Flags ---------------- */
	@Column(name = "status")
	private String status;

	@Column(name = "deletedFlag")
	private String deletedFlag;

	/* ---------------- Audit ---------------- */
	@Column(name = "createdBy")
	private String createdBy;

	@Column(name = "createdDate")
	private LocalDateTime createdDate;

	@Column(name = "updatedBy")
	private String updatedBy;

	@Column(name = "updatedDate")
	private LocalDateTime updatedDate;

	/* ---------------- Child Entities ---------------- */
	@OneToMany(mappedBy = "bank", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<SurrenderEntity> surrenderEntity = new ArrayList<>();

	@OneToMany(mappedBy = "bank", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ClaimEntity> claimEntity = new ArrayList<>();

	@OneToMany(mappedBy = "bank", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<VerificationRecord> verificationRecords = new ArrayList<>();

	@OneToMany(mappedBy = "bank", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Workitem> workitems = new ArrayList<>();

	@OneToMany(mappedBy = "bank", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Payments> payments = new ArrayList<>();

	public String getKycStatus() {
		return kycStatus;
	}

	public void setKycStatus(String kycStatus) {
		this.kycStatus = kycStatus;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
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

	public byte[] getKycDocument() {
		return kycDocument;
	}

	public void setKycDocument(byte[] kycDocument) {
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

	public List<SurrenderEntity> getSurrenderEntity() {
		return surrenderEntity;
	}

	public void setSurrenderEntity(List<SurrenderEntity> surrenderEntity) {
		this.surrenderEntity = surrenderEntity;
	}

	public List<ClaimEntity> getClaimEntity() {
		return claimEntity;
	}

	public void setClaimEntity(List<ClaimEntity> claimEntity) {
		this.claimEntity = claimEntity;
	}

	public List<VerificationRecord> getVerificationRecords() {
		return verificationRecords;
	}

	public void setVerificationRecords(List<VerificationRecord> verificationRecords) {
		this.verificationRecords = verificationRecords;
	}

	public List<Workitem> getWorkitems() {
		return workitems;
	}

	public void setWorkitems(List<Workitem> workitems) {
		this.workitems = workitems;
	}

	public List<Payments> getPayments() {
		return payments;
	}

	public void setPayments(List<Payments> payments) {
		this.payments = payments;
	}

}