package com.SecureAccessPortal.Modal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RoleDTO {
	private String name;
	private String userCode;
	private List<String> roles;
	private LocalDateTime loggedInTime;
	private String phNo;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	public LocalDateTime getLoggedInTime() {
		return loggedInTime;
	}
	public void setLoggedInTime(LocalDateTime loggedInTime) {
		this.loggedInTime = loggedInTime;
	}
	public String getPhNo() {
		return phNo;
	}
	public void setPhNo(String phNo) {
		this.phNo = phNo;
	}
	
	

}
