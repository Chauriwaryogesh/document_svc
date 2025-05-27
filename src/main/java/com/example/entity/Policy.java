package com.example.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Policy")
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "policyNumber", unique = true, nullable = false, length = 12)
    private String policyNumber;

    @OneToMany(mappedBy = "policy", fetch = FetchType.LAZY)
    private List<BankAccount> bankAccounts = new ArrayList<>(); // Initialized to avoid null issues

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "policy_company_name")
    private String polCompanyName;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "deleted_flag")
    private String deletedFlag;

    @Column(name = "policy_name")
    private String policyName;

    @Column(name = "customerNo")
    private String customerNo;

    @Column(name = "work_item_ref_no")
    private String workItemRefNo;

    @Column(name = "fcu_details")
    private String fcuFlag;

    @Column(name = "policy_type")
    private String policyType;

    @Column(name = "policy_premium")
    private BigDecimal policyPremium; // Changed to BigDecimal for currency precision

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
    private Integer policyTerm; // Changed to Integer for consistency

    @Column(name = "compliance_flag")
    private String complianceFlag;

    @Column(name = "payment_frequency")
    private String paymentFrequency;

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

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(List<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getWorkItemRefNo() {
        return workItemRefNo;
    }

    public void setWorkItemRefNo(String workItemRefNo) {
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

    public Integer getPolicyTerm() {
        return policyTerm;
    }

    public void setPolicyTerm(Integer policyTerm) {
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

    // Helper method to maintain bidirectional relationship
    public void addBankAccount(BankAccount bankAccount) {
        bankAccounts.add(bankAccount);
        bankAccount.setPolicy(this);
    }
}