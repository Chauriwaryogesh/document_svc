package com.SecureAccessPortal.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SecureAccessPortal.Entity.BankAccount;
import com.SecureAccessPortal.Entity.Customer;
import com.SecureAccessPortal.Entity.Policy;
import com.SecureAccessPortal.Modal.BankDetailsDTO;
import com.SecureAccessPortal.Modal.PolicyRequest;
import com.SecureAccessPortal.Repo.BankAccountRepo;
import com.SecureAccessPortal.Repo.CustomerRepo;
import com.SecureAccessPortal.Repo.IPolicyRepo;

@Service
public class BankDetailsService {

    private final BankAccountRepo bankDetailsRepository;
    
    @Autowired
    private CustomerRepo customerRepo;
    
    @Autowired
    private IPolicyRepo policyRepo;
    

    public BankDetailsService(BankAccountRepo bankDetailsRepository) {
        this.bankDetailsRepository = bankDetailsRepository;
    }

	public List<BankDetailsDTO> getBankDetails(String bankAccNo, String policyNo, String customerNo, String userCode) {
		List<BankAccount> bankDetails = new ArrayList<BankAccount>();
		if (bankAccNo != null) {
			bankDetails = bankDetailsRepository.findByBankAccNo(bankAccNo);
		} else if (policyNo != null) {
			bankDetails = bankDetailsRepository.findByPolicyNumber(policyNo);
		} else if (customerNo != null) {
			bankDetails = bankDetailsRepository.findByCustomerNo(customerNo);
		}
		return bankDetails.stream().map(this::convertToDTO).collect(Collectors.toList());
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

	public String addBankDetails(BankDetailsDTO bankAccount, String userCode) {
		String message = "";
		try {
			BankAccount dto = new BankAccount();
			dto.setAccountNo(bankAccount.getAccountNumber());
			dto.setIfscCode(bankAccount.getIfscCode());
			dto.setBankName(bankAccount.getBankName());
			dto.setAccountType(bankAccount.getAccountType());
			dto.setStatus(bankAccount.getStatus());
			if (bankAccount.getCustomerNumber() != null) {
				Customer customer = customerRepo.findByCustomerNoNew(bankAccount.getCustomerNumber());
				dto.setCustomer(customer);
			}
			if (bankAccount.getPolicyNumber() != null) {
				Policy policy = policyRepo.findByPolicyNum(bankAccount.getPolicyNumber());
				dto.setPolicy(policy);
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

			BankAccount save = bankDetailsRepository.save(dto);
			message = "Bank details saved for customer " + save.getCustomer().getName() + " "
					+ save.getCustomer().getSurname() + " " + save.getPolicy().getCustomerNo();
		} catch (Exception e) {
			e.getMessage();
			message = "Customer Not identified";
		}
		return message;
	}

	public List<String> getBankNames(String userCode) {
		 List<String> bankNames=List.of("Bank of Baroda","Bank of Maharashtra","Bank of India","HDFC Bank","Bank of China");
		return bankNames;
	}
	public List<String> getBranchCodes(String userCode) {
		 List<String> bankNames=List.of("HDFC700989","HDFC700989","HDFC700989","HDFC700979","HDFC700990");
		return bankNames;
	}

	public PolicyRequest getCustomerDetails(String policyNo, String customerNo, String userCode) {
		PolicyRequest customer = new PolicyRequest();
		if (policyNo != null) {
			Policy policyNum = policyRepo.findByPolicyNum(policyNo);
			Customer byCustomerNoNew = customerRepo.findByCustomerNoNew(policyNum.getCustomerNo());
			customer.setCustName(byCustomerNoNew.getName() + " " + byCustomerNoNew.getSurname());
			customer.setCustomerNo(byCustomerNoNew.getCustomerNo());
			customer.setDateOfBirth(byCustomerNoNew.getDateOfBirth());
			customer.setEmail(byCustomerNoNew.getEmail());
			customer.setuserCode(byCustomerNoNew.getUserCode());
		}
		return customer;
	}
}

