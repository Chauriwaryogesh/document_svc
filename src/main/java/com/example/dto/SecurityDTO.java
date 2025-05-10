package com.example.dto;

public class SecurityDTO {
	private String id;
	private String email;
	private String userName;
	private String userCode;
	private String isEmailVerified;
	private String isUserCodeVerified;
	private String createdTime;
	private String remainingTime;
	
	
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getRemainingTime() {
		return remainingTime;
	}
	public void setRemainingTime(String remainingTime) {
		this.remainingTime = remainingTime;
	}
	public String getIsUserCodeVerified() {
		return isUserCodeVerified;
	}
	public void setIsUserCodeVerified(String isUserCodeVerified) {
		this.isUserCodeVerified = isUserCodeVerified;
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
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
