package com.SecureAccessPortal.Modal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PolicyInfoDTO {
	private String policyId;
	private String policyName;
	private String productCode;
	private String policyCompanyName;
	private String supportedTerms;
	private String frequency;
	private BigDecimal minTotalAmount;
	private BigDecimal maxTotalAmount;
	private BigDecimal coverageAmount;
	private int defaultDurationMonths;
	private LocalDate policyStartDate;
	private LocalDate policyEndDate;
	private LocalDate policyRenewalDate;
	private LocalDate policyExpiryDate;
	private String policyType;
	private String status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
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

	public String getPolicyCompanyName() {
		return policyCompanyName;
	}

	public void setPolicyCompanyName(String policyCompanyName) {
		this.policyCompanyName = policyCompanyName;
	}

	public String getSupportedTerms() {
		return supportedTerms;
	}

	public void setSupportedTerms(String supportedTerms) {
		this.supportedTerms = supportedTerms;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public BigDecimal getMinTotalAmount() {
		return minTotalAmount;
	}

	public void setMinTotalAmount(BigDecimal minTotalAmount) {
		this.minTotalAmount = minTotalAmount;
	}

	public BigDecimal getMaxTotalAmount() {
		return maxTotalAmount;
	}

	public void setMaxTotalAmount(BigDecimal maxTotalAmount) {
		this.maxTotalAmount = maxTotalAmount;
	}

	public BigDecimal getCoverageAmount() {
		return coverageAmount;
	}

	public void setCoverageAmount(BigDecimal coverageAmount) {
		this.coverageAmount = coverageAmount;
	}

	public int getDefaultDurationMonths() {
		return defaultDurationMonths;
	}

	public void setDefaultDurationMonths(int defaultDurationMonths) {
		this.defaultDurationMonths = defaultDurationMonths;
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

	public LocalDate getPolicyRenewalDate() {
		return policyRenewalDate;
	}

	public void setPolicyRenewalDate(LocalDate policyRenewalDate) {
		this.policyRenewalDate = policyRenewalDate;
	}

	public LocalDate getPolicyExpiryDate() {
		return policyExpiryDate;
	}

	public void setPolicyExpiryDate(LocalDate policyExpiryDate) {
		this.policyExpiryDate = policyExpiryDate;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

}
