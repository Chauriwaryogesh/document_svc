package com.SecureAccessPortal.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Entity.BankAccount;
import com.SecureAccessPortal.Entity.Payments;
import com.SecureAccessPortal.Entity.Policy;
import com.SecureAccessPortal.Modal.DashboardStats;
import com.SecureAccessPortal.Modal.PaymentRequest;
import com.SecureAccessPortal.Repo.BankAccountRepo;
import com.SecureAccessPortal.Repo.IPolicyRepo;
import com.SecureAccessPortal.Repo.PaymentsRepo;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class PaymentService {
	
	private static final Logger logger = LoggerFactory.getLogger(PolicyService.class);


	@Autowired
	private PaymentsRepo paymentsRepository;
	
	@Autowired
	private IPolicyRepo policyRepository;

	@Autowired
	private Environment env;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private IWorkItemService workItemService;
	
	@Autowired
	private BankAccountRepo bankAccountRepository;

	@Transactional
	public Payments processPayment(PaymentRequest request, String paymentId, String paymentMethod, String userCode) {
		Payments payment = new Payments();
		if (paymentId.contains("SURR") || paymentId.contains("CLAIM") ) {
//		 Optional<Payments> byPaymentIs = paymentsRepository.findByPaymentIs(paymentId);
//			if (byPaymentIs == null) {
//				logger.error("payment not found");
////						.orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));
////						if ("Paid".equals(payment.getStatus())) {
////							throw new IllegalStateException("Payment is already done: " + paymentId);
////						}	
//			}
			
			payment = createPaymentEntries(request);
		} else {
			payment = paymentsRepository.findById(paymentId)
					.orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));
			if ("Paid".equals(payment.getStatus())) {
				throw new IllegalStateException("Payment is already done: " + paymentId);
			}
			String mockResponse = simulatePaymentGateway();
			boolean paymentSuccess;
			try {
				// Parse JSON response
				Map<String, String> responseMap = objectMapper.readValue(mockResponse, Map.class);
				paymentSuccess = "success".equalsIgnoreCase(responseMap.get("status"));
			} catch (Exception e) {
				throw new RuntimeException("Failed to parse payment gateway response: " + mockResponse, e);
			}

			if (paymentSuccess) {
				payment.setStatus("Paid");
				payment.setTransactionId(generateTransactionId());
				payment.setPaymentDate(LocalDate.now());
				payment.setPaymentMethod(paymentMethod);
				payment.setPaymentType(CommonConstant.POL_INSTLMNT);
				payment.setEmailStatus("Service Not Available Now");
				sendPaymentConfirmationEmail(payment);
			} else {
				payment.setStatus("Failed");
				payment.setPaymentDate(LocalDate.now());
				payment.setEmailStatus("NotSent");
			}

			payment.setUpdatedBy(userCode);
			payment.setUpdatedTime(LocalDateTime.now());
			 payment = paymentsRepository.save(payment);
		}

		return payment;
	}

	private Payments createPaymentEntries(PaymentRequest request) {
		Payments payment = new Payments();
		Policy byPolicyNum = new Policy();
		payment.setPaymentId(request.getPaymentId());
		if (request.getPolicyNumber() != null) {
			byPolicyNum = policyRepository.findByPolicyNum(request.getPolicyNumber(), CommonConstant.N);
			if(byPolicyNum != null) {
				byPolicyNum.setUpdatedBy(request.getUserCode());
				byPolicyNum.setUpdatedTime(LocalDateTime.now());
				if (request.getPaymentId().contains("SURR")) {
					byPolicyNum.setPolicyStatus(CommonConstant.SURRENDERED);
				}
				if(	request.getPaymentId().contains("CLAIM")) {
					byPolicyNum.setPolicyStatus(CommonConstant.CLAIMED);
				}
				byPolicyNum=policyRepository.save(byPolicyNum);
			}
			payment.setPolicy(byPolicyNum);
			payment.setCustomer(byPolicyNum.getCustomer());
			payment.setProductCode(byPolicyNum.getProductCode());
			payment.setPolicyName(byPolicyNum.getPolicyName());
			payment.setInstallmentAmount(BigDecimal.valueOf(byPolicyNum.getTotalAmount()));
			payment.setDueDate(LocalDate.now());
		}

		if (request.getPaymentDetails().getBankaccount() != null) {
			BankAccount bankAccounts = bankAccountRepository
					.findByAccountNo(request.getPaymentDetails().getBankaccount(), CommonConstant.N);
			payment.setBankAccount(bankAccounts);
		}
        payment.setPaymentType(request.getPaymentType());
		payment.setInstallmentCount(0);
		payment.setTotalInstallments(0);
		String mockResponse = simulatePaymentGateway();
		boolean paymentSuccess;
		try {
			// Parse JSON response
			Map<String, String> responseMap = objectMapper.readValue(mockResponse, Map.class);
			paymentSuccess = "success".equalsIgnoreCase(responseMap.get("status"));
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse payment gateway response: " + mockResponse, e);
		}
		if (paymentSuccess) {
			payment.setStatus(CommonConstant.PAID);
			payment.setTransactionId(generateTransactionId());
			payment.setPaymentDate(LocalDate.now());
			payment.setPaymentMethod(request.getPaymentMethod());
			payment.setEmailStatus("Service Not Available Now");
			sendPaymentConfirmationEmail(payment);
		} else {
			payment.setStatus(CommonConstant.FAILED);
			payment.setPaymentDate(LocalDate.now());
			payment.setEmailStatus("NotSent");
		}
		payment.setCreatedBy(request.getUserCode());
		payment.setUpdatedBy(request.getUserCode());
		payment.setCreatedTime(LocalDateTime.now());
		payment.setUpdatedTime(LocalDateTime.now());
		payment.setTotalAmountPaid(new BigDecimal(request.getPaymentAmount()));
		Payments paymentList = paymentsRepository.save(payment);
		// Create work item
		String workType = CommonConstant.PAYMENT;
		String workItemName = CommonConstant.POLICY_SURENDERRED;
		String status = CommonConstant.APPROVED;
		String comment = "Policy has been Surrrendred on " + LocalDateTime.now() + "and amount disbursed "
				+ request.getPaymentAmount();
		workItemService.mapRequetforWorkItem(request.getUserCode(), byPolicyNum, byPolicyNum.getCustomer(), workType,
				workItemName, comment, null, null, paymentList, status);

		return paymentList;
	}

	private String simulatePaymentGateway() {
		boolean mockSuccess = Boolean.parseBoolean(env.getProperty("payment.gateway.mock.success", "true"));
		return mockSuccess ? "{\"status\": \"success\", \"message\": \"Payment processed successfully\"}"
				: "{\"status\": \"failed\", \"message\": \"Payment failed due to insufficient funds\"}";
	}

	private String generateTransactionId() {
		LocalDateTime now = LocalDateTime.now();
		return "TXN_" + now.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
	}

	@Async
	public void sendPaymentConfirmationEmail(Payments payment) {
		// Implement email sending logic (e.g., using JavaMailSender or message queue)
		System.out.println("Sending payment confirmation email for payment: " + payment.getPaymentId() + ", Policy: "
				+ payment.getPolicyName() + ", Amount: " + payment.getInstallmentAmount() + ", Transaction ID: "
				+ payment.getTransactionId() + ", Status: " + payment.getStatus());
	}

	public Page<Payments> findHistoryOfPayments(String policyNumber, String customerNumber, String paymentId,
			String transactionId, int page, int size, String status, String userCode) {
		Pageable pageable = PageRequest.of(page, size,Sort.by("createdTime").descending());
		if (policyNumber != null && !policyNumber.isEmpty()) {
			return paymentsRepository.findByPolicyPolicyNumber(policyNumber, pageable);
		} else if (customerNumber != null && !customerNumber.isEmpty()) {
			return paymentsRepository.findByCustomerCustomerNo(customerNumber, pageable);
		} else if (paymentId != null && !paymentId.isEmpty()) {
			Optional<Payments> payment = paymentsRepository.findById(paymentId);
			return payment.map(p -> new PageImpl<>(List.of(p), pageable, 1))
					.orElseGet(() -> new PageImpl<>(List.of(), pageable, 0));
		} else if (transactionId != null && !transactionId.isEmpty()) {
			Optional<Payments> payment = paymentsRepository.findByTransactionId(transactionId);
			return payment.map(p -> new PageImpl<>(List.of(p), pageable, 1))
					.orElseGet(() -> new PageImpl<>(List.of(), pageable, 0));
		} else if (status != null && !status.isEmpty()) {
			List<Payments> payments = paymentsRepository.findByStatus(status);
			System.out.println("Found " + payments.size() + " payments with status: " + status);
			return new PageImpl<>(payments, pageable, payments.size());
		} else {
			return paymentsRepository.findAll(pageable);
		}
	}

	public ResponseEntity<DashboardStats> fetchAllcounts(String userCode) {
		ResponseEntity<DashboardStats> response = new ResponseEntity<>();
		List<Payments> paymentList = paymentsRepository.findAll();

		// all.stream().filter(null)
		DashboardStats stats = new DashboardStats();
		stats.setTotalPolicies(20);
		stats.setTotalCustomers(10);
		stats.setTotalPayments(10);
		stats.setPaid(55);
		stats.setUnpaid(12);
		stats.setFailed(0);
		stats.setCancelled(0);
		stats.setPending(0);
		response.setData(stats);
		response.setStatus(CommonConstant.SUCCESS);
		return response;
	}
}