package com.SecureAccessPortal.Modal;

import java.util.List;


public class GroupedPolicyDTO {

	private String policyName;
	private String productCode;
	private List<PolicyInfoDTO> policies;
	public String getPolicyName() {
		return policyName;
	}
	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public List<PolicyInfoDTO> getPolicies() {
		return policies;
	}
	public void setPolicies(List<PolicyInfoDTO> policies) {
		this.policies = policies;
	}
	public GroupedPolicyDTO(String policyName, String productCode, List<PolicyInfoDTO> policies) {
		super();
		this.policyName = policyName;
		this.productCode = productCode;
		this.policies = policies;
	}
	
	
	
}
