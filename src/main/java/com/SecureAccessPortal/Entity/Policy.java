package com.SecureAccessPortal.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Policy")
public class Policy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "policyNumber", unique = true, length = 12)
	private String policyNumber;

	@OneToMany(mappedBy = "policy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Payments> payments = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customerNo", referencedColumnName = "customerNo")
	private Customer customer;

	@OneToMany(mappedBy = "policy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<BankAccount> bankAccounts = new ArrayList<>();

	@OneToMany(mappedBy = "policy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<VerificationRecord> verificationRecords = new ArrayList<>();

	@OneToMany(mappedBy = "policy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Workitem> workitems = new ArrayList<>();

	@OneToMany(mappedBy = "policy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Complaint> complaint = new ArrayList<>();

	public List<Complaint> getComplaint() {
		return complaint;
	}

	public void setComplaint(List<Complaint> complaint) {
		this.complaint = complaint;
	}

	@Column(name = "product_code")
	private String productCode;

	@Column(name = "policy_company_name")
	private String polCompanyName;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_Time")
	private LocalDateTime createdTime;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_Time")
	private LocalDateTime updatedTime;

	@Column(name = "userCode")
	private String userCode;

	@Column(name = "deleted_flag")
	private String deletedFlag;

	@Column(name = "policy_name")
	private String policyName;

	@Column(name = "fcu_details")
	private String fcuFlag;

	@Column(name = "policy_type")
	private String policyType;

	@Column(name = "policy_premium")
	private BigDecimal policyPremium;

	@Column(name = "policy_status")
	private String policyStatus;

	@Column(name = "premium_due_date")
	private LocalDate premiumDueDate;

	@Column(name = "coverage_amount")
	private BigDecimal coverageAmount;

	@Column(name = "renewal_date")
	private LocalDate renewalDate;

	@Column(name = "beneficiary_name")
	private String beneficiaryName;

	@Column(name = "beneficiary_relationship")
	private String beneficiaryRelationship;

	@Column(name = "policy_term")
	private String policyTerm;

	@Column(name = "compliance_flag")
	private String complianceFlag;

	@Column(name = "payment_frequency")
	private String paymentFrequency;

	@Column(name = "smoker_status")
	private String smokerStatus;
	@Column(name = "policy_frequency")
	private String policyfrequency;
	@Column(name = "total_amount")
	private int totalAmount;
	@Column(name = "monthly_installment")
	private int monthlyInstallment;
	@Column(name = "total_claimableAmount")
	private int totalClaimableAmount;
	@Column(name = "policy_startDate")
	private LocalDate policyStartDate;
	@Column(name = "policy_endDate")
	private LocalDate policyEndDate;
	@Column(name = "beneficiary_identityNumber")
	private String beneficiaryIdentityNumber;
	@Column(name = "beneficiary_contactNumber")
	private String beneficiaryContactNumber;

	@Column(name = "reson")
	private String reson;

	public String getReson() {
		return reson;
	}

	public void setReson(String reson) {
		this.reson = reson;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public LocalDateTime getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(LocalDateTime updatedTime) {
		this.updatedTime = updatedTime;
	}

	public List<Payments> getPayments() {
		return payments;
	}

	public void setPayments(List<Payments> payments) {
		this.payments = payments;
	}

	public String getSmokerStatus() {
		return smokerStatus;
	}

	public void setSmokerStatus(String smokerStatus) {
		this.smokerStatus = smokerStatus;
	}

	public String getPolicyfrequency() {
		return policyfrequency;
	}

	public void setPolicyfrequency(String policyfrequency) {
		this.policyfrequency = policyfrequency;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getMonthlyInstallment() {
		return monthlyInstallment;
	}

	public void setMonthlyInstallment(int monthlyInstallment) {
		this.monthlyInstallment = monthlyInstallment;
	}

	public int getTotalClaimableAmount() {
		return totalClaimableAmount;
	}

	public void setTotalClaimableAmount(int totalClaimableAmount) {
		this.totalClaimableAmount = totalClaimableAmount;
	}

	public LocalDate getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(LocalDate policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public LocalDate getPolicyEndDate() {
		return policyEndDate;
	}

	public void setPolicyEndDate(LocalDate policyEndDate) {
		this.policyEndDate = policyEndDate;
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

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
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

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public String getFcuFlag() {
		return fcuFlag;
	}

	public void setFcuFlag(String fcuFlag) {
		this.fcuFlag = fcuFlag;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public BigDecimal getPolicyPremium() {
		return policyPremium;
	}

	public void setPolicyPremium(BigDecimal policyPremium) {
		this.policyPremium = policyPremium;
	}

	public String getPolicyStatus() {
		return policyStatus;
	}

	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}

	public LocalDate getPremiumDueDate() {
		return premiumDueDate;
	}

	public void setPremiumDueDate(LocalDate premiumDueDate) {
		this.premiumDueDate = premiumDueDate;
	}

	public BigDecimal getCoverageAmount() {
		return coverageAmount;
	}

	public void setCoverageAmount(BigDecimal coverageAmount) {
		this.coverageAmount = coverageAmount;
	}

	public LocalDate getRenewalDate() {
		return renewalDate;
	}

	public void setRenewalDate(LocalDate renewalDate) {
		this.renewalDate = renewalDate;
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

	public String getComplianceFlag() {
		return complianceFlag;
	}

	public void setComplianceFlag(String complianceFlag) {
		this.complianceFlag = complianceFlag;
	}

	public String getPaymentFrequency() {
		return paymentFrequency;
	}

	public void setPaymentFrequency(String paymentFrequency) {
		this.paymentFrequency = paymentFrequency;
	}

	// Helper methods to maintain bidirectional relationships
	public void addBankAccount(BankAccount bankAccount) {
		bankAccounts.add(bankAccount);
		bankAccount.setPolicy(this);
	}

	public void addVerificationRecord(VerificationRecord verificationRecord) {
		verificationRecords.add(verificationRecord);
		verificationRecord.setPolicy(this);
	}

	public void addWorkitem(Workitem workitem) {
		workitems.add(workitem);
		workitem.setPolicy(this);
	}
}