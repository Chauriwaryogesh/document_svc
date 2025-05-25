package com.example.controller;

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

import com.example.dto.CustomerDTO;
import com.example.dto.PolicyDTO;
import com.example.dto.PolicyRequest;
import com.example.dto.ResponseDTO;
import com.example.service.OtpService;
import com.example.service.PolicyService;
import com.example.service.ResponseEntity;

@RestController
@RequestMapping("/policy")
public class PolicyController {
	
	@Autowired
	private  PolicyService policyService;
	
	@Autowired
	private OtpService otpService;

	@PostMapping(value = "/policy-create", consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseDTO> fetchVideofromDir(@RequestBody PolicyRequest policyDTO,
			@RequestHeader String userId) {
		ResponseEntity<ResponseDTO> response = new ResponseEntity<ResponseDTO>();
		ResponseDTO responseDTO = new ResponseDTO();

		responseDTO = policyService.createPolicy(policyDTO, userId);

		if (responseDTO.getStatus().equalsIgnoreCase("Success")) {
			response.setData(responseDTO);
		} else {
			response.setErrorMessage("Failed to create policy");
		}
		return response;
	}
	@GetMapping("/policy-details")
	public com.example.service.ResponseEntity<List<PolicyDTO>> getCustomerlDetails(
			@RequestParam(value = "policyNo", required = false) String policyNo,
			@RequestParam(value = "allpolSearch", required = false) String allPol,
			@RequestParam(value = "customerNo", required = false) String customerNo,
			@RequestParam(value = "workItemRefNo", required = false) String workItemRefNo,
			@RequestHeader String userId) {
		if(allPol == null) {
			allPol="N";
		}
		com.example.service.ResponseEntity<List<PolicyDTO>> emailResp = policyService.getPolicyDetails(policyNo,
				customerNo,allPol,workItemRefNo, userId);

		return emailResp;
	}
	
	@PostMapping("/customer-details/update")
	public com.example.service.ResponseEntity<String> getCustomerlDetails(
			@RequestBody CustomerDTO customerDTO, @RequestHeader String userId) {
		com.example.service.ResponseEntity<String> resp = new com.example.service.ResponseEntity<>();

		String message = otpService.updateCustomerDetails(customerDTO, userId);

		if (message.contains("Success")) {
			resp.setData(message);
		} else {
			resp.setErrorMessage("Error while updating  customerDetails ");
		}
		return resp;
	}
	@GetMapping("/policy-domain")
	public com.example.service.ResponseEntity<List<String>> getPolicyDomain(
			@RequestParam(value = "value", required = true) String value,
			@RequestHeader String userId) {
		com.example.service.ResponseEntity<List<String>> emailResp = new com.example.service.ResponseEntity<>();
		List<String> policyDTO = policyService.getDoaminData(value, userId);
		if (policyDTO != null && !policyDTO.isEmpty()) {
			emailResp.setData(policyDTO);
		} else {
			emailResp.setErrorMessage("Error while fetching damin data ");
		}
		return emailResp;
	}
	
	@PostMapping("/policy-details/update")
	public com.example.service.ResponseEntity<String> getPolicyDetails(
			@RequestBody CustomerDTO customerDTO, @RequestHeader String userId) {
		com.example.service.ResponseEntity<String> resp = new com.example.service.ResponseEntity<>();

		String message = otpService.updateCustomerDetails(customerDTO, userId);

		if (message.contains("Success")) {
			resp.setData(message);
		} else {
			resp.setErrorMessage("Error while updating  customerDetails ");
		}
		return resp;
	}
}
