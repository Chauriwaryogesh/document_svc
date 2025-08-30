package com.SecureAccessPortal.Transformer;

import java.util.Base64;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Entity.Bank;
import com.SecureAccessPortal.Entity.ClaimEntity;
import com.SecureAccessPortal.Entity.SurrenderEntity;
import com.SecureAccessPortal.Modal.BankDetailsDTO;
import com.SecureAccessPortal.Modal.SurrenderClaimDTO;
import com.SecureAccessPortal.Repo.BankAccountRepo;
import com.SecureAccessPortal.Service.BankService;

@Component
public class PolicyMapper {

	private static final Logger logger = LoggerFactory.getLogger(PolicyMapper.class);

	@Autowired
	private BankAccountRepo bankAccountRepository;
	
	@Autowired
	private BankService bankDetailsService;

	private BankDetailsDTO mapToBankAccountDTO(Bank bankAccount) {
	    if (bankAccount == null) {
	        return null;
	    }
	    BankDetailsDTO dto = new BankDetailsDTO();

	    // Primary key
	    dto.setBankId(bankAccount.getBankId());

	    // Relations
	    if (bankAccount.getCustomer() != null) {
	        dto.setCustomerNumber(bankAccount.getCustomer().getCustomerNo());
	        dto.setCustomerName(
	                (bankAccount.getCustomer().getName() != null ? bankAccount.getCustomer().getName() : "") + " "
	                        + (bankAccount.getCustomer().getMiddleName() != null ? bankAccount.getCustomer().getMiddleName()
	                                : "")
	                        + " "
	                        + (bankAccount.getCustomer().getSurname() != null ? bankAccount.getCustomer().getSurname() : ""));
	    }
	    if (bankAccount.getPolicy() != null) {
	        dto.setPolicyNumber(bankAccount.getPolicy().getPolicyNumber());
	        dto.setPolicyStatus(bankAccount.getPolicy().getPolicyStatus());
	    }
	    dto.setAccountNumber(bankAccount.getAccountNumber());
	    dto.setAccountHolderName(bankAccount.getAccountHolderName());
	    dto.setAccountHolderType(bankAccount.getAccountHolderType());
	    dto.setBankName(bankAccount.getBankName());
	    dto.setBranchCode(bankAccount.getBranchCode());
	    dto.setIfscCode(bankAccount.getIfscCode());
	    dto.setSwiftCode(bankAccount.getSwiftCode());
	    dto.setAccountType(bankAccount.getAccountType());
	    dto.setCurrency(bankAccount.getCurrency());
	    dto.setAccountBalance(bankAccount.getAccountBalance());
	    dto.setIsDefaultAccount(bankAccount.getIsDefaultAccount());
	    dto.setAccountOpeningDate(bankAccount.getAccountOpeningDate());
	    dto.setAccountClosingDate(bankAccount.getAccountClosingDate());
	    dto.setKycDocumentStatus(bankAccount.getKycDocumentStatus());
	    dto.setKycStatus(bankAccount.getKycStatus());
	    if (bankAccount.getKycDocument() != null) {
	        dto.setKycDocument(Base64.getEncoder().encodeToString(bankAccount.getKycDocument())); // byte[] -> Base64 string
	    }
	    dto.setAmlStatus(bankAccount.getAmlStatus());

	    // Payment & Verification
	    dto.setPaymentMethodStatus(bankAccount.getPaymentMethodStatus());
	    dto.setLinkedPaymentMethod(bankAccount.getLinkedPaymentMethod());
	    dto.setLastPaymentDate(bankAccount.getLastPaymentDate());
	    dto.setLastVerificationDate(bankAccount.getLastVerificationDate());
	    dto.setVerificationAttempts(bankAccount.getVerificationAttempts());
	    dto.setVerifierComment(bankAccount.getVerifierComment());
	    dto.setCustomerComment(bankAccount.getCustomerComment());

	    // Status Flags
	    dto.setStatus(bankAccount.getStatus());
	    dto.setDeletedFlag(bankAccount.getDeletedFlag());

	    // Audit
	    dto.setCreatedBy(bankAccount.getCreatedBy());
	    dto.setCreatedDate(bankAccount.getCreatedDate());
	    dto.setUpdatedBy(bankAccount.getUpdatedBy());
	    dto.setUpdatedDate(bankAccount.getUpdatedDate());

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
				if (surr.getBank() != null) {
					BankDetailsDTO convertToDTO = bankDetailsService.convertToDTO(surr.getBank());
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
				if (surr.getBank() != null) {
					BankDetailsDTO convertToDTO = bankDetailsService.convertToDTO(surr.getBank());
					surrender.setBankAccount(convertToDTO);
				}
				return surrender;
			});
		}
		return response;
	}

}
