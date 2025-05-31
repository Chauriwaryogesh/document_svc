package com.SecureAccessPortal.Entity;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employees")
public class Employees implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name="id")
    private String id;
    
    @Column(name="first_name")
    private String firstName;
    
    @Column(name="last_name")
    private String lastName;
    
    @Column(name="email")
    private String email;
    
    @Column(name="phone")
    private String phone;
    
    @Column(name="address")
    private String address;
    
    @Column(name="city")
    private String city;
    
    @Column(name="state")
    private String state;
    
    @Column(name="zip_code")
    private String zipCode;
    
    @Column(name="country")
    private String country;
    
    @Column(name="department")
    private String department;
    
    @Column(name="updateBy")
    private String updateBy;

    // Getters and setters for all fields
    
    
    public String getId() {
        return id;
    }
    public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public void setId(String id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
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
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    
    @Override
    public String toString() {
        return "Employees [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName +
               ", email=" + email + ", phone=" + phone + ", address=" + address +
               ", city=" + city + ", state=" + state + ", zipCode=" + zipCode +
               ", country=" + country + ", department=" + department + "]";
    }
}
