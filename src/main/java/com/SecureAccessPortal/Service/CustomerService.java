package com.SecureAccessPortal.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Entity.Customer;
import com.SecureAccessPortal.Entity.FeedbackEntity;
import com.SecureAccessPortal.Modal.FeedbackResponse;
import com.SecureAccessPortal.Repo.CustomerRepo;
import com.SecureAccessPortal.Repo.FeedbackRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepo customerRepository;

	@Autowired
	private FeedbackRepo feedbackRepository;

	public ResponseEntity<FeedbackResponse> saveFeedback(FeedbackResponse feedbackResponse, String userCode) {
		ResponseEntity<FeedbackResponse> response = new ResponseEntity<>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		if (userCode != null) {
			Customer customer = customerRepository.findByUserCodeAndDeletedFlagN(userCode, CommonConstant.N);
			if (customer != null) {
				try {
					FeedbackEntity feedbackentity = new FeedbackEntity();
					feedbackentity.setCreatedBy(userCode);
					feedbackentity.setCreatedTime(LocalDateTime.now());
					feedbackentity.setCustomerNo(customer.getCustomerNo());
					feedbackentity.setDeletedFlag(CommonConstant.N);
					feedbackentity.setFeedbackData(mapper.writeValueAsString(feedbackResponse));
					feedbackentity.setUserCode(userCode);
					feedbackRepository.save(feedbackentity);
                    response.setStatus(CommonConstant.SUCCESS);
				} catch (JsonProcessingException e) {

					e.printStackTrace();
				}
			}else {
				response.setStatus(CommonConstant.FAILURE);
				response.setErrorMessage("customerNot found");
			}
		} else {
			
			response.setErrorMessage("userCode null");
		}
		return response;
	}

	public ResponseEntity<List<FeedbackResponse>> getFeedback(String userCode) {
		ResponseEntity<List<FeedbackResponse>> response = new ResponseEntity<>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<FeedbackResponse> listResp= new ArrayList<>();
		if(userCode != null) {
		List<FeedbackEntity> feedbackList=feedbackRepository.findByUserCodeAndDeletedFlagN(userCode,CommonConstant.N);
		listResp=feedbackList.stream().filter(Objects :: nonNull).map(feedback ->{
			FeedbackResponse feedbackResponse = new FeedbackResponse();
			try {
				feedbackResponse= mapper.readValue(feedback.getFeedbackData(), FeedbackResponse.class);
			}catch(Exception e) {
				
			}
			return feedbackResponse;
		}).collect(Collectors.toList());
		response.setData(listResp);
		response.setStatus(CommonConstant.SUCCESS);
		}else {
			response.setErrorMessage("userCode Not found");
		}
		
		return response;
	}

}
