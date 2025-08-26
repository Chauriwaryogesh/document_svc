package com.SecureAccessPortal.Modal;

import java.math.BigDecimal;

public class PolicyInfoDTO {
	    private int policyId;
	    private String policyName;
	    private String productCode;
	    private String policyType;
	    private String policyStatus;
	    private String polCompanyName;
	    private String policyTerm;
	    private BigDecimal totalAmount;
	    private String userCode;
		public int getPolicyId() {
			return policyId;
		}
		public void setPolicyId(int policyId) {
			this.policyId = policyId;
		}
		public String getPolicyName() {
			return policyName;
		}
		public void setPolicyName(String policyName) {
			this.policyName = policyName;
		}
		public String getProductCode() {
			return productCode;
		}
		public void setProductCode(String productCode) {
			this.productCode = productCode;
		}
		public String getPolicyType() {
			return policyType;
		}
		public void setPolicyType(String policyType) {
			this.policyType = policyType;
		}
		public String getPolicyStatus() {
			return policyStatus;
		}
		public void setPolicyStatus(String policyStatus) {
			this.policyStatus = policyStatus;
		}
		public String getPolCompanyName() {
			return polCompanyName;
		}
		public void setPolCompanyName(String polCompanyName) {
			this.polCompanyName = polCompanyName;
		}
		public String getPolicyTerm() {
			return policyTerm;
		}
		public void setPolicyTerm(String policyTerm) {
			this.policyTerm = policyTerm;
		}
		public BigDecimal getTotalAmount() {
			return totalAmount;
		}
		public void setTotalAmount(BigDecimal totalAmount) {
			this.totalAmount = totalAmount;
		}
		public String getUserCode() {
			return userCode;
		}
		public void setUserCode(String userCode) {
			this.userCode = userCode;
		}
	}

