package com.SecureAccessPortal.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Entity.BankAccount;
import com.SecureAccessPortal.Entity.Customer;
import com.SecureAccessPortal.Entity.Policy;
import com.SecureAccessPortal.Entity.Workitem;
import com.SecureAccessPortal.Modal.BankAccountDTO;
import com.SecureAccessPortal.Modal.PolicyDTO;
import com.SecureAccessPortal.Modal.PolicyList;
import com.SecureAccessPortal.Modal.PolicyRequest;
import com.SecureAccessPortal.Modal.ResponseDTO;
import com.SecureAccessPortal.Repo.BankAccountRepo;
import com.SecureAccessPortal.Repo.CustomerRepo;
import com.SecureAccessPortal.Repo.IPolicyRepo;
import com.SecureAccessPortal.Repo.WorkItemRepo;
import com.SecureAccessPortal.util.DateUtil;

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

	@Autowired
	private BankAccountRepo bankAccountRepository;
	
	@Autowired
	private WorkItemRepo workItemRepo;
	
	@Autowired
	private DateUtil dateUtil;

	public ResponseDTO createPolicy(PolicyRequest policyDTO, String userCode) {
		ResponseDTO response = new ResponseDTO();
		Customer customer= null;
		try {
			Policy policy = new Policy();
			policy.setCreatedBy(userCode);
			policy.setCreatedDate(LocalDateTime.now());
			if(policyDTO.getCustomerNo() != null) {
				customer= customerRepo.findByCustomerNoNew(policyDTO.getCustomerNo());
				policy.setCustomer(customer);
			}
			policy.setDeletedFlag("N");
			policy.setPolCompanyName(policyDTO.getPolCompanyName());
			policy.setPolicyName(policyDTO.getPolicyName());
			policy.setProductCode(policyDTO.getProductCode());
			policy.setUpdatedBy(userCode);
			policy.setUserCode(userCode);
			policy.setFcuFlag(policyDTO.getFcuFlag());
			String newPolicyNumber = generatePolicyNumber();
			policy.setPolicyNumber(newPolicyNumber);
			
			
			policy.setBeneficiaryContactNumber(policyDTO.getNomineeContactNumber());
			policy.setBeneficiaryIdentityNumber(policyDTO.getBeneficiaryAadharNumber());
			policy.setBeneficiaryName(policyDTO.getBeneficiaryName());
			policy.setBeneficiaryRelationship(policyDTO.getBeneficiaryRelationship());
			policy.setComplianceFlag(CommonConstant.NO);
			policy.setCoverageAmount(BigDecimal.valueOf(policyDTO.getTotalAmount()));
			policy.setMonthlyInstallment(policyDTO.getMonthlyInstallment());
			policy.setPaymentFrequency(policyDTO.getTerm());
			
			policy.setPolicyEndDate(dateUtil.stringToLocalDateConvert(policyDTO.getEndDate()));
			policy.setPolicyfrequency(policyDTO.getTerm());
			policy.setPolicyPremium(BigDecimal.valueOf(policyDTO.getPremium()));
			policy.setPolicyStartDate(dateUtil.stringToLocalDateConvert(policyDTO.getStartDate()));
			policy.setPolicyStatus(policyDTO.getStatus());
			policy.setPolicyTerm(policyDTO.getFrequency());
			policy.setPolicyType(policyDTO.getType());
			policy.setPremiumDueDate(dateUtil.stringToLocalDateConvert(policyDTO.getDueDate()));
			policy.setRenewalDate(dateUtil.stringToLocalDateConvert(policyDTO.getRenewalDate()));
			policy.setSmokerStatus(policyDTO.getSmokerStatus());
			policy.setTotalAmount(policyDTO.getTotalAmount());
			policy.setTotalClaimableAmount(policyDTO.getTotalClaimableAmount());
			
			
			policyRepository.save(policy);
			// call workitem Service to generate WIrefNo.
			String workType = CommonConstant.ADD_POL;
			String workItemName = CommonConstant.POLICY_CREATED;
			String comment="Policy is created " + policy.getPolicyNumber() + "for the customer";
			workItemService.mapRequetforWorkItem(userCode, policy, customer, workType, workItemName,comment,null,null);

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
	        String workItemRefNo, String userCode) {
	    ResponseEntity<List<PolicyDTO>> resp = new ResponseEntity<>();
	    List<PolicyDTO> response = new ArrayList<>();

	    if (policyNo != null && !policyNo.isEmpty()) {
	        Policy policy = policyRepository.findByPolicyNum(policyNo);
	        if (policy == null) {
	            resp.setErrorMessage("No policy found for policy number: " + policyNo);
	            return resp;
	        }
	        String custNo = policy.getCustomer() != null ? policy.getCustomer().getCustomerNo() : null;
	        if (custNo == null) {
	            resp.setErrorMessage("No customer associated with policy number: " + policyNo);
	            return resp;
	        }
	        List<Policy> policies = (allpol != null && allpol.equalsIgnoreCase("Y"))
	                ? policyRepository.findByCustomerNo(custNo)
	                : List.of(policy);
	        if (policies.isEmpty()) {
	            resp.setErrorMessage("No policies found for customer number: " + custNo);
	        } else {
	            response = mapPolicyListDetails(policies, userCode);
	            resp.setData(response);
	        }
	    } else if (customerNo != null && !customerNo.isEmpty()) {
	        Optional<Customer> customerDtls = customerRepo.findByCustomerNo(customerNo);
	        if (!customerDtls.isPresent()) {
	            resp.setErrorMessage("No customer found for customer number: " + customerNo);
	        } else {
	            Customer customer = customerDtls.get();
	            List<Policy> policies = policyRepository.findByCustomerNo(customerNo);
	            if (policies.isEmpty()) {
	                PolicyDTO policyDTO = new PolicyDTO();
	                policyDTO.setAssociatedPolicyCount("0");
	                policyDTO.setCustomerNo(customerNo);
	                policyDTO.setUserCode(userCode != null ? userCode : "");
	                policyDTO.setPhoneNumber(customer.getPhoneNumber() != null ? customer.getPhoneNumber() : "");
	                policyDTO.setCustomerName(customer.getName() != null ? customer.getName() : "");
	                policyDTO.setSmokerStatus(customer.getSmokerStatus() != null ? customer.getSmokerStatus() : "");
	                policyDTO.setSurname(customer.getSurname() != null ? customer.getSurname() : "");
	                policyDTO.setGender(customer.getGender() != null ? customer.getGender() : "");
	                policyDTO.setMiddleName(customer.getMiddleName() != null ? customer.getMiddleName() : "");
	                policyDTO.setDateOfBirth(customer.getDateOfBirth() != null ? customer.getDateOfBirth().toString() : "");
	                policyDTO.setEmail(customer.getEmail() != null ? customer.getEmail() : "");
	                policyDTO.setPolicyList(new ArrayList<>());
	                response.add(policyDTO);
	                resp.setData(response);
	            } else {
	                response = mapPolicyListDetails(policies, userCode);
	                resp.setData(response);
	            }
	        }
	    } else if (workItemRefNo != null && !workItemRefNo.isEmpty()) {
	        Optional<Workitem> byWiRefNum = workItemRepo.findByWiRefNum(workItemRefNo);
	        if (!byWiRefNum.isPresent()) {
	            resp.setErrorMessage("No work item found for reference number: " + workItemRefNo);
	            return resp;
	        }
	        Workitem workitem = byWiRefNum.get();
	        Policy policy = workitem.getPolicy();
	        if (policy == null) {
	            resp.setErrorMessage("No policy associated with work item reference number: " + workItemRefNo);
	            return resp;
	        }
	        String custNo = policy.getCustomer() != null ? policy.getCustomer().getCustomerNo() : null;
	        if (custNo == null) {
	            resp.setErrorMessage("No customer associated with policy: " + policy.getPolicyNumber());
	            return resp;
	        }
	        List<Policy> policies = policyRepository.findByCustomerNo(custNo);
	        if (policies.isEmpty()) {
	            resp.setErrorMessage("No policies found for customer number: " + custNo);
	        } else {
	            response = mapPolicyListDetails(policies, userCode);
	            resp.setData(response);
	        }
	    } else {
	        resp.setErrorMessage("At least one parameter (policyNo, customerNo, workItemRefNo) is required");
	    }
	    return resp;
	}

	private List<PolicyDTO> mapPolicyListDetails(List<Policy> policies, String userCode) {
	    return policies.stream()
	            .collect(Collectors.groupingBy(policy -> policy.getCustomer().getCustomerNo()))
	            .entrySet().stream()
	            .map(entry -> {
	                String customerNo = entry.getKey();
	                List<Policy> customerPolicies = entry.getValue();
	                Optional<Customer> customerDtls = customerRepo.findByCustomerNo(customerNo);
	                if (customerDtls.isEmpty()) {
	                    return null;
	                }
	                Customer customer = customerDtls.get();
	                PolicyDTO policyDTO = new PolicyDTO();
	                policyDTO.setAssociatedPolicyCount(String.valueOf(customerPolicies.size()));
	                policyDTO.setCustomerNo(customerNo);
	                policyDTO.setUserCode(userCode != null ? userCode : "");
	                policyDTO.setPhoneNumber(customer.getPhoneNumber() != null ? customer.getPhoneNumber() : "");
	                policyDTO.setCustomerName(customer.getName() != null ? customer.getName() : "");
	                policyDTO.setSmokerStatus(customer.getSmokerStatus() != null ? customer.getSmokerStatus() : "");
	                policyDTO.setSurname(customer.getSurname() != null ? customer.getSurname() : "");
	                policyDTO.setGender(customer.getGender() != null ? customer.getGender() : "");
	                policyDTO.setMiddleName(customer.getMiddleName() != null ? customer.getMiddleName() : "");
	                policyDTO.setDateOfBirth(customer.getDateOfBirth() != null ? customer.getDateOfBirth().toString() : "");
	                policyDTO.setEmail(customer.getEmail() != null ? customer.getEmail() : "");
	                policyDTO.setPolicyList(customerPolicies.stream()
	                        .map(this::mapPolicyToPolicyList)
	                        .collect(Collectors.toList()));
	                return policyDTO;
	            })
	            .filter(Objects::nonNull)
	            .collect(Collectors.toList());
	}

	private PolicyList mapPolicyToPolicyList(Policy policy) {
	    PolicyList pol = new PolicyList();

	    pol.setId(policy.getId());
	    pol.setFcuFlag(policy.getFcuFlag() != null ? policy.getFcuFlag() : "");
	    pol.setPolicyNumber(policy.getPolicyNumber() != null ? policy.getPolicyNumber() : "");
	    pol.setPolCompanyName(policy.getPolCompanyName() != null ? policy.getPolCompanyName() : "");
	    pol.setPolicyName(policy.getPolicyName() != null ? policy.getPolicyName() : "");
	    pol.setProductCode(policy.getProductCode() != null ? policy.getProductCode() : "");
	    pol.setCreatedBy(policy.getCreatedBy() != null ? policy.getCreatedBy() : "");
	    pol.setCreatedDate(String.valueOf(policy.getCreatedDate()));
	    pol.setWorkItemRefNo(policy.getWorkitems() != null
	            ? policy.getWorkitems().stream().map(Workitem::getWorkItemRefNumber).collect(Collectors.toList())
	            : new ArrayList<>());
	    pol.setUpdatedBy(policy.getUpdatedBy() != null ? policy.getUpdatedBy() : "");
	    pol.setUserCode(policy.getUserCode() != null ? policy.getUserCode() : "");
	    pol.setDeletedFlag(policy.getDeletedFlag() != null ? policy.getDeletedFlag() : "");
	    pol.setPolicyType(policy.getPolicyType() != null ? policy.getPolicyType() : "");
	    pol.setPolicyPremium(policy.getPolicyPremium() != null ? policy.getPolicyPremium() : BigDecimal.ZERO);
	    pol.setPremium(policy.getPolicyPremium() != null ? policy.getPolicyPremium() : BigDecimal.ZERO); // Added for frontend
	    pol.setPolicyStatus(policy.getPolicyStatus() != null ? policy.getPolicyStatus() : "");
	    pol.setCoverageAmount(policy.getCoverageAmount() != null ? policy.getCoverageAmount() : BigDecimal.ZERO);
	    pol.setCustomerNo(policy.getCustomer() != null ? policy.getCustomer().getCustomerNo() : "");
	    pol.setBeneficiaryName(policy.getBeneficiaryName() != null ? policy.getBeneficiaryName() : "");
	    pol.setBeneficiaryRelationship(policy.getBeneficiaryRelationship() != null ? policy.getBeneficiaryRelationship() : "");
	    pol.setComplianceFlag(policy.getComplianceFlag() != null ? policy.getComplianceFlag() : "");
	    pol.setPaymentFrequency(policy.getPaymentFrequency() != null ? policy.getPaymentFrequency() : "");
	    pol.setSmokerStatus(policy.getSmokerStatus() != null ? policy.getSmokerStatus() : "");
	    pol.setPolicyAmount(String.valueOf(policy.getPolicyPremium() != null ? policy.getPolicyPremium() : BigDecimal.ZERO));
	    pol.setInstallmentCount(String.valueOf(policy.getPolicyfrequency() != null ? policy.getPolicyfrequency() : ""));
	    pol.setFrequency(policy.getPolicyfrequency() != null ? policy.getPolicyfrequency() : "");
	    pol.setPolicyTerm(policy.getPolicyTerm() != null ? policy.getPolicyTerm() : "");
	    pol.setTerm(policy.getPolicyTerm() != null ? policy.getPolicyTerm() : "");
	    pol.setTotalAmount(policy.getTotalAmount());
	    pol.setMonthlyInstallment(policy.getMonthlyInstallment());
	    pol.setTotalClaimableAmount(policy.getTotalClaimableAmount());
	    pol.setBeneficiaryAadharNumber(policy.getBeneficiaryIdentityNumber() != null ? policy.getBeneficiaryIdentityNumber() : "");
	    pol.setNomineeContactNumber(policy.getBeneficiaryContactNumber() != null ? policy.getBeneficiaryContactNumber() : "");
	    pol.setStatus(policy.getPolicyStatus() != null ? policy.getPolicyStatus() : "");
	    pol.setType(policy.getPolicyType() != null ? policy.getPolicyType() : "");
	    pol.setPremiumDueDate(String.valueOf(policy.getPremiumDueDate()));
	    pol.setRenewalDate(String.valueOf(policy.getRenewalDate()));
	    pol.setStartDate(String.valueOf(policy.getPolicyStartDate()));
	    pol.setEndDate(String.valueOf(policy.getPolicyEndDate()));
	    pol.setPolicyDate(String.valueOf(policy.getPolicyStartDate()));
	    pol.setDueDate(String.valueOf(policy.getPremiumDueDate()));

	    List<BankAccount> bankAccounts = bankAccountRepository.findByPolicyNumber(policy.getPolicyNumber());
	    pol.setBankAccounts(bankAccounts != null
	            ? bankAccounts.stream().map(this::mapToBankAccountDTO).collect(Collectors.toList())
	            : new ArrayList<>());

	    return pol;
	}

	private BankAccountDTO mapToBankAccountDTO(BankAccount bankAccount) {
		BankAccountDTO dto = new BankAccountDTO();
		if (bankAccount == null) {
			return dto;
		}
		dto.setId(bankAccount.getId());
		dto.setAccountNumber(bankAccount.getAccountNo() != null ? bankAccount.getAccountNo() : "");
		dto.setIfscCode(bankAccount.getIfscCode() != null ? bankAccount.getIfscCode() : "");
		dto.setBankName(bankAccount.getBankName() != null ? bankAccount.getBankName() : "");
		dto.setAccountType(bankAccount.getAccountType() != null ? bankAccount.getAccountType() : "");
		dto.setStatus(bankAccount.getStatus() != null ? bankAccount.getStatus() : "");
		if (bankAccount.getCustomer() != null) {
			dto.setCustomerNumber(
					bankAccount.getCustomer().getCustomerNo() != null ? bankAccount.getCustomer().getCustomerNo() : "");
			dto.setHolderName(bankAccount.getCustomer().getName() != null ? bankAccount.getCustomer().getName() : "");
		} else {
			dto.setCustomerNumber("");
			dto.setHolderName("");
		}
		if (bankAccount.getPolicy() != null) {
			dto.setPolicyNumber(
					bankAccount.getPolicy().getPolicyNumber() != null ? bankAccount.getPolicy().getPolicyNumber() : "");
			dto.setPolicyStatus(
					bankAccount.getPolicy().getPolicyStatus() != null ? bankAccount.getPolicy().getPolicyStatus() : "");
		} else {
			dto.setPolicyNumber("");
			dto.setPolicyStatus("");
		}
		dto.setLastVerificationDate(bankAccount.getLastVerificationDate() != null
				? bankAccount.getLastVerificationDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
				: "");
		dto.setCreatedBy(bankAccount.getCreatedBy() != null ? bankAccount.getCreatedBy() : "");
		dto.setCreatedDate(bankAccount.getCreatedDate() != null
				? bankAccount.getCreatedDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
				: "");
		dto.setNotes(bankAccount.getNotes() != null ? bankAccount.getNotes() : "");
		dto.setAccountHolderType(bankAccount.getAccountHolderType() != null ? bankAccount.getAccountHolderType() : "");
		dto.setBranchCode(bankAccount.getBranchCode() != null ? bankAccount.getBranchCode() : "");
		dto.setSwiftCode(bankAccount.getSwiftCode() != null ? bankAccount.getSwiftCode() : "");
		dto.setPaymentMethodStatus(
				bankAccount.getPaymentMethodStatus() != null ? bankAccount.getPaymentMethodStatus() : "");
		dto.setLastPaymentDate(bankAccount.getLastPaymentDate() != null
				? bankAccount.getLastPaymentDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
				: "");
		dto.setAmlStatus(bankAccount.getAmlStatus() != null ? bankAccount.getAmlStatus() : "");
		dto.setAccountBalance(bankAccount.getAccountBalance());
		dto.setLinkedPaymentMethod(
				bankAccount.getLinkedPaymentMethod() != null ? bankAccount.getLinkedPaymentMethod() : "");
		dto.setVerificationAttempts(bankAccount.getVerificationAttempts());
		return dto;
	}

	public List<String> getDoaminData(String value, String userCode) {
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
