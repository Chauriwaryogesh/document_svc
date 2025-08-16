package com.SecureAccessPortal.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Entity.Payments;
import com.SecureAccessPortal.Modal.DashboardStats;
import com.SecureAccessPortal.Modal.PaymentList;
import com.SecureAccessPortal.Modal.PaymentRequest;
import com.SecureAccessPortal.Modal.PaymentResponse;
import com.SecureAccessPortal.Service.PaymentService;
import com.SecureAccessPortal.Service.ResponseEntity;

@RestController
@RequestMapping("/Payments")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@PostMapping(value = "/process", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest request) {
		ResponseEntity responseEntity = new ResponseEntity<>();
		try {
			Payments payment = paymentService.processPayment(request,request.getPaymentId(), request.getPaymentMethod(),
					request.getUserCode());
			PaymentList response = new PaymentList();
			response.setPaymentDate(payment.getPaymentDate());
			response.setBankAccNo(payment.getBankAccount().getAccountNo());
			response.setTotalAmount(String.valueOf(payment.getTotalAmountPaid()));
			response.setPaymentMethod(payment.getPaymentMethod());
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
			responseEntity.setErrorMessage("Payment Already Done");
		}
		return responseEntity;
	}

	@GetMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<PaymentResponse>> getPayment(
			@RequestParam(value = "policyNumber", required = false) String policyNumber,
			@RequestParam(value = "customerNumber", required = false) String customerNumber,
			@RequestParam(value = "paymentId", required = false) String paymentId,
			@RequestParam(value = "transactionId", required = false) String transactionId,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "100") int size,
			@RequestParam(value = "status", required = false) String status,
			@RequestHeader(value = "userCode", required = false) String userCode) {
		ResponseEntity<Page<PaymentResponse>> responseList = new ResponseEntity<>();
		Page<Payments> paymentsPage = paymentService.findHistoryOfPayments(policyNumber, customerNumber, paymentId,
				transactionId, page, size, status, userCode);
		Map<String, List<Payments>> groupedByPolicy = paymentsPage.getContent().stream()
				.collect(Collectors.groupingBy(p -> p.getPolicy().getPolicyNumber()));

		// Convert to Page of PaymentResponse
		List<PaymentResponse> response = groupedByPolicy.entrySet().stream().map(entry -> {
			PaymentResponse pr = new PaymentResponse();
			pr.setResponseStatus(CommonConstant.SUCCESS);

			// Get first payment to set policy details
			Payments firstPayment = entry.getValue().get(0);
			pr.setPolicyNumber(entry.getKey());
			pr.setCustomerNumber(firstPayment.getCustomer().getCustomerNo());
			pr.setEmail(firstPayment.getCustomer().getEmail());
			pr.setPolicyName(firstPayment.getPolicyName());
			pr.setPolicyType(firstPayment.getPolicy().getPolicyType());

			// Map all payments to PaymentList
			List<PaymentList> paymentList = entry.getValue().stream().map(payment -> {
				PaymentList pl = new PaymentList();
				pl.setPaymentDate(payment.getPaymentDate());
				pl.setBankAccNo(payment.getBankAccount().getAccountNo());
				pl.setTotalAmount(String.valueOf(payment.getTotalAmountPaid()));
				pl.setPaymentMethod(payment.getPaymentMethod());
				pl.setPaymentId(payment.getPaymentId());
				pl.setTransactionId(payment.getTransactionId());
				pl.setStatus(payment.getStatus());
				pl.setEmailStatus(payment.getEmailStatus());
				pl.setMessage("Payment processed successfully");
				pl.setResponseStatus(CommonConstant.SUCCESS);
				return pl;
			})
					// .sorted((p1, p2) -> p1.getDueDate().compareTo(p2.getDueDate())) // Sort by
					// due date
					.collect(Collectors.toList());
			pr.setPaymentList(paymentList);
			return pr;
		}).collect(Collectors.toList());
		Page<PaymentResponse> responsePage = new PageImpl<>(response,
				PageRequest.of(paymentsPage.getNumber(), paymentsPage.getSize(), paymentsPage.getSort()),
				paymentsPage.getTotalElements() // THIS IS THE CRITICAL PART: Use the totalElements from the original
												// Page
		);
		responseList.setData(responsePage);
		return responseList;
	}

	@RequestMapping(value = "/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DashboardStats> paymentCount(
			@RequestHeader(value = "userCode", required = false) String userCode) {
		ResponseEntity<DashboardStats> response = new ResponseEntity<>();
		response = paymentService.fetchAllcounts(userCode);
		return response;

	}
}
