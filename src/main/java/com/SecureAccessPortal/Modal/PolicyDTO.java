package com.SecureAccessPortal.Modal;

import java.util.List;

public class PolicyDTO {
	private String associatedPolicyCount;
	private String userCode;
	private String customerNo;
	private String customerName; 
	private String middleName;
	private String surname;
	private String phoneNumber;
	private String email;
	private String dateOfBirth;
	private String gender;
	private String smokerStatus;
	private List<PolicyList> policyList;

	// new added
	private String policyName;
	private String policyType;

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	// Getters and Setters
	public String getAssociatedPolicyCount() {
		return associatedPolicyCount;
	}

	public void setAssociatedPolicyCount(String associatedPolicyCount) {
		this.associatedPolicyCount = associatedPolicyCount;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
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

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
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

	public String getSmokerStatus() {
		return smokerStatus;
	}

	public void setSmokerStatus(String smokerStatus) {
		this.smokerStatus = smokerStatus;
	}

	public List<PolicyList> getPolicyList() {
		return policyList;
	}

	public void setPolicyList(List<PolicyList> policyList) {
		this.policyList = policyList;
	}
}