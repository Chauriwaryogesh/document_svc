package com.SecureAccessPortal.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SecureAccessPortal.Modal.CustomerDTO;
import com.SecureAccessPortal.Modal.GroupedPolicyDTO;
import com.SecureAccessPortal.Modal.PolicyDTO;
import com.SecureAccessPortal.Modal.PolicyRequest;
import com.SecureAccessPortal.Modal.ResponseDTO;
import com.SecureAccessPortal.Service.EmailService;
import com.SecureAccessPortal.Service.PolicyService;
import com.SecureAccessPortal.Service.ResponseEntity;

@RestController
@RequestMapping("/policy")
public class PolicyController {
	
	@Autowired
	private  PolicyService policyService;
	
	@Autowired
	private EmailService otpService;

	@PostMapping(value = "/policy-create", consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseDTO> fetchVideofromDir(@RequestBody PolicyRequest policyDTO,
			@RequestHeader String userCode) {
		ResponseEntity<ResponseDTO> response = new ResponseEntity<ResponseDTO>();
		ResponseDTO responseDTO = new ResponseDTO();

		responseDTO = policyService.createPolicy(policyDTO, userCode);

		if (responseDTO.getStatus().equalsIgnoreCase("Success")) {
			response.setData(responseDTO);
		} else {
			response.setErrorMessage("Failed to create policy");
		}
		return response;
	}
	@GetMapping("/policy-details")
	public com.SecureAccessPortal.Service.ResponseEntity<List<PolicyDTO>> getCustomerlDetails(
			@RequestParam(value = "policyNo", required = false) String policyNo,
			@RequestParam(value = "allpolSearch", required = false) String allPol,
			@RequestParam(value = "customerNo", required = false) String customerNo,
			@RequestParam(value = "workItemRefNo", required = false) String workItemRefNo,
			@RequestHeader String userCode) {
		if (allPol == null) {
			allPol = "N";
		}
		com.SecureAccessPortal.Service.ResponseEntity<List<PolicyDTO>> emailResp = policyService.getPolicyDetails(policyNo,
				customerNo, allPol, workItemRefNo, userCode);

		return emailResp;
	}
	
	@PostMapping("/customer-details/update")
	public com.SecureAccessPortal.Service.ResponseEntity<String> getCustomerlDetails(
			@RequestBody CustomerDTO customerDTO, @RequestHeader String userCode) {
		com.SecureAccessPortal.Service.ResponseEntity<String> resp = new com.SecureAccessPortal.Service.ResponseEntity<>();

		String message = otpService.updateCustomerDetails(customerDTO, userCode);

		if (message.contains("Success")) {
			resp.setData(message);
		} else {
			resp.setErrorMessage(message);
		}
		return resp;
	}
	@GetMapping("/policy-domain")
	public com.SecureAccessPortal.Service.ResponseEntity<List<GroupedPolicyDTO>> getPolicyDomain(
			@RequestParam(value = "policyNo", required = false) String policyNo,
			@RequestHeader(value="useCode", required=false) String userCode) {
		com.SecureAccessPortal.Service.ResponseEntity<List<GroupedPolicyDTO>> response = new com.SecureAccessPortal.Service.ResponseEntity<>();
		List<GroupedPolicyDTO> policyDTO = policyService.getDomainData(policyNo, userCode);
		if (policyDTO != null && !policyDTO.isEmpty()) {
			response.setData(policyDTO);
		} else {
			response.setErrorMessage("Error while fetching domin data ");
		}
		return response;
	}
	
	@PostMapping("/policy-details/update")
	public com.SecureAccessPortal.Service.ResponseEntity<String> getPolicyDetails(
			@RequestBody CustomerDTO customerDTO, @RequestHeader String userCode) {
		com.SecureAccessPortal.Service.ResponseEntity<String> resp = new com.SecureAccessPortal.Service.ResponseEntity<>();

		String message = otpService.updateCustomerDetails(customerDTO, userCode);

		if (message.contains("Success")) {
			resp.setData(message);
		} else {
			resp.setErrorMessage("Error while updating  customerDetails ");
		}
		return resp;
	}
	
	
}
