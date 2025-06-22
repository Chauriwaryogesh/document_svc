package com.SecureAccessPortal.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.SecureAccessPortal.Entity.Payments;
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

	public List<Payments> findHistoryOfPayments(String policyNumber, String customerNumber, String paymentId,
			String transactionId) {
		List<Payments> payments = new ArrayList<Payments>();
		if (policyNumber != null) {
			payments = paymentsRepository.findByPolicyPolicyNumber(policyNumber);
		} else if (customerNumber != null) {
			payments = paymentsRepository.findByCustomerNumber(customerNumber);
		} else if (paymentId != null) {
			Optional<Payments> payment = paymentsRepository.findById(paymentId);
			payments.add(payment.get());
		} else if (transactionId != null) {
			Optional<Payments> payment = paymentsRepository.findByTxnId(transactionId);
			payments.add(payment.get());
		}else {
			payments = paymentsRepository.findAll();
		}
		return payments;
	}
}