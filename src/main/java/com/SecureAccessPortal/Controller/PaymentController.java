package com.SecureAccessPortal.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Entity.Payments;
import com.SecureAccessPortal.Modal.PaymentRequest;
import com.SecureAccessPortal.Modal.PaymentResponse;
import com.SecureAccessPortal.Service.PaymentService;
import com.SecureAccessPortal.Service.ResponseEntity;

@RestController
@RequestMapping("/Payments")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@PostMapping("/process")
	public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest request) {
		ResponseEntity responseEntity = new ResponseEntity<>();
		try {
			Payments payment = paymentService.processPayment(request.getPaymentId(), request.getPaymentMethod(),
					request.getUserCode());
			PaymentResponse response = new PaymentResponse();
			response.setPaymentId(payment.getPaymentId());
			response.setTransactionId(payment.getTransactionId());
			response.setStatus(payment.getStatus());
			response.setEmailStatus(payment.getEmailStatus());
			response.setMessage("Payment processed successfully");
			response.setResponseStatus(CommonConstant.SUCCESS);
			responseEntity.setStatus(CommonConstant.SUCCESS);
			responseEntity.setData(response);
		} catch (Exception e) {
			responseEntity.setStatus(CommonConstant.FAILURE);
			responseEntity.setErrorMessage("Payment has been failed");
		}
		return responseEntity;
	}

	@GetMapping("/history")
	public ResponseEntity<List<PaymentResponse>> getPayment(
			@RequestParam(value = "policyNumber", required = false) String policyNumber,
			@RequestParam(value = "customerNumber", required = false) String customerNumber,
			@RequestParam(value = "paymentId", required = false) String paymentId,
			@RequestParam(value = "transactionId", required = false) String transactionId,
			@RequestHeader(value = "userCode", required = false) String userCode) {
		ResponseEntity<List<PaymentResponse>> responseList = new ResponseEntity<List<PaymentResponse>>();

		List<Payments> payments = paymentService.findHistoryOfPayments(policyNumber, customerNumber, paymentId,
				transactionId);
		List<PaymentResponse> response = payments.stream().map(p -> {
			PaymentResponse pr = new PaymentResponse();
			pr.setPaymentId(p.getPaymentId());
			pr.setPolicyNumber(p.getPolicy().getPolicyNumber());
			pr.setCustomerNumber(p.getCustomer().getCustomerNo());
			pr.setPolicyName(p.getPolicyName());
			pr.setInstallmentAmount(p.getInstallmentAmount());
			pr.setDueDate(p.getDueDate());
			pr.setPaymentDate(p.getPaymentDate());
			pr.setStatus(p.getStatus());
			pr.setTransactionId(p.getTransactionId());
			pr.setEmailStatus(p.getEmailStatus());
			pr.setResponseStatus(CommonConstant.SUCCESS);
			return pr;
		}).collect(Collectors.toList());
		responseList.setData(response);
		return responseList;
	}
}
