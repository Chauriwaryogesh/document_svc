package com.SecureAccessPortal.Modal;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class SurrenderClaimDTO {
	private String policyNo;
	private String customerNo;
	private String customerName;
	private String surrenderRefNo;
	private String surrenderStatus;
	private BigDecimal surrAmount;
	private String surrDate;
	private String surrenderBy;
	private String surrReason;
	private String paymentId;
	private String paymentTransId;
	private String paymentStatus;
	private String paymentDate;
	private String transactionDate;
	private String verificationDocument;
	private String verificationDocumentName;
	private String verificationStatus;
	private String verificationComment;
	private String otherSupportingDocument;
	private String otherSupportingDocumentName;
	private String otherSupportingDocumentVerificationStatus;
//	private String bankDocument;
//	private String bankDocumentStatus;
	private String idDocument;
	private String idDocumentStatus;
	private String claimRefNo;
	private String claimReason;
	private String claimStatus;
	private BigDecimal claimAmount;
	private String claimDate;
	private String claimBy;
	private String claimType;
	private String action;

	private String surrDescription;
	private String bankAccNo;

	private String bankPassbookDocument;
	private String bankPassbookDocumentName;
	private String bankPassbookDocumentNameStatus;

	private String idProofDocument;
	private String idProofDocumentName;
	private String idProofDocumentStatus;
	
	private String updatedBy;
	private String updatedDate;
	
	// for updateDocument
	private String documentStatus;
	private String documentType;
	
	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getDocumentStatus() {
		return documentStatus;
	}

	public void setDocumentStatus(String documentStatus) {
		this.documentStatus = documentStatus;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	private BankDetailsDTO bankAccount;
	
	public BankDetailsDTO getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankDetailsDTO bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	

	public String getBankPassbookDocumentNameStatus() {
		return bankPassbookDocumentNameStatus;
	}

	public void setBankPassbookDocumentNameStatus(String bankPassbookDocumentNameStatus) {
		this.bankPassbookDocumentNameStatus = bankPassbookDocumentNameStatus;
	}

	public String getIdProofDocumentStatus() {
		return idProofDocumentStatus;
	}

	public void setIdProofDocumentStatus(String idProofDocumentStatus) {
		this.idProofDocumentStatus = idProofDocumentStatus;
	}

	public String getIdProofDocument() {
		return idProofDocument;
	}

	public void setIdProofDocument(String idProofDocument) {
		this.idProofDocument = idProofDocument;
	}

	public String getIdProofDocumentName() {
		return idProofDocumentName;
	}

	public void setIdProofDocumentName(String idProofDocumentName) {
		this.idProofDocumentName = idProofDocumentName;
	}

	public String getSurrDescription() {
		return surrDescription;
	}

	public void setSurrDescription(String surrDescription) {
		this.surrDescription = surrDescription;
	}

	public String getBankAccNo() {
		return bankAccNo;
	}

	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
	}

	public String getBankPassbookDocument() {
		return bankPassbookDocument;
	}

	public void setBankPassbookDocument(String bankPassbookDocument) {
		this.bankPassbookDocument = bankPassbookDocument;
	}

	public String getBankPassbookDocumentName() {
		return bankPassbookDocumentName;
	}

	public void setBankPassbookDocumentName(String bankPassbookDocumentName) {
		this.bankPassbookDocumentName = bankPassbookDocumentName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

//	public String getBankDocument() {
//		return bankDocument;
//	}
//
//	public void setBankDocument(String bankDocument) {
//		this.bankDocument = bankDocument;
//	}
//
//	public String getBankDocumentStatus() {
//		return bankDocumentStatus;
//	}
//
//	public void setBankDocumentStatus(String bankDocumentStatus) {
//		this.bankDocumentStatus = bankDocumentStatus;
//	}

	public String getIdDocument() {
		return idDocument;
	}

	public void setIdDocument(String idDocument) {
		this.idDocument = idDocument;
	}

	public String getIdDocumentStatus() {
		return idDocumentStatus;
	}

	public void setIdDocumentStatus(String idDocumentStatus) {
		this.idDocumentStatus = idDocumentStatus;
	}

	public String getVerificationDocument() {
		return verificationDocument;
	}

	public void setVerificationDocument(String verificationDocument) {
		this.verificationDocument = verificationDocument;
	}

	public String getVerificationDocumentName() {
		return verificationDocumentName;
	}

	public void setVerificationDocumentName(String verificationDocumentName) {
		this.verificationDocumentName = verificationDocumentName;
	}

	public String getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public String getVerificationComment() {
		return verificationComment;
	}

	public void setVerificationComment(String verificationComment) {
		this.verificationComment = verificationComment;
	}

	public String getOtherSupportingDocument() {
		return otherSupportingDocument;
	}

	public void setOtherSupportingDocument(String otherSupportingDocument) {
		this.otherSupportingDocument = otherSupportingDocument;
	}

	public String getOtherSupportingDocumentName() {
		return otherSupportingDocumentName;
	}

	public void setOtherSupportingDocumentName(String otherSupportingDocumentName) {
		this.otherSupportingDocumentName = otherSupportingDocumentName;
	}

	public String getOtherSupportingDocumentVerificationStatus() {
		return otherSupportingDocumentVerificationStatus;
	}

	public void setOtherSupportingDocumentVerificationStatus(String otherSupportingDocumentVerificationStatus) {
		this.otherSupportingDocumentVerificationStatus = otherSupportingDocumentVerificationStatus;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getPaymentTransId() {
		return paymentTransId;
	}

	public void setPaymentTransId(String paymentTransId) {
		this.paymentTransId = paymentTransId;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getSurrenderRefNo() {
		return surrenderRefNo;
	}

	public void setSurrenderRefNo(String surrenderRefNo) {
		this.surrenderRefNo = surrenderRefNo;
	}

	public String getSurrenderStatus() {
		return surrenderStatus;
	}

	public void setSurrenderStatus(String surrenderStatus) {
		this.surrenderStatus = surrenderStatus;
	}

	public String getSurrDate() {
		return surrDate;
	}

	public void setSurrDate(String surrDate) {
		this.surrDate = surrDate;
	}

	public String getSurrenderBy() {
		return surrenderBy;
	}

	public void setSurrenderBy(String surrenderBy) {
		this.surrenderBy = surrenderBy;
	}

	public String getSurrReason() {
		return surrReason;
	}

	public void setSurrReason(String surrReason) {
		this.surrReason = surrReason;
	}

	public String getClaimRefNo() {
		return claimRefNo;
	}

	public void setClaimRefNo(String claimRefNo) {
		this.claimRefNo = claimRefNo;
	}

	public String getClaimReason() {
		return claimReason;
	}

	public void setClaimReason(String claimReason) {
		this.claimReason = claimReason;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public BigDecimal getSurrAmount() {
		return surrAmount;
	}

	public void setSurrAmount(BigDecimal surrAmount) {
		this.surrAmount = surrAmount;
	}

	public BigDecimal getClaimAmount() {
		return claimAmount;
	}

	public void setClaimAmount(BigDecimal claimAmount) {
		this.claimAmount = claimAmount;
	}

	public String getClaimDate() {
		return claimDate;
	}

	public void setClaimDate(String claimDate) {
		this.claimDate = claimDate;
	}

	public String getClaimBy() {
		return claimBy;
	}

	public void setClaimBy(String claimBy) {
		this.claimBy = claimBy;
	}

}
