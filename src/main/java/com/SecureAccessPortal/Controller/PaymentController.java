package com.SecureAccessPortal.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Entity.Payments;
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

	@PostMapping("/process")
	public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest request) {
		ResponseEntity responseEntity = new ResponseEntity<>();
		try {
			Payments payment = paymentService.processPayment(request.getPaymentId(), request.getPaymentMethod(),
					request.getUserCode());
			PaymentList response = new PaymentList();
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

	 @GetMapping("/history") // This should match the frontend API call
	    public ResponseEntity<Page<PaymentResponse>> getPayment(
	            @RequestParam(value = "policyNumber", required = false) String policyNumber,
	            @RequestParam(value = "customerNumber", required = false) String customerNumber,
	            @RequestParam(value = "paymentId", required = false) String paymentId,
	            @RequestParam(value = "transactionId", required = false) String transactionId,
	            @RequestParam(value = "page", defaultValue = "0") int page,
	            @RequestParam(value = "size", defaultValue = "100") int size, // Ensure this default matches frontend itemsPerPage
	            @RequestHeader(value = "userCode", required = false) String userCode) {
		ResponseEntity<Page<PaymentResponse>> responseList = new ResponseEntity<>();
		
	    Page<Payments> paymentsPage = paymentService.findHistoryOfPayments(policyNumber, customerNumber, paymentId,
	            transactionId, page, size);
	    
	    // Group payments by policy number
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
	        pr.setPolicyName(firstPayment.getPolicyName());
	        pr.setPolicyType(firstPayment.getPolicy().getPolicyType());
	        
	        // Map all payments to PaymentList
	        List<PaymentList> paymentList = entry.getValue().stream()
	                .map(p -> {
	                    PaymentList pl = new PaymentList();
	                    pl.setPaymentId(p.getPaymentId());
	                    pl.setInstallmentAmount(p.getInstallmentAmount());
	                    pl.setDueDate(p.getDueDate());
	                    pl.setPaymentDate(p.getPaymentDate());
	                    pl.setStatus(p.getStatus());
	                    pl.setTransactionId(p.getTransactionId());
	                    pl.setEmailStatus(p.getEmailStatus());
	                    return pl;
	                })
	               // .sorted((p1, p2) -> p1.getDueDate().compareTo(p2.getDueDate())) // Sort by due date
	                .collect(Collectors.toList());
	        
	        pr.setPaymentList(paymentList);
	        return pr;
	    }).collect(Collectors.toList());
	    
	    // Create new Page with grouped results
        Page<PaymentResponse> responsePage = new PageImpl<>(
        		response,      
                PageRequest.of(paymentsPage.getNumber(), paymentsPage.getSize(), paymentsPage.getSort()),
                paymentsPage.getTotalElements() // THIS IS THE CRITICAL PART: Use the totalElements from the original Page
        );
	    responseList.setData(responsePage);
	    return responseList;
	}
}
