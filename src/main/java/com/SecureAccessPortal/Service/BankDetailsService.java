package com.SecureAccessPortal.Service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.SecureAccessPortal.Entity.BankAccount;
import com.SecureAccessPortal.Modal.BankDetailsDTO;
import com.SecureAccessPortal.Repo.BankAccountRepo;

@Service
public class BankDetailsService {

    private final BankAccountRepo bankDetailsRepository;

    public BankDetailsService(BankAccountRepo bankDetailsRepository) {
        this.bankDetailsRepository = bankDetailsRepository;
    }

    public List<BankDetailsDTO> getAllBankDetails() {
        List<BankAccount> bankDetails = bankDetailsRepository.findAll();
        return bankDetails.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private BankDetailsDTO convertToDTO(BankAccount bankAccount) {
    	BankDetailsDTO dto = new BankDetailsDTO();
        dto.setId(bankAccount.getId());
        dto.setAccountNumber(bankAccount.getAccountNo());
        dto.setIfscCode(bankAccount.getIfscCode());
        dto.setBankName(bankAccount.getBankName());
        dto.setAccountType(bankAccount.getAccountType());
        dto.setStatus(bankAccount.getStatus());
        // Map Customer
        if (bankAccount.getCustomer() != null) {
            dto.setCustomerNumber(bankAccount.getCustomer().getCustomerNo());
            dto.setHolderName(bankAccount.getCustomer().getName()); // Assuming Customer has a name field
        }
        // Map Policy
        if (bankAccount.getPolicy() != null) {
            dto.setPolicyNumber(bankAccount.getPolicy().getPolicyNumber());
            dto.setPolicyStatus(bankAccount.getPolicy().getPolicyStatus()); // Assuming Policy has a status field
        }
        dto.setLastVerificationDate(bankAccount.getLastVerificationDate());
        dto.setCreatedBy(bankAccount.getCreatedBy());
        dto.setCreatedDate(bankAccount.getCreatedDate());
        dto.setNotes(bankAccount.getNotes());
        dto.setAccountHolderType(bankAccount.getAccountHolderType());
        dto.setBranchCode(bankAccount.getBranchCode());
        dto.setSwiftCode(bankAccount.getSwiftCode());
        dto.setPaymentMethodStatus(bankAccount.getPaymentMethodStatus());
        dto.setLastPaymentDate(bankAccount.getLastPaymentDate());
        dto.setAmlStatus(bankAccount.getAmlStatus());
        dto.setAccountBalance(bankAccount.getAccountBalance());
        dto.setLinkedPaymentMethod(bankAccount.getLinkedPaymentMethod());
        dto.setVerificationAttempts(bankAccount.getVerificationAttempts());
        return dto;
    }
}

