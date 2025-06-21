package com.SecureAccessPortal.Modal;

import java.sql.Timestamp;

public class LoginHistroryResponse {

	private Long id;
	private Timestamp loginTime;
	private String totalScreenTime;
	private Timestamp logoutTime;
	private String userCode;
	private String loggedinStatus;
	private String loginMethod;
	private String ipAddress;
	private String deviceInfo;
	private String sessionId;
	private String location;
	private int riskScore;
	private String createdBy;
	private Timestamp createdTime;
	private String updatedBy;
	private Timestamp updatedTime;
	private boolean mfaUsed;

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

	public String getTotalScreenTime() {
		return totalScreenTime;
	}

	public void setTotalScreenTime(String totalScreenTime) {
		this.totalScreenTime = totalScreenTime;
	}

	public Timestamp getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Timestamp logoutTime) {
		this.logoutTime = logoutTime;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getLoggedinStatus() {
		return loggedinStatus;
	}

	public void setLoggedinStatus(String loggedinStatus) {
		this.loggedinStatus = loggedinStatus;
	}

	public String getLoginMethod() {
		return loginMethod;
	}

	public void setLoginMethod(String loginMethod) {
		this.loginMethod = loginMethod;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getRiskScore() {
		return riskScore;
	}

	public void setRiskScore(int riskScore) {
		this.riskScore = riskScore;
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
