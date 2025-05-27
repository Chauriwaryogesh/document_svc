package com.example.dto;

import java.util.List;

public class PolicyList {
    private String policyNumber;
    private String productCode;
    private String polCompanyName;
    private String policyName;
    private String fcuFlag;
    private String createdDate;
    private String workItemRefNo;
    private String createdBy;
    private List<BankAccountDTO> bankAccounts; // Added to include bank account details

    // Getters and Setters
    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getWorkItemRefNo() {
        return workItemRefNo;
    }

    public void setWorkItemRefNo(String workItemRefNo) {
        this.workItemRefNo = workItemRefNo;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<BankAccountDTO> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(List<BankAccountDTO> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }
}