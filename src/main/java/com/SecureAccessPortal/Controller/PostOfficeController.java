package com.SecureAccessPortal.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SecureAccessPortal.Modal.PinCodeCount;
import com.SecureAccessPortal.Modal.PostalResponse;
import com.SecureAccessPortal.Service.PostOfficeService;
import com.SecureAccessPortal.Service.ResponseEntity;

@RestController
@RequestMapping("/Post-office")
public class PostOfficeController {

	@Autowired
	private PostOfficeService postOfficeService;

	@GetMapping("/serch-postCd/Name")
	public PostalResponse getPOstOfficeDetails(@RequestParam(value = "pincode", required = false) String pincode,
			@RequestParam(value = "branchName", required = false) String brnchName,
			@RequestParam(value = "userCode", required = true) String userCode) {

		PostalResponse postOffice = postOfficeService.getPostOfficeInfo(pincode, brnchName, userCode);
		return postOffice;

	}

	@GetMapping("/PincodeOrBranch")
	public ResponseEntity<List<PinCodeCount>> getpinCodeDetails(
			@RequestParam(value = "pincode", required = false) String pincode,
			@RequestParam(value = "branchName", required = false) String brnchName,
			@RequestHeader(value = "userCode", required = true) String userCode) {

		ResponseEntity<List<PinCodeCount>> response = new ResponseEntity<>();
		List<PinCodeCount> postOffice = postOfficeService.getPinCodeInfo(pincode, brnchName, userCode);
		response.setData(postOffice);
		return response;

	}

}
