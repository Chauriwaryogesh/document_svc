
package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostOffice {
	@JsonProperty("Name")
	public String name;
	@JsonProperty("Description")
	public Object description;
	@JsonProperty("BranchType")
	public String branchType;
	@JsonProperty("DeliveryStatus")
	public String deliveryStatus;
	@JsonProperty("Circle")
	public String circle;
	@JsonProperty("District")
	public String district;
	@JsonProperty("Division")
	public String division;
	@JsonProperty("Region")
	public String region;
	@JsonProperty("Block")
	public String block;
	@JsonProperty("State")
	public String state;
	@JsonProperty("Country")
	public String country;
	@JsonProperty("Pincode")
	public String pincode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getDescription() {
		return description;
	}

	public void setDescription(Object description) {
		this.description = description;
	}

	public String getBranchType() {
		return branchType;
	}

	public void setBranchType(String branchType) {
		this.branchType = branchType;
	}

	public String getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

}
