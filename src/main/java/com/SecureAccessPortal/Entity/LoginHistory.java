package com.SecureAccessPortal.Entity;

import java.security.Timestamp;

import io.lettuce.core.dynamic.annotation.CommandNaming.Strategy;
import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "LOGIN_HISTORY")
public class LoginHistory {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "email", referencedColumnName = "email")
	private Security security;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO )
	private Long id;

	@Column(name = "loginTime")
	private Timestamp loginTime;
	
	@Column(name = "userCode")
	private Timestamp userCode;

	@Column(name = "success")
	private boolean success;

	@Column(name = "ipAddress")
	private String ipAddress;

	@Column(name = "deviceInfo")
	private String deviceInfo;

	@Column(name = "sessionId")
	private String sessionId;

	@Column(name = "logoutTime")
	private Timestamp logoutTime;

	@Column(name = "location")
	private String location;

	@Column(name = "loginMethod")
	private String loginMethod;

	@Column(name = "riskScore")
	private int riskScore;

	@Column(name = "deletedFlag")
	private String deletedFlag;

	@Column(name = "createdBy")
	private String createdBy;

	@Column(name = "createdTime")
	private Timestamp createdTime;

	@Column(name = "updatedBy")
	private String updatedBy;

	@Column(name = "updatedTime")
	private Timestamp updatedTime;

	@Column(name = "mfaUsed")
	private boolean mfaUsed;

	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}

	public Timestamp getUserCode() {
		return userCode;
	}

	public void setUserCode(Timestamp userCode) {
		this.userCode = userCode;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Timestamp getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Timestamp logoutTime) {
		this.logoutTime = logoutTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLoginMethod() {
		return loginMethod;
	}

	public void setLoginMethod(String loginMethod) {
		this.loginMethod = loginMethod;
	}

	public int getRiskScore() {
		return riskScore;
	}

	public void setRiskScore(int riskScore) {
		this.riskScore = riskScore;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
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

	public boolean isMfaUsed() {
		return mfaUsed;
	}

	public void setMfaUsed(boolean mfaUsed) {
		this.mfaUsed = mfaUsed;
	}

	

}
