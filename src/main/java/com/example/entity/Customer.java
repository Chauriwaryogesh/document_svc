package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name="Customer")
public class Customer {
	
	@Id
	@Column
	@NotEmpty
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
	private String gender;
	@Column
	private String phoneNuber;
	@Column
	private String id;
	@Column
	private String userId;
	@Column
	private String smokerStatus;
	@Column
	private String adminAccess;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPhoneNuber() {
		return phoneNuber;
	}
	public void setPhoneNuber(String phoneNuber) {
		this.phoneNuber = phoneNuber;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
