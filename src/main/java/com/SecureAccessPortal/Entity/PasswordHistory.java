package com.SecureAccessPortal.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "PASSWORD_HISTORY")
public class PasswordHistory {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMAIL", referencedColumnName = "email")
	private Security security;

	@Column(name = "USER_CODE")
	private String userCode;

	@Column(name = "HASHED_PASSWORD")
	private String hashedPassword;

	@Column(name = "IS_CURRENT")
	private boolean isCurrent;

	@Column(name = "EXPIRY_TIME")
	private Timestamp expireTime;

	@Column(name = "CREATED_TIME")
	private Timestamp createdTime;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "DELETED_FLAG")
	private String deletedFlag;

	@Column(name = "UPDATE_TIME")
	private Timestamp updatedTime;

	@Column(name = "PASSWORD_CHANGE_REASON")
	private String passwordChangeReason;

	@Column(name = "FAILED_LOGIN_COUNT")
	private int failedLoginCount;

	@Column(name = "MFA_ENABLED")
	private boolean mfaEnabled;

	@Column(name = "LAST_USED_TIME")
	private Timestamp lastUsedTime;

	@Column(name = "BREACH_STATUS")
	private boolean breachStatus;

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public boolean isCurrent() {
		return isCurrent;
	}

	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

	public Timestamp getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Timestamp expireTime) {
		this.expireTime = expireTime;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
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

	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getPasswordChangeReason() {
		return passwordChangeReason;
	}

	public void setPasswordChangeReason(String passwordChangeReason) {
		this.passwordChangeReason = passwordChangeReason;
	}

	public int getFailedLoginCount() {
		return failedLoginCount;
	}

	public void setFailedLoginCount(int failedLoginCount) {
		this.failedLoginCount = failedLoginCount;
	}

	public boolean isMfaEnabled() {
		return mfaEnabled;
	}

	public void setMfaEnabled(boolean mfaEnabled) {
		this.mfaEnabled = mfaEnabled;
	}

	public Timestamp getLastUsedTime() {
		return lastUsedTime;
	}

	public void setLastUsedTime(Timestamp lastUsedTime) {
		this.lastUsedTime = lastUsedTime;
	}

	public boolean isBreachStatus() {
		return breachStatus;
	}

	public void setBreachStatus(boolean breachStatus) {
		this.breachStatus = breachStatus;
	}

}
