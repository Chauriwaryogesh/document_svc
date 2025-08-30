package com.SecureAccessPortal.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "Customer")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(unique = true)
	@NotBlank
	private String customerNo;

	@NotBlank
	@Column(unique = true)
	private String email;

	@Column
	private String name;

	@Column
	private String middleName;

	@Column
	private String surname;

	@Column
	private String age;

	@Column
	private String dateOfBirth;

	@Column
	private String gender;

	@Column
	private String createdBy;

	@Column
	private LocalDateTime createdTime;

	@Column
	private String userCode;

	@Column
	private String smokerStatus;

	@Column
	private String adminAccess;

	@Column
	private String deletedFlag;

	@Column
	private String street;

	@Column
	private String city;

	@Column
	private String state;

	@Column
	private String zipCode;

	@Column
	private String country;

	@Column
	private String alternateEmail;

	@Column
	private String phoneCountryCode;

	@Column
	private String phoneNumber;

	@Column
	private String emergencyContactName;

	@Column
	private String emergencyContactPhone;

	@Column
	private String verificationStatus;

	@Column
	private LocalDateTime lastLoginDate;

	@Column
	private String kycStatus;

	@Column
	private double riskScore;

	@Column
	private String preferredContactMethod;

	@Column
	private String documentId;

	@Column
	private LocalDate documentExpiryDate;

	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Payments> payment = new ArrayList<>();

	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Policy> policies = new ArrayList<>();
	
	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<SurrenderEntity> surrenderEntity = new ArrayList<>();
	
	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ClaimEntity> claimEntity = new ArrayList<>();

	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Bank> bankAccounts = new ArrayList<>();

	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<VerificationRecord> verificationRecords = new ArrayList<>();

	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Workitem> workitems = new ArrayList<>();

	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Complaint> complaint = new ArrayList<>();

	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<FeedbackEntity> feedback;
	
//	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	private List<Note> notes = new ArrayList<>();

	public List<FeedbackEntity> getFeedback() {
		return feedback;
	}

	public List<ClaimEntity> getClaimEntity() {
		return claimEntity;
	}

	public void setClaimEntity(List<ClaimEntity> claimEntity) {
		this.claimEntity = claimEntity;
	}

	public void setFeedback(List<FeedbackEntity> feedback) {
		this.feedback = feedback;
	}

	@Column
	private List<String> roles;

	@Column
	private String updatedBy;

	public List<SurrenderEntity> getSurrenderEntity() {
		return surrenderEntity;
	}

	public void setSurrenderEntity(List<SurrenderEntity> surrenderEntity) {
		this.surrenderEntity = surrenderEntity;
	}

	@Column
	private LocalDateTime updatedTime;

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public LocalDateTime getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(LocalDateTime updatedTime) {
		this.updatedTime = updatedTime;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public List<Complaint> getComplaint() {
		return complaint;
	}

	public void setComplaint(List<Complaint> complaint) {
		this.complaint = complaint;
	}

	public List<Payments> getPayment() {
		return payment;
	}

	public void setPayment(List<Payments> payment) {
		this.payment = payment;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
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

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAlternateEmail() {
		return alternateEmail;
	}

	public void setAlternateEmail(String alternateEmail) {
		this.alternateEmail = alternateEmail;
	}

	public String getPhoneCountryCode() {
		return phoneCountryCode;
	}

	public void setPhoneCountryCode(String phoneCountryCode) {
		this.phoneCountryCode = phoneCountryCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmergencyContactName() {
		return emergencyContactName;
	}

	public void setEmergencyContactName(String emergencyContactName) {
		this.emergencyContactName = emergencyContactName;
	}

	public String getEmergencyContactPhone() {
		return emergencyContactPhone;
	}

	public void setEmergencyContactPhone(String emergencyContactPhone) {
		this.emergencyContactPhone = emergencyContactPhone;
	}

	public String getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public LocalDateTime getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(LocalDateTime lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getKycStatus() {
		return kycStatus;
	}

	public void setKycStatus(String kycStatus) {
		this.kycStatus = kycStatus;
	}

	public double getRiskScore() {
		return riskScore;
	}

	public void setRiskScore(double riskScore) {
		this.riskScore = riskScore;
	}

	public String getPreferredContactMethod() {
		return preferredContactMethod;
	}

	public void setPreferredContactMethod(String preferredContactMethod) {
		this.preferredContactMethod = preferredContactMethod;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public LocalDate getDocumentExpiryDate() {
		return documentExpiryDate;
	}

	public void setDocumentExpiryDate(LocalDate documentExpiryDate) {
		this.documentExpiryDate = documentExpiryDate;
	}

	public List<Policy> getPolicies() {
		return policies;
	}

	public void setPolicies(List<Policy> policies) {
		this.policies = policies;
	}

	public List<Bank> getBankAccounts() {
		return bankAccounts;
	}

	public void setBankAccounts(List<Bank> bankAccounts) {
		this.bankAccounts = bankAccounts;
	}

	public List<VerificationRecord> getVerificationRecords() {
		return verificationRecords;
	}

	public void setVerificationRecords(List<VerificationRecord> verificationRecords) {
		this.verificationRecords = verificationRecords;
	}

	public List<Workitem> getWorkitems() {
		return workitems;
	}

	public void setWorkitems(List<Workitem> workitems) {
		this.workitems = workitems;
	}

	// Helper methods to maintain bidirectional relationships
	public void addPolicy(Policy policy) {
		policies.add(policy);
		policy.setCustomer(this);
	}

	public void addBankAccount(Bank bankAccount) {
		bankAccounts.add(bankAccount);
		bankAccount.setCustomer(this);
	}

	public void addVerificationRecord(VerificationRecord verificationRecord) {
		verificationRecords.add(verificationRecord);
		verificationRecord.setCustomer(this);
	}

	public void addWorkitem(Workitem workitem) {
		workitems.add(workitem);
		workitem.setCustomer(this);
	}
}