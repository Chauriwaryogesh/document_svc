package com.SecureAccessPortal.Modal;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class PolicyList {
	private int policyId;
	private String policyNumber;
	private String policyName;
	private String productCode;
	private String polCompanyName;
	private String createdBy;
	private String updatedBy;
	private String deletedFlag;
	private String policyType;
	private String policyStatus;

	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;

	private LocalDateTime premiumDueDate;
	private String beneficiaryName;
	private String beneficiaryRelationship;
	private String beneficiaryIdentityNumber;
	private String beneficiaryStatus;
	private String beneficiaryContactNumber;
	
	private String policyTerm;
	private String smokerStatus;

	

	private LocalDateTime policyStartDate;
	private LocalDateTime policyEndDate;

	

	private String reason;
	private String fcuStatus;

	private BigDecimal totalAmount;
	private BigDecimal monthlyInstallment;
	private int totalInstallments;
	private int totalInstallmentsPaid;
	private int totalInstallmentsUnPaid;
	private BigDecimal totalAmtPaidByInstallemt;
	private BigDecimal totalAmtUnPaidByInstallemt;

	// extra field

	// ---------------- Optional/Extra Fields (UI only) ----------------
	private String associatedPolicyCount; // extra
	private String customerNo; // extra
	private String customerName; // extra
	private String phoneNumber; // extra
	private String email; // extra
	private String dateOfBirth; // extra
	private String gender; // extra

	// ---------------- Related Entities ----------------
	private List<BankAccountDTO> bankAccounts;
	private List<PaymentList> paymentListDTO;
	private List<SurrenderClaimDTO> surrenderDTO;
	private List<SurrenderClaimDTO> claimDTO;
	private List<String> workItemRefNo;

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

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
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

	public LocalDateTime getPremiumDueDate() {
		return premiumDueDate;
	}

	public void setPremiumDueDate(LocalDateTime premiumDueDate) {
		this.premiumDueDate = premiumDueDate;
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

	public String getSmokerStatus() {
		return smokerStatus;
	}

	public void setSmokerStatus(String smokerStatus) {
		this.smokerStatus = smokerStatus;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getMonthlyInstallment() {
		return monthlyInstallment;
	}

	public void setMonthlyInstallment(BigDecimal monthlyInstallment) {
		this.monthlyInstallment = monthlyInstallment;
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

	public String getBeneficiaryIdentityNumber() {
		return beneficiaryIdentityNumber;
	}

	public void setBeneficiaryIdentityNumber(String beneficiaryIdentityNumber) {
		this.beneficiaryIdentityNumber = beneficiaryIdentityNumber;
	}

	public String getBeneficiaryStatus() {
		return beneficiaryStatus;
	}

	public void setBeneficiaryStatus(String beneficiaryStatus) {
		this.beneficiaryStatus = beneficiaryStatus;
	}

	public String getBeneficiaryContactNumber() {
		return beneficiaryContactNumber;
	}

	public void setBeneficiaryContactNumber(String beneficiaryContactNumber) {
		this.beneficiaryContactNumber = beneficiaryContactNumber;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getFcuStatus() {
		return fcuStatus;
	}

	public void setFcuStatus(String fcuStatus) {
		this.fcuStatus = fcuStatus;
	}

	public int getTotalInstallments() {
		return totalInstallments;
	}

	public void setTotalInstallments(int totalInstallments) {
		this.totalInstallments = totalInstallments;
	}

	public int getTotalInstallmentsPaid() {
		return totalInstallmentsPaid;
	}

	public void setTotalInstallmentsPaid(int totalInstallmentsPaid) {
		this.totalInstallmentsPaid = totalInstallmentsPaid;
	}

	public int getTotalInstallmentsUnPaid() {
		return totalInstallmentsUnPaid;
	}

	public void setTotalInstallmentsUnPaid(int totalInstallmentsUnPaid) {
		this.totalInstallmentsUnPaid = totalInstallmentsUnPaid;
	}

	public BigDecimal getTotalAmtPaidByInstallemt() {
		return totalAmtPaidByInstallemt;
	}

	public void setTotalAmtPaidByInstallemt(BigDecimal totalAmtPaidByInstallemt) {
		this.totalAmtPaidByInstallemt = totalAmtPaidByInstallemt;
	}

	public BigDecimal getTotalAmtUnPaidByInstallemt() {
		return totalAmtUnPaidByInstallemt;
	}

	public void setTotalAmtUnPaidByInstallemt(BigDecimal totalAmtUnPaidByInstallemt) {
		this.totalAmtUnPaidByInstallemt = totalAmtUnPaidByInstallemt;
	}
	public String getAssociatedPolicyCount() {
		return associatedPolicyCount;
	}

	public void setAssociatedPolicyCount(String associatedPolicyCount) {
		this.associatedPolicyCount = associatedPolicyCount;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<BankAccountDTO> getBankAccounts() {
		return bankAccounts;
	}

	public void setBankAccounts(List<BankAccountDTO> bankAccounts) {
		this.bankAccounts = bankAccounts;
	}

	public List<PaymentList> getPaymentListDTO() {
		return paymentListDTO;
	}

	public void setPaymentListDTO(List<PaymentList> paymentListDTO) {
		this.paymentListDTO = paymentListDTO;
	}

	public List<SurrenderClaimDTO> getSurrenderDTO() {
		return surrenderDTO;
	}

	public void setSurrenderDTO(List<SurrenderClaimDTO> surrenderDTO) {
		this.surrenderDTO = surrenderDTO;
	}

	public List<SurrenderClaimDTO> getClaimDTO() {
		return claimDTO;
	}

	public void setClaimDTO(List<SurrenderClaimDTO> claimDTO) {
		this.claimDTO = claimDTO;
	}

	public List<String> getWorkItemRefNo() {
		return workItemRefNo;
	}

	public void setWorkItemRefNo(List<String> workItemRefNo) {
		this.workItemRefNo = workItemRefNo;
	}

}