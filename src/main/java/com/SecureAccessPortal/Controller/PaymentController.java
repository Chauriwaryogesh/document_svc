package com.SecureAccessPortal.Controller;

import java.util.Arrays;
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
import com.SecureAccessPortal.Modal.PaymentDetails;
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
		ResponseEntity<PaymentResponse> responseEntity = new ResponseEntity<>();
		PaymentResponse paymentResponse = new PaymentResponse();

		try {
			// Call service layer to process payment
			Payments payment = paymentService.processPayment(request, request.getPaymentId(),
					request.getPaymentMethod(), request.getUserCode());

			// Map Payment to PaymentList (response part)
			PaymentList response = new PaymentList();
			response.setPaymentDate(payment.getPaymentDate());
			response.setDueDate(payment.getDueDate());
			response.setInstallmentAmount(payment.getInstallmentAmount());
			response.setInstallmentCount(payment.getInstallmentCount());
			response.setTotalAmount(payment.getTotalAmountPaid());
			response.setPaymentMethod(payment.getPaymentMethod());
			response.setPaymentId(payment.getPaymentId());
			response.setTransactionId(payment.getTransactionId());
			response.setStatus(payment.getStatus());
			response.setPaymentAmount(payment.getPaymentAmount());
			response.setPaymentType(payment.getPaymentType());
			response.setEmailStatus(payment.getEmailStatus());
			response.setMessage("Payment processed successfully");
			response.setResponseStatus(CommonConstant.SUCCESS);

			// Map PaymentDetails fields
			PaymentDetails paymentDetails = new PaymentDetails();
			paymentDetails.setUpiId(payment.getUpiId());
			paymentDetails.setCardNumber(payment.getCardNumber());
			paymentDetails.setCardType(payment.getCardType());
			paymentDetails.setCvv(payment.getCvv());
			paymentDetails.setExpiryDate(payment.getExpiryDate() != null ? payment.getExpiryDate().toString() : null);
			paymentDetails.setOtp(payment.getOtp());
			paymentDetails.setChequeNumber(payment.getChequeNumber());
			paymentDetails.setIfscCode(payment.getBank() != null ? payment.getBank().getIfscCode() : null);
			paymentDetails.setBankaccount(payment.getBank() != null ? payment.getBank().getAccountNumber() : null);

			response.setPaymentDetails(paymentDetails);

			// Map policy/customer info to response
			paymentResponse.setCustomerNumber(payment.getCustomer().getCustomerNo());
			paymentResponse.setEmail(payment.getCustomer().getEmail());
			paymentResponse.setPolicyName(payment.getPolicy().getPolicyName());
			paymentResponse.setPolicyNumber(payment.getPolicy().getPolicyNumber());
			paymentResponse.setPolicyType(payment.getPolicy().getPolicyType());
			paymentResponse.setResponseStatus(CommonConstant.SUCCESS);
			paymentResponse.setPaymentList(Arrays.asList(response)); // Always returns single payment in list

			responseEntity.setStatus(CommonConstant.SUCCESS);
			responseEntity.setData(paymentResponse);

		} catch (Exception e) {
			responseEntity.setStatus(CommonConstant.FAILURE);
			responseEntity.setErrorMessage("Payment Already Done");
		}

		return responseEntity;
	}

	@GetMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<PaymentResponse>> getPayment(
	        @RequestParam(value = "paymentId", required = false) String paymentId,
	        @RequestParam(value = "transactionId", required = false) String transactionId,
	        @RequestParam(value = "status", required = false) String status,
	        @RequestParam(value = "policyNumber", required = false) String policyNumber,
	        @RequestParam(value = "customerNumber", required = false) String customerNumber,
	        @RequestParam(value = "page", defaultValue = "0") int page,
	        @RequestParam(value = "size", defaultValue = "100") int size,
	        @RequestHeader(value = "userCode", required = false) String userCode) {
		ResponseEntity<Page<PaymentResponse>> response= new ResponseEntity<Page<PaymentResponse>>();
	    List<Payments> paymentsList = paymentService.findHistoryOfPaymentsWithoutPagination(
	            policyNumber, customerNumber, paymentId, transactionId, status, userCode);

	    List<PaymentResponse> fullResponse;

	    if (paymentId != null || transactionId != null) {
	        fullResponse = paymentsList.stream().map(payment -> {
	            PaymentResponse pr = new PaymentResponse();
	            pr.setResponseStatus(CommonConstant.SUCCESS);
	            pr.setPolicyNumber(payment.getPolicy().getPolicyNumber());
	            pr.setCustomerNumber(payment.getCustomer().getCustomerNo());
	            pr.setEmail(payment.getCustomer().getEmail());
	            pr.setPolicyName(payment.getPolicyName());
	            pr.setPolicyType(payment.getPolicy().getPolicyType());
	            PaymentList pl = mapPaymentToPaymentList(payment);
	            pr.setPaymentList(Arrays.asList(pl));
	            return pr;
	        }).collect(Collectors.toList());

	    } else if (customerNumber != null) {
	        Map<String, List<Payments>> groupedByCustomerPolicy = paymentsList.stream()
	                .collect(Collectors.groupingBy(p -> p.getCustomer().getCustomerNo() + "_" + p.getPolicy().getPolicyNumber()));

	        fullResponse = groupedByCustomerPolicy.entrySet().stream().map(entry -> {
	            PaymentResponse pr = new PaymentResponse();
	            pr.setResponseStatus(CommonConstant.SUCCESS);

	            Payments firstPayment = entry.getValue().get(0);
	            pr.setPolicyNumber(firstPayment.getPolicy().getPolicyNumber());
	            pr.setCustomerNumber(firstPayment.getCustomer().getCustomerNo());
	            pr.setEmail(firstPayment.getCustomer().getEmail());
	            pr.setPolicyName(firstPayment.getPolicyName());
	            pr.setPolicyType(firstPayment.getPolicy().getPolicyType());

	            List<PaymentList> paymentList = entry.getValue().stream()
	                    .map(this::mapPaymentToPaymentList)
	                    .collect(Collectors.toList());

	            pr.setPaymentList(paymentList);
	            return pr;
	        }).collect(Collectors.toList());
	    }
 else {
	        Map<String, List<Payments>> groupedByPolicy = paymentsList.stream()
	                .collect(Collectors.groupingBy(p -> p.getPolicy().getPolicyNumber()));

	        fullResponse = groupedByPolicy.entrySet().stream().map(entry -> {
	            PaymentResponse pr = new PaymentResponse();
	            pr.setResponseStatus(CommonConstant.SUCCESS);
	            Payments firstPayment = entry.getValue().get(0);
	            pr.setPolicyNumber(entry.getKey());
	            pr.setCustomerNumber(firstPayment.getCustomer().getCustomerNo());
	            pr.setEmail(firstPayment.getCustomer().getEmail());
	            pr.setPolicyName(firstPayment.getPolicyName());
	            pr.setPolicyType(firstPayment.getPolicy().getPolicyType());

	            List<PaymentList> paymentList = entry.getValue().stream()
	                    .map(this::mapPaymentToPaymentList)
	                    .collect(Collectors.toList());

	            pr.setPaymentList(paymentList);
	            return pr;
	        }).collect(Collectors.toList());
	    }

	    // Apply manual pagination
	    int start = Math.min(page * size, fullResponse.size());
	    int end = Math.min(start + size, fullResponse.size());
	    List<PaymentResponse> pagedContent = fullResponse.subList(start, end);

	    Page<PaymentResponse> responsePage = new PageImpl<>(pagedContent,
	            PageRequest.of(page, size),
	            fullResponse.size());
	    response.setData(responsePage);
	    response.setStatus(CommonConstant.SUCCESS);
	    return response;
	}

	private PaymentList mapPaymentToPaymentList(Payments payment) {
		PaymentList pl = new PaymentList();
		pl.setPaymentDate(payment.getPaymentDate());
		pl.setDueDate(payment.getDueDate());
		pl.setInstallmentAmount(payment.getInstallmentAmount());
		pl.setInstallmentCount(payment.getInstallmentCount());
		pl.setTotalAmount(payment.getTotalAmountPaid());
		pl.setPaymentMethod(payment.getPaymentMethod());
		pl.setPaymentId(payment.getPaymentId());
		pl.setTransactionId(payment.getTransactionId());
		pl.setStatus(payment.getStatus());
		pl.setEmailStatus(payment.getEmailStatus());
		pl.setMessage("Payment processed successfully");
		pl.setResponseStatus(CommonConstant.SUCCESS);
		pl.setPaymentAmount(payment.getPaymentAmount());
		pl.setPaymentType(payment.getPaymentType());

		PaymentDetails paymentDetails = new PaymentDetails();
		paymentDetails.setUpiId(payment.getUpiId());
		paymentDetails.setCardNumber(payment.getCardNumber());
		paymentDetails.setCardType(payment.getCardType());
		paymentDetails.setCvv(payment.getCvv());
		paymentDetails.setExpiryDate(payment.getExpiryDate() != null ? payment.getExpiryDate().toString() : null);
		paymentDetails.setOtp(payment.getOtp());
		paymentDetails.setChequeNumber(payment.getChequeNumber());
		paymentDetails.setIfscCode(payment.getBank() != null ? payment.getBank().getIfscCode() : null);
		paymentDetails.setBankaccount(payment.getBank() != null ? payment.getBank().getAccountNumber() : null);

		pl.setPaymentDetails(paymentDetails);

		return pl;
	}

	@RequestMapping(value = "/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DashboardStats> paymentCount(
			@RequestHeader(value = "userCode", required = false) String userCode) {
		ResponseEntity<DashboardStats> response = new ResponseEntity<>();
		response = paymentService.fetchAllcounts(userCode);
		return response;

	}
}
