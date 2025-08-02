package com.SecureAccessPortal.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.SecureAccessPortal.Entity.Payments;
import com.SecureAccessPortal.Modal.DashboardStats;
import com.SecureAccessPortal.Repo.PaymentsRepo;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class PaymentService {

	@Autowired
	private PaymentsRepo paymentsRepository;

	@Autowired
	private Environment env;

	@Autowired
	private ObjectMapper objectMapper;

	@Transactional
	public Payments processPayment(String paymentId, String paymentMethod, String userCode) {
		Payments payment = paymentsRepository.findById(paymentId)
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
			payment.setEmailStatus("Service Not Available Now");
			sendPaymentConfirmationEmail(payment);
		} else {
			payment.setStatus("Failed");
			payment.setPaymentDate(LocalDate.now());
			payment.setEmailStatus("NotSent");
		}

		payment.setUpdatedBy(userCode);
		payment.setUpdatedTime(LocalDateTime.now());
		return paymentsRepository.save(payment);
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
		Pageable pageable = PageRequest.of(page, size);
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