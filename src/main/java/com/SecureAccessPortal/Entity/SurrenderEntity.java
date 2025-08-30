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
@Table(name = "SURRENDER_POLICY")
public class SurrenderEntity {
	@Id
	@Column
	private String surrRefNo;
	@Column
	private String surrenderStatus;
	@Column
	private BigDecimal surrAmount;
	@Column
	private String surrenderReason;
	@Column
	private LocalDateTime surrDate;
	@Column
	private String surrenderBy;
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
	private byte[] otherSupportingDocument;
	@Column
	private String otherSupportingDocumentName;
	@Column
	private String otherSupportingDocumentVerificationStatus;
	
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] bankDocument;
	@Column
	private String bankDocumentName;
	@Column
	private String bankDocumentStatus;
	
	
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

	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
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

	public String getSurrenderReason() {
		return surrenderReason;
	}

	public void setSurrenderReason(String surrenderReason) {
		this.surrenderReason = surrenderReason;
	}

	

	

	public Payments getPayment() {
		return payment;
	}

	public void setPayment(Payments payment) {
		this.payment = payment;
	}

	public String getSurrRefNo() {
		return surrRefNo;
	}

	public void setSurrRefNo(String surrRefNo) {
		this.surrRefNo = surrRefNo;
	}

	public String getSurrenderStatus() {
		return surrenderStatus;
	}

	public void setSurrenderStatus(String surrenderStatus) {
		this.surrenderStatus = surrenderStatus;
	}

	public BigDecimal getSurrAmount() {
		return surrAmount;
	}

	public void setSurrAmount(BigDecimal surrAmount) {
		this.surrAmount = surrAmount;
	}

	public LocalDateTime getSurrDate() {
		return surrDate;
	}

	public void setSurrDate(LocalDateTime surrDate) {
		this.surrDate = surrDate;
	}

	public String getSurrenderBy() {
		return surrenderBy;
	}

	public void setSurrenderBy(String surrenderBy) {
		this.surrenderBy = surrenderBy;
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
