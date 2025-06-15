package com.SecureAccessPortal.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.SecureAccessPortal.Exception.ResourceNotFoundException;
import com.SecureAccessPortal.Modal.BankDetailsDTO;
import com.SecureAccessPortal.Modal.PhotoDTO;
import com.SecureAccessPortal.Modal.PolicyRequest;
import com.SecureAccessPortal.Modal.VerificationRecordDTO;
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

		List<BankDetailsDTO> bankDetails = bankDetailsService.getBankDetails(bankAccNo, policyNo, customerNo, userCode);
		if (bankDetails != null && !bankDetails.isEmpty()) {
			response.setData(bankDetails);
		} else {
			response.setErrorMessage("No Bank account found for Customer");
		}
		return response;
	}

	@PostMapping(value = "/addBank-Details", consumes = MediaType.APPLICATION_JSON_VALUE)
	public com.SecureAccessPortal.Service.ResponseEntity<String> addBankDetails(
			@RequestBody BankDetailsDTO bankDetailsDTO,
			@RequestHeader(value = "userCode", required = true) String userCode) {
		com.SecureAccessPortal.Service.ResponseEntity<String> response = new com.SecureAccessPortal.Service.ResponseEntity<>();
		try {
			String resp = bankDetailsService.addBankDetails(bankDetailsDTO, userCode);
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
		PolicyRequest custDtl = bankDetailsService.getCustomerDetails(policyNo, customerNo, userCode);
		if (custDtl != null && !custDtl.isBlank()) {
			response.setData(custDtl);
		} else {
			response.setErrorMessage("No Customer details found for the given Policy");
		}
		return response;
	}

	@PostMapping(value = "/verification/upload-doc", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public org.springframework.http.ResponseEntity<Map<String, Object>> createVerificationRecord(
			@RequestParam("action") String action, @RequestParam("accountNo") String accountNo,
			@RequestParam("document") MultipartFile document,
			@RequestParam(value = "status", required = false, defaultValue = "PENDING") String status,
			@RequestParam("userCode") String userCode,
			@RequestParam(value = "details", required = false) String details,
			@RequestParam(value = "customerNo", required = false) String customerNo,
			@RequestParam(value = "policyNumber", required = false) String policyNumber) {

		try {
			VerificationRecordDTO verificationRecordDTO = bankDetailsService.createVerificationRecord(action, accountNo,
					document, status, userCode, details, customerNo, policyNumber);
			Map<String, Object> response = new HashMap<>();
			response.put("id", verificationRecordDTO.getId());
			response.put("accountNo", verificationRecordDTO.getAccountNo());
			response.put("action", verificationRecordDTO.getAction());
			response.put("status", verificationRecordDTO.getStatus());
			response.put("message", "Verification record created successfully");
			return new org.springframework.http.ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("error", e.getMessage());
			return new org.springframework.http.ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		} catch (ResourceNotFoundException e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("error", e.getMessage());
			return new org.springframework.http.ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value ="getVerificationRecord")
	public org.springframework.http.ResponseEntity<Page<VerificationRecordDTO>> getVerificationRecords(
			@RequestParam("accountNo") String accountNo,
			@RequestParam(value = "action", required = false) String action,
			@RequestParam(value = "status", required = false) String status, @RequestParam("userCode") String userCode,
			Pageable pageable) {

		try {
			Page<VerificationRecordDTO> verificationRecords = bankDetailsService.getVerificationRecords(accountNo,
					action, status, userCode, pageable);
			return new org.springframework.http.ResponseEntity<>(verificationRecords, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		} catch (ResourceNotFoundException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	@GetMapping("/{id}/document")
	public org.springframework.http.ResponseEntity<byte[]> getVerificationDocument(@PathVariable String id,
			@RequestParam("action") String action, @RequestParam("userCode") String userCode) {

		try {
			byte[] document = bankDetailsService.getVerificationDocument(id, action, userCode);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF); // Adjust based on document type
			headers.setContentDispositionFormData("attachment", "document.pdf");
			return new org.springframework.http.ResponseEntity<>(document, headers, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		} catch (ResourceNotFoundException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}
	@PostMapping("/{id}/updateStatus")
	public org.springframework.http.ResponseEntity<String> updateStatus(@PathVariable String id,
			@RequestBody VerificationRecordDTO verificationRecordDTO, 
			@RequestHeader (value= "userCode", required =false) String userCode) {
		try {
			String message  = bankDetailsService.updateStatus(id, verificationRecordDTO,userCode);
			return new org.springframework.http.ResponseEntity<>(message, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		} catch (ResourceNotFoundException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}
	@RequestMapping(value = "/getAllDocuments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public com.SecureAccessPortal.Service.ResponseEntity<List<VerificationRecordDTO>> getDocument(
			@RequestParam (value="accountNo",required=false) String accountNo,
			@RequestHeader(value = "userCode", required = false) String userCode) {

		com.SecureAccessPortal.Service.ResponseEntity<List<VerificationRecordDTO>> docslist = new com.SecureAccessPortal.Service.ResponseEntity<>();
		List<VerificationRecordDTO> document = bankDetailsService.getAllDocuments(accountNo,userCode);
		if (document != null) {
			docslist.setData(document);
		} else {
			docslist.setErrorMessage("document List isEmpty");

		}
		return docslist;
	}
	

}
