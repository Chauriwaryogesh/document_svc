package com.SecureAccessPortal.Modal;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupedPolicyDTO {

	private String policyName;
	private List<PolicyInfoDTO> policies;
	
	 // Default constructor
    public GroupedPolicyDTO() {}

    // Constructor
    public GroupedPolicyDTO(String policyName, List<PolicyInfoDTO> policies) {
        this.policyName = policyName;
        this.policies = policies;
    }

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public List<PolicyInfoDTO> getPolicies() {
		return policies;
	}

	public void setPolicies(List<PolicyInfoDTO> policies) {
		this.policies = policies;
	}

}
