package com.SecureAccessPortal.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Modal.CustomerDTO;
import com.SecureAccessPortal.Modal.DashboardStats;
import com.SecureAccessPortal.Modal.GroupedPolicyDTO;
import com.SecureAccessPortal.Modal.PolicyDTO;
import com.SecureAccessPortal.Modal.PolicyRequest;
import com.SecureAccessPortal.Modal.ResponseDTO;
import com.SecureAccessPortal.Modal.SurrenderClaimDTO;
import com.SecureAccessPortal.Service.EmailService;
import com.SecureAccessPortal.Service.PolicyService;
import com.SecureAccessPortal.Service.ResponseEntity;

@RestController
@RequestMapping("/policy")
public class PolicyController {

	@Autowired
	private PolicyService policyService;

	@Autowired
	private EmailService otpService;

	@PostMapping(value = "/policy-create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseDTO> createPolicy(@RequestBody PolicyRequest policyDTO,
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
			@RequestHeader(required = false) String userCode) {
		if (allPol == null) {
			allPol = "N";
		}
		com.SecureAccessPortal.Service.ResponseEntity<List<PolicyDTO>> emailResp = policyService
				.getPolicyDetails(policyNo, customerNo, allPol, workItemRefNo, userCode);

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
			@RequestParam(value = "policyName", required = false) String policyName,
			@RequestParam(value = "productCode", required = false) String productCode,
			@RequestParam(value = "policyTAmount", required = false) String policyTAmount,
			@RequestParam(value = "policyFrequency", required = false) String policyFrequency,
			@RequestParam(value = "policyInstallment", required = false) String policyInstallmentm,
			@RequestHeader(value = "useCode", required = false) String userCode) {
		com.SecureAccessPortal.Service.ResponseEntity<List<GroupedPolicyDTO>> response = new com.SecureAccessPortal.Service.ResponseEntity<>();
		List<GroupedPolicyDTO> policyDTO = policyService.getDomainData(productCode, userCode);
		if (policyDTO != null && !policyDTO.isEmpty()) {
			response.setData(policyDTO);
			response.setStatus(CommonConstant.SUCCESS);
		} else {
			response.setErrorMessage("Error while fetching domin data ");
			response.setStatus(CommonConstant.FAILURE);
		}
		return response;
	}

	@PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseDTO> updatePolicy(@RequestBody PolicyRequest policyDTO,
			@RequestHeader String userCode) {
		ResponseEntity<ResponseDTO> response = new ResponseEntity<ResponseDTO>();
		ResponseDTO responseDTO = new ResponseDTO();

		responseDTO = policyService.updatePolicy(policyDTO, userCode);

		if (responseDTO.getStatus().equalsIgnoreCase("Success")) {
			response.setData(responseDTO);
			response.setStatus(CommonConstant.SUCCESS);
		} else {
			response.setStatus(CommonConstant.FAILURE);
			response.setErrorMessage("Failed to update policy");
		}
		return response;
	}

