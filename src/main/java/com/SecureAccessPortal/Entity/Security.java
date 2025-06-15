package com.SecureAccessPortal.Entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "security")
public class Security {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customerNo", referencedColumnName = "customerNo", insertable = false, updatable = false)
	private Customer customer;

	@Column(name = "email")
	private String email;

	@Column(name = "isEmailVerified")
	private String isEmailVerified;

	@Column(name = "userName")
	private String userName;

	@Column(name = "userCode")
	private String userCode;

	@Column(name = "isUserCodeVerified")
	private String isUserCodeVerified;

	@Column(name = "customerNo", unique = true)
	private String customerNo;

	@Column(name = "createdBy")
	private String createdBy;

	@Column(name = "updateBy")
	private String updateBy;

	@Column(name = "createdTime")
	private LocalDate createdTime;

	@Column(name = "updateTime")
	private LocalDate updateTime;

	@Column(name = "endTime")
	private LocalDate endTime;

	@Column(name = "deletedFlag")
	private String deletedFlag;

	@Column(name = "credentialId", length = 255)
	private String credentialId; // Base64-encoded credential ID

	@Column(name = "publicKey", columnDefinition = "TEXT")
	private String publicKey; // Base64-encoded COSE public key

	@Column(name = "userHandle", length = 255)
	private String userHandle; // Base64-encoded user handle

	@Column(name = "signatureCounter")
	private Long signatureCounter; // Signature counter for anti-replay

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDate getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDate createdTime) {
		this.createdTime = createdTime;
	}

	public void setUpdateTime(LocalDate updateTime) {
		this.updateTime = updateTime;
	}

	public void setEndTime(LocalDate endTime) {
		this.endTime = endTime;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public LocalDate getUpdateTime() {
		return updateTime;
	}

	public LocalDate getEndTime() {
		return endTime;
	}

	// Getters and Setters
	public String getCredentialId() {
		return credentialId;
	}

	public void setCredentialId(String credentialId) {
		this.credentialId = credentialId;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getUserHandle() {
		return userHandle;
	}

	public void setUserHandle(String userHandle) {
		this.userHandle = userHandle;
	}

	public Long getSignatureCounter() {
		return signatureCounter;
	}

	public void setSignatureCounter(Long signatureCounter) {
		this.signatureCounter = signatureCounter;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIsEmailVerified() {
		return isEmailVerified;
	}

	public void setIsEmailVerified(String isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public String getIsUserCodeVerified() {
		return isUserCodeVerified;
	}

	public void setIsUserCodeVerified(String isUserCodeVerified) {
		this.isUserCodeVerified = isUserCodeVerified;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
}