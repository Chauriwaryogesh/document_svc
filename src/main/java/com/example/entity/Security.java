package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="security")
public class Security {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	long id;
	@Column
	private String email;
	@Column
	private String isEmailVerified;
	@Column(name="userName")
	String userName;
	@Column
	private String isUserCodeVerified;
	@Column (name="userCode")
	String userCode;
	
	@Column(name="updateBy")
    private String updateBy;

	
	@Column(name="updateTime")
    private String updateTime;

	
	@Column(name="endTime")
    private String endTime;
	
	@Column
    private String deletedFlag;
	
	
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

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

	@Override
	public String toString() {
		return "Security [id=" + id + ", userName=" + userName + ", userCode=" + userCode + "]";
	}
	
	

}
