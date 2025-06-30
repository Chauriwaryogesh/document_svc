package com.SecureAccessPortal.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.SecureAccessPortal.Entity.PaymentSequence;
import com.SecureAccessPortal.Entity.Payments;
import com.SecureAccessPortal.Entity.Policy;
import com.SecureAccessPortal.Entity.Policy_Info;
import com.SecureAccessPortal.Entity.Workitem;
import com.SecureAccessPortal.Modal.BankAccountDTO;
import com.SecureAccessPortal.Modal.GroupedPolicyDTO;
import com.SecureAccessPortal.Modal.PolicyDTO;
import com.SecureAccessPortal.Modal.PolicyInfoDTO;
import com.SecureAccessPortal.Modal.PolicyList;
import com.SecureAccessPortal.Modal.PolicyRequest;
import com.SecureAccessPortal.Modal.ResponseDTO;
import com.SecureAccessPortal.Repo.BankAccountRepo;
import com.SecureAccessPortal.Repo.CustomerRepo;
import com.SecureAccessPortal.Repo.IPolicyRepo;
import com.SecureAccessPortal.Repo.PaymentSequenceRepo;
import com.SecureAccessPortal.Repo.PaymentsRepo;
import com.SecureAccessPortal.Repo.PolicyInfoRepo;
import com.SecureAccessPortal.Repo.WorkItemRepo;
import com.SecureAccessPortal.Transformer.PolicyMapper;
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
	private CustomerRepo customerRepository;

	@Autowired
	private BankAccountRepo bankAccountRepository;

	@Autowired
	private WorkItemRepo workItemRepo;

	@Autowired
	private PolicyInfoRepo policyInfoRepo;
	
	@Autowired
	private PaymentsRepo paymentsRepository;
	
	@Autowired
	private PaymentSequenceRepo paymentSequenceRepository;
	
	@Autowired
	private PolicyMapper policyMapper;

	@Autowired
	private DateUtil dateUtil;

	 @Transactional
	    public ResponseDTO createPolicy(PolicyRequest policyDTO, String userCode) {
	        ResponseDTO response = new ResponseDTO();
	        Customer customer = null;
	        try {
	            if (policyDTO.getInstallmentCount() <= 0 || policyDTO.getMonthlyInstallment() <= 0) {
	                throw new IllegalArgumentException("Invalid installment count or amount");
	            }
	            Policy policy = new Policy();
	            policy.setCreatedBy(userCode);
	            policy.setCreatedTime(LocalDateTime.now());
	            if (policyDTO.getCustomerNo() != null) {
	                customer = customerRepository.findByCustomerNoNew(policyDTO.getCustomerNo(),"N");
	                if (customer == null) {
	                    throw new IllegalArgumentException("Customer not found");
	                }
	                policy.setCustomer(customer);
	            }
	            policy.setDeletedFlag("N");
	            policy.setPolCompanyName(policyDTO.getPolCompanyName());
	            policy.setPolicyName(policyDTO.getPolicyName());
	            policy.setProductCode(policyDTO.getProductCode());
	           // policy.setUpdatedBy(userCode);
	            policy.setUserCode(policyDTO.getUserCode());
	            policy.setFcuFlag(policyDTO.getFcuFlag());
	            String newPolicyNumber = generatePolicyNumber();
	            policy.setPolicyNumber(newPolicyNumber);
	            policy.setBeneficiaryContactNumber(policyDTO.getNomineeContactNumber());
	            policy.setBeneficiaryIdentityNumber(policyDTO.getBeneficiaryAadharNumber());
	            policy.setBeneficiaryName(policyDTO.getBeneficiaryName());
	            policy.setBeneficiaryRelationship(policyDTO.getBeneficiaryRelationship());
	            policy.setComplianceFlag(CommonConstant.NO);
	            policy.setCoverageAmount(BigDecimal.valueOf(policyDTO.getTotalClaimableAmount()));
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

	            // Save policy
	            policy = policyRepository.save(policy);

	            // Create payment entries
	            createPaymentEntries(
	                policy,
	                customer != null ? customer.getCustomerNo() : policyDTO.getCustomerNo(),
	                policyDTO.getProductCode(),
	                policyDTO.getPolicyName(),
	                BigDecimal.valueOf(policyDTO.getMonthlyInstallment()),
	                policyDTO.getInstallmentCount(),
	                dateUtil.stringToLocalDateConvert(policyDTO.getStartDate()),
	                policyDTO.getTerm(),
	                userCode,
	                customer
	            );

	            // Create work item
	            String workType = CommonConstant.ADD_POL;
	            String workItemName = CommonConstant.POLICY_CREATED;
	            String comment = "Policy is created " + policy.getPolicyNumber() + " for the customer";
	            Workitem mapRequetforWorkItem = workItemService.mapRequetforWorkItem(
	                userCode, policy, customer, workType, workItemName, comment, null, null,null
	            );

	            response.setPolicyNo(newPolicyNumber);
	            response.setStatus(CommonConstant.SUCCESS);
	        } catch (Exception e) {
	            e.printStackTrace();
	            response.setStatus(CommonConstant.FAILURE);
	          //  response.(e.getMessage());
	        }
	        return response;
	    }
	 
	 private void createPaymentEntries(
		        Policy policy,
		        String customerNo,
		        String productCode,
		        String policyName,
		        BigDecimal installmentAmount,
		        int installmentCount,
		        LocalDate policyStartDate,
		        String term,
		        String userCode,
		        Customer customer
		    ) {
		        List<Payments> paymentsList = new ArrayList<>();
		        LocalDate baseDueDate = policyStartDate.withDayOfMonth(1).plusMonths(1); // Start from 1st of next month

		        for (int i = 0; i < installmentCount; i++) {
		            Payments payment = new Payments();
		            payment.setPaymentId(generatePaymentId());
		            payment.setPolicy(policy);
		            payment.setInstallmentCount(installmentCount);
		            payment.setCustomer(customer);
		            payment.setProductCode(productCode);
		            payment.setPolicyName(policyName);
		            payment.setInstallmentAmount(installmentAmount);
		            payment.setDueDate(calculateDueDate(baseDueDate, term, i));
		            payment.setStatus("Unpaid");
		            payment.setTransactionId(null);
		            payment.setEmailStatus("NotSent");
		            payment.setCreatedBy(userCode);
		            payment.setUpdatedBy(userCode);
		            payment.setCreatedTime(LocalDateTime.now());
		            payment.setUpdatedTime(LocalDateTime.now());
		            paymentsList.add(payment);
		        }

		        List<Payments> paymentList = paymentsRepository.saveAll(paymentsList);
		     // Create work item
	            String workType = CommonConstant.PAYMENT_CREATED;
	            String workItemName = CommonConstant.INSTALLEMT_CREATED;
	            String comment = "Policy Amount "+installmentAmount +"and installemt are "+ installmentCount +"created for customer "+customerNo + customer.getName() +"and Policy No is"+policy.getPolicyNumber();
	            workItemService.mapRequetforWorkItem(
	                userCode, policy, customer, workType, workItemName, comment, null, null,paymentList.get(0)
	            );
		    }

		    private LocalDate calculateDueDate(LocalDate baseDueDate, String term, int index) {
		        switch (term.toLowerCase()) {
		            case "monthly":
		                return baseDueDate.plusMonths(index);
		            case "quarterly":
		                return baseDueDate.plusMonths(index * 3);
		            case "yearly":
		                return baseDueDate.plusYears(index);
		            case "daily":
		                return baseDueDate.plusDays(index);
		            default:
		                return baseDueDate.plusMonths(index);
		        }
		    }
		    
		    @Transactional
		    private synchronized String generatePaymentId() {
		        LocalDateTime now = LocalDateTime.now();
		        String yearMonth = now.format(DateTimeFormatter.ofPattern("yyyyMM"));
		        PaymentSequence sequence = paymentSequenceRepository.findByYearMonth(yearMonth)
		            .orElseGet(() -> {
		                PaymentSequence newSequence = new PaymentSequence();
		                newSequence.setYearMonth(yearMonth);
		                newSequence.setSequenceNumber(0L);
		                return newSequence;
		            });

		        Long nextSequence = sequence.getSequenceNumber() + 1;
		        sequence.setSequenceNumber(nextSequence);
		        paymentSequenceRepository.save(sequence);

		        return String.format("%s%03d", yearMonth, nextSequence);
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
			Policy policy = policyRepository.findByPolicyNum(policyNo,"N");
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
					? policyRepository.findByCustomerNo(custNo,"N")
					: List.of(policy);
			if (policies.isEmpty()) {
				resp.setErrorMessage("No policies found for customer number: " + custNo);
			} else {
				response = mapPolicyListDetails(policies, userCode);
				resp.setData(response);
			}
		} else if (customerNo != null && !customerNo.isEmpty()) {
			Optional<Customer> customerDtls = customerRepository.findByCustomerNo(customerNo);
			if (!customerDtls.isPresent()) {
				resp.setErrorMessage("No customer found for customer number: " + customerNo);
			} else {
				Customer customer = customerDtls.get();
				List<Policy> policies = policyRepository.findByCustomerNo(customerNo,"N");
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
					policyDTO.setDateOfBirth(
							customer.getDateOfBirth() != null ? customer.getDateOfBirth().toString() : "");
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
			List<Policy> policies = policyRepository.findByCustomerNo(custNo,"N");
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
		return policies.stream().collect(Collectors.groupingBy(policy -> policy.getCustomer().getCustomerNo()))
				.entrySet().stream().map(entry -> {
					String customerNo = entry.getKey();
					List<Policy> customerPolicies = entry.getValue();
					Optional<Customer> customerDtls = customerRepository.findByCustomerNo(customerNo);
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
					policyDTO.setDateOfBirth(
							customer.getDateOfBirth() != null ? customer.getDateOfBirth().toString() : "");
					policyDTO.setEmail(customer.getEmail() != null ? customer.getEmail() : "");
					policyDTO.setPolicyList(
							customerPolicies.stream().map(this::mapPolicyToPolicyList).collect(Collectors.toList()));
					return policyDTO;
				}).filter(Objects::nonNull).collect(Collectors.toList());
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
		pol.setCreatedDate(String.valueOf(policy.getCreatedTime()));
		pol.setWorkItemRefNo(policy.getWorkitems() != null
				? policy.getWorkitems().stream().map(Workitem::getWorkItemRefNumber).collect(Collectors.toList())
				: new ArrayList<>());
		pol.setUpdatedBy(policy.getUpdatedBy() != null ? policy.getUpdatedBy() : "");
		pol.setUserCode(policy.getUserCode() != null ? policy.getUserCode() : "");
		pol.setDeletedFlag(policy.getDeletedFlag() != null ? policy.getDeletedFlag() : "");
		pol.setPolicyType(policy.getPolicyType() != null ? policy.getPolicyType() : "");
		pol.setPolicyPremium(policy.getPolicyPremium() != null ? policy.getPolicyPremium() : BigDecimal.ZERO);
		pol.setPremium(policy.getPolicyPremium() != null ? policy.getPolicyPremium() : BigDecimal.ZERO); // Added for
																											// frontend
		pol.setPolicyStatus(policy.getPolicyStatus() != null ? policy.getPolicyStatus() : "");
		pol.setCoverageAmount(policy.getCoverageAmount() != null ? policy.getCoverageAmount() : BigDecimal.ZERO);
		pol.setCustomerNo(policy.getCustomer() != null ? policy.getCustomer().getCustomerNo() : "");
		pol.setBeneficiaryName(policy.getBeneficiaryName() != null ? policy.getBeneficiaryName() : "");
		pol.setBeneficiaryRelationship(
				policy.getBeneficiaryRelationship() != null ? policy.getBeneficiaryRelationship() : "");
		pol.setComplianceFlag(policy.getComplianceFlag() != null ? policy.getComplianceFlag() : "");
		pol.setPaymentFrequency(policy.getPaymentFrequency() != null ? policy.getPaymentFrequency() : "");
		pol.setSmokerStatus(policy.getSmokerStatus() != null ? policy.getSmokerStatus() : "");
		pol.setPolicyAmount(
				String.valueOf(policy.getPolicyPremium() != null ? policy.getPolicyPremium() : BigDecimal.ZERO));
		pol.setInstallmentCount(String.valueOf(policy.getPolicyfrequency() != null ? policy.getPolicyfrequency() : ""));
		pol.setFrequency(policy.getPolicyfrequency() != null ? policy.getPolicyfrequency() : "");
		pol.setPolicyTerm(policy.getPolicyTerm() != null ? policy.getPolicyTerm() : "");
		pol.setTerm(policy.getPolicyTerm() != null ? policy.getPolicyTerm() : "");
		pol.setTotalAmount(policy.getTotalAmount());
		pol.setMonthlyInstallment(policy.getMonthlyInstallment());
		pol.setTotalClaimableAmount(policy.getTotalClaimableAmount());
		pol.setBeneficiaryAadharNumber(
				policy.getBeneficiaryIdentityNumber() != null ? policy.getBeneficiaryIdentityNumber() : "");
		pol.setNomineeContactNumber(
				policy.getBeneficiaryContactNumber() != null ? policy.getBeneficiaryContactNumber() : "");
		pol.setStatus(policy.getPolicyStatus() != null ? policy.getPolicyStatus() : "");
		pol.setType(policy.getPolicyType() != null ? policy.getPolicyType() : "");
		pol.setPremiumDueDate(String.valueOf(policy.getPremiumDueDate()));
		pol.setRenewalDate(String.valueOf(policy.getRenewalDate()));
		pol.setStartDate(String.valueOf(policy.getPolicyStartDate()));
		pol.setEndDate(String.valueOf(policy.getPolicyEndDate()));
		pol.setPolicyDate(String.valueOf(policy.getPolicyStartDate()));
		pol.setDueDate(String.valueOf(policy.getPremiumDueDate()));

		List<BankAccount> bankAccounts = bankAccountRepository.findByPolicyNumber(policy.getPolicyNumber());
		pol.setBankAccounts(
				bankAccounts != null ? bankAccounts.stream().map(this::mapToBankAccountDTO).collect(Collectors.toList())
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

	public List<GroupedPolicyDTO> getDomainData(String policyNo, String userCode) {
		List<Policy_Info> policies;

		if (policyNo != null && !policyNo.trim().isEmpty()) {
			policies = policyInfoRepo.findByProductCode(policyNo.trim());
		} else {
			policies = policyInfoRepo.findAllByOrderByProductCode();
		}

		// Grouping by policyName -> then by (productCode, policyType)
		Map<String, Map<String, Map<String, List<PolicyInfoDTO>>>> grouped = policies.stream().map(this::mapToDTO)
				.collect(Collectors.groupingBy(PolicyInfoDTO::getPolicyName, Collectors.groupingBy(
						PolicyInfoDTO::getProductCode, Collectors.groupingBy(PolicyInfoDTO::getPolicyType))));

		// Flattening into GroupedPolicyDTO list
		return grouped.entrySet().stream().flatMap(policyNameEntry -> policyNameEntry.getValue().entrySet().stream()
				.flatMap(productCodeEntry -> productCodeEntry.getValue().entrySet().stream()
						.map(policyTypeEntry -> new GroupedPolicyDTO(policyNameEntry.getKey(),
								productCodeEntry.getKey(), policyTypeEntry.getKey(), policyTypeEntry.getValue()))))
				.collect(Collectors.toList());
	}
	

	private PolicyInfoDTO mapToDTO(Policy_Info policy) {
		PolicyInfoDTO dto = new PolicyInfoDTO();
		dto.setPolicyId(String.valueOf(policy.getPolicy_id()));
		dto.setPolicyName(policy.getPolicy_name());
		dto.setProductCode(policy.getProduct_code());
		dto.setPolicyCompanyName(policy.getPolicy_company_name());
		dto.setSupportedTerms(policy.getSupported_terms());
		dto.setFrequency(policy.getFrequency());
		dto.setMinTotalAmount(policy.getMin_total_amount());
		dto.setMaxTotalAmount(policy.getMax_total_amount());
		dto.setCoverageAmount(policy.getCoverage_amount());
		dto.setDefaultDurationMonths(policy.getDefault_duration_months());
		dto.setPolicyStartDate(policy.getPolicy_start_date());
		dto.setPolicyEndDate(policy.getPolicy_end_date());
		dto.setPolicyRenewalDate(policy.getPolicy_renewal_date());
		dto.setPolicyExpiryDate(policy.getPolicy_expiry_date());
		dto.setPolicyType(policy.getPolicy_type());
		dto.setStatus(policy.getStatus());
		dto.setCreatedAt(policy.getCreated_at() != null ? policy.getCreated_at().toLocalDateTime() : null);
		dto.setUpdatedAt(policy.getUpdated_at() != null ? policy.getUpdated_at().toLocalDateTime() : null);
		return dto;
	}

	public ResponseEntity<String> validateEmailUserCode(String email, String userCode) {
		ResponseEntity<String> response = new ResponseEntity<String>();
		String message = "";
		if (email != null) {
			Optional<Customer> byEmail = customerRepository.findByEmail(email, "N");
			if (byEmail.isPresent()) {
				message = "Email is exist in System please enter new email";
				response.setErrorMessage(message);
			} else {
				message = "Great";
				response.setStatus(message);
			}
		} else if (userCode != null) {
			Customer customer = customerRepository.findByUserCodeAndDeletedFlagN(userCode, "N");
			if (customer != null) {
				message = "UserCode is exist in System please enter new email";
				response.setErrorMessage(message);
			} else {
				message = "Great";
				response.setStatus(message);
			}
		}
		return response;
	}

	public ResponseEntity<List<PolicyDTO>> fetchAllPolicies(String email, String userCode) {
		ResponseEntity<List<PolicyDTO>> response = new ResponseEntity<>();
		List<Customer> customer= customerRepository.findAll();
		List<Policy> policies= policyRepository.findAllDeletedflagN("N");
		
		response= policyMapper.mapAllPolicies(customer,policies);
		return response;
	}

	public ResponseDTO updatePolicy(PolicyRequest policyDTO, String userCode) {
		ResponseDTO response = new ResponseDTO();
		Customer customer = null;
		Policy policy =policyRepository.findByPolicyNum(policyDTO.getPolicyNumber(),"N");
		if (policyDTO.getCustomerNo() != null) {
			customer = customerRepository.findByCustomerNoNew(policyDTO.getCustomerNo(),"N");
			if (customer == null) {
				throw new IllegalArgumentException("Customer not found");
			}
			policy.setCustomer(customer);
		}
		try {
			policy.setDeletedFlag("N");
			//policy.setPolCompanyName(policyDTO.getPolCompanyName());
			policy.setPolicyName(policyDTO.getPolicyName());
			policy.setProductCode(policyDTO.getProductCode());
			policy.setUpdatedBy(userCode);
			policy.setUpdatedTime(LocalDateTime.now());
			policy.setUserCode(userCode);
			policy.setFcuFlag(policyDTO.getFcuFlag());
			//String newPolicyNumber = generatePolicyNumber();
			//policy.setPolicyNumber(newPolicyNumber);
			policy.setBeneficiaryContactNumber(policyDTO.getNomineeContactNumber());
			policy.setBeneficiaryIdentityNumber(policyDTO.getBeneficiaryAadharNumber());
			policy.setBeneficiaryName(policyDTO.getBeneficiaryName());
			policy.setBeneficiaryRelationship(policyDTO.getBeneficiaryRelationship());
			policy.setComplianceFlag(CommonConstant.NO);
			policy.setCoverageAmount(BigDecimal.valueOf(policyDTO.getTotalClaimableAmount()));
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

			// Save policy
			policy = policyRepository.save(policy);

			// Create work item
			String workType = CommonConstant.UPDATE_POL_DETAIL;
			String workItemName = CommonConstant.POLICY_UPDATE_DETAILS;
			String comment = "Policy is created " + policy.getPolicyNumber() + " for the customer";
			Workitem mapRequetforWorkItem = workItemService.mapRequetforWorkItem(userCode, policy, customer, workType,
					workItemName, comment, null, null, null);

			response.setPolicyNo(policy.getPolicyNumber());
			response.setStatus(CommonConstant.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(CommonConstant.FAILURE);
			// response.(e.getMessage());
		}
		return response;
	}

	public ResponseDTO updateDeletePolicy(String policyNumber, String reson, String userCode) {
		ResponseDTO response = new ResponseDTO();
		Customer customer = null;
		Policy policy = policyRepository.findByPolicyNum(policyNumber, "N");
		if (policy != null) {
			if (policy.getCustomer() != null) {
				customer = customerRepository.findByCustomerNoNew(policy.getCustomer().getCustomerNo(),"N");
				if (customer == null) {
					throw new IllegalArgumentException("Customer not found");
				}
				policy.setCustomer(customer);
			}

			policy.setDeletedFlag("Y");
			policy.setPolicyStatus("LAPSED");
			policy.setUpdatedBy(userCode);
			policy.setUpdatedTime(LocalDateTime.now());
			policy.setReson(reson);
			// Save policy
			policy = policyRepository.save(policy);

			response.setPolicyNo(policy.getPolicyNumber());
			response.setStatus(CommonConstant.SUCCESS);
			// Create work item
			String workType = CommonConstant.POLICY_DELETED;
			String workItemName = CommonConstant.POLICY_DELETED;
			String comment = "Policy Deleted  " + policy.getPolicyNumber() + " for the customer";
			Workitem mapRequetforWorkItem = workItemService.mapRequetforWorkItem(userCode, policy, customer, workType,
					workItemName, comment, null, null, null);
		} else {
			response.setStatus("Policy Not found");
		}
		return response;
	}

}
