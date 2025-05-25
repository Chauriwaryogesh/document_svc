package com.example.dto;

public class CustomerDTO {
	
	private String name;
	private String middleName;
	private String surname;
	private String age;
	private String dateOfBirth;
	private String gender;
	private String phoneNumber;
	private String email;
	private String customerNo;
	private String userId;
	private String smokerStatus;
	private String adminAccess;
	private Address address;
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	private ContactDetails  contactDetails;
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public ContactDetails getContactDetails() {
		return contactDetails;
	}
	public void setContactDetails(ContactDetails contactDetails) {
		this.contactDetails = contactDetails;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSmokerStatus() {
		return smokerStatus;
	}
	public void setSmokerStatus(String smokerStatus) {
		this.smokerStatus = smokerStatus;
	}
	public String getAdminAccess() {
		return adminAccess;
	}
	public void setAdminAccess(String adminAccess) {
		this.adminAccess = adminAccess;
	}

}
