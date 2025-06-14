package com.SecureAccessPortal.Modal;

public class PolicyRequest {
	private String  policyNumber;
	private String productCode;
	private String polCompanyName;
	private String policyName;
	private String fcuFlag;
	private String associatedpolicyCount;
	private String userCode;
	private String customerNo;
	private String custName;
	private String middleName;
	private String surname;
	private String phoneNum;
	private String email;
	private String dateOfBirth;
	private String gender;
	private String smokerStatus;
	private String policyAmount;
	private String policyDate;
	private String installmentCount;
	private String dueDate;
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
	/**
	 * @return the productCode
	 */
	public String getProductCode() {
		return productCode;
	}
	/**
	 * @param productCode the productCode to set
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	/**
	 * @return the polCompanyName
	 */
	public String getPolCompanyName() {
		return polCompanyName;
	}
	/**
	 * @param polCompanyName the polCompanyName to set
	 */
	public void setPolCompanyName(String polCompanyName) {
		this.polCompanyName = polCompanyName;
	}
	/**
	 * @return the policyName
	 */
	public String getPolicyName() {
		return policyName;
	}
	/**
	 * @param policyName the policyName to set
	 */
	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}
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
	 * @return the associatedpolicyCount
	 */
	public String getAssociatedpolicyCount() {
		return associatedpolicyCount;
	}
	/**
	 * @param associatedpolicyCount the associatedpolicyCount to set
	 */
	public void setAssociatedpolicyCount(String associatedpolicyCount) {
		this.associatedpolicyCount = associatedpolicyCount;
	}
	/**
	 * @return the userCode
	 */
	public String getuserCode() {
		return userCode;
	}
	/**
	 * @param userCode the userCode to set
	 */
	public void setuserCode(String userCode) {
		this.userCode = userCode;
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
	 * @return the custName
	 */
	public String getCustName() {
		return custName;
	}
	/**
	 * @param custName the custName to set
	 */
	public void setCustName(String custName) {
		this.custName = custName;
	}
	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}
	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}
	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}
	/**
	 * @return the phoneNum
	 */
	public String getPhoneNum() {
		return phoneNum;
	}
	/**
	 * @param phoneNum the phoneNum to set
	 */
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the dateOfBirth
	 */
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	/**
	 * @param dateOfBirth the dateOfBirth to set
	 */
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return the smokerStatus
	 */
	public String getSmokerStatus() {
		return smokerStatus;
	}
	/**
	 * @param smokerStatus the smokerStatus to set
	 */
	public void setSmokerStatus(String smokerStatus) {
		this.smokerStatus = smokerStatus;
	}
	
	public boolean isBlank() {
        return (policyNumber == null || policyNumber.isEmpty())
            && (customerNo == null || customerNo.isEmpty());
        // add other relevant field checks as needed
    }
	

}
