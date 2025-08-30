package com.SecureAccessPortal.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "CLAIM_POLICY")
public class ClaimEntity {

	@Id
	@Column
	private String claimRefNo;

	@Column
	private String claimStatus;

	@Column
	private BigDecimal claimAmount;

	@Column
	private String claimBy;

	@Column
	private String claimType;
	
	@Column
	private String claimReason;

	@Column
	private LocalDateTime claimDate;

	@Column
	private String createdBy;
	@Column
	private LocalDateTime createdDate;
	@Column
	private String updatedBy;
	@Column
	private LocalDateTime updatedDate;
	@Column
	private String deletedFlag;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customerNo", referencedColumnName = "customerNo")
	private Customer customer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "policyNumber", referencedColumnName = "policyNumber")
	private Policy policy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_id", referencedColumnName = "payment_id")
	private Payments payment;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accountNumber", referencedColumnName = "accountNumber")
	private Bank bank;
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] verificationDocument;
	@Column
	private String verificationDocumentName;
	@Column
	private String verificationStatus;
	@Column
	private String verificationComment;
	
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] bankDocument;
	@Column
	private String bankDocumentName;
	@Column
	private String bankDocumentStatus;
	
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] idDocument;
	@Column
	private String idDocumentName;
	@Column
	private String idDocumentStatus;
	
	
	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public byte[] getIdDocument() {
		return idDocument;
	}

	public void setIdDocument(byte[] idDocument) {
		this.idDocument = idDocument;
	}
	public String getIdDocumentName() {
		return idDocumentName;
	}

	public void setIdDocumentName(String idDocumentName) {
		this.idDocumentName = idDocumentName;
	}

	public String getIdDocumentStatus() {
		return idDocumentStatus;
	}

	public void setIdDocumentStatus(String idDocumentStatus) {
		this.idDocumentStatus = idDocumentStatus;
	}

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] otherSupportingDocument;
	@Column
	private String otherSupportingDocumentName;
	@Column
	private String otherSupportingDocumentVerificationStatus;

	public BigDecimal getClaimAmount() {
		return claimAmount;
	}

	public void setClaimAmount(BigDecimal claimAmount) {
		this.claimAmount = claimAmount;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public byte[] getBankDocument() {
		return bankDocument;
	}

	public void setBankDocument(byte[] bankDocument) {
		this.bankDocument = bankDocument;
	}

	public String getBankDocumentName() {
		return bankDocumentName;
	}

	public void setBankDocumentName(String bankDocumentName) {
		this.bankDocumentName = bankDocumentName;
	}

	public String getBankDocumentStatus() {
		return bankDocumentStatus;
	}

	public void setBankDocumentStatus(String bankDocumentStatus) {
		this.bankDocumentStatus = bankDocumentStatus;
	}


	public byte[] getOtherSupportingDocument() {
		return otherSupportingDocument;
	}

	public void setOtherSupportingDocument(byte[] otherSupportingDocument) {
		this.otherSupportingDocument = otherSupportingDocument;
	}

	public String getOtherSupportingDocumentName() {
		return otherSupportingDocumentName;
	}

	public void setOtherSupportingDocumentName(String otherSupportingDocumentName) {
		this.otherSupportingDocumentName = otherSupportingDocumentName;
	}

	public String getOtherSupportingDocumentVerificationStatus() {
		return otherSupportingDocumentVerificationStatus;
	}

	public void setOtherSupportingDocumentVerificationStatus(String otherSupportingDocumentVerificationStatus) {
		this.otherSupportingDocumentVerificationStatus = otherSupportingDocumentVerificationStatus;
	}

	

	public Payments getPayment() {
		return payment;
	}

	public void setPayment(Payments payment) {
		this.payment = payment;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public String getVerificationComment() {
		return verificationComment;
	}

	public void setVerificationComment(String verificationComment) {
		this.verificationComment = verificationComment;
	}

	public byte[] getVerificationDocument() {
		return verificationDocument;
	}

	public void setVerificationDocument(byte[] verificationDocument) {
		this.verificationDocument = verificationDocument;
	}

	public String getVerificationDocumentName() {
		return verificationDocumentName;
	}

	public void setVerificationDocumentName(String verificationDocumentName) {
		this.verificationDocumentName = verificationDocumentName;
	}

	public String getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public String getClaimBy() {
		return claimBy;
	}

	public void setClaimBy(String claimBy) {
		this.claimBy = claimBy;
	}

	public String getClaimReason() {
		return claimReason;
	}

	public void setClaimReason(String claimReason) {
		this.claimReason = claimReason;
	}

	public String getClaimRefNo() {
		return claimRefNo;
	}

	public void setClaimRefNo(String claimRefNo) {
		this.claimRefNo = claimRefNo;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}


	public LocalDateTime getClaimDate() {
		return claimDate;
	}

	public void setClaimDate(LocalDateTime claimDate) {
		this.claimDate = claimDate;
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

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

}
