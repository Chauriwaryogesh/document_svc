package com.SecureAccessPortal.Modal;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class PolicyList {
	private Long id;
    private String policyNumber;
    private List<BankAccountDTO> bankAccounts;
    private String createdDate; // ISO string (e.g., "2025-05-27T10:22:00")
    private String productCode;
    private String polCompanyName;
    private String createdBy;
    private String updatedBy;
    private String userCode;
    private String deletedFlag;
    private String policyName;
    private String customerNo;
    private List<String> workItemRefNo;
    private String fcuFlag;
    private String policyType;
    private BigDecimal policyPremium;
    private String policyStatus;
    private String premiumDueDate; // ISO date string (e.g., "2025-05-27")
    private BigDecimal coverageAmount;
    private String renewalDate; // ISO date string
    private String beneficiaryName;
    private String beneficiaryRelationship;
    private String policyTerm;
    private String complianceFlag;
    private String paymentFrequency;
	private String smokerStatus;
	private String policyAmount;
	private String policyDate;
	private String installmentCount;
	private String dueDate;
	private String term;
	private String frequency;
	private BigDecimal premium;
	private int totalAmount;
	private int monthlyInstallment;
	private int totalClaimableAmount;
	private String startDate;
	private String endDate;
	private String beneficiaryAadharNumber;
	private String nomineeContactNumber;
	private String status;
	private String type;
	
	public BigDecimal getPremium() {
		return premium;
	}
	public void setPremium(BigDecimal premium) {
		this.premium = premium;
	}
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
	public List<BankAccountDTO> getBankAccounts() {
		return bankAccounts;
	}
	public void setBankAccounts(List<BankAccountDTO> bankAccounts) {
		this.bankAccounts = bankAccounts;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
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
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public List<String> getWorkItemRefNo() {
		return workItemRefNo;
	}
	public void setWorkItemRefNo(List<String> workItemRefNo) {
		this.workItemRefNo = workItemRefNo;
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
	public String getPremiumDueDate() {
		return premiumDueDate;
	}
	public void setPremiumDueDate(String premiumDueDate) {
		this.premiumDueDate = premiumDueDate;
	}
	public BigDecimal getCoverageAmount() {
		return coverageAmount;
	}
	public void setCoverageAmount(BigDecimal coverageAmount) {
		this.coverageAmount = coverageAmount;
	}
	public String getRenewalDate() {
		return renewalDate;
	}
	public void setRenewalDate(String renewalDate) {
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
	public String getSmokerStatus() {
		return smokerStatus;
	}
	public void setSmokerStatus(String smokerStatus) {
		this.smokerStatus = smokerStatus;
	}
	public String getPolicyAmount() {
		return policyAmount;
	}
	public void setPolicyAmount(String policyAmount) {
		this.policyAmount = policyAmount;
	}
	public String getPolicyDate() {
		return policyDate;
	}
	public void setPolicyDate(String policyDate) {
		this.policyDate = policyDate;
	}
	public String getInstallmentCount() {
		return installmentCount;
	}
	public void setInstallmentCount(String installmentCount) {
		this.installmentCount = installmentCount;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
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
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getBeneficiaryAadharNumber() {
		return beneficiaryAadharNumber;
	}
	public void setBeneficiaryAadharNumber(String beneficiaryAadharNumber) {
		this.beneficiaryAadharNumber = beneficiaryAadharNumber;
	}
	public String getNomineeContactNumber() {
		return nomineeContactNumber;
	}
	public void setNomineeContactNumber(String nomineeContactNumber) {
		this.nomineeContactNumber = nomineeContactNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}