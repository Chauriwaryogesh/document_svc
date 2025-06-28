package com.SecureAccessPortal.Transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.SecureAccessPortal.Entity.Customer;
import com.SecureAccessPortal.Entity.Policy;
import com.SecureAccessPortal.Modal.PolicyDTO;
import com.SecureAccessPortal.Modal.PolicyList;
import com.SecureAccessPortal.Service.ResponseEntity;

@Component
public class PolicyMapper {

	public ResponseEntity<List<PolicyDTO>> mapAllPolicies(List<Customer> customer, List<Policy> policies) {

		ResponseEntity<List<PolicyDTO>> reponse = new ResponseEntity<>();
		List<PolicyDTO> policyListResp=customer.stream().map(cust -> {
			PolicyDTO policyDTO = new PolicyDTO();
			policyDTO.setCustomerNo(cust.getCustomerNo());
			policyDTO.setCustomerName(cust.getName() + "" + cust.getSurname());
			List<PolicyList> policyList = new ArrayList<>();
			policies.stream()
					.filter(policy -> policy.getCustomer().getCustomerNo().equalsIgnoreCase(cust.getCustomerNo()))
					.forEach(policy -> {
						PolicyList policyLi = new PolicyList();
						policyLi.setPolicyNumber(policy.getPolicyNumber());
						policyLi.setPolicyType(policy.getPolicyType());
						policyList.add(policyLi);
					});
			policyDTO.setPolicyList(policyList);
			return policyDTO;
		}).collect(Collectors.toList());
          reponse.setData(policyListResp);
		return reponse;
	}

}
