package com.SecureAccessPortal.Modal;

import java.util.List;

public class PaymentResponse {
	private String responseStatus;
	private String policyNumber;
	private String customerNumber;
	private String policyName;
	private String policyType;
	private String email;
	private List<PaymentList> paymentList;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public List<PaymentList> getPaymentList() {
		return paymentList;
	}

	public void setPaymentList(List<PaymentList> paymentList) {
		this.paymentList = paymentList;
	}

}
