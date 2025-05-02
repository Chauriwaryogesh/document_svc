package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.OtpService;

@RestController
@RequestMapping("otpService")
public class OtpController {

	@Autowired
	private OtpService otpService;

	@Cacheable(value="otpCache", key="#email")
	@RequestMapping(value = "/generateOtpService", method = RequestMethod.POST)
	public ResponseEntity<String> sendOtp(/* @RequestBody EmailRequest request */
			@RequestParam(value = "Email id", required = true) String email,
			@RequestParam(value = "user id", required = true) String userId) {
		String ok = otpService.sendOtp(email,userId);
		return ResponseEntity.ok(ok);
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<String> verifyOtp(/* @RequestBody OtpRequest request */
			@RequestParam(value = "Email id") String email, @RequestParam(value = "Otp") String otp,
			@RequestParam(value = "user id", required = true) String userId) {
		boolean isValid = otpService.verifyOtp(email, otp ,userId);
		return isValid ? ResponseEntity.ok("OTP verified")
				: ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
	}

}
