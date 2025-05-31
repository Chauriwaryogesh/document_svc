package com.SecureAccessPortal.Modal;

import java.math.BigDecimal;
import java.util.List;

public class PolicyList {
	private Long id;
    private String policyNumber;
    private List<BankAccountDTO> bankAccounts;
    private String createdDate; // ISO string (e.g., "2025-05-27T10:22:00")
    private String productCode;
    private String polCompanyName;
    private String createdBy;
    private String updatedBy;
    private String userId;
    private String deletedFlag;
    private String policyName;
    private String customerNo;
    private String workItemRefNo;
    private String fcuFlag;
    private String policyType;
    private BigDecimal policyPremium;
    private String policyStatus;
    private String premiumDueDate; // ISO date string (e.g., "2025-05-27")
    private BigDecimal coverageAmount;
    private String renewalDate; // ISO date string
    private String beneficiaryName;
    private String beneficiaryRelationship;
    private Integer policyTerm;
    private String complianceFlag;
    private String paymentFrequency;
    /**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the updatedBy
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the deletedFlag
	 */
	public String getDeletedFlag() {
		return deletedFlag;
	}

	/**
	 * @param deletedFlag the deletedFlag to set
	 */
	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	/**
	 * @return the customerNo
	 */
	public String getCustomerNo() {
		return customerNo;
	}

	/**
	 * @param customerNo the customerNo to set
	 */
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	/**
	 * @return the policyType
	 */
	public String getPolicyType() {
		return policyType;
	}

	/**
	 * @param policyType the policyType to set
	 */
	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	/**
	 * @return the policyPremium
	 */
	public BigDecimal getPolicyPremium() {
		return policyPremium;
	}

	/**
	 * @param policyPremium the policyPremium to set
	 */
	public void setPolicyPremium(BigDecimal policyPremium) {
		this.policyPremium = policyPremium;
	}

	/**
	 * @return the policyStatus
	 */
	public String getPolicyStatus() {
		return policyStatus;
	}

	/**
	 * @param policyStatus the policyStatus to set
	 */
	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}

	/**
	 * @return the premiumDueDate
	 */
	public String getPremiumDueDate() {
		return premiumDueDate;
	}

	/**
	 * @param premiumDueDate the premiumDueDate to set
	 */
	public void setPremiumDueDate(String premiumDueDate) {
		this.premiumDueDate = premiumDueDate;
	}

	/**
	 * @return the coverageAmount
	 */
	public BigDecimal getCoverageAmount() {
		return coverageAmount;
	}

	/**
	 * @param coverageAmount the coverageAmount to set
	 */
	public void setCoverageAmount(BigDecimal coverageAmount) {
		this.coverageAmount = coverageAmount;
	}

	/**
	 * @return the renewalDate
	 */
	public String getRenewalDate() {
		return renewalDate;
	}

	/**
	 * @param renewalDate the renewalDate to set
	 */
	public void setRenewalDate(String renewalDate) {
		this.renewalDate = renewalDate;
	}

	/**
	 * @return the beneficiaryName
	 */
	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	/**
	 * @param beneficiaryName the beneficiaryName to set
	 */
	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	/**
	 * @return the beneficiaryRelationship
	 */
	public String getBeneficiaryRelationship() {
		return beneficiaryRelationship;
	}

	/**
	 * @param beneficiaryRelationship the beneficiaryRelationship to set
	 */
	public void setBeneficiaryRelationship(String beneficiaryRelationship) {
		this.beneficiaryRelationship = beneficiaryRelationship;
	}

	/**
	 * @return the policyTerm
	 */
	public Integer getPolicyTerm() {
		return policyTerm;
	}

	/**
	 * @param policyTerm the policyTerm to set
	 */
	public void setPolicyTerm(Integer policyTerm) {
		this.policyTerm = policyTerm;
	}

	/**
	 * @return the complianceFlag
	 */
	public String getComplianceFlag() {
		return complianceFlag;
	}

	/**
	 * @param complianceFlag the complianceFlag to set
	 */
	public void setComplianceFlag(String complianceFlag) {
		this.complianceFlag = complianceFlag;
	}

	/**
	 * @return the paymentFrequency
	 */
	public String getPaymentFrequency() {
		return paymentFrequency;
	}

	/**
	 * @param paymentFrequency the paymentFrequency to set
	 */
	public void setPaymentFrequency(String paymentFrequency) {
		this.paymentFrequency = paymentFrequency;
	}

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