package com.SecureAccessPortal.Transformer;

import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Entity.BankAccount;
import com.SecureAccessPortal.Entity.ClaimEntity;
import com.SecureAccessPortal.Entity.SurrenderEntity;
import com.SecureAccessPortal.Modal.BankAccountDTO;
import com.SecureAccessPortal.Modal.BankDetailsDTO;
import com.SecureAccessPortal.Modal.SurrenderClaimDTO;
import com.SecureAccessPortal.Repo.BankAccountRepo;
import com.SecureAccessPortal.Service.BankDetailsService;

@Component
public class PolicyMapper {

	private static final Logger logger = LoggerFactory.getLogger(PolicyMapper.class);

	@Autowired
	private BankAccountRepo bankAccountRepository;
	
	@Autowired
	private BankDetailsService bankDetailsService;

	private BankAccountDTO mapToBankAccountDTO(BankAccount bankAccount) {
		BankAccountDTO dto = new BankAccountDTO();
		if (bankAccount == null) {
			return dto;
		}
		dto.setId(bankAccount.getId());
		dto.setAccountNumber(bankAccount.getAccountNo() != null ? bankAccount.getAccountNo() : "");
		dto.setIfscCode(bankAccount.getIfscCode() != null ? bankAccount.getIfscCode() : "");
		dto.setBankName(bankAccount.getBankName() != null ? bankAccount.getBankName() : "");
		dto.setAccountType(bankAccount.getAccountType() != null ? bankAccount.getAccountType() : "");
		dto.setStatus(bankAccount.getStatus() != null ? bankAccount.getStatus() : "");
		if (bankAccount.getCustomer() != null) {
			dto.setCustomerNumber(
					bankAccount.getCustomer().getCustomerNo() != null ? bankAccount.getCustomer().getCustomerNo() : "");
			dto.setHolderName(bankAccount.getCustomer().getName() != null ? bankAccount.getCustomer().getName() : "");
		} else {
			dto.setCustomerNumber("");
			dto.setHolderName("");
		}
		if (bankAccount.getPolicy() != null) {
			dto.setPolicyNumber(
					bankAccount.getPolicy().getPolicyNumber() != null ? bankAccount.getPolicy().getPolicyNumber() : "");
			dto.setPolicyStatus(
					bankAccount.getPolicy().getPolicyStatus() != null ? bankAccount.getPolicy().getPolicyStatus() : "");
		} else {
			dto.setPolicyNumber("");
			dto.setPolicyStatus("");
		}
		dto.setLastVerificationDate(bankAccount.getLastVerificationDate() != null
				? bankAccount.getLastVerificationDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
				: "");
		dto.setCreatedBy(bankAccount.getCreatedBy() != null ? bankAccount.getCreatedBy() : "");
		dto.setCreatedDate(bankAccount.getCreatedDate() != null
				? bankAccount.getCreatedDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
				: "");
		dto.setNotes(bankAccount.getNotes() != null ? bankAccount.getNotes() : "");
		dto.setAccountHolderType(bankAccount.getAccountHolderType() != null ? bankAccount.getAccountHolderType() : "");
		dto.setBranchCode(bankAccount.getBranchCode() != null ? bankAccount.getBranchCode() : "");
		dto.setSwiftCode(bankAccount.getSwiftCode() != null ? bankAccount.getSwiftCode() : "");
		dto.setPaymentMethodStatus(
				bankAccount.getPaymentMethodStatus() != null ? bankAccount.getPaymentMethodStatus() : "");
		dto.setLastPaymentDate(bankAccount.getLastPaymentDate() != null
				? bankAccount.getLastPaymentDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
				: "");
		dto.setAmlStatus(bankAccount.getAmlStatus() != null ? bankAccount.getAmlStatus() : "");
		dto.setAccountBalance(bankAccount.getAccountBalance());
		dto.setLinkedPaymentMethod(
				bankAccount.getLinkedPaymentMethod() != null ? bankAccount.getLinkedPaymentMethod() : "");
		dto.setVerificationAttempts(bankAccount.getVerificationAttempts());
		return dto;
	}

