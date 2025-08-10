package com.SecureAccessPortal.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Entity.BankAccount;
import com.SecureAccessPortal.Entity.Complaint;
import com.SecureAccessPortal.Entity.Customer;
import com.SecureAccessPortal.Entity.FeedbackEntity;
import com.SecureAccessPortal.Entity.Payments;
import com.SecureAccessPortal.Entity.Policy;
import com.SecureAccessPortal.Entity.Workitem;
import com.SecureAccessPortal.Modal.Address;
import com.SecureAccessPortal.Modal.BankDetailsDTO;
import com.SecureAccessPortal.Modal.ComplaintDTO;
import com.SecureAccessPortal.Modal.ContactDetails;
import com.SecureAccessPortal.Modal.CustomerDTO;
import com.SecureAccessPortal.Modal.FeedbackResponse;
import com.SecureAccessPortal.Modal.PolicyDTO;
import com.SecureAccessPortal.Modal.WorkItemDTO;
import com.SecureAccessPortal.Repo.BankAccountRepo;
import com.SecureAccessPortal.Repo.ComplaintRepo;
import com.SecureAccessPortal.Repo.CustomerRepo;
import com.SecureAccessPortal.Repo.FeedbackRepo;
import com.SecureAccessPortal.Repo.IPolicyRepo;
import com.SecureAccessPortal.Repo.PaymentsRepo;
import com.SecureAccessPortal.Repo.WorkItemRepo;
import com.SecureAccessPortal.Transformer.CustomerMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepo customerRepository;

	@Autowired
	private FeedbackRepo feedbackRepository;

	@Autowired
	private IPolicyRepo policyRepository;

	@Autowired
	private WorkItemRepo workItemRepository;

	@Autowired
	private ComplaintRepo complaintRepository;

	@Autowired
	private BankAccountRepo bankRepository;

	@Autowired
	private PaymentsRepo paymentRepository;

	@Autowired
	private CustomerMapper customerMapper;
	
	@Autowired
	private PolicyService policyService;
	
	@Autowired
	private ComplaintService complaintService;
	
	@Autowired
	private IWorkItemService workItemService;
	
	@Autowired
	private BankDetailsService bankDetailsService;

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
			} else {
				response.setStatus(CommonConstant.FAILURE);
				response.setErrorMessage("customerNot found");
			}
		} else {
			response.setErrorMessage("userCode null");
		}
		return response;
	}

	public ResponseEntity<List<FeedbackResponse>> getFeedback(String customerNo, String userCode) {
		ResponseEntity<List<FeedbackResponse>> response = new ResponseEntity<>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<FeedbackResponse> listResp = new ArrayList<>();
		if (userCode != null) {
			List<FeedbackEntity> feedbackList = feedbackRepository.findByCustomerNoAndDeletedFlagN(customerNo,
					CommonConstant.N);
			listResp = feedbackList.stream().filter(Objects::nonNull).map(feedback -> {
				FeedbackResponse feedbackResponse = new FeedbackResponse();
				try {
					feedbackResponse = mapper.readValue(feedback.getFeedbackData(), FeedbackResponse.class);
				} catch (Exception e) {

				}
				return feedbackResponse;
			}).collect(Collectors.toList());
			response.setData(listResp);
			response.setStatus(CommonConstant.SUCCESS);
		} else {
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
		} else {
			response.setStatus(CommonConstant.FAILURE);
			response.setErrorMessage("No customer Details Found for this userCode " + userCode);
		}
		return response;
	}

	public ResponseEntity<List<String>> getRolesDomain(String userCode) {
		ResponseEntity<List<String>> response = new ResponseEntity<List<String>>();
		List<String> rolesList = List.of("Admin,Complaints-Admin");
		response.setData(rolesList);
		return response;
	}

	public ResponseEntity<CustomerDTO> getAllSerchUsingInput(String number, String type, String allSearch,
			String userCode) {
		ResponseEntity<CustomerDTO> response = new ResponseEntity<>();
		Customer customer = new Customer();
		List<Payments> payments = new ArrayList<>();
		List<BankAccount> bankAccountList = new ArrayList<>();
		List<Complaint> complaints = new ArrayList<>();
		List<Workitem> workitems = new ArrayList<>();
		List<Policy> policy = new ArrayList<>();
		CustomerDTO customerDTO = new CustomerDTO();
		if (number == null || type == null) {
			response.setErrorMessage("Blank Serch not allowed");
			return response;
		}
		switch (type) {
		case CommonConstant.CUSTOMER -> {
			if (allSearch.equalsIgnoreCase(CommonConstant.N)) {
				customer = customerRepository.findByCustomerNoNew(number, CommonConstant.N);
				if(customer == null) {
					response.setStatus(CommonConstant.FAILURE);	
					response.setErrorMessage("Invalid CustomerNo");
					return response;
				}
			} else {
				customer = customerRepository.findByCustomerNoNew(number, CommonConstant.N);
				if(customer == null) {
					response.setStatus(CommonConstant.FAILURE);	
					response.setErrorMessage("Invalid CustomerNo");
					return response;
				}
				policy = policyRepository.findByCustomerNoNew(number, "N");
				workitems = workItemRepository.findByCustomerNo(number);
				complaints = complaintRepository.findByCustomerNo(number);
				bankAccountList = bankRepository.findByCustomerNo(number);
				payments = paymentRepository.findByCustomerNo(number);
			}
			customerDTO = customerMapper.mapCustomerDetails(customer, policy, workitems, complaints,bankAccountList, payments,userCode);	
		}
		case CommonConstant.POLICY -> {
			List<Policy> policies = policyRepository.findByPolicyNumber(number, CommonConstant.N);
			if(policies.isEmpty()) {
				response.setStatus(CommonConstant.FAILURE);
				response.setErrorMessage("Invalid PolicyNo");
				return response;
			}
			List<PolicyDTO> mapPolicyToPolicyList = policyService.mapPolicyListDetails(policies, userCode);
			customerDTO.setPolicy(mapPolicyToPolicyList);
		}
		case CommonConstant.COMPLAINT -> {
			Complaint complaint = complaintRepository.findByComplaintNo(number, CommonConstant.N);
			if(complaint == null) {
				response.setStatus(CommonConstant.FAILURE);	
				response.setErrorMessage("Invalid complaintNo");
				return response;
			}
			ComplaintDTO mapComplaint = complaintService.mapComplaint(complaint);
			customerDTO.setComplaint(Arrays.asList(mapComplaint));

		}
		case CommonConstant.WORKITEM -> {
			List<Workitem> workitem = workItemRepository.findByWorkItemReferenceNo(number);
			if(workitem.isEmpty()) {
				response.setStatus(CommonConstant.FAILURE);	
				response.setErrorMessage("Invalid WorkitemNo");
				return response;
			}
			List<WorkItemDTO> allWorkitems = workItemService.getAllWorkitems(workitem);
			customerDTO.setWorkitems(allWorkitems);
		}
		case CommonConstant.BANK -> {
			BankAccount bankAccount = bankRepository.findByAccountNo(number, CommonConstant.N);
			if(bankAccount == null) {
				response.setStatus(CommonConstant.FAILURE);	
				response.setErrorMessage("Invalid BankAccount No");
				return response;
			}
			BankDetailsDTO  banlAccountDTO=bankDetailsService.convertToDTO( bankAccount);
			customerDTO.setBank(Arrays.asList(banlAccountDTO));
		}
		case CommonConstant.PAYMENT -> {
			Payments payment = paymentRepository.findByPaymentId(number);
			if(payment == null) {
				response.setStatus(CommonConstant.FAILURE);	
				response.setErrorMessage("Invalid PaymentId");
				return response;
			}
			//will add
		}
		}
		response.setData(customerDTO);
		response.setStatus(CommonConstant.SUCCESS);
		return response;
	}

}
