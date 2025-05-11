package com.example.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PinCodeCount {
	private String pinCount;
	private String branchCount;
	private List<PinCodeOrBranchDTO> pinCodeOrBranchDTO;

	public String getPinCount() {
		return pinCount;
	}

	public void setPinCount(String pinCount) {
		this.pinCount = pinCount;
	}

	public String getBranchCount() {
		return branchCount;
	}

	public void setBranchCount(String branchCount) {
		this.branchCount = branchCount;
	}

	public List<PinCodeOrBranchDTO> getPinCodeOrBranchDTO() {
		return pinCodeOrBranchDTO;
	}

	public void setPinCodeOrBranchDTO(List<PinCodeOrBranchDTO> pinCodeOrBranchDTO) {
		this.pinCodeOrBranchDTO = pinCodeOrBranchDTO;
	}
	
	

}
