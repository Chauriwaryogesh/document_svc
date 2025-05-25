package com.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Policy")
public class Policy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	// This is the field that will store your generated policy number
	
	@Column(name = "policyNumber",unique = true, nullable = false, length = 12) // POLC (4) + 00001 (5) + 25 (2) = 11 characters, 12 for
	private String policyNumber;

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

	@Column(name = "workItemRefNo")
	private String workItemRefNo;
	
	@Column(name = "fcu_details")
	private String fcuFlag;

	/**
	 * @return the fcuFlag
	 */
	public String getFcuFlag() {
		return fcuFlag;
	}

	/**
	 * @param fcuFlag the fcuFlag to set
	 */
	public void setFcuFlag(String fcuFlag) {
		this.fcuFlag = fcuFlag;
	}

	/**
	 * @return the workItemRefNo
	 */
	public String getWorkItemRefNo() {
		return workItemRefNo;
	}

	/**
	 * @param workItemRefNo the workItemRefNo to set
	 */
	public void setWorkItemRefNo(String workItemRefNo) {
		this.workItemRefNo = workItemRefNo;
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
	 * @return the policyNumber
	 */
	public String getPolicyNumber() {
		return policyNumber;
	}

	/**
	 * @param policyNumber the policyNumber to set
	 */
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
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

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}
}
