package com.SecureAccessPortal.Modal;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PinCodeOrBranchDTO {
	private String id;
	private String pincCode;
	private String branchName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPincCode() {
		return pincCode;
	}

	public void setPincCode(String pincCode) {
		this.pincCode = pincCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

}
