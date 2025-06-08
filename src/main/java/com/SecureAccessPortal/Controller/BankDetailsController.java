package com.SecureAccessPortal.Controller;


import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SecureAccessPortal.Modal.BankDetailsDTO;
import com.SecureAccessPortal.Modal.PolicyRequest;
import com.SecureAccessPortal.Service.BankDetailsService;
import com.SecureAccessPortal.Service.ResponseEntity;

@RestController
@RequestMapping("/bankService")
public class BankDetailsController {

    private final BankDetailsService bankDetailsService;

    public BankDetailsController(BankDetailsService bankDetailsService) {
        this.bankDetailsService = bankDetailsService;
    }

	@GetMapping(value = "/bank-details")
	public ResponseEntity<List<BankDetailsDTO>> getBankDetails(
			@RequestParam(value = "bankAccNo", required = false) String bankAccNo,
			@RequestParam(value = "policyNo", required = false) String policyNo,
			@RequestParam(value = "customerNo", required = false) String customerNo,
			@RequestHeader(value = "userCode", required = true) String userCode) {
		com.SecureAccessPortal.Service.ResponseEntity<List<BankDetailsDTO>> response = new com.SecureAccessPortal.Service.ResponseEntity<>();

		List<BankDetailsDTO> bankDetails = bankDetailsService.getBankDetails(bankAccNo,policyNo,customerNo,userCode);
		if (bankDetails != null) {
			response.setData(bankDetails);
		} else {
			response.setErrorMessage("No Bank account found for Customer");
		}
		return response;
	}
   
    @PostMapping(value="/addBank-Details",consumes= MediaType.APPLICATION_JSON_VALUE)
    	public com.SecureAccessPortal.Service.ResponseEntity<String> addBankDetails(@RequestBody BankDetailsDTO bankDetailsDTO,
    			@RequestHeader(value = "userCode", required = true) String userCode
    			) {
    		com.SecureAccessPortal.Service.ResponseEntity<String> response = new com.SecureAccessPortal.Service.ResponseEntity<>();
    		try {
    			String resp = bankDetailsService.addBankDetails(bankDetailsDTO,userCode);
    			if (resp.contains("successfully")) {
    				response.setData(resp);
    			} else {
    				response.setData(resp);
    			}

    		} catch (Exception e) {
    			e.getStackTrace();
    		}
    		return response;
    	}
    
    @GetMapping(value = "/bank-details/BankNames")
	public ResponseEntity<List<String>> getBankNames(
			@RequestHeader(value = "userCode", required = true) String userCode) {
		com.SecureAccessPortal.Service.ResponseEntity<List<String>> response = new com.SecureAccessPortal.Service.ResponseEntity<>();
		List<String> bankDetails = bankDetailsService.getBankNames(userCode);
		if (bankDetails != null) {
			response.setData(bankDetails);
		} else {
			response.setErrorMessage("No BAnk NAmes found");
		}
		return response;
	}
    @GetMapping(value = "/bank-details/branchCodes")
	public ResponseEntity<List<String>> getBranchCodes(
			@RequestHeader(value = "userCode", required = true) String userCode) {
		com.SecureAccessPortal.Service.ResponseEntity<List<String>> response = new com.SecureAccessPortal.Service.ResponseEntity<>();
		List<String> bankDetails = bankDetailsService.getBranchCodes(userCode);
		if (bankDetails != null) {
			response.setData(bankDetails);
		} else {
			response.setErrorMessage("No BAnk NAmes found");
		}
		return response;
	}
    
    @GetMapping(value = "/bank-details/PolicySearch")
	public ResponseEntity<PolicyRequest> getCustomerDetails(
			@RequestParam(value = "policyNo", required = false) String policyNo,
			@RequestParam(value = "customerNo", required = false) String customerNo,
			@RequestHeader(value = "userCode", required = true) String userCode) {
		com.SecureAccessPortal.Service.ResponseEntity<PolicyRequest> response = new com.SecureAccessPortal.Service.ResponseEntity<>();
		PolicyRequest custDtl = bankDetailsService.getCustomerDetails(policyNo,customerNo,userCode);
		if (custDtl != null) {
			response.setData(custDtl);
		} else {
			response.setErrorMessage("No BAnk NAmes found");
		}
		return response;
	}
    
    
    
}
