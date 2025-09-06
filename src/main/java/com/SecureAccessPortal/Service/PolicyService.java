package com.SecureAccessPortal.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.CommonConstants.ErrorConstants;
import com.SecureAccessPortal.Entity.Bank;
import com.SecureAccessPortal.Entity.ClaimEntity;
import com.SecureAccessPortal.Entity.Customer;
import com.SecureAccessPortal.Entity.PaymentSequence;
import com.SecureAccessPortal.Entity.Payments;
import com.SecureAccessPortal.Entity.Policy;
import com.SecureAccessPortal.Entity.PolicyRequestEntity;
import com.SecureAccessPortal.Entity.Policy_Info;
import com.SecureAccessPortal.Entity.SurrenderEntity;
import com.SecureAccessPortal.Entity.Workitem;
import com.SecureAccessPortal.Modal.BankDetailsDTO;
import com.SecureAccessPortal.Modal.DashboardStats;
import com.SecureAccessPortal.Modal.GroupedPolicyDTO;
import com.SecureAccessPortal.Modal.PaymentList;
import com.SecureAccessPortal.Modal.PolicyDTO;
import com.SecureAccessPortal.Modal.PolicyInfoDTO;
import com.SecureAccessPortal.Modal.PolicyList;
import com.SecureAccessPortal.Modal.PolicyRequest;
import com.SecureAccessPortal.Modal.ResponseDTO;
import com.SecureAccessPortal.Modal.SurrenderClaimDTO;
import com.SecureAccessPortal.Repo.BankAccountRepo;
import com.SecureAccessPortal.Repo.ClaimRepo;
import com.SecureAccessPortal.Repo.CustomerRepo;
import com.SecureAccessPortal.Repo.IPolicyRepo;
import com.SecureAccessPortal.Repo.PaymentSequenceRepo;
import com.SecureAccessPortal.Repo.PaymentsRepo;
import com.SecureAccessPortal.Repo.PolicyInfoRepo;
import com.SecureAccessPortal.Repo.PolicyRequestRepo;
import com.SecureAccessPortal.Repo.SurrenderRepo;
import com.SecureAccessPortal.Repo.WorkItemRepo;
import com.SecureAccessPortal.Transformer.PolicyMapper;
import com.SecureAccessPortal.util.DateUtil;

