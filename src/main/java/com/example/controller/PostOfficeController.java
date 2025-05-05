package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.PinCodeCount;
import com.example.dto.PostalResponse;
import com.example.service.PostOfficeService;
import com.example.service.ResponseEntity;

@RestController
@RequestMapping("/Post-office")
public class PostOfficeController {

	@Autowired
	private PostOfficeService postOfficeService;
	@GetMapping("/serch-postCd/Name")
	public PostalResponse getPOstOfficeDetails(@RequestParam(value = "pincode", required = false) String pincode,
			@RequestParam(value = "branchName", required = false) String brnchName,
					@RequestParam(value = "userId", required = true) String userId) {
		
		PostalResponse postOffice= postOfficeService.getPostOfficeInfo(pincode,brnchName,userId);
		return postOffice;

	}
	
	@GetMapping("/PincodeOrBranch")
	public ResponseEntity<List<PinCodeCount>> getpinCodeDetails(@RequestParam( value = "pincode", required = false) String pincode,
	@RequestParam(value = "branchName", required = false) String brnchName,
	@RequestHeader(value = "userId", required = true) String userId) {

		ResponseEntity<List<PinCodeCount>> response=new  ResponseEntity<>();
		List<PinCodeCount> postOffice= postOfficeService.getPinCodeInfo(pincode,brnchName,userId);
		response.setData(postOffice);
		return response;

	}
	

}