	@GetMapping("validate/EmailOrUserCode")
	public ResponseEntity<String> validateEmailUserCode(@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "userCode", required = false) String userCode) {
		ResponseEntity<String> response = new ResponseEntity<String>();
		response = policyService.validateEmailUserCode(email, userCode);
		return response;

	}

	@GetMapping("policy/fetchAllPolicies")
	public ResponseEntity<List<PolicyDTO>> fetchAllPolicies(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "userCode", required = false) String userCode) {
		ResponseEntity<List<PolicyDTO>> response = new ResponseEntity<>();
		response = policyService.fetchAllPolicies(email, userCode);
		return response;

	}

	@PostMapping(value = "/update/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseDTO> updateDeletePolicy(@RequestParam String PolicyNumber,
			@RequestParam String reason, @RequestHeader String userCode) {
		ResponseEntity<ResponseDTO> response = new ResponseEntity<ResponseDTO>();
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO = policyService.updateDeletePolicy(PolicyNumber, reason, userCode);
		if (responseDTO.getStatus().equalsIgnoreCase("Success")) {
			response.setData(responseDTO);
			response.setStatus(CommonConstant.SUCCESS);
		} else {
			response.setStatus(CommonConstant.FAILURE);
			response.setErrorMessage("Failed to update policy");
		}
		return response;
	}

	@GetMapping("/policy-detailsNew")
	public com.SecureAccessPortal.Service.ResponseEntity<Page<PolicyDTO>> getCustomerlDetails(
			@RequestParam(value = "policyNo", required = false) String policyNo,
			@RequestParam(value = "allpolSearch", required = false) String allPol,
			@RequestParam(value = "customerNo", required = false) String customerNo,
			@RequestParam(value = "workItemRefNo", required = false) String workItemRefNo,
			@RequestHeader(required = false) String userCode,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {

		if (allPol == null) {
			allPol = "N";
		}

		Pageable pageable = PageRequest.of(page, size);
		com.SecureAccessPortal.Service.ResponseEntity<Page<PolicyDTO>> emailResp = policyService
				.getPolicyDetailsNew(policyNo, customerNo, allPol, workItemRefNo, userCode, pageable);

		return emailResp;
	}

	@PostMapping(value = "/apply-policy", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> applyPolicy(@RequestBody PolicyDTO policyDTO,
			@RequestHeader(required = false) String userCode) {
		ResponseEntity<String> response = policyService.applyForPolicy(policyDTO, userCode);
		return response;
	}
	
	@RequestMapping(value="getSurrenderDtls" ,method= RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<SurrenderClaimDTO>> getSurrenderDtls(
			@RequestParam(required = false) String action,
			@RequestParam(required = false) String surrenderRefNo,
			@RequestParam(required = false) String policyNo, @RequestParam(required = false) String customerNo,
			@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate,
			@RequestParam(required = false) String type,
			@RequestParam(defaultValue="0") int page,
			@RequestParam(defaultValue = "20") int size, @RequestHeader(required = false) String userCode) {
		ResponseEntity<Page<SurrenderClaimDTO>> response = new ResponseEntity<>();
		response = policyService.getSurrender(action,surrenderRefNo, policyNo, customerNo, startDate, endDate, type,
				page, size, userCode);

		return response;
	}
	@RequestMapping(value="getClaimDtls" ,method= RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<SurrenderClaimDTO>> getSurrenderClaimDetails(
			@RequestParam(required = false) String action, @RequestParam(required = false) String claimRefNo,
			@RequestParam(required = false) String policyNo, @RequestParam(required = false) String customerNo,
			@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate,
			@RequestParam(required = false) String type,
			@RequestParam(defaultValue="0") int page,
			@RequestParam(defaultValue = "20") int size, @RequestHeader(required = false) String userCode) {
		ResponseEntity<Page<SurrenderClaimDTO>> response = new ResponseEntity<>();

		response = policyService.getClaim(action, claimRefNo, policyNo, customerNo, startDate, endDate, type,
				page, size, userCode);

		return response;
	}
	
	@PostMapping(value ="/surrender", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SurrenderClaimDTO> submitSurrender(@RequestBody SurrenderClaimDTO request,
			@RequestHeader String userCode) {
		ResponseEntity<SurrenderClaimDTO> response = new ResponseEntity<>();

		response = policyService.submitSurrenderRequest(request, userCode);

		return response;
	}
	@PostMapping(value ="/cancelSurrender", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SurrenderClaimDTO> cancelSurrender(@RequestBody SurrenderClaimDTO request,
			@RequestHeader String userCode) {
		ResponseEntity<SurrenderClaimDTO> response = new ResponseEntity<>();

		response = policyService.submitSurrenderRequest(request, userCode);

		return response;
	}
	@PostMapping(value ="/claim", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SurrenderClaimDTO> claim(@RequestBody SurrenderClaimDTO request,
			@RequestHeader String userCode) {
		ResponseEntity<SurrenderClaimDTO> response = new ResponseEntity<>();
		response = policyService.submitSurrenderRequest(request, userCode);
		return response;
	}
	@RequestMapping(value="getSurrenderCount" ,method= RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DashboardStats> getSurrenderCount(
			@RequestParam(required = false) String customerNo,
			@RequestHeader(required = false) String userCode) {
		ResponseEntity<DashboardStats> response = new ResponseEntity<>();

		response = policyService.getSurrenderCount(customerNo, userCode);

		return response;
	}

}
