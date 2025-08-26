package com.SecureAccessPortal.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Policy")
public class Policy {

	@Id
	@Column(name = "policyId")
	private int policyId;

	// 🔹 Core Identifiers
	@NotNull
	@Column(name = "policyNumber", unique = true, nullable = false)
	private String policyNumber;

	@NotNull
	@Column(name = "policy_name", nullable = false)
	private String policyName;

	@NotNull
	@Column(name = "product_code", nullable = false)
	private String productCode;

	@NotNull
	@Column(name = "policy_type", nullable = false)
	private String policyType;

	@NotNull
	@Column(name = "policy_status", nullable = false)
	private String policyStatus;

	@NotNull
	@Column(name = "policy_company_name")
	private String polCompanyName;

	// 🔹 Customer Mapping
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customerNo", referencedColumnName = "customerNo", nullable = false)
	private Customer customer;

	// 🔹 Relationships with other entities
	@OneToMany(mappedBy = "policy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Payments> payments = new ArrayList<>();

	@OneToMany(mappedBy = "policy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<BankAccount> bankAccounts = new ArrayList<>();

	@OneToMany(mappedBy = "policy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<VerificationRecord> verificationRecords = new ArrayList<>();

	@OneToMany(mappedBy = "policy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Workitem> workitems = new ArrayList<>();

	@OneToMany(mappedBy = "policy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Complaint> complaint = new ArrayList<>();

	@OneToMany(mappedBy = "policy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<SurrenderEntity> surrender = new ArrayList<>();

	@OneToMany(mappedBy = "policy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ClaimEntity> claim = new ArrayList<>();

	// 🔹 Policy Duration
	@NotNull
	@Column(name = "policy_startDate", nullable = false)
	private LocalDateTime policyStartDate;

	@Column(name = "policy_endDate")
	private LocalDateTime policyEndDate;

	@NotNull
	@Column(name = "policy_term")
	private String policyTerm;

	// 🔹 Financial Info
	@NotNull
	@Column(name = "total_amount")
	private BigDecimal totalAmount;

	@NotNull
	@Column(name = "total_paid_amount")
	private BigDecimal totalPaidAmount;

	@NotNull
	@Column(name = "total_unPaid_amount")
	private BigDecimal totalUnPaidAmount;

	@NotNull
	@Column(name = "monthly_installment")
	private BigDecimal monthlyInstallment;

	@NotNull
	@Column(name = "premium_due_date")
	private LocalDateTime premiumDueDate;

	@Column(name = "total_installments")
	private int totalInstallments;

	@Column(name = "total_paid_installments")
	private int totalPaidInstallments;

	@Column(name = "total_unpaid_installments")
	private int totalUnPaidInstallments;

	// 🔹 Beneficiary Info
	@NotNull
	@Column(name = "beneficiary_name")
	private String beneficiaryName;

	@Column(name = "beneficiary_relationship")
	private String beneficiaryRelationship;

	@Column(name = "beneficiary_identityNumber")
	private String beneficiaryIdentityNumber;

	@Column(name = "beneficiary_contactNumber")
	private String beneficiaryContactNumber;

	@Column(name = "beneficiary_status")
	private String beneficiaryStatus;

	// 🔹 Other Details
	@Column(name = "smoker_status")
	private String smokerStatus;

	@Column(name = "fcu_status")
	private String fcuStatus;

	@Column(name = "reason") // fixed typo
	private String reason;

	// 🔹 Audit Info
	@NotNull
	@Column(name = "created_by", nullable = false)
	private String createdBy;

	@NotNull
	@Column(name = "created_Date", nullable = false)
	private LocalDateTime createdDate;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_Date")
	private LocalDateTime updatedDate;

	@NotNull
	@Column(name = "deleted_flag", nullable = false, length = 1)
	private String deletedFlag;

	@Column(name = "userCode") // fixed typo
	private String userCode;

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public BigDecimal getTotalUnPaidAmount() {
		return totalUnPaidAmount;
	}

	public void setTotalUnPaidAmount(BigDecimal totalUnPaidAmount) {
		this.totalUnPaidAmount = totalUnPaidAmount;
	}

	public int getPolicyId() {
		return policyId;
	}

	public void setPolicyId(int policyId) {
		this.policyId = policyId;
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

	public String getPolCompanyName() {
		return polCompanyName;
	}

	public void setPolCompanyName(String polCompanyName) {
		this.polCompanyName = polCompanyName;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Payments> getPayments() {
		return payments;
	}

	public void setPayments(List<Payments> payments) {
		this.payments = payments;
	}

	public List<BankAccount> getBankAccounts() {
		return bankAccounts;
	}

	public void setBankAccounts(List<BankAccount> bankAccounts) {
		this.bankAccounts = bankAccounts;
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

	public List<Complaint> getComplaint() {
		return complaint;
	}

	public void setComplaint(List<Complaint> complaint) {
		this.complaint = complaint;
	}

	public List<SurrenderEntity> getSurrender() {
		return surrender;
	}

	public void setSurrender(List<SurrenderEntity> surrender) {
		this.surrender = surrender;
	}

	public List<ClaimEntity> getClaim() {
		return claim;
	}

	public void setClaim(List<ClaimEntity> claim) {
		this.claim = claim;
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

	public String getPolicyTerm() {
		return policyTerm;
	}

	public void setPolicyTerm(String policyTerm) {
		this.policyTerm = policyTerm;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getTotalPaidAmount() {
		return totalPaidAmount;
	}

	public void setTotalPaidAmount(BigDecimal totalPaidAmount) {
		this.totalPaidAmount = totalPaidAmount;
	}

	public BigDecimal getMonthlyInstallment() {
		return monthlyInstallment;
	}

	public void setMonthlyInstallment(BigDecimal monthlyInstallment) {
		this.monthlyInstallment = monthlyInstallment;
	}

	public LocalDateTime getPremiumDueDate() {
		return premiumDueDate;
	}

	public void setPremiumDueDate(LocalDateTime premiumDueDate) {
		this.premiumDueDate = premiumDueDate;
	}

	public int getTotalInstallments() {
		return totalInstallments;
	}

	public void setTotalInstallments(int totalInstallments) {
		this.totalInstallments = totalInstallments;
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

	public String getBeneficiaryIdentityNumber() {
		return beneficiaryIdentityNumber;
	}

	public void setBeneficiaryIdentityNumber(String beneficiaryIdentityNumber) {
		this.beneficiaryIdentityNumber = beneficiaryIdentityNumber;
	}

	public String getBeneficiaryContactNumber() {
		return beneficiaryContactNumber;
	}

	public void setBeneficiaryContactNumber(String beneficiaryContactNumber) {
		this.beneficiaryContactNumber = beneficiaryContactNumber;
	}

	public String getBeneficiaryStatus() {
		return beneficiaryStatus;
	}

	public void setBeneficiaryStatus(String beneficiaryStatus) {
		this.beneficiaryStatus = beneficiaryStatus;
	}

	public String getSmokerStatus() {
		return smokerStatus;
	}

	public void setSmokerStatus(String smokerStatus) {
		this.smokerStatus = smokerStatus;
	}

	public String getFcuStatus() {
		return fcuStatus;
	}

	public void setFcuStatus(String fcuStatus) {
		this.fcuStatus = fcuStatus;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
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

}