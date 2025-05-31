package com.SecureAccessPortal.Entity;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "Customer")
public class Customer {

    @Column
    @NotEmpty
    private String email;

    @NotBlank
    @Column
    private String name;

    @Column
    private String middleName;

    @Column
    private String surname;

    @Column
    private String age;

    @NotBlank
    @Column
    private String dateOfBirth; // Consider changing to LocalDate for better date handling

    @NotBlank
    @Column
    private String gender;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String customerNo;

    @Column
    private String createdBy;

    @Column
    private LocalDateTime createdTime; // Changed to LocalDateTime for consistency

    @Column
    private String userId;

    @Column
    private String smokerStatus;

    @Column
    private String adminAccess;

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

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<BankAccount> bankAccounts = new ArrayList<>(); // Initialized to avoid null issues

    // Getters and Setters
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(List<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    // Helper method to maintain bidirectional relationship
    public void addBankAccount(BankAccount bankAccount) {
        bankAccounts.add(bankAccount);
        bankAccount.setCustomer(this);
    }
}