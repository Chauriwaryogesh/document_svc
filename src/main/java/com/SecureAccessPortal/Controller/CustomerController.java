package com.SecureAccessPortal.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SecureAccessPortal.Modal.CustomerDTO;
import com.SecureAccessPortal.Modal.FeedbackResponse;
import com.SecureAccessPortal.Service.CustomerService;
import com.SecureAccessPortal.Service.ResponseEntity;


@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@RequestMapping(value = "/save-feedback", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FeedbackResponse> saveFeedback(@RequestBody FeedbackResponse feedbackResponse,
			@RequestHeader String userCode) {
		ResponseEntity<FeedbackResponse> response = customerService.saveFeedback(feedbackResponse, userCode);
		return response;
	}
	
	@RequestMapping(value="/getFeedback",method=RequestMethod.GET)
	public ResponseEntity<List<FeedbackResponse>> getFeedback(
			@RequestParam String customerNo
			,@RequestHeader String userCode){
		ResponseEntity<List<FeedbackResponse>> response = customerService.getFeedback(customerNo, userCode);
		return response;
	}
	
	@RequestMapping(value="/getCustomerDetails",method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CustomerDTO> getCustmerDetails(@RequestHeader String userCode){
		ResponseEntity<CustomerDTO> response = customerService.getCustomerDetails(userCode);
		return response;
	}
	
	@PostMapping(value= "/add-Roles", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CustomerDTO> addRoles(@RequestBody CustomerDTO customerDTO ,
			@RequestHeader String userCode) {
		ResponseEntity<CustomerDTO> response = customerService.addRoles(customerDTO, userCode);
		return response;

	}
	@RequestMapping(value="/domain-role",method=RequestMethod.GET)
	public ResponseEntity<List<String>> getDomainRole(@RequestHeader(required= false) String userCode){
		ResponseEntity<List<String>> response = customerService.getRolesDomain(userCode);
		return response;
	}

}
