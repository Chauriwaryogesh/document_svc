package com.example.service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.CommonConstants.CommonConstant;
import com.example.dto.PolicyDTO;
import com.example.dto.PolicyList;
import com.example.dto.PolicyRequest;
import com.example.dto.ResponseDTO;
import com.example.dto.WorkItemDTO;
import com.example.entity.Customer;
import com.example.entity.Policy;
import com.example.exception.CustomerNotFoundException;
import com.example.repo.CustomerRepo;
import com.example.repo.IPolicyRepo;

import jakarta.transaction.Transactional;

@Component
public class PolicyService {

	private static final Logger logger = LoggerFactory.getLogger(PolicyService.class);

	@Autowired
	private IPolicyRepo policyRepository;
	
	@Autowired
	private IWorkItemService workItemService;
	
	@Autowired
	private CustomerRepo customerRepo;

	public ResponseDTO createPolicy(PolicyRequest policyDTO, String userId) {
		ResponseDTO response = new ResponseDTO();
		try {
			Policy policy = new Policy();
			policy.setCreatedBy(userId);
			policy.setCreatedDate(LocalDateTime.now());
			policy.setCustomerNo(policyDTO.getCustomerNo());
			policy.setDeletedFlag("N");
			policy.setPolCompanyName(policyDTO.getPolCompanyName());
			policy.setPolicyName(policyDTO.getPolicyName());
			policy.setProductCode(policyDTO.getProductCode());
			policy.setUpdatedBy(userId);
			policy.setUserId(userId);
			policy.setFcuFlag(policyDTO.getFcuFlag());

			String newPolicyNumber = generatePolicyNumber();
			policy.setPolicyNumber(newPolicyNumber);
			// call workitem Service to generate WIrefNo.

			WorkItemDTO workItemRequest = new WorkItemDTO();
			workItemRequest.setComment("Policy is created " + newPolicyNumber + "for the customer");
			workItemRequest.setCreatedBy(userId);
			workItemRequest.setUserId(userId);
			workItemRequest.setWorkItemName(CommonConstant.POLICY_CREATED);
			workItemRequest.setCreatedTime(String.valueOf(LocalDateTime.now()));
			workItemRequest.setWorkType(CommonConstant.ADD_POL);
			workItemRequest.setStatus(CommonConstant.OPEN);
			workItemRequest.setQueue(CommonConstant.TEAM_MEMBER);
			WorkItemDTO workItem = workItemService.createWorkItem(workItemRequest, userId);
			if (workItem != null) {
				policy.setWorkItemRefNo(workItem.getWorkItemReferenceNumber());
				logger.info("WorkItemcreated succesfully");

			} else {
				new RuntimeErrorException(null, "Error while creating WorkItem");
			}
			policyRepository.save(policy);
			response.setPolicyNo(newPolicyNumber);
			response.setStatus(CommonConstant.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(CommonConstant.FAILURE);
		}
		return response;
	}

	@Transactional
	public synchronized String generatePolicyNumber() {
		String currentYearTwoDigits = String.valueOf(Year.now().getValue()).substring(2); // e.g., "25" for 2025
		String lastPolicyNo = policyRepository.findTopPolicyNumberForCurrentYear(currentYearTwoDigits);
		int nextSequenceNumber = 1;
		if (lastPolicyNo != null && lastPolicyNo.length() >= 9) { // Ensure it's long enough to parse
			try {
				String numberPart = lastPolicyNo.substring(4, 9);
				nextSequenceNumber = Integer.parseInt(numberPart) + 1;
			} catch (NumberFormatException e) {
				logger.warn("Could not parse sequence number from last policy number: {}. Starting sequence from 1.",
						lastPolicyNo, e);
				nextSequenceNumber = 1;
			}
		}
		String newPolicyNumber = String.format("POLC%05d%s", nextSequenceNumber, currentYearTwoDigits);
		logger.info("Generated new policy number: {}", newPolicyNumber);
		return newPolicyNumber;
	}

	public ResponseEntity<List<PolicyDTO>> getPolicyDetails(String policyNo, String customerNo, String allpol,
			String workItemRefNo, String userId) {

		ResponseEntity<List<PolicyDTO>> resp = new ResponseEntity<List<PolicyDTO>>();
		List<PolicyDTO> response = new ArrayList<>();
		if (policyNo != null) {
			Policy policy = policyRepository.findByPolicyNum(policyNo);
			if (policy != null && allpol.equalsIgnoreCase("Y")) {
				String custNo = policy.getCustomerNo();
				List<Policy> customerPolicies = policyRepository.findByCustomerNo(custNo);
				if (customerPolicies.isEmpty()) {
					resp.setErrorMessage("No customer found for policy number: " + policyNo);
				} else {
					response = mapPolciyListDetails(customerPolicies, response, userId);
				}
				resp.setData(response);
			} else if (policy != null && allpol.equalsIgnoreCase("N")) {
				Optional<Customer> customerDtls = customerRepo.findByCustomerNo(policy.getCustomerNo());
				if (customerDtls.isPresent()) {
					Customer customer = customerDtls.get();
					PolicyDTO policyDTO = new PolicyDTO();
					policyDTO.setCustomerNo(customer.getCustomerNo());
					policyDTO.setUserId(policy.getUserId());
					policyDTO.setPhoneNum(customer.getPhoneNumber());
					policyDTO.setCustName(customer.getName());
					policyDTO.setSmokerStatus(customer.getSmokerStatus());
					policyDTO.setSurname(customer.getSurname());
					policyDTO.setGender(customer.getGender());
					policyDTO.setMiddleName(customer.getMiddleName());
					policyDTO.setDateOfBirth(customer.getDateOfBirth());
					policyDTO.setEmail(customer.getEmail());

					// Add only the searched policy
					List<PolicyList> policyList = new ArrayList<>();
					PolicyList pol = new PolicyList();
					pol.setFcuFlag(policy.getFcuFlag());
					pol.setPolicyNumber(policy.getPolicyNumber());
					pol.setPolCompanyName(policy.getPolCompanyName());
					pol.setPolicyName(policy.getPolicyName());
					pol.setProductCode(policy.getProductCode());
					policyList.add(pol);
					policyDTO.setPolicyList(policyList);
					policyDTO.setAssociatedpolicyCount("1");

					response.add(policyDTO);
					resp.setData(response);
				}
			} else {
				resp.setErrorMessage("No customer found for policy number: " + policyNo);
			}

		} else if (customerNo != null) {
			Optional<Customer> customerDtls = customerRepo.findByCustomerNo(customerNo);
			if (!customerDtls.isPresent()) {
				resp.setErrorMessage("No customer found for customer number: " + customerNo);
			} else {
				Customer customer = customerDtls.get();
				List<Policy> policyList = policyRepository.findByCustomerNo(customerNo);
				if (policyList != null && !policyList.isEmpty()) {
					response = mapPolciyListDetails(policyList, response, userId);
				} else {
					// Return customer details with no policies
					PolicyDTO policyDTO = new PolicyDTO();
					policyDTO.setAssociatedpolicyCount("0");
					policyDTO.setCustomerNo(customerNo);
					policyDTO.setUserId(userId);
					policyDTO.setPhoneNum(customer.getPhoneNumber());
					policyDTO.setCustName(customer.getName());
					policyDTO.setSmokerStatus(customer.getSmokerStatus());
					policyDTO.setSurname(customer.getSurname());
					policyDTO.setGender(customer.getGender());
					policyDTO.setMiddleName(customer.getMiddleName());
					policyDTO.setDateOfBirth(customer.getDateOfBirth());
					policyDTO.setEmail(customer.getEmail());
					response.add(policyDTO);
				}
				resp.setData(response);
			}

		}else if(workItemRefNo != null) {
			List<Policy> policy = policyRepository.findByWorkItemRefNum(workItemRefNo);
			if (policy != null &&  !policy.isEmpty()) {
				String custNo = policy.get(0).getCustomerNo();
				List<Policy> customerPolicies = policyRepository.findByCustomerNo(custNo);
				if (customerPolicies.isEmpty()) {
					resp.setErrorMessage("No customer found for policy number: " + policyNo);
				} else {
					response = mapPolciyListDetails(customerPolicies, response, userId);
				}
				resp.setData(response);
			}else {
				resp.setErrorMessage("No customer and Policy found for give "+ workItemRefNo +" number ");
			}
		}
//		else {
//			List<Policy> policyList = policyRepository.findAll();
//			if (policyList != null && !policyList.isEmpty()) {
//				response = mapPolicyListDetails(policyList, response, userId);
//				resp.setData(response);
//			}
//		}

		return resp;
	}

	private List<PolicyDTO> mapPolciyListDetails(List<Policy> policyListPOl, List<PolicyDTO> response, String userId) {
	    // Group policies by customer number
	    Map<String, List<Policy>> customerPolicyMap = policyListPOl.stream()
	        .collect(Collectors.groupingBy(Policy::getCustomerNo));

	    response = customerPolicyMap.entrySet().stream().map(entry -> {
	        String customerNo = entry.getKey();
	        List<Policy> policies = entry.getValue();

	        // Fetch customer details once
	        Optional<Customer> customerDtls = customerRepo.findByCustomerNo(customerNo);
	        if (customerDtls.isEmpty()) {
	            return null; // or skip, or throw an exception based on your business logic
	        }
	        Customer customer = customerDtls.get();

	        // Create DTO and set customer details
	        PolicyDTO policyDTO = new PolicyDTO();
	        policyDTO.setAssociatedpolicyCount(String.valueOf(policies.size()));
	        policyDTO.setCustomerNo(customerNo);
	        policyDTO.setUserId(userId); // You might want to use policies.get(0).getUserId() if it varies
	        policyDTO.setPhoneNum(customer.getPhoneNumber());
	        policyDTO.setCustName(customer.getName());
	        policyDTO.setSmokerStatus(customer.getSmokerStatus());
	        policyDTO.setSurname(customer.getSurname());
	        policyDTO.setGender(customer.getGender());
	        policyDTO.setMiddleName(customer.getMiddleName());
	        policyDTO.setDateOfBirth(customer.getDateOfBirth());
	        policyDTO.setEmail(customer.getEmail());

	        // Map all associated policies to PolicyList
	        List<PolicyList> policyList = policies.stream().map(policy -> {
	            PolicyList pol = new PolicyList();
	            pol.setFcuFlag(policy.getFcuFlag());
	            pol.setPolicyNumber(policy.getPolicyNumber());
	            pol.setPolCompanyName(policy.getPolCompanyName());
	            pol.setPolicyName(policy.getPolicyName());
	            pol.setProductCode(policy.getProductCode());
	            pol.setCreatedBy(policy.getCreatedBy());
	            pol.setCreatedDate(String.valueOf(policy.getCreatedDate()));
	            pol.setWorkItemRefNo(policy.getWorkItemRefNo());
	            return pol;
	        }).collect(Collectors.toList());

	        policyDTO.setPolicyList(policyList);
	        return policyDTO;
	    })
	    .filter(Objects::nonNull) // in case of missing customers
	    .collect(Collectors.toList());

	    return response;
	}


	public List<String> getDoaminData(String value, String userId) {
		List<String> productCode = new ArrayList<>();
		if (value.equalsIgnoreCase("PRODUCT")) {
			productCode = Arrays.asList("POL_XC_01", "POL_XC_01", "POL_XC_01", "POL_XC_02", "POL_XC_03", "POL_XC_04",
					"POL_XC_05", "POL_XC_06", "POL_XC_07", "POL_XC_08", "POL_XC_08", "POL_XC_10", "POL_XC_11",
					"POL_XC_12", "POL_XC_13", "POL_XC_14", "POL_BANCS_01", "POL_BANCS_03");

		} else if (value.equalsIgnoreCase("POLICY")) {
			productCode = Arrays.asList("HealthGuard_Plus_2025", "AutoShield_Gold_Plan", "HomeProtect_Essentials",
					"LifeSecure_Term_Premium", "TravelCare_Global_Pro", "BusinessSafe_Advantage",
					"CyberProtect_Ultimate", "PetCare_Vitality_2024", "RentersGuard_Smart", "Umbrella_Excess_Liability",
					"MarineCargo_Standard", "FarmYield_Pro_2025", "EventSecure_Flexi", "GadgetCover_AllRisk",
					"Jewellery_Valuables_Elite", "PersonalAccident_Secure", "ChildEducation_Future",
					"Retirement_Income_Plus", "CreditShield_Max", "Disability_Income_Aid",
					"PropertyAll_Risk_Commercial", "Professional_Indemnity_Pro", "Directors_Officers_Secure",
					"ProductLiability_Plus", "Workforce_Compensation_Gold", "MachineryBreakdown_Pro",
					"Contractors_AllRisk_Build", "ErectionAll_Risk_Install", "Fidelity_Guarantee_Sure",
					"Bonds_Guarantee_Trade", "Motorcycle_Rider_Safe", "Caravan_Leisure_Tour", "Boat_Yacht_Marine",
					"ArtCollection_Safeguard", "Antique_Heirloom_Guard", "SportsInjury_ProActive",
					"StudentTravel_Abroad", "WeddingPlan_Joy", "FuneralExpense_Peace", "CriticalIllness_Benefit",
					"LongTermCare_Support", "IncomeProtection_Shield", "MortgageProtection_Secure",
					"KeyMan_Business_Vital", "GroupHealth_Corporate", "VoluntaryBenefits_Choice", "ExportCredit_Risk",
					"PoliticalRisk_Stability", "Environmental_Liability_Eco", "Aviation_Hull_Liab");
		}
		return productCode;
	}

}
