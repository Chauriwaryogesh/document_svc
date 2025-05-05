package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.PostalResponse;
import com.example.service.PostOfficeService;

@RestController
@RequestMapping("/Post-office")
public class PostOfficeController {

	@Autowired
	private PostOfficeService postOfficeService;
	@GetMapping("/serch-postCd/Name")
	public PostalResponse getPOstOfficeDetails(@RequestParam(value = "pincode", required = false) String pincode,
			@RequestParam(value = "branchName", required = false) String brnchName,
					@RequestParam(value = "userCode", required = true) String userId) {
		
		PostalResponse postOffice= postOfficeService.getPostOfficeInfo(pincode,brnchName,userId);
		return postOffice;

	}

}