import jakarta.persistence.criteria.Predicate;
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

	@Autowired
	private SurrenderRepo surrenderRepoSitory;

	@Autowired
	private ClaimRepo claimRepoSitory;

	@Autowired
	private PolicyRequestRepo policyRequestRepository;

	@Transactional
	public ResponseDTO createPolicy(PolicyRequest policyDTO, String userCode) {
		ResponseDTO response = new ResponseDTO();
		Customer customer = null;
		// 🔹 Decide installment amount & total installments
		BigDecimal installmentAmount = BigDecimal.ZERO;
		int totalInstallments = 0;

		try {
			Policy policy = new Policy();

			// 🔹 Audit info
			policy.setCreatedBy(userCode);
			policy.setCreatedDate(LocalDateTime.now());
			policy.setUserCode(userCode);
			policy.setDeletedFlag(CommonConstant.N);

			// 🔹 Customer mapping
			if (policyDTO.getCustomerNo() != null) {
				customer = customerRepository.findByCustomerNoNew(policyDTO.getCustomerNo(), "N");
				if (customer == null) {
					throw new IllegalArgumentException("Customer not found");
				}
				policy.setCustomer(customer);
			}

			// 🔹 Core policy info
			policy.setPolCompanyName(policyDTO.getPolCompanyName());
			policy.setPolicyName(policyDTO.getPolicyName());
			policy.setProductCode(policyDTO.getProductCode());
			policy.setPolicyType(policyDTO.getPolicyType());
			policy.setPolicyStatus(policyDTO.getPolicyStatus());
			policy.setFcuStatus(policyDTO.getFcuStatus());
			policy.setSmokerStatus(policyDTO.getSmokerStatus());
			policy.setReason(policyDTO.getReason());

			// 🔹 Generate policy number & policyId
			String newPolicyNumber = generatePolicyNumber();
			policy.setPolicyNumber(newPolicyNumber);
			String[] parts = newPolicyNumber.split("/");
			policy.setPolicyId(Integer.parseInt(parts[1]));

			// 🔹 Beneficiary info
			policy.setBeneficiaryName(policyDTO.getBeneficiaryName());
			policy.setBeneficiaryRelationship(policyDTO.getBeneficiaryRelationship());
			policy.setBeneficiaryContactNumber(policyDTO.getBeneficiaryContactNumber());
			policy.setBeneficiaryIdentityNumber(policyDTO.getBeneficiaryIdentityNumber());
			policy.setBeneficiaryStatus(policyDTO.getBeneficiaryStatus());

			// 🔹 Policy dates
			LocalDateTime startDate = LocalDateTime.now();
			policy.setPolicyStartDate(startDate);

			String term = policyDTO.getPolicyTerm(); // "MONTHLY", "YEARLY", etc.
			policy.setPolicyTerm(term);
			try {
				// 🔹 Amount (from UI)
				BigDecimal totalAmount = policyDTO.getTotalAmount();
				policy.setTotalAmount(totalAmount);

				switch (term.toUpperCase()) {
				case "MONTHLY":
					totalInstallments = 12; // 12 months in a year
					installmentAmount = totalAmount.divide(BigDecimal.valueOf(totalInstallments), RoundingMode.HALF_UP);
					break;
				case "YEARLY":
					totalInstallments = 1; // One yearly payment
					installmentAmount = totalAmount;
					break;
				case "QUATERLY":
					totalInstallments = 4; // 52 weeks in a year
					installmentAmount = totalAmount.divide(BigDecimal.valueOf(totalInstallments), RoundingMode.HALF_UP);
					break;
				case "DAILY":
					totalInstallments = 365; // Days in a year
					installmentAmount = totalAmount.divide(BigDecimal.valueOf(totalInstallments), RoundingMode.HALF_UP);
					break;
				default:
					throw new IllegalArgumentException("Unsupported policy term: " + term);
				}

				policy.setMonthlyInstallment(installmentAmount);
				policy.setTotalInstallments(totalInstallments);

				// 🔹 Calculate end date from startDate + term * installments
				LocalDateTime endDate;
				switch (term.toUpperCase()) {
				case "MONTHLY":
					endDate = startDate.plusMonths(totalInstallments);
					break;
				case "YEARLY":
					endDate = startDate.plusYears(totalInstallments);
					break;
				case "QUATERLY":
					endDate = startDate.plusWeeks(totalInstallments);
					break;
				case "DAILY":
					endDate = startDate.plusDays(totalInstallments);
					break;
				default:
					throw new IllegalArgumentException("Unsupported policy term: " + term);
				}
				policy.setPolicyEndDate(endDate);

				// 🔹 Premium due date = first installment
				LocalDateTime firstDueDate = dateUtil.calculateNextDueDate(startDate, term, 1);
				policy.setPremiumDueDate(firstDueDate);
				// 🔹 Payment tracking
				policy.setTotalPaidAmount(BigDecimal.ZERO);
				policy.setTotalUnPaidAmount(totalAmount);

				policy.setTotalPaidInstallments(0);
				policy.setTotalUnPaidInstallments(totalInstallments);
			} catch (NullPointerException n) {
				n.printStackTrace();
			}
			policy = policyRepository.saveAndFlush(policy);

			// 🔹 Create payment entries (schedule)
			createPaymentEntries(policy, customer != null ? customer.getCustomerNo() : policyDTO.getCustomerNo(),
					policyDTO.getProductCode(), policyDTO.getPolicyName(), installmentAmount, totalInstallments,
					startDate, term, userCode, customer);

			// 🔹 Work item
			workItemService.mapRequetforWorkItem(userCode, policy, customer, CommonConstant.ADD_POL,
					CommonConstant.POLICY_CREATED, "Policy created " + newPolicyNumber, null, null, null,
					CommonConstant.OPEN);

			response.setPolicyNo(newPolicyNumber);
			response.setStatus(CommonConstant.SUCCESS);

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(CommonConstant.FAILURE);
		}
		return response;

	}

	private void createPaymentEntries(Policy policy, String customerNo, String productCode, String policyName,
			BigDecimal installmentAmount, int installmentCount, LocalDateTime policyStartDate, String term,
			String userCode, Customer customer) {
		List<Payments> paymentsList = new ArrayList<>();
		LocalDateTime baseDueDate = policyStartDate.withDayOfMonth(1).plusMonths(1); // Start from 1st of next month

		for (int i = 0; i < installmentCount; i++) {
			Payments payment = new Payments();
			payment.setPolicy(policy);
			payment.setPaymentId(generatePaymentId());
			payment.setPolicy(policy);
			payment.setInstallmentCount(i + 1);
			payment.setTotalInstallments(installmentCount);
			payment.setCustomer(customer);
			payment.setProductCode(productCode);
			payment.setPolicyName(policyName);
			payment.setInstallmentAmount(installmentAmount);
			payment.setDueDate(dateUtil.calculateNextDueDate(baseDueDate, term, i));
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
		String status = CommonConstant.OPEN;
		String comment = "Policy Amount " + installmentAmount + "and installemt are " + installmentCount
				+ "created for customer " + customerNo + customer.getName() + "and Policy No is"
				+ policy.getPolicyNumber();
		workItemService.mapRequetforWorkItem(userCode, policy, customer, workType, workItemName, comment, null, null,
				paymentList.get(0), status);
	}

	@Transactional
	private synchronized String generatePaymentId() {
		LocalDateTime now = LocalDateTime.now();
		String yearMonth = now.format(DateTimeFormatter.ofPattern("yyyyMM"));
		PaymentSequence sequence = paymentSequenceRepository.findByYearMonth(yearMonth).orElseGet(() -> {
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
		String currentYear = String.valueOf(Year.now().getValue()); // e.g., "2025"
		String lastPolicyNo = policyRepository.findTopPolicyNumberForCurrentYear(currentYear);
		int nextSequenceNumber = 1;
		if (lastPolicyNo != null && lastPolicyNo.contains("/")) {
			try {
				String numberPart = lastPolicyNo.split("/")[1];
				nextSequenceNumber = Integer.parseInt(numberPart) + 1;
			} catch (NumberFormatException e) {
				logger.warn("Could not parse sequence number from last policy number: {}. Resetting to 001.",
						lastPolicyNo, e);
				nextSequenceNumber = 1;
			}
		}
		String newPolicyNumber = String.format("POLICY/%03d/%s", nextSequenceNumber, currentYear);
		logger.info("Generated new policy number: {}", newPolicyNumber);

		return newPolicyNumber;
	}
	
	
	public List<PolicyDTO> mapPolicyListDetails(List<Policy> policies, String userCode) {
		AtomicInteger count = new AtomicInteger();
		return policies.stream().collect(Collectors.groupingBy(policy -> policy.getCustomer().getCustomerNo()))
				.entrySet().stream().map(entry -> {
					String customerNo = entry.getKey();
					List<Policy> customerPolicies = entry.getValue();
					Optional<Customer> customerOpt = customerRepository.findByCustomerNo(customerNo);
					if (customerOpt.isEmpty())
						return null;
					Customer customer = customerOpt.get();
					PolicyDTO policyDTO = new PolicyDTO();

					customerPolicies.stream().forEach(policy -> count.getAndIncrement());
					policyDTO.setAssociatedPolicyCount(String.valueOf(customerPolicies.size()));
					policyDTO.setCustomerNo(customer.getCustomerNo());
					policyDTO.setUserCode(customer.getUserCode() != null ? customer.getUserCode() : null);
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

	public  PolicyList mapPolicyToPolicyList(Policy policy) {
		PolicyList pol = new PolicyList();
		pol.setPolicyId(policy.getPolicyId());
		pol.setFcuStatus(policy.getFcuStatus() != null ? policy.getFcuStatus() : "");
		pol.setPolicyNumber(policy.getPolicyNumber() != null ? policy.getPolicyNumber() : "");
		pol.setPolCompanyName(policy.getPolCompanyName() != null ? policy.getPolCompanyName() : "");
		pol.setPolicyName(policy.getPolicyName() != null ? policy.getPolicyName() : "");
		pol.setProductCode(policy.getProductCode() != null ? policy.getProductCode() : "");
		pol.setCreatedBy(policy.getCreatedBy() != null ? policy.getCreatedBy() : "");
		pol.setCreatedDate(policy.getCreatedDate());
		// workitem mapping
		pol.setWorkItemRefNo(policy.getWorkitems() != null
				? policy.getWorkitems().stream().map(Workitem::getWorkItemRefNumber).collect(Collectors.toList())
				: new ArrayList<>());
		pol.setUpdatedDate(policy.getUpdatedDate());
		pol.setUpdatedBy(policy.getUpdatedBy() != null ? policy.getUpdatedBy() : "");
		pol.setDeletedFlag(policy.getDeletedFlag() != null ? policy.getDeletedFlag() : "");
		pol.setPolicyType(policy.getPolicyType() != null ? policy.getPolicyType() : "");
		pol.setPolicyStatus(policy.getPolicyStatus() != null ? policy.getPolicyStatus() : "");
		pol.setCustomerNo(policy.getCustomer() != null ? policy.getCustomer().getCustomerNo() : "");
		pol.setBeneficiaryName(policy.getBeneficiaryName() != null ? policy.getBeneficiaryName() : "");
		pol.setBeneficiaryRelationship(
				policy.getBeneficiaryRelationship() != null ? policy.getBeneficiaryRelationship() : "");
		pol.setSmokerStatus(policy.getSmokerStatus() != null ? policy.getSmokerStatus() : "");
		pol.setTotalAmount((policy.getTotalAmount() != null ? policy.getTotalAmount() : BigDecimal.ZERO));
		pol.setTotalInstallments(policy.getTotalInstallments());
		pol.setPolicyTerm(policy.getPolicyTerm() != null ? policy.getPolicyTerm() : "");
		pol.setMonthlyInstallment(policy.getMonthlyInstallment());
		pol.setBeneficiaryIdentityNumber(
				policy.getBeneficiaryIdentityNumber() != null ? policy.getBeneficiaryIdentityNumber() : "");
		pol.setBeneficiaryContactNumber(
				policy.getBeneficiaryContactNumber() != null ? policy.getBeneficiaryContactNumber() : "");
		pol.setPremiumDueDate(policy.getPremiumDueDate());
		pol.setBeneficiaryStatus(policy.getBeneficiaryStatus() != null ? policy.getBeneficiaryStatus() : "");
		pol.setPolicyStartDate(policy.getPolicyStartDate());
		pol.setPolicyEndDate(policy.getPolicyEndDate());
		pol.setReason(policy.getReason() != null ? policy.getReason() : "");

		List<Bank> bankAccounts = bankAccountRepository.findByPolicyNumber(policy.getPolicyNumber(), CommonConstant.N);
		pol.setBankAccounts(
				bankAccounts != null ? bankAccounts.stream().map(this::mapToBankAccountDTO).collect(Collectors.toList())
						: new ArrayList<>());
		pol.setPaymentListDTO(mapForPayments(policy.getPayments()));
		pol.setTotalAmount(policy.getTotalAmount());

		List<Payments> payments = policy.getPayments();
		if (payments != null) {
			long paidCount = payments.stream().filter(Objects::nonNull)
					.filter(p -> CommonConstant.PAID.equalsIgnoreCase(p.getStatus())).count();
			long unpaidCount = payments.stream().filter(Objects::nonNull)
					.filter(p -> CommonConstant.UNPAID.equalsIgnoreCase(p.getStatus())).count();
			BigDecimal totalPaidAmount = payments.stream().filter(Objects::nonNull)
					.filter(p -> CommonConstant.PAID.equalsIgnoreCase(p.getStatus()))
					.map(Payments::getInstallmentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal totalUnpaidAmount = payments.stream().filter(Objects::nonNull)
					.filter(p -> CommonConstant.UNPAID.equalsIgnoreCase(p.getStatus()))
					.map(Payments::getInstallmentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
			pol.setTotalInstallmentsPaid((int) paidCount);
			pol.setTotalInstallmentsUnPaid((int) unpaidCount);
			pol.setTotalAmtPaidByInstallemt(totalPaidAmount);
			pol.setTotalAmtUnPaidByInstallemt(totalUnpaidAmount);
		}

		if (policy.getSurrender() != null) {
			List<SurrenderClaimDTO> surrenderList = policy.getSurrender().stream().filter(Objects::nonNull)
					.map(surr -> {
						SurrenderClaimDTO surrender = new SurrenderClaimDTO();
						surrender.setPolicyNo(policy.getPolicyNumber());
						surrender.setCustomerNo(policy.getCustomer().getCustomerNo());
						surrender.setCustomerName(policy.getCustomer().getName());
						surrender.setSurrenderRefNo(surr.getSurrRefNo());
						BigDecimal totalPaidAmount = payments.stream().filter(Objects::nonNull)
								.filter(p -> CommonConstant.PAID.equalsIgnoreCase(p.getStatus()))
								.map(Payments::getInstallmentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
						surrender.setSurrAmount(totalPaidAmount);
						surrender.setSurrDate(String.valueOf(surr.getSurrDate()));
						surrender.setSurrenderStatus(surr.getSurrenderStatus());
						surrender.setSurrenderBy(surr.getSurrenderBy());
						surrender.setSurrReason(surr.getSurrenderReason());
						surrender.setPaymentDate(null);
						surrender.setPaymentStatus(null);
						surrender.setTransactionDate(null);
						surrender.setPaymentId(null);
						surrender.setPaymentTransId(null);
						surrender.setVerificationComment(surr.getVerificationComment());
						surrender.setVerificationDocument(null);
						surrender.setVerificationDocumentName(surr.getVerificationDocumentName());
						surrender.setVerificationStatus(surr.getVerificationStatus());
						surrender.setOtherSupportingDocumentVerificationStatus(
								surr.getOtherSupportingDocumentVerificationStatus());
						surrender.setOtherSupportingDocument(null);
						surrender.setOtherSupportingDocumentName(surr.getOtherSupportingDocumentName());
						return surrender;
					}).collect(Collectors.toList());
			pol.setSurrenderDTO(surrenderList);
		}
		if (policy.getClaim() != null) {
			List<SurrenderClaimDTO> claimList = policy.getClaim().stream().filter(Objects::nonNull).map(surr -> {
				SurrenderClaimDTO surrender = new SurrenderClaimDTO();
				surrender.setPolicyNo(policy.getPolicyNumber());
				surrender.setCustomerNo(policy.getCustomer().getCustomerNo());
				surrender.setCustomerName(policy.getCustomer().getName());
				surrender.setClaimRefNo(surr.getClaimRefNo());
				surrender.setClaimDate(String.valueOf(surr.getClaimDate()));
				surrender.setClaimStatus(surr.getClaimStatus());
				surrender.setClaimBy(surr.getClaimBy());
				surrender.setClaimReason(surr.getClaimReason());
				surrender.setPaymentDate(null);
				surrender.setPaymentStatus(null);
				surrender.setTransactionDate(null);
				surrender.setPaymentId(null);
				surrender.setPaymentTransId(null);
				surrender.setVerificationComment(surr.getVerificationComment());
				surrender.setVerificationDocument(null);
				surrender.setVerificationDocumentName(surr.getVerificationDocumentName());
				surrender.setVerificationStatus(surr.getVerificationStatus());
				surrender.setOtherSupportingDocumentVerificationStatus(
						surr.getOtherSupportingDocumentVerificationStatus());
				surrender.setOtherSupportingDocument(null);
				surrender.setOtherSupportingDocumentName(surr.getOtherSupportingDocumentName());
				return surrender;
			}).collect(Collectors.toList());
			pol.setClaimDTO(claimList);
		}
		return pol;
	}

	private List<PaymentList> mapForPayments(List<Payments> payments) {
		List<PaymentList> paymentL = payments.stream().filter(Objects::nonNull).map(payment -> {
			PaymentList paymentList = new PaymentList();
			paymentList.setDueDate(payment.getDueDate());
			paymentList.setEmailStatus(payment.getEmailStatus());
			paymentList.setInstallmentAmount(payment.getInstallmentAmount());
			paymentList.setInstallmentCount(payment.getInstallmentCount());
			paymentList.setPaymentMethod(payment.getPaymentMethod());
			// paymentList.setMessage(payment.get);
			paymentList.setPaymentDate(payment.getPaymentDate());
			paymentList.setPaymentId(payment.getPaymentId());
			paymentList.setResponseStatus(CommonConstant.SUCCESS);
			paymentList.setTransactionId(payment.getTransactionId());
			paymentList.setStatus(payment.getStatus());
			return paymentList;
		}).collect(Collectors.toList());
		return paymentL;
	}

	private BankDetailsDTO mapToBankAccountDTO(Bank bankAccount) {
		if (bankAccount == null) {
			return null;
		}

		BankDetailsDTO dto = new BankDetailsDTO();

		// Primary key
		dto.setBankId(bankAccount.getBankId());

		// Relations
		if (bankAccount.getCustomer() != null) {
			dto.setCustomerNumber(bankAccount.getCustomer().getCustomerNo());
			dto.setCustomerName((bankAccount.getCustomer().getName() != null ? bankAccount.getCustomer().getName() : "")
					+ " "
					+ (bankAccount.getCustomer().getMiddleName() != null ? bankAccount.getCustomer().getMiddleName()
							: "")
					+ " "
					+ (bankAccount.getCustomer().getSurname() != null ? bankAccount.getCustomer().getSurname() : ""));
		}
		if (bankAccount.getPolicy() != null) {
			dto.setPolicyNumber(bankAccount.getPolicy().getPolicyNumber());
			dto.setPolicyStatus(bankAccount.getPolicy().getPolicyStatus());
		}
		dto.setAccountNumber(bankAccount.getAccountNumber());
		dto.setAccountHolderName(bankAccount.getAccountHolderName());
		dto.setAccountHolderType(bankAccount.getAccountHolderType());
		dto.setBankName(bankAccount.getBankName());
		dto.setBranchCode(bankAccount.getBranchCode());
		dto.setIfscCode(bankAccount.getIfscCode());
		dto.setSwiftCode(bankAccount.getSwiftCode());
		dto.setAccountType(bankAccount.getAccountType());
		dto.setCurrency(bankAccount.getCurrency());
		dto.setAccountBalance(bankAccount.getAccountBalance());
		dto.setIsDefaultAccount(bankAccount.getIsDefaultAccount());
		dto.setAccountOpeningDate(bankAccount.getAccountOpeningDate());
		dto.setAccountClosingDate(bankAccount.getAccountClosingDate());
		dto.setKycDocumentStatus(bankAccount.getKycDocumentStatus());
		dto.setKycStatus(bankAccount.getKycStatus());
		if (bankAccount.getKycDocument() != null) {
			dto.setKycDocument(Base64.getEncoder().encodeToString(bankAccount.getKycDocument())); // byte[] -> Base64
																									// string
		}
		dto.setAmlStatus(bankAccount.getAmlStatus());

		// Payment & Verification
		dto.setPaymentMethodStatus(bankAccount.getPaymentMethodStatus());
		dto.setLinkedPaymentMethod(bankAccount.getLinkedPaymentMethod());
		dto.setLastPaymentDate(bankAccount.getLastPaymentDate());
		dto.setLastVerificationDate(bankAccount.getLastVerificationDate());
		dto.setVerificationAttempts(bankAccount.getVerificationAttempts());
		dto.setVerifierComment(bankAccount.getVerifierComment());
		dto.setCustomerComment(bankAccount.getCustomerComment());

		// Status Flags
		dto.setStatus(bankAccount.getStatus());
		dto.setDeletedFlag(bankAccount.getDeletedFlag());

		// Audit
		dto.setCreatedBy(bankAccount.getCreatedBy());
		dto.setCreatedDate(bankAccount.getCreatedDate());
		dto.setUpdatedBy(bankAccount.getUpdatedBy());
		dto.setUpdatedDate(bankAccount.getUpdatedDate());

		return dto;
	}

	public List<GroupedPolicyDTO> getDomainData(String policyNo, String userCode) {
		List<Policy_Info> policies = new ArrayList<>();
		if (policyNo != null && !policyNo.trim().isEmpty()) {
			policies = policyInfoRepo.findByProductCode(policyNo.trim());
		} else {
			policies = policyInfoRepo.findAllByOrderByProductCode();
		}
		Map<String, Map<String, Map<String, List<PolicyInfoDTO>>>> grouped = policies.stream().map(this::mapToDTO)
				.collect(Collectors.groupingBy(PolicyInfoDTO::getPolicyName, Collectors.groupingBy(
						PolicyInfoDTO::getProductCode, Collectors.groupingBy(PolicyInfoDTO::getPolicyType))));
		return grouped.entrySet().stream().flatMap(policyNameEntry -> policyNameEntry.getValue().entrySet().stream()
				.flatMap(productCodeEntry -> productCodeEntry.getValue().entrySet().stream()
						.map(policyTypeEntry -> new GroupedPolicyDTO(policyNameEntry.getKey(),
								productCodeEntry.getKey(), policyTypeEntry.getKey(), policyTypeEntry.getValue()))))
				.collect(Collectors.toList());
	}

	private PolicyInfoDTO mapToDTO(Policy_Info policy) {
		PolicyInfoDTO dto = new PolicyInfoDTO();
		dto.setPolicyId(policy.getPolicyId());
		dto.setPolicyName(policy.getPolicyName());
		dto.setProductCode(policy.getProductCode());
		dto.setPolicyType(policy.getPolicyType());
		dto.setPolicyStatus(policy.getPolicyStatus());
		dto.setPolCompanyName(policy.getPolCompanyName());
		dto.setPolicyTerm(policy.getPolicyTerm());
		dto.setTotalAmount(policy.getTotalAmount());
		dto.setUserCode(policy.getUserCode());
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

	public ResponseDTO updatePolicy(PolicyRequest policyDTO, String userCode) {
		ResponseDTO response = new ResponseDTO();
		Customer customer = null;
		Policy policy = policyRepository.findByPolicyNum(policyDTO.getPolicyNumber(), "N");
		if (policy == null) {
			throw new IllegalArgumentException("Policy not found");
		}
		if (policyDTO.getCustomerNo() != null) {
			customer = customerRepository.findByCustomerNoNew(policyDTO.getCustomerNo(), "N");
			if (customer == null) {
				throw new IllegalArgumentException("Customer not found");
			}
			policy.setCustomer(customer);
		}
		try {
			// Always set audit fields
			policy.setUpdatedBy(userCode);
			policy.setUpdatedDate(LocalDateTime.now());
			policy.setDeletedFlag("N");

			// ✅ Conditional field updates
			if (policyDTO.getPolicyName() != null)
				policy.setPolicyName(policyDTO.getPolicyName());
			if (policyDTO.getProductCode() != null)
				policy.setProductCode(policyDTO.getProductCode());
			if (policyDTO.getFcuStatus() != null)
				policy.setFcuStatus(policyDTO.getFcuStatus());
			if (policyDTO.getBeneficiaryContactNumber() != null)
				policy.setBeneficiaryContactNumber(policyDTO.getBeneficiaryContactNumber());
			if (policyDTO.getBeneficiaryIdentityNumber() != null)
				policy.setBeneficiaryIdentityNumber(policyDTO.getBeneficiaryIdentityNumber());
			if (policyDTO.getBeneficiaryName() != null)
				policy.setBeneficiaryName(policyDTO.getBeneficiaryName());
			if (policyDTO.getBeneficiaryRelationship() != null)
				policy.setBeneficiaryRelationship(policyDTO.getBeneficiaryRelationship());
			if (policyDTO.getMonthlyInstallment() != null)
				policy.setMonthlyInstallment(policyDTO.getMonthlyInstallment());
			if (policyDTO.getPolicyEndDate() != null)
				policy.setPolicyEndDate(policyDTO.getPolicyEndDate());
			if (policyDTO.getPolicyStartDate() != null)
				policy.setPolicyStartDate(policyDTO.getPolicyStartDate());
			if (policyDTO.getPolicyStatus() != null)
				policy.setPolicyStatus(policyDTO.getPolicyStatus());
			if (policyDTO.getPolicyTerm() != null)
				policy.setPolicyTerm(policyDTO.getPolicyTerm());
			if (policyDTO.getPolicyType() != null)
				policy.setPolicyType(policyDTO.getPolicyType());
			if (policyDTO.getPremiumDueDate() != null)
				policy.setPremiumDueDate(policyDTO.getPremiumDueDate());
			if (policyDTO.getSmokerStatus() != null)
				policy.setSmokerStatus(policyDTO.getSmokerStatus());
			if (policyDTO.getTotalAmount() != null)
				policy.setTotalAmount(policyDTO.getTotalAmount());

			// Create work item
			// String workType = CommonConstant.UPDATE_POL_DETAIL;
			// String workItemName = CommonConstant.POLICY_UPDATE_DETAILS;
			// String status=CommonConstant.OPEN;
			// String comment = "Policy is created " + policy.getPolicyNumber() + " for the
			// customer";
			// Workitem mapRequetforWorkItem =
			// workItemService.mapRequetforWorkItem(userCode, policy, customer, workType,
			// workItemName, comment, null, null, null,status);
			policy = policyRepository.save(policy);
			response.setPolicyNo(policy.getPolicyNumber());
			response.setStatus(CommonConstant.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(CommonConstant.FAILURE);
		}
		return response;
	}

	public ResponseDTO updateDeletePolicy(String policyNumber, String reson, String userCode) {
		ResponseDTO response = new ResponseDTO();
		Customer customer = null;
		Policy policy = policyRepository.findByPolicyNum(policyNumber, "N");
		if (policy != null) {
			if (policy.getCustomer() != null) {
				customer = customerRepository.findByCustomerNoNew(policy.getCustomer().getCustomerNo(), "N");
				if (customer == null) {
					throw new IllegalArgumentException("Customer not found");
				}
				policy.setCustomer(customer);
			}

			policy.setDeletedFlag("Y");
			policy.setPolicyStatus("LAPSED");
			policy.setUpdatedBy(userCode);
			policy.setUpdatedDate(LocalDateTime.now());
			policy.setReason(reson);
			// Save policy
			policy = policyRepository.save(policy);

			response.setPolicyNo(policy.getPolicyNumber());
			response.setStatus(CommonConstant.SUCCESS);
			// Create work item
			String workType = CommonConstant.POLICY_DELETED;
			String workItemName = CommonConstant.POLICY_DELETED;
			String status = CommonConstant.APPROVED;
			String comment = "Policy Deleted  " + policy.getPolicyNumber() + " for the customer";
			Workitem mapRequetforWorkItem = workItemService.mapRequetforWorkItem(userCode, policy, customer, workType,
					workItemName, comment, null, null, null, status);
		} else {
			response.setStatus("Policy Not found");
		}
		return response;
	}

	public ResponseEntity<Page<PolicyDTO>> getPolicyDetailsNew(String policyNo, String customerNo, String allPolicy,
			String workItemRefNo, String status, String type, String startDate, String endDate, String bankAccountNo,
			String userCode, int page, int size) {

		ResponseEntity<Page<PolicyDTO>> resp = new ResponseEntity<>();

		// Step 1: Build Specification (same as before, but without pageable)
		Specification<Policy> spec = (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (workItemRefNo != null && !workItemRefNo.isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("workitems").get("workItemRefNumber")),
						"%" + workItemRefNo.toLowerCase() + "%"));
			}
			if (policyNo != null && !policyNo.isEmpty()) {
				predicates.add(cb.equal(root.get("policyNumber"), policyNo));
			}
			if (customerNo != null && !customerNo.isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("customer").get("customerNo")),
						"%" + customerNo.toLowerCase() + "%"));
			}
			if (type != null && !type.isEmpty()) {
				predicates.add(cb.equal(root.get("policyType"), type));
			}
			if (bankAccountNo != null && !bankAccountNo.isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("bankAccounts").get("accountNo")),
						"%" + bankAccountNo.toLowerCase() + "%"));
			}
			if (status != null && !status.isEmpty()) {
				predicates.add(cb.equal(root.get("policyStatus"), status));
			}
			predicates.add(cb.equal(root.get("deletedFlag"), "N"));

			if (startDate != null && !startDate.isEmpty()) {
				try {
					LocalDateTime startDateTime = LocalDateTime.parse(startDate + " 00:00:00",
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					predicates.add(cb.greaterThanOrEqualTo(root.get("createdTime"), startDateTime));
				} catch (DateTimeParseException e) {
					System.err.println("Invalid startDate format: " + startDate + ". Skipping date filter.");
				}
			}

			if (endDate != null && !endDate.isEmpty()) {
				try {
					LocalDateTime endDateTime = LocalDateTime.parse(endDate + " 23:59:59",
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					predicates.add(cb.lessThanOrEqualTo(root.get("createdTime"), endDateTime));
				} catch (DateTimeParseException e) {
					System.err.println("Invalid endDate format: " + endDate + ". Skipping date filter.");
				}
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
		List<Policy> allPolicies = policyRepository.findAll(spec);
		Map<String, List<Policy>> groupedByCustomer = allPolicies.stream()
				.collect(Collectors.groupingBy(p -> p.getCustomer().getCustomerNo()));

		List<String> allCustomerNos = new ArrayList<>(groupedByCustomer.keySet());
		int start = Math.min(page * size, allCustomerNos.size());
		int end = Math.min(start + size, allCustomerNos.size());
		List<String> pagedCustomerNos = allCustomerNos.subList(start, end);

		List<PolicyDTO> policyDTOs = pagedCustomerNos.stream().map(custNo -> {
			List<Policy> policiesOfCustomer = groupedByCustomer.get(custNo);
			// Map your policies -> PolicyDTO (you already have mapPolicyListDetails)
			return mapPolicyListDetails(policiesOfCustomer, userCode).get(0);
		}).collect(Collectors.toList());

		// Step 6: Wrap result into Page
		Page<PolicyDTO> response = new PageImpl<>(policyDTOs, PageRequest.of(page, size), allCustomerNos.size());

		resp.setData(response);
		resp.setStatus(CommonConstant.SUCCESS);
		return resp;
	}

	@Transactional
	public ResponseEntity<String> applyForPolicy(PolicyList policyDTO, String action, String userCode) {
		ResponseEntity<String> response = new ResponseEntity<>();
		PolicyRequestEntity policyRequest = new PolicyRequestEntity();
		try {
			Customer byCustomerNoNew = customerRepository.findByCustomerNoNew(policyDTO.getCustomerNo(),
					CommonConstant.N);
			if (byCustomerNoNew != null) {
				policyRequest.setCustomer(byCustomerNoNew);
			}
			switch (action) {
			case CommonConstant.APPLY -> {
				policyRequest.setRequestNumber(generateRequestNumberforCust());
				policyRequest.setPolicyName(policyDTO.getPolicyName());
				policyRequest.setDeletedFlag(CommonConstant.N);
				policyRequest.setPolicyStatus(policyDTO.getPolicyStatus());
				policyRequest.setPolCompanyName(policyDTO.getPolCompanyName());
				policyRequest.setPolicyTerm(policyDTO.getPolicyTerm());

				policyRequest.setCreatedBy(userCode);
				policyRequest.setCreatedDate(LocalDateTime.now());
				policyRequest.setTotalAmount(policyDTO.getTotalAmount());
				policyRequest.setPolicyType(policyDTO.getPolicyType());
				policyRequest.setProductCode(policyDTO.getProductCode());
				policyRequest.setReason(policyDTO.getReason());
				policyRequest.setRequestedBy(userCode);
				policyRequest.setStatus(CommonConstant.IN_PROGRESS);
			}
			case CommonConstant.UPDATE -> { 
				policyRequest.setRequestNumber(policyDTO.getPolicyNumber());
				policyRequest.setVerifier(userCode);
				policyRequest.setVerifierReason(policyDTO.getReason());
				policyRequest.setStatus(policyDTO.getPolicyStatus());
				policyRequest.setUpdatedBy(userCode);
				policyRequest.setUpdatedDate(LocalDateTime.now());
			}
			}
			policyRequestRepository.save(policyRequest);
			response.setStatus(CommonConstant.SUCCESS);
		} catch (Exception e) {
			response.setStatus(ErrorConstants.FAILURE);
			response.setErrorMessage("Error occured" + e);
		}
		return response;
	}

	public ResponseEntity<List<PolicyList>> applyPolicyRequest(String customerNo, String userCode) {
		ResponseEntity<List<PolicyList>> response = new ResponseEntity<>();
		List<PolicyRequestEntity> policies = policyRequestRepository.findByCustomerNo(customerNo, CommonConstant.N);
		if (policies != null) {
			List<PolicyList> policyResponse = policyMapper.mapPolicyRequest(policies);
			Collections.reverse(policies);
			response.setData(policyResponse);
			response.setStatus(CommonConstant.SUCCESS);
		} else {
			response.setStatus(ErrorConstants.FAILURE);
			response.setErrorMessage("Policy List Empty");
		}
		return response;
	}

	@Transactional
	public synchronized String generateRequestNumberforCust() {
		String currentYear = String.valueOf(Year.now().getValue()); // e.g., "2025"
		String lastPolicyNo = policyRequestRepository.findTopRequestNumberForCurrentYear(currentYear);
		int nextSequenceNumber = 1;
		if (lastPolicyNo != null && lastPolicyNo.contains("/")) {
			try {
				String numberPart = lastPolicyNo.split("/")[1];
				nextSequenceNumber = Integer.parseInt(numberPart) + 1;
			} catch (NumberFormatException e) {
				logger.warn("Could not parse sequence number from last policy number: {}. Resetting to 001.",
						lastPolicyNo, e);
				nextSequenceNumber = 1;
			}
		}
		String newPolicyNumber = String.format("RQST/%03d/%s", nextSequenceNumber, currentYear);
		logger.info("Generated new policy number: {}", newPolicyNumber);
		return newPolicyNumber;
	}

	public ResponseEntity<Page<SurrenderClaimDTO>> getSurrender(String action, String surrenderRefNo, String policyNo,
			String customerNo, String startDate, String endDate, String type, int page, int size, String userCode) {
		ResponseEntity<Page<SurrenderClaimDTO>> response = new ResponseEntity<>();
		try {
			Pageable pagable = PageRequest.of(page, size, Sort.by("createdDate").descending());
			Specification<SurrenderEntity> spec = (root, query, cb) -> {
				List<jakarta.persistence.criteria.Predicate> predicate = new ArrayList<>();
				if (surrenderRefNo != null) {
					predicate.add(cb.equal(root.get("surrRefNo"), surrenderRefNo));
				}
				if (policyNo != null) {
					predicate.add(cb.like(cb.lower(root.get("policy").get("policyNumber")),
							"%" + policyNo.toLowerCase() + "%"));
				}
				if (customerNo != null) {
					predicate.add(cb.like(cb.lower(root.get("customer").get("customerNo")),
							"%" + customerNo.toLowerCase() + "%"));
				}
				if (type != null && !type.equalsIgnoreCase(CommonConstant.ALL)) {
					predicate.add(cb.equal(root.get("surrenderStatus"), type));
				}
				if (startDate != null && !startDate.isEmpty()) {
					try {
						LocalDateTime startDateTime = LocalDateTime.parse(startDate + " 00:00:00",
								DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
						predicate.add(cb.greaterThanOrEqualTo(root.get("createdDate"), startDateTime));
					} catch (DateTimeParseException e) {
						System.err.println("Invalid startDate format: " + startDate + ". Skipping date filter.");
					}
				}
				if (endDate != null && !endDate.isEmpty()) {
					try {
						LocalDateTime endDateTime = LocalDateTime.parse(endDate + " 23:59:59",
								DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
						predicate.add(cb.lessThanOrEqualTo(root.get("createdDate"), endDateTime));
					} catch (DateTimeParseException e) {
						System.err.println("Invalid endDate format: " + endDate + ". Skipping date filter.");
					}
				}
				return cb.and(predicate.toArray(new jakarta.persistence.criteria.Predicate[0]));
			};
			Page<SurrenderEntity> surrenderEntity = surrenderRepoSitory.findAll(spec, pagable);
			Page<SurrenderClaimDTO> mapClaimSurreResponse = policyMapper.mapClaimSurreResponse(surrenderEntity);
			response.setData(mapClaimSurreResponse);
			response.setStatus(CommonConstant.SUCCESS);
		} catch (Exception e) {
			response.setStatus(CommonConstant.FAILURE);
			response.setErrorMessage("No Data found");
			e.getLocalizedMessage();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return response;
	}

	public ResponseEntity<Page<SurrenderClaimDTO>> getClaim(String action, String claimRefNo, String policyNo,
			String customerNo, String startDate, String endDate, String type, int page, int size, String userCode) {

		ResponseEntity<Page<SurrenderClaimDTO>> response = new ResponseEntity<>();
		try {
			Pageable pagable = PageRequest.of(page, size, Sort.by("createdDate").descending());
			Specification<ClaimEntity> spec = (root, query, cb) -> {
				List<jakarta.persistence.criteria.Predicate> predicate = new ArrayList<>();
				if (claimRefNo != null) {
					predicate.add(cb.equal(root.get("claimRefNo"), claimRefNo));
				}
				if (policyNo != null) {
					predicate.add(cb.like(cb.lower(root.get("policy").get("policyNumber")),
							"%" + policyNo.toLowerCase() + "%"));
				}
				if (customerNo != null) {
					predicate.add(cb.like(cb.lower(root.get("customer").get("customerNo")),
							"%" + customerNo.toLowerCase() + "%"));
				}
				if (type != null && !type.equalsIgnoreCase(CommonConstant.ALL)) {
					predicate.add(cb.equal(root.get("claimStatus"), type));
				}
				if (startDate != null && !startDate.isEmpty()) {
					try {
						LocalDateTime startDateTime = LocalDateTime.parse(startDate + " 00:00:00",
								DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
						predicate.add(cb.greaterThanOrEqualTo(root.get("createdDate"), startDateTime));
					} catch (DateTimeParseException e) {
						System.err.println("Invalid startDate format: " + startDate + ". Skipping date filter.");
					}
				}
				if (endDate != null && !endDate.isEmpty()) {
					try {
						LocalDateTime endDateTime = LocalDateTime.parse(endDate + " 23:59:59",
								DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
						predicate.add(cb.lessThanOrEqualTo(root.get("createdDate"), endDateTime));
					} catch (DateTimeParseException e) {
						System.err.println("Invalid endDate format: " + endDate + ". Skipping date filter.");
					}
				}
				return cb.and(predicate.toArray(new jakarta.persistence.criteria.Predicate[0]));
			};
			Page<ClaimEntity> claimEntity = claimRepoSitory.findAll(spec, pagable);
			Page<SurrenderClaimDTO> mapClaimSurreResponse = policyMapper.mapClaimResponse(claimEntity);
			response.setData(mapClaimSurreResponse);
			response.setStatus(CommonConstant.SUCCESS);
		} catch (Exception e) {
			response.setStatus(CommonConstant.FAILURE);
			response.setErrorMessage("No Data found");
			e.getLocalizedMessage();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return response;

	}

	public ResponseEntity<SurrenderClaimDTO> submitSurrenderRequest(SurrenderClaimDTO request, String userCode) {
		ResponseEntity<SurrenderClaimDTO> response = new ResponseEntity<>();
		SurrenderClaimDTO surrenderClaimDTO = new SurrenderClaimDTO();
		switch (request.getAction()) {
		case CommonConstant.SURRENDER -> {
			SurrenderEntity surrenderEntity = new SurrenderEntity();
			surrenderEntity.setSurrRefNo(generateSurrenderRefNo());
			surrenderEntity.setCreatedBy(userCode);
			surrenderEntity.setDeletedFlag(CommonConstant.N);
			surrenderEntity.setCreatedDate(LocalDateTime.now());
			surrenderEntity.setSurrAmount(request.getSurrAmount());
			surrenderEntity.setSurrDate(LocalDateTime.now());
			surrenderEntity.setSurrenderBy(request.getSurrenderBy());
			surrenderEntity.setSurrenderReason(request.getSurrReason());
			surrenderEntity.setSurrenderStatus(CommonConstant.IN_PROGRESS);

			// surrenderEntity.setVerificationComment(userCode);
			// surrenderEntity.setPayments(null);
//			surrenderEntity.setUpdatedBy(userCode);
//			surrenderEntity.setUpdatedDate(null);

			Bank byAccountNo = bankAccountRepository.findByAccountNo(request.getBankAccNo(), CommonConstant.N);
			if (byAccountNo != null) {
				surrenderEntity.setBank(byAccountNo);
			}
			Customer byCustomerNoNew = customerRepository.findByCustomerNoNew(request.getCustomerNo(),
					CommonConstant.N);
			if (byCustomerNoNew != null) {
				surrenderEntity.setCustomer(byCustomerNoNew);
			}
			if (request.getOtherSupportingDocument() != null) {
				byte[] fileBytes = Base64.getDecoder().decode(request.getOtherSupportingDocument());
				surrenderEntity.setOtherSupportingDocument(fileBytes);
				surrenderEntity.setOtherSupportingDocumentName(CommonConstant.OTH_DOC);
				surrenderEntity.setOtherSupportingDocumentVerificationStatus(CommonConstant.IN_PROGRESS);
			}
			if (request.getVerificationDocument() != null) {
				byte[] fileBytes = Base64.getDecoder().decode(request.getVerificationDocument());
				surrenderEntity.setVerificationDocument(fileBytes);
				surrenderEntity.setVerificationDocumentName(CommonConstant.ID_PROOF);
				surrenderEntity.setVerificationStatus(CommonConstant.IN_PROGRESS);
			}
			if (request.getBankPassbookDocument() != null) {
				byte[] fileBytes = Base64.getDecoder().decode(request.getBankPassbookDocument());
				surrenderEntity.setBankDocument(fileBytes);
				surrenderEntity.setBankDocumentName(CommonConstant.BANK_PROOF);
				surrenderEntity.setBankDocumentStatus(CommonConstant.IN_PROGRESS);
			}

			Policy byPolicyNum = policyRepository.findByPolicyNum(request.getPolicyNo(), CommonConstant.N);
			if (byPolicyNum != null) {
				surrenderEntity.setPolicy(byPolicyNum);
			}

			SurrenderEntity save = surrenderRepoSitory.save(surrenderEntity);
			if (save != null) {
				logger.info("{}", save.getSurrRefNo());
				surrenderClaimDTO.setSurrenderRefNo(save.getSurrRefNo());
				surrenderClaimDTO.setSurrenderStatus(save.getSurrenderStatus());
				response.setData(surrenderClaimDTO);
				response.setStatus(CommonConstant.SUCCESS);
			} else {
				response.setStatus(CommonConstant.FAILURE);
			}
		}
		case CommonConstant.CANCEL -> {
			if (request.getSurrenderRefNo() != null) {
				SurrenderEntity surrender = surrenderRepoSitory.findBySurrenderRefNo(request.getSurrenderRefNo(),
						CommonConstant.N);
				surrender.setSurrenderStatus(request.getSurrenderStatus());
				surrender.setSurrenderReason(request.getSurrReason());
				surrender.setUpdatedBy(userCode);
				surrender.setVerificationComment("Cancelled By user");
				surrender.setVerificationStatus(CommonConstant.CANCELED);
				surrender.setUpdatedDate(LocalDateTime.now());
				SurrenderEntity save = surrenderRepoSitory.save(surrender);
				if (save != null) {
					logger.info("{}", save.getSurrRefNo());
					surrenderClaimDTO.setSurrenderRefNo(save.getSurrRefNo());
					surrenderClaimDTO.setSurrenderStatus(save.getSurrenderStatus());
					response.setData(surrenderClaimDTO);
					response.setStatus(CommonConstant.SUCCESS);
				} else {
					response.setStatus(CommonConstant.FAILURE);
				}
			}

		}
		case CommonConstant.CLAIM -> {
			ClaimEntity surrenderEntity = new ClaimEntity();
			surrenderEntity.setClaimRefNo(generateClaimRefNo());
			surrenderEntity.setCreatedBy(userCode);
			surrenderEntity.setDeletedFlag(CommonConstant.N);
			surrenderEntity.setCreatedDate(LocalDateTime.now());
			surrenderEntity.setClaimAmount(request.getClaimAmount());
			surrenderEntity.setClaimDate(LocalDateTime.now());
			surrenderEntity.setClaimBy(request.getClaimBy());
			surrenderEntity.setClaimReason(request.getClaimReason());
			surrenderEntity.setClaimStatus(CommonConstant.IN_PROGRESS);
			surrenderEntity.setClaimType(request.getClaimType());
			// surrenderEntity.setVerificationComment(userCode);
			// surrenderEntity.setPayments(null);
//			surrenderEntity.setUpdatedBy(userCode);
//			surrenderEntity.setUpdatedDate(null);

			Bank byAccountNo = bankAccountRepository.findByAccountNo(request.getBankAccNo(), CommonConstant.N);
			if (byAccountNo != null) {
				surrenderEntity.setBank(byAccountNo);
			}
			Customer byCustomerNoNew = customerRepository.findByCustomerNoNew(request.getCustomerNo(),
					CommonConstant.N);
			if (byCustomerNoNew != null) {
				surrenderEntity.setCustomer(byCustomerNoNew);
			}
			if (request.getOtherSupportingDocument() != null) {
				byte[] fileBytes = Base64.getDecoder().decode(request.getOtherSupportingDocument());
				surrenderEntity.setOtherSupportingDocument(fileBytes);
				surrenderEntity.setOtherSupportingDocumentName(CommonConstant.OTH_DOC_1);
				surrenderEntity.setOtherSupportingDocumentVerificationStatus(CommonConstant.IN_PROGRESS);
			}
			if (request.getBankPassbookDocument() != null) {
				byte[] fileBytes = Base64.getDecoder().decode(request.getBankPassbookDocument());
				surrenderEntity.setBankDocument(fileBytes);
				;
				surrenderEntity.setBankDocumentName(CommonConstant.BANK_PROOF);
				surrenderEntity.setBankDocumentStatus(CommonConstant.IN_PROGRESS);
			}
			if (request.getIdProofDocument() != null) {
				byte[] fileBytes = Base64.getDecoder().decode(request.getIdProofDocument());
				surrenderEntity.setIdDocument(fileBytes);
				surrenderEntity.setIdDocumentName(CommonConstant.ID_PROOF);
				surrenderEntity.setIdDocumentStatus(CommonConstant.IN_PROGRESS);
			}
			if (request.getVerificationDocument() != null) {
				byte[] fileBytes = Base64.getDecoder().decode(request.getVerificationDocument());
				surrenderEntity.setVerificationDocument(fileBytes);
				surrenderEntity.setVerificationDocumentName(CommonConstant.OTH_DOC);
				surrenderEntity.setVerificationStatus(CommonConstant.IN_PROGRESS);
			}

			Policy byPolicyNum = policyRepository.findByPolicyNum(request.getPolicyNo(), CommonConstant.N);
			if (byPolicyNum != null) {
				surrenderEntity.setPolicy(byPolicyNum);
			}

			ClaimEntity save = claimRepoSitory.save(surrenderEntity);
			if (save != null) {
				logger.info("{}", save.getClaimRefNo());
				surrenderClaimDTO.setSurrenderRefNo(save.getClaimRefNo());
				surrenderClaimDTO.setSurrenderStatus(save.getClaimStatus());
				response.setData(surrenderClaimDTO);
				response.setStatus(CommonConstant.SUCCESS);
			} else {
				response.setStatus(CommonConstant.FAILURE);
			}

		}
		case CommonConstant.UPDATE -> {
			if (request.getSurrenderRefNo() != null) {
				SurrenderEntity surrender = surrenderRepoSitory.findBySurrenderRefNo(request.getSurrenderRefNo(),
						CommonConstant.N);
				surrender.setSurrenderStatus(request.getSurrenderStatus());
				surrender.setSurrenderReason(request.getSurrReason());
				surrender.setUpdatedBy(request.getUpdatedBy());
				if (request.getVerificationComment() != null) {
					surrender.setVerificationComment(request.getVerificationComment());
				} else {
					surrender.setVerificationComment(CommonConstant.APPROVED);
				}
				surrender.setUpdatedDate(LocalDateTime.now());
				SurrenderEntity save = surrenderRepoSitory.save(surrender);
				if (save != null) {
					logger.info("{}", save.getSurrRefNo());
					surrenderClaimDTO.setSurrenderRefNo(save.getSurrRefNo());
					surrenderClaimDTO.setSurrenderStatus(save.getSurrenderStatus());
					response.setData(surrenderClaimDTO);
					response.setStatus(CommonConstant.SUCCESS);
				} else {
					response.setStatus(CommonConstant.FAILURE);
				}
			} else if (request.getClaimRefNo() != null) {
				ClaimEntity surrender = claimRepoSitory.findByClaimRefNo(request.getClaimRefNo(), CommonConstant.N);
				surrender.setClaimStatus(request.getClaimStatus());
				surrender.setClaimReason(request.getClaimReason());
				surrender.setUpdatedBy(request.getUpdatedBy());
				if (request.getVerificationComment() != null) {
					surrender.setVerificationComment(request.getVerificationComment());
				} else {
					surrender.setVerificationComment(CommonConstant.APPROVED);
				}
				surrender.setUpdatedDate(LocalDateTime.now());
				ClaimEntity save = claimRepoSitory.save(surrender);
				if (save != null) {
					logger.info("{}", save.getClaimRefNo());
					surrenderClaimDTO.setClaimRefNo(save.getClaimRefNo());
					surrenderClaimDTO.setClaimStatus(save.getClaimStatus());
					response.setData(surrenderClaimDTO);
					response.setStatus(CommonConstant.SUCCESS);
				} else {
					response.setStatus(CommonConstant.FAILURE);
				}
			}
		}
		case CommonConstant.UPDATE_DOCUMENT_STATUS -> {
			if (request.getSurrenderRefNo() != null) {
				SurrenderEntity surrender = surrenderRepoSitory.findBySurrenderRefNo(request.getSurrenderRefNo(),
						CommonConstant.N);
				switch (request.getDocumentType()) {
				case CommonConstant.BANK_PROOF -> {
					surrender.setBankDocumentStatus(request.getDocumentStatus());
				}
				case CommonConstant.ID_PROOF -> {
					surrender.setVerificationStatus(request.getDocumentStatus());
				}
				case CommonConstant.OTH_DOC -> {
					surrender.setOtherSupportingDocumentVerificationStatus(request.getDocumentStatus());
				}
				}
				surrender.setUpdatedDate(LocalDateTime.now());
				surrender.setUpdatedBy(request.getUpdatedBy());
				SurrenderEntity save = surrenderRepoSitory.save(surrender);
				if (save != null) {
					logger.info("{}", save.getSurrRefNo());
					surrenderClaimDTO.setSurrenderRefNo(save.getSurrRefNo());
					surrenderClaimDTO.setSurrenderStatus(save.getSurrenderStatus());
					response.setData(surrenderClaimDTO);
					response.setStatus(CommonConstant.SUCCESS);
				} else {
					response.setStatus(CommonConstant.FAILURE);
				}
			} else if (request.getClaimRefNo() != null) {
				ClaimEntity surrender = claimRepoSitory.findByClaimRefNo(request.getClaimRefNo(), CommonConstant.N);
				switch (request.getDocumentType()) {
				case CommonConstant.BANK_PROOF -> {
					surrender.setBankDocumentStatus(request.getDocumentStatus());
				}
				case CommonConstant.ID_PROOF -> {
					surrender.setIdDocumentStatus(request.getDocumentStatus());
				}
				case CommonConstant.OTH_DOC -> {
					surrender.setVerificationStatus(request.getDocumentStatus());
				}
				case CommonConstant.OTH_DOC_1 -> {
					surrender.setOtherSupportingDocumentVerificationStatus(request.getDocumentStatus());
				}
				}
				surrender.setUpdatedDate(LocalDateTime.now());
				surrender.setUpdatedBy(request.getUpdatedBy());
				ClaimEntity save = claimRepoSitory.save(surrender);
				if (save != null) {
					logger.info("{}", save.getClaimRefNo());
					surrenderClaimDTO.setClaimRefNo(save.getClaimRefNo());
					surrenderClaimDTO.setClaimStatus(save.getClaimStatus());
					response.setData(surrenderClaimDTO);
					response.setStatus(CommonConstant.SUCCESS);
				} else {
					response.setStatus(CommonConstant.FAILURE);
				}
			}
		}
		}
		return response;
	}

	public String generateSurrenderRefNo() {
		String prefix = "SURR/";
		int currentYear = LocalDate.now().getYear();
		String latestComplaintNumber = surrenderRepoSitory.findLatestByNotesId();
		int nextNumber = 1;
		if (latestComplaintNumber != null && latestComplaintNumber.startsWith(prefix)
				&& latestComplaintNumber.endsWith("/" + currentYear)) {
			String numberPart = latestComplaintNumber.replace(prefix, "").replace("/" + currentYear, "");
			try {
				nextNumber = Integer.parseInt(numberPart) + 1;
			} catch (NumberFormatException e) {
			}
		}
		return String.format("%s%06d/%d", prefix, nextNumber, currentYear);
	}

	public String generateClaimRefNo() {
		String prefix = "CLAIM/";
		int currentYear = LocalDate.now().getYear();
		String latestComplaintNumber = claimRepoSitory.findLatestByNotesId();
		int nextNumber = 1;
		if (latestComplaintNumber != null && latestComplaintNumber.startsWith(prefix)
				&& latestComplaintNumber.endsWith("/" + currentYear)) {
			String numberPart = latestComplaintNumber.replace(prefix, "").replace("/" + currentYear, "");
			try {
				nextNumber = Integer.parseInt(numberPart) + 1;
			} catch (NumberFormatException e) {
			}
		}
		return String.format("%s%06d/%d", prefix, nextNumber, currentYear);
	}

	public ResponseEntity<DashboardStats> getSurrenderCount(String customerNo, String userCode) {
		ResponseEntity<DashboardStats> response = new ResponseEntity<DashboardStats>();
		List<SurrenderEntity> surrender = null;
		List<ClaimEntity> claim = null;
		DashboardStats dashboardStats = new DashboardStats();
		if (customerNo != null) {
			surrender = surrenderRepoSitory.findBuCustomerNo(customerNo, CommonConstant.N);
			claim = claimRepoSitory.findBuCustomerNo(customerNo, CommonConstant.N);
		} else {
			surrender = surrenderRepoSitory.findAll();
			claim = claimRepoSitory.findAll();
		}
		if (surrender != null) {
			dashboardStats
					.setAllSurrender(surrender.stream().filter(surr -> surr.getSurrenderStatus() != null).count());
			dashboardStats
					.setSurrApproved(
							surrender.stream()
									.filter(surr -> surr.getSurrenderStatus() != null
											&& surr.getSurrenderStatus().equalsIgnoreCase(CommonConstant.APPROVED))
									.count());
			dashboardStats
					.setSurrInProgress(
							surrender.stream()
									.filter(surr -> surr.getSurrenderStatus() != null
											&& surr.getSurrenderStatus().equalsIgnoreCase(CommonConstant.IN_PROGRESS))
									.count());
			dashboardStats
					.setSurrRejected(
							surrender.stream()
									.filter(surr -> surr.getSurrenderStatus() != null
											&& surr.getSurrenderStatus().equalsIgnoreCase(CommonConstant.REJECTED))
									.count());

		}
		if (claim != null) {
			dashboardStats.setAllClaim(claim.stream().filter(surr -> surr.getClaimStatus() != null).count());
			dashboardStats
					.setClaimApproved(
							claim.stream()
									.filter(surr -> surr.getClaimStatus() != null
											&& surr.getClaimStatus().equalsIgnoreCase(CommonConstant.APPROVED))
									.count());
			dashboardStats
					.setClaimInProgress(
							claim.stream()
									.filter(surr -> surr.getClaimStatus() != null
											&& surr.getClaimStatus().equalsIgnoreCase(CommonConstant.IN_PROGRESS))
									.count());
			dashboardStats
					.setClaimRejected(
							claim.stream()
									.filter(surr -> surr.getClaimStatus() != null
											&& surr.getClaimStatus().equalsIgnoreCase(CommonConstant.REJECTED))
									.count());
		}
		response.setData(dashboardStats);
		response.setStatus(CommonConstant.SUCCESS);
		return response;
	}

	private LocalDate calculateNextDueDate(LocalDate startDate, String frequency, int installmentNumber) {
		switch (frequency.toUpperCase()) {
		case "DAILY":
			return startDate.plusDays(installmentNumber);
		case "MONTHLY":
			return startDate.plusMonths(installmentNumber);
		case "QUARTERLY":
			return startDate.plusMonths(3L * installmentNumber);
		case "YEARLY":
			return startDate.plusYears(installmentNumber);
		default:
			throw new IllegalArgumentException("Invalid frequency: " + frequency);
		}
	}

	public ResponseEntity<Page<PolicyDTO>> getPolicyDetailsForCustomer(String policyNo, String customerNo,
			String allPol, String workItemRefNo, String status, String type, String startDate, String endDate,
			String bankAccountNo, String userCode, int page, int size) {
		ResponseEntity<Page<PolicyDTO>> resp = new ResponseEntity<>();
        Pageable pageable= PageRequest.of(page, size, Sort.by("createdDate").ascending());
        Specification<Policy> spec = (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (workItemRefNo != null && !workItemRefNo.isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("workitems").get("workItemRefNumber")),
						"%" + workItemRefNo.toLowerCase() + "%"));
			}
			if (policyNo != null && !policyNo.isEmpty()) {
				predicates.add(cb.equal(root.get("policyNumber"), policyNo));
			}
			if (customerNo != null && !customerNo.isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("customer").get("customerNo")),
						"%" + customerNo.toLowerCase() + "%"));
			}
			if (type != null && !type.isEmpty()) {
				predicates.add(cb.equal(root.get("policyType"), type));
			}
			if (bankAccountNo != null && !bankAccountNo.isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("bankAccounts").get("accountNo")),
						"%" + bankAccountNo.toLowerCase() + "%"));
			}
			if (status != null && !status.isEmpty()) {
				predicates.add(cb.equal(root.get("policyStatus"), status));
			}
			predicates.add(cb.equal(root.get("deletedFlag"), "N"));
			if (startDate != null && !startDate.isEmpty()) {
				try {
					LocalDateTime startDateTime = LocalDateTime.parse(startDate + " 00:00:00",
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					predicates.add(cb.greaterThanOrEqualTo(root.get("createdTime"), startDateTime));
				} catch (DateTimeParseException e) {
					System.err.println("Invalid startDate format: " + startDate + ". Skipping date filter.");
				}
			}

			if (endDate != null && !endDate.isEmpty()) {
				try {
					LocalDateTime endDateTime = LocalDateTime.parse(endDate + " 23:59:59",
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					predicates.add(cb.lessThanOrEqualTo(root.get("createdTime"), endDateTime));
				} catch (DateTimeParseException e) {
					System.err.println("Invalid endDate format: " + endDate + ". Skipping date filter.");
				}
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
		Page<Policy> allPolicies = policyRepository.findAll(spec,pageable);
	
		Page<PolicyDTO> response = policyMapper.mapPolicyForCustomer(allPolicies);

		resp.setData(response);
		resp.setStatus(CommonConstant.SUCCESS);
		return resp;
	
	}

}
