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
import com.SecureAccessPortal.Modal.Address;
import com.SecureAccessPortal.Modal.ContactDetails;
import com.SecureAccessPortal.Modal.CustomerDTO;
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
					feedbackentity.setCustomer(customer);
					feedbackentity.setDeletedFlag(CommonConstant.N);
					feedbackentity.setFeedbackData(mapper.writeValueAsString(feedbackResponse));			
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

	public ResponseEntity<List<FeedbackResponse>> getFeedback(String customerNo,   String userCode) {
		ResponseEntity<List<FeedbackResponse>> response = new ResponseEntity<>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<FeedbackResponse> listResp= new ArrayList<>();
		if(userCode != null) {
		List<FeedbackEntity> feedbackList=feedbackRepository.findByCustomerNoAndDeletedFlagN(customerNo,CommonConstant.N);
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

	public ResponseEntity<CustomerDTO> getCustomerDetails(String userCode) {

		ResponseEntity<CustomerDTO> response = new ResponseEntity<>();
		Customer customer = customerRepository.findByUserCodeAndDeletedFlagN(userCode, CommonConstant.N);
		if (customer != null) {
			CustomerDTO custDTO = new CustomerDTO();
			custDTO.setAdminAccess(customer.getAdminAccess());
			custDTO.setAge(customer.getAge());
			custDTO.setEmail(customer.getEmail());
			custDTO.setGender(customer.getGender());
			custDTO.setCustomerNo(customer.getCustomerNo());
			custDTO.setMiddleName(customer.getMiddleName());
			custDTO.setName(customer.getName());
			custDTO.setPhoneNumber(customer.getPhoneNumber());
			custDTO.setSurname(customer.getSurname());
			custDTO.setUserCode(customer.getUserCode());
			custDTO.setSmokerStatus(customer.getSmokerStatus());
			custDTO.setDateOfBirth(customer.getDateOfBirth());
			// mapping for address
			Address address = new Address();
			address.setCity(customer.getCity());
			address.setCountry(customer.getCountry());
			address.setState(customer.getState());
			address.setStreet(customer.getStreet());
			address.setZipCode(customer.getZipCode());
			// mapping for contactDetails
			ContactDetails contact = new ContactDetails();
			contact.setAlternateEmail(customer.getAlternateEmail());
			contact.setEmergencyContactName(customer.getEmergencyContactName());
			contact.setEmergencyContactPhone(customer.getEmergencyContactPhone());
			contact.setPhoneCountryCode(customer.getPhoneCountryCode());
			contact.setPhoneNumber(customer.getPhoneNumber());
			custDTO.setAddress(address);
			custDTO.setContactDetails(contact);
			custDTO.setRoles(customer.getRoles());
			response.setData(custDTO);
			response.setStatus(CommonConstant.SUCCESS);
		} else {
			response.setStatus(CommonConstant.FAILURE);
			response.setErrorMessage("No customer Details Found for this userCode " + userCode);
		}
		return response;
	}

	public ResponseEntity<CustomerDTO> addRoles(CustomerDTO customerDTO, String userCode) {
		ResponseEntity<CustomerDTO> response = new ResponseEntity<>();
		Customer customer = customerRepository.findByCustomerNoNew(customerDTO.getCustomerNo(), CommonConstant.N);
		if (customer != null) {
			customer.setRoles(customerDTO.getRoles());
			customer.setUpdatedBy(userCode);
			customer.setUpdatedTime(LocalDateTime.now());
			customerRepository.save(customer);
			response.setStatus(CommonConstant.SUCCESS);
		}else {
			response.setStatus(CommonConstant.FAILURE);
			response.setErrorMessage("No customer Details Found for this userCode " + userCode);
		}
		return response;
	}

	public ResponseEntity<List<String>> getRolesDomain(String userCode) {
		ResponseEntity<List<String>> response= new ResponseEntity<List<String>>();
		List<String> rolesList= List.of("Admin,Complaints-Admin");
		response.setData(rolesList);
		return response;
	}

}
