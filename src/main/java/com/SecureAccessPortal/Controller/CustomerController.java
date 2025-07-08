package com.SecureAccessPortal.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.SecureAccessPortal.Entity.FeedbackEntity;
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
	public ResponseEntity<List<FeedbackResponse>> getFeedback(@RequestHeader String userCode){
		ResponseEntity<List<FeedbackResponse>> response = customerService.getFeedback( userCode);
		return response;
	}

}
