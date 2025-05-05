package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.EmailDTO;
import com.example.service.OtpService;

@RestController
@RequestMapping("EmailService")
public class EmailController {

	@Autowired
	private OtpService otpService;

	//@Cacheable(value = "otpCache", key = "#email")
	@RequestMapping(value = "/generateOtpService", method = RequestMethod.POST)
	public com.example.service.ResponseEntity<String> sendOtp(
			@RequestParam(value = "Email id", required = true) String email,
			@RequestParam(value = "user id", required = true) String userId) {
		com.example.service.ResponseEntity<String> ok = otpService.sendOtp(email, userId);

		return ok;
	}

	@PostMapping("/verify-otp")
	public com.example.service.ResponseEntity<String> verifyOtp(
			@RequestParam(value = "Email id") String email, @RequestParam(value = "Otp") String otp,
			@RequestHeader(value = "user-id", required = true) String userId) {
		boolean isValid = otpService.verifyOtp(email, otp, userId);
		com.example.service.ResponseEntity<String> data= new com.example.service.ResponseEntity<>();
		if(isValid) {
			data.setData("Otp Verified Successfully");
		}else {
			data.setErrorMessage("Invalid otp , please enter correct OTP or click on generate otp button");
		}
//		return isValid ? ResponseEntity.ok("OTP verified")
//				: ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
		return data;
	}

	@PostMapping(value = "/add-email", consumes = MediaType.APPLICATION_JSON_VALUE)
	public com.example.service.ResponseEntity<String> addEmailService(@RequestBody List<EmailDTO> emailDTO,
			@RequestHeader(value = "userId", required = true) String userId) {

		com.example.service.ResponseEntity<String> emailResp = new com.example.service.ResponseEntity<>();

		String email = otpService.addEmailList(emailDTO, userId);

		if (email != null && !email.isEmpty()) {
			emailResp.setData(email);
		} else {
			emailResp.setErrorMessage("Error while adding email ");
		}
		return emailResp;

	}
	
	@GetMapping("/fetchEmailids")
	public com.example.service.ResponseEntity<List<EmailDTO>> fetchEmailDetails(@RequestParam (value="Email id", required = false) String id,
			@RequestHeader String userId) {
		com.example.service.ResponseEntity<List<EmailDTO>> emailResp = new com.example.service.ResponseEntity<>();

		List<EmailDTO> email = otpService.fetchListOfEmailIds(id, userId);

		if (email != null && !email.isEmpty()) {
			emailResp.setData(email);
		} else {
			emailResp.setErrorMessage("Error while fetching email ");
		}
		return emailResp;
	}

}
