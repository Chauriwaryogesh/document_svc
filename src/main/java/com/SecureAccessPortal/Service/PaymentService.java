package com.SecureAccessPortal.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Entity.Bank;
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
	    Payments payment= new Payments();

	    boolean isSurrenderOrClaim = paymentId.contains("SURR") || paymentId.contains("CLAIM");
   try {
	    if (isSurrenderOrClaim) {
	        // Create new payment entries for Surrender or Claim
	        payment = createPaymentEntries(request);
	    } else {
	        // Fetch existing installment payment
	        payment = paymentsRepository.findById(paymentId)
	                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));

	        // Prevent double payment
	        if ("Paid".equals(payment.getStatus())) {
	            throw new IllegalStateException("Payment is already done: " + paymentId);
	        }

	        // Simulate payment gateway
	        String mockResponse = simulatePaymentGateway();
	        boolean paymentSuccess;
	        try {
	            Map<String, String> responseMap = objectMapper.readValue(mockResponse, Map.class);
	            paymentSuccess = "success".equalsIgnoreCase(responseMap.get("status"));
	        } catch (Exception e) {
	            throw new RuntimeException("Failed to parse payment gateway response: " + mockResponse, e);
	        }

	        // Map common PaymentDetails fields from request
	        payment = mapCommonPaymentDetails(request, payment);

	        // Update payment status and transaction info
	        if (paymentSuccess) {
	            payment.setStatus(CommonConstant.PAID);
	            payment.setTransactionId(generateTransactionId());
	            payment.setPaymentDate(LocalDateTime.now());
	            payment.setPaymentMethod(paymentMethod);
	            payment.setPaymentType(CommonConstant.POL_INSTLMNT); // Installment
	            payment.setEmailStatus("Service Not Available Now");
	            sendPaymentConfirmationEmail(payment);
	        } else {
	            payment.setStatus(CommonConstant.FAILED);
	            payment.setPaymentDate(LocalDateTime.now());
	            payment.setEmailStatus("NotSent");
	        }

	        // Update audit fields
	        payment.setUpdatedBy(userCode);
	        payment.setUpdatedTime(LocalDateTime.now());

	        // Update total amount if provided
	        if (request.getPaymentAmount() != null) {
	            payment.setTotalAmountPaid(request.getPaymentAmount());
	        }

	        // Save updated payment
	        payment = paymentsRepository.save(payment);
	    }
}catch(Exception e) {
	e.printStackTrace();
}
	    return payment;
	}

	private Payments createPaymentEntries(PaymentRequest request) {
	    Payments payment = new Payments();
	    payment.setPaymentId(request.getPaymentId());
	    payment.setPaymentType(request.getPaymentType());

	    // Map common details
	    payment = mapCommonPaymentDetails(request, payment);

	    // Simulate payment gateway
	    String mockResponse = simulatePaymentGateway();
	    boolean paymentSuccess;
	    try {
	        Map<String, String> responseMap = objectMapper.readValue(mockResponse, Map.class);
	        paymentSuccess = "success".equalsIgnoreCase(responseMap.get("status"));
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to parse payment gateway response: " + mockResponse, e);
	    }

	    // Set payment status and transaction info
	    if (paymentSuccess) {
	        payment.setStatus(CommonConstant.PAID);
	        payment.setTransactionId(generateTransactionId());
	        payment.setPaymentDate(LocalDateTime.now());
	        payment.setPaymentMethod(request.getPaymentMethod());
	        payment.setEmailStatus("Service Not Available Now");
	        sendPaymentConfirmationEmail(payment);
	    } else {
	        payment.setStatus(CommonConstant.FAILED);
	        payment.setPaymentDate(LocalDateTime.now());
	        payment.setEmailStatus("NotSent");
	    }

	    // Audit fields
	    payment.setCreatedBy(request.getUserCode());
	    payment.setUpdatedBy(request.getUserCode());
	    payment.setCreatedTime(LocalDateTime.now());
	    payment.setUpdatedTime(LocalDateTime.now());
	    if (request.getPaymentAmount() != null) {
	        payment.setTotalAmountPaid(request.getPaymentAmount());
	    }

	    // Save payment
	    Payments paymentSaved = paymentsRepository.save(payment);

	    // Map work item for tracking
	    workItemService.mapRequetforWorkItem(request.getUserCode(), payment.getPolicy(), payment.getCustomer(),
	            CommonConstant.PAYMENT, CommonConstant.POLICY_SURENDERRED,
	            "Policy has been Surrendered on " + LocalDateTime.now() + " and amount disbursed "
	                    + request.getPaymentAmount(),
	            null, null, paymentSaved, CommonConstant.APPROVED);

	    return paymentSaved;
	}

	private Payments mapCommonPaymentDetails(PaymentRequest request, Payments payment) {
	    // Map policy info
	    if (request.getPolicyNumber() != null) {
	        Policy byPolicyNum = policyRepository.findByPolicyNum(request.getPolicyNumber(), CommonConstant.N);
	        if (byPolicyNum != null) {
	            byPolicyNum.setUpdatedBy(request.getUserCode());
	            byPolicyNum.setUpdatedDate(LocalDateTime.now());
                payment.setPolicyAmount(byPolicyNum.getTotalAmount());
	            payment.setPolicy(byPolicyNum);
	            payment.setCustomer(byPolicyNum.getCustomer());
	            payment.setProductCode(byPolicyNum.getProductCode());
	            payment.setPolicyName(byPolicyNum.getPolicyName());
	            payment.setInstallmentAmount(byPolicyNum.getMonthlyInstallment());
payment.setPaymentAmount(request.getPaymentAmount());
	            // Do not override dueDate for existing installments
	            if (request.getPaymentType() != null &&
	                    (request.getPaymentType().equals(CommonConstant.SURRENDERED) ||
	                            request.getPaymentType().equals(CommonConstant.CLAIMED))) {
	                payment.setDueDate(LocalDateTime.now());
	            }
	        }
	    }

	    // Map bank info
	    if (request.getPaymentDetails() != null && request.getPaymentDetails().getBankaccount() != null) {
	        Bank bankAccounts = bankAccountRepository.findByAccountNo(
	                request.getPaymentDetails().getBankaccount(), CommonConstant.N);
	        payment.setBank(bankAccounts);
	    }

	    // Map payment details
	    if (request.getPaymentDetails() != null) {
	        payment.setCardType(request.getPaymentDetails().getCardType());
	        payment.setUpiId(request.getPaymentDetails().getUpiId());
	        payment.setCardNumber(request.getPaymentDetails().getCardNumber());
	        payment.setCvv(request.getPaymentDetails().getCvv());
	        if (request.getPaymentDetails().getExpiryDate() != null) {
	            String expiryDateStr = request.getPaymentDetails().getExpiryDate();
	            DateTimeFormatter formatter;
	            try {
	                payment.setExpiryDate(LocalDateTime.parse(expiryDateStr));
	            } catch (DateTimeParseException e1) {
	                try {
	                    LocalDate date = LocalDate.parse(expiryDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	                    payment.setExpiryDate(date.atStartOfDay());
	                } catch (DateTimeParseException e2) {
	                    try {
	                        LocalDate date = LocalDate.parse(expiryDateStr, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
	                        payment.setExpiryDate(date.atStartOfDay());
	                    } catch (DateTimeParseException e3) {
	                        throw new RuntimeException("Invalid expiry date format: " + expiryDateStr);
	                    }
	                }
	            }
	        }
	        payment.setOtp(request.getPaymentDetails().getOtp()); // Should be Integer in entity
	        payment.setChequeNumber(request.getPaymentDetails().getChequeNumber());
	    }

	    return payment;
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

	public List<Payments> findHistoryOfPaymentsWithoutPagination(String policyNumber, String customerNumber,
	        String paymentId, String transactionId, String status, String userCode) {

	    Specification<Payments> spec = Specification.where(null);

	    if (paymentId != null && !paymentId.isEmpty()) {
	        Optional<Payments> payment = paymentsRepository.findById(paymentId);
	        return payment.map(List::of).orElse(List.of());
	    }

	    if (transactionId != null && !transactionId.isEmpty()) {
	        Optional<Payments> payment = paymentsRepository.findByTransactionId(transactionId);
	        return payment.map(List::of).orElse(List.of());
	    }

	    if (policyNumber != null && !policyNumber.isEmpty()) {
	        spec = spec.and((root, query, cb) ->
	                cb.equal(root.get("policy").get("policyNumber"), policyNumber));
	    }

	    if (customerNumber != null && !customerNumber.isEmpty()) {
	        spec = spec.and((root, query, cb) ->
	                cb.equal(root.get("customer").get("customerNo"), customerNumber));
	    }

	    if (status != null && !status.isEmpty()) {
	        spec = spec.and((root, query, cb) ->
	                cb.equal(root.get("status"), status));
	    }

	    return paymentsRepository.findAll(spec, Sort.by("createdTime").descending());
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