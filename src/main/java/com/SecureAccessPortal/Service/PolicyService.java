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
			if (allpol != null && allpol.equalsIgnoreCase("Y")) {
				String custNo = policy.getCustomer().getCustomerNo();
				if (custNo == null) {
					resp.setErrorMessage("No customer associated with policy number: " + policyNo);
					return resp;
				}
				List<Policy> customerPolicies = policyRepository.findByCustomerNo(custNo);
				if (customerPolicies == null || customerPolicies.isEmpty()) {
					resp.setErrorMessage("No policies found for customer number: " + custNo);
				} else {
					response = mapPolicyListDetails(customerPolicies, userCode);
				}
				resp.setData(response);
			} else {
				Optional<Customer> customerDtls = customerRepo.findByCustomerNo(policy.getCustomer().getCustomerNo());
				if (customerDtls.isPresent()) {
					Customer customer = customerDtls.get();
					PolicyDTO policyDTO = new PolicyDTO();
					policyDTO.setCustomerNo(customer.getCustomerNo() != null ? customer.getCustomerNo() : "");
					policyDTO.setuserCode(userCode != null ? userCode : "");
					policyDTO.setPhoneNumber(customer.getPhoneNumber() != null ? customer.getPhoneNumber() : "");
					policyDTO.setCustomerName(customer.getName() != null ? customer.getName() : "");
					policyDTO.setSmokerStatus(customer.getSmokerStatus() != null ? customer.getSmokerStatus() : "");
					policyDTO.setSurname(customer.getSurname() != null ? customer.getSurname() : "");
					policyDTO.setGender(customer.getGender() != null ? customer.getGender() : "");
					policyDTO.setMiddleName(customer.getMiddleName() != null ? customer.getMiddleName() : "");
					policyDTO.setDateOfBirth(
							customer.getDateOfBirth() != null ? customer.getDateOfBirth().toString() : "");
					policyDTO.setEmail(customer.getEmail() != null ? customer.getEmail() : "");

					List<PolicyList> policyList = new ArrayList<>();
					PolicyList pol = new PolicyList();
					pol.setFcuFlag(policy.getFcuFlag() != null ? policy.getFcuFlag() : "");
					pol.setPolicyNumber(policy.getPolicyNumber() != null ? policy.getPolicyNumber() : "");
					pol.setPolCompanyName(policy.getPolCompanyName() != null ? policy.getPolCompanyName() : "");
					pol.setPolicyName(policy.getPolicyName() != null ? policy.getPolicyName() : "");
					pol.setProductCode(policy.getProductCode() != null ? policy.getProductCode() : "");
					pol.setCreatedBy(policy.getPolicyName() != null ? policy.getPolicyName() : "");
					pol.setCreatedDate(policy.getCreatedDate() != null
							? policy.getCreatedDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
							: "");
					pol.setWorkItemRefNo(policy.getWorkitems().get(0).getWorkItemRefNumber());

					List<BankAccount> bankAccounts = bankAccountRepository.findByPolicyNumber(policy.getPolicyNumber());
					List<BankAccountDTO> bankAccountDTOs = bankAccounts != null
							? bankAccounts.stream().map(this::mapToBankAccountDTO).collect(Collectors.toList())
							: new ArrayList<>();
					pol.setBankAccounts(bankAccountDTOs);

					policyList.add(pol);
					policyDTO.setPolicyList(policyList);
					policyDTO.setAssociatedPolicyCount("1");

					response.add(policyDTO);
					resp.setData(response);
				} else {
					resp.setErrorMessage("No customer found for policy number: " + policyNo);
				}
			}
		} else if (customerNo != null && !customerNo.isEmpty()) {
			Optional<Customer> customerDtls = customerRepo.findByCustomerNo(customerNo);
			if (!customerDtls.isPresent()) {
				resp.setErrorMessage("No customer found for customer number: " + customerNo);
			} else {
				Customer customer = customerDtls.get();
				List<Policy> policyList = policyRepository.findByCustomerNo(customerNo);
				if (policyList != null && !policyList.isEmpty()) {
					response = mapPolicyListDetails(policyList, userCode);
				} else {
					PolicyDTO policyDTO = new PolicyDTO();
					policyDTO.setAssociatedPolicyCount("0");
					policyDTO.setCustomerNo(customerNo);
					policyDTO.setuserCode(userCode != null ? userCode : "");
					policyDTO.setPhoneNumber(customer.getPhoneNumber() != null ? customer.getPhoneNumber() : "");
					policyDTO.setCustomerName(customer.getName() != null ? customer.getName() : "");
					policyDTO.setSmokerStatus(customer.getSmokerStatus() != null ? customer.getSmokerStatus() : "");
					policyDTO.setSurname(customer.getSurname() != null ? customer.getSurname() : "");
					policyDTO.setGender(customer.getGender() != null ? customer.getGender() : "");
					policyDTO.setMiddleName(customer.getMiddleName() != null ? customer.getMiddleName() : "");
					policyDTO.setDateOfBirth(
							customer.getDateOfBirth() != null ? customer.getDateOfBirth().toString() : "");
					policyDTO.setEmail(customer.getEmail() != null ? customer.getEmail() : "");
					policyDTO.setPolicyList(new ArrayList<>());
					response.add(policyDTO);
				}
				resp.setData(response);
			}
		} else if (workItemRefNo != null && !workItemRefNo.isEmpty()) {
			if (workItemRefNo == null || workItemRefNo.isEmpty()) {
				resp.setErrorMessage("Work item reference number cannot be null or empty");
				return resp;
			}
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
			Customer customer = policy.getCustomer();
			if (customer == null || customer.getCustomerNo() == null) {
				resp.setErrorMessage("No customer associated with policy: " + policy.getPolicyNumber());
				return resp;
			}
			// Fetch all policies for the customer
			List<Policy> policies = policyRepository.findByCustomerNo(customer.getCustomerNo());
			if (policies.isEmpty()) {
				resp.setErrorMessage("No policies found for customer number: " + customer.getCustomerNo());
			}
			if (policies != null && !policies.isEmpty()) {
				String custNo = policies.get(0).getCustomer().getCustomerNo();
				if (custNo == null) {
					resp.setErrorMessage("No customer associated with work item reference number: " + workItemRefNo);
					return resp;
				}
				List<Policy> customerPolicies = policyRepository.findByCustomerNo(custNo);
				if (customerPolicies == null || customerPolicies.isEmpty()) {
					resp.setErrorMessage("No policies found for customer number: " + custNo);
				} else {
					response = mapPolicyListDetails(customerPolicies, userCode);
				}
				resp.setData(response);
			} else {
				resp.setErrorMessage("No policies found for work item reference number: " + workItemRefNo);
			}

		} else {
			resp.setErrorMessage("At least one parameter (policyNo, customerNo, workItemRefNo) is required");
		}
		return resp;
	}

	private List<PolicyDTO> mapPolicyListDetails(List<Policy> policies, String userCode) {
		return policies.stream().collect(Collectors.groupingBy(policy -> policy.getCustomer().getCustomerNo())).entrySet().stream()
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
					policyDTO.setuserCode(userCode != null ? userCode : "");
					policyDTO.setPhoneNumber(customer.getPhoneNumber() != null ? customer.getPhoneNumber() : "");
					policyDTO.setCustomerName(customer.getName() != null ? customer.getName() : "");
					policyDTO.setSmokerStatus(customer.getSmokerStatus() != null ? customer.getSmokerStatus() : "");
					policyDTO.setSurname(customer.getSurname() != null ? customer.getSurname() : "");
					policyDTO.setGender(customer.getGender() != null ? customer.getGender() : "");
					policyDTO.setMiddleName(customer.getMiddleName() != null ? customer.getMiddleName() : "");
					policyDTO.setDateOfBirth(
							customer.getDateOfBirth() != null ? customer.getDateOfBirth().toString() : "");
					policyDTO.setEmail(customer.getEmail() != null ? customer.getEmail() : "");

					List<PolicyList> policyList = customerPolicies.stream().map(policy -> {
						PolicyList pol = new PolicyList();
						pol.setFcuFlag(policy.getFcuFlag() != null ? policy.getFcuFlag() : "");
						pol.setPolicyNumber(policy.getPolicyNumber() != null ? policy.getPolicyNumber() : "");
						pol.setPolCompanyName(policy.getPolCompanyName() != null ? policy.getPolCompanyName() : "");
						pol.setPolicyName(policy.getPolicyName() != null ? policy.getPolicyName() : "");
						pol.setProductCode(policy.getProductCode() != null ? policy.getProductCode() : "");
						pol.setCreatedBy(policy.getCreatedBy() != null ? policy.getCreatedBy() : "");
						pol.setCreatedDate(policy.getCreatedDate() != null
								? policy.getCreatedDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
								: "");
						pol.setWorkItemRefNo(policy.getWorkitems() != null ? policy.getWorkitems().get(0).getWorkItemRefNumber() : "");

						List<BankAccount> bankAccounts = bankAccountRepository
								.findByPolicyNumber(policy.getPolicyNumber());
						List<BankAccountDTO> bankAccountDTOs = bankAccounts != null
								? bankAccounts.stream().map(this::mapToBankAccountDTO).collect(Collectors.toList())
								: new ArrayList<>();
						pol.setBankAccounts(bankAccountDTOs);

						return pol;
					}).collect(Collectors.toList());

					policyDTO.setPolicyList(policyList);
					return policyDTO;
				}).filter(Objects::nonNull).collect(Collectors.toList());
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