	public Page<SurrenderClaimDTO> mapClaimSurreResponse(Page<SurrenderEntity> surrenderEntity) {
		Page<SurrenderClaimDTO> response = null;
		if (Objects.nonNull(surrenderEntity)) {
			try {
			response = surrenderEntity.map(surr -> {
				SurrenderClaimDTO surrender = new SurrenderClaimDTO();
				surrender.setCustomerName(surr.getPolicy().getCustomer().getName());
				surrender.setCustomerNo(surr.getPolicy().getCustomer().getCustomerNo());
				surrender.setPolicyNo(surr.getPolicy().getPolicyNumber());
				if(surr.getOtherSupportingDocument() != null) {
					String fileBytes = Base64.getEncoder().encodeToString(surr.getOtherSupportingDocument());
					surrender.setOtherSupportingDocument(fileBytes);
					surrender.setOtherSupportingDocumentName(surr.getOtherSupportingDocumentName());
					surrender.setOtherSupportingDocumentVerificationStatus(
							surr.getOtherSupportingDocumentVerificationStatus());
				}
				
				if(surr.getVerificationDocument() != null) {
					String fileBytes = Base64.getEncoder().encodeToString(surr.getVerificationDocument());
					surrender.setVerificationDocument(fileBytes);
					surrender.setVerificationDocumentName(surr.getVerificationDocumentName());
					surrender.setVerificationStatus(surr.getVerificationStatus());
					
				}
				if(surr.getVerificationDocument() != null) {
					String fileBytes = Base64.getEncoder().encodeToString(surr.getVerificationDocument());
					surrender.setVerificationDocument(fileBytes);
					surrender.setVerificationDocumentName(surr.getVerificationDocumentName());
					surrender.setVerificationStatus(surr.getVerificationStatus());
					
				}
				if(surr.getBankDocument() != null) {
					String fileBytes = Base64.getEncoder().encodeToString(surr.getBankDocument());
					surrender.setBankPassbookDocument(fileBytes);
					surrender.setBankPassbookDocumentName(surr.getBankDocumentName());
					surrender.setBankPassbookDocumentNameStatus(surr.getBankDocumentStatus());
					
				}
				if(surr.getPayment() != null) {
					surrender.setPaymentDate(String.valueOf(surr.getPayment().getPaymentDate()));
					surrender.setPaymentId(surr.getPayment().getPaymentId());
					surrender.setPaymentStatus(surr.getPayment().getStatus());
					surrender.setPaymentTransId(surr.getPayment().getTransactionId());	
					surrender.setTransactionDate(String.valueOf(surr.getPayment().getPaymentDate()));
				}	
				surrender.setSurrAmount(surr.getSurrAmount());
				surrender.setSurrDate(String.valueOf(surr.getSurrDate()));
				surrender.setSurrenderBy(surr.getSurrenderBy());
				surrender.setSurrenderRefNo(surr.getSurrRefNo());
				surrender.setSurrenderStatus(surr.getSurrenderStatus());
				surrender.setSurrReason(surr.getSurrenderReason());
				surrender.setVerificationComment(surr.getVerificationComment());
				surrender.setUpdatedBy(surr.getUpdatedBy());
				surrender.setUpdatedDate(String.valueOf(surr.getUpdatedDate()));
				if (surr.getBankAccount() != null) {
					BankDetailsDTO convertToDTO = bankDetailsService.convertToDTO(surr.getBankAccount());
					surrender.setBankAccount(convertToDTO);
				}
				return surrender;
			});
		}catch(Exception e) {
			logger.error("Issue found in mapping", e);
		}
		}
		return response;
	}

	public Page<SurrenderClaimDTO> mapClaimResponse(Page<ClaimEntity> claimEntity) {
		Page<SurrenderClaimDTO> response = null;
		if (Objects.nonNull(claimEntity)) {
			response = claimEntity.map(surr -> {
				SurrenderClaimDTO surrender = new SurrenderClaimDTO();
				surrender.setCustomerName(surr.getPolicy().getCustomer().getName());
				surrender.setCustomerNo(surr.getPolicy().getCustomer().getCustomerNo());
				surrender.setPolicyNo(surr.getPolicy().getPolicyNumber());
				surrender.setUpdatedBy(surr.getUpdatedBy());
				surrender.setUpdatedDate(String.valueOf(surr.getUpdatedDate()));
				if(surr.getOtherSupportingDocument() != null) {
					String fileBytes = Base64.getEncoder().encodeToString(surr.getOtherSupportingDocument());
					surrender.setOtherSupportingDocument(fileBytes);
					surrender.setOtherSupportingDocumentName(surr.getOtherSupportingDocumentName());
					surrender.setOtherSupportingDocumentVerificationStatus(
							surr.getOtherSupportingDocumentVerificationStatus());
				}
				if (surr.getBankDocument() != null) {
					String fileBytes = Base64.getEncoder().encodeToString(surr.getBankDocument());
					surrender.setBankPassbookDocument(fileBytes);
					surrender.setBankPassbookDocumentName(CommonConstant.BANK_PROOF);
					surrender.setBankPassbookDocumentNameStatus(surr.getBankDocumentStatus());
				}
				if (surr.getIdDocument() != null) {
					String fileBytes = Base64.getEncoder().encodeToString(surr.getIdDocument());
					surrender.setIdProofDocument(fileBytes);
					surrender.setIdProofDocumentStatus(surr.getIdDocumentStatus());
					surrender.setIdProofDocumentName(CommonConstant.ID_PROOF);
				}				
				if(surr.getVerificationDocument() != null) {
					String fileBytes = Base64.getEncoder().encodeToString(surr.getVerificationDocument());
					surrender.setVerificationDocument(fileBytes);
					surrender.setVerificationDocumentName(surr.getVerificationDocumentName());
					surrender.setVerificationStatus(surr.getVerificationStatus());
					
				}
				if(surr.getPayment() != null) {
					surrender.setPaymentDate(String.valueOf(surr.getPayment().getPaymentDate()));
					surrender.setPaymentId(surr.getPayment().getPaymentId());
					surrender.setPaymentStatus(surr.getPayment().getStatus());
					surrender.setPaymentTransId(surr.getPayment().getTransactionId());	
					surrender.setTransactionDate(String.valueOf(surr.getPayment().getPaymentDate()));
				}	
				surrender.setClaimAmount(surr.getClaimAmount());
				surrender.setClaimDate(String.valueOf(surr.getClaimDate()));
				surrender.setClaimBy(surr.getClaimBy());
				surrender.setClaimRefNo(surr.getClaimRefNo());
				surrender.setClaimStatus(surr.getClaimStatus());
				surrender.setClaimReason(surr.getClaimReason());
				surrender.setClaimType(surr.getClaimType());
				surrender.setVerificationComment(surr.getVerificationComment());
				if (surr.getBankAccount() != null) {
					BankDetailsDTO convertToDTO = bankDetailsService.convertToDTO(surr.getBankAccount());
					surrender.setBankAccount(convertToDTO);
				}
				return surrender;
			});
		}
		return response;
	}

}
