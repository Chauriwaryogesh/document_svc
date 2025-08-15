package com.SecureAccessPortal.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_id", referencedColumnName = "payment_id", insertable = false, updatable = false)
	private Payments payments;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accountNo", referencedColumnName = "accountNo", insertable = false, updatable = false)
	private BankAccount bankAccount;
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] verificationDocument;
	@Column
	private String verificationDocumentName;
	@Column
	private String verificationStatus;
	@Column
	private String verificationComment;

	@OneToMany(mappedBy = "surrenderEntity", fetch = FetchType.LAZY)
	private List<SurrenderWorkflowStep> surrenderWorkflowStep;
	
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] otherSupportingDocument;
	@Column
	private String otherSupportingDocumentName;
	@Column
	private String otherSupportingDocumentVerificationStatus;
	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public byte[] getOtherSupportingDocument() {
		return otherSupportingDocument;
	}

	public List<SurrenderWorkflowStep> getSurrenderWorkflowStep() {
		return surrenderWorkflowStep;
	}

	public void setSurrenderWorkflowStep(List<SurrenderWorkflowStep> surrenderWorkflowStep) {
		this.surrenderWorkflowStep = surrenderWorkflowStep;
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

	

	public Payments getPayments() {
		return payments;
	}

	public void setPayments(Payments payments) {
		this.payments = payments;
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
