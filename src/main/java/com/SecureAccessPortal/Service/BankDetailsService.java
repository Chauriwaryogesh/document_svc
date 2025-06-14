package com.SecureAccessPortal.Service;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Entity.BankAccount;
import com.SecureAccessPortal.Entity.Customer;
import com.SecureAccessPortal.Entity.Policy;
import com.SecureAccessPortal.Entity.VerificationRecord;
import com.SecureAccessPortal.Exception.ResourceNotFoundException;
import com.SecureAccessPortal.Modal.BankDetailsDTO;
import com.SecureAccessPortal.Modal.PolicyRequest;
import com.SecureAccessPortal.Modal.VerificationRecordDTO;
import com.SecureAccessPortal.Modal.WorkItemDTO;
import com.SecureAccessPortal.Repo.BankAccountRepo;
import com.SecureAccessPortal.Repo.CustomerRepo;
import com.SecureAccessPortal.Repo.IPolicyRepo;
import com.SecureAccessPortal.Repo.VerificationRecordRepo;
import com.SecureAccessPortal.Transformer.BankMapper;

@Service
public class BankDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(PolicyService.class);

    private final BankAccountRepo bankAccountRepository;
    
    @Autowired
    private CustomerRepo customerRepo;
    
    @Autowired
    private BankMapper bankMapper;
    
    @Autowired
    private IPolicyRepo policyRepo;
    
    @Autowired
	private IWorkItemService workItemService;
     
    @Autowired
    private VerificationRecordRepo verificationRecordRepository;

    public BankDetailsService(BankAccountRepo bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

	public List<BankDetailsDTO> getBankDetails(String bankAccNo, String policyNo, String customerNo, String userCode) {
		List<BankAccount> bankDetails = new ArrayList<BankAccount>();
		if (bankAccNo != null) {
			bankDetails = bankAccountRepository.findByBankAccNo(bankAccNo);
		} else if (policyNo != null) {
			bankDetails = bankAccountRepository.findByPolicyNumber(policyNo);
		} else if (customerNo != null) {
			bankDetails = bankAccountRepository.findByCustomerNo(customerNo);
		}else {
			bankDetails = bankAccountRepository.findAll();
		}
		return bankDetails.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

    public List<BankDetailsDTO> getAllBankDetails() {
        List<BankAccount> bankDetails = bankAccountRepository.findAll();
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
			if (bankAccount.getPolicyNumber() != null) {
				Policy policy = policyRepo.findByPolicyNum(bankAccount.getPolicyNumber());
				dto.setPolicy(policy);
			
			//create workitem
			WorkItemDTO workItemRequest = new WorkItemDTO();
			workItemRequest.setComment("Bank Account is created " + dto.getCustomer().getName() + " "
					+ dto.getCustomer().getSurname() + "for the customer");
			workItemRequest.setCreatedBy(userCode);
			workItemRequest.setuserCode(userCode);
			workItemRequest.setWorkItemName(CommonConstant.BANK_ACC_CREATED);
			workItemRequest.setCreatedTime(String.valueOf(LocalDateTime.now()));
			workItemRequest.setWorkType(CommonConstant.BANK_ACC_CREATED);
			workItemRequest.setStatus(CommonConstant.OPEN);
			workItemRequest.setQueue(CommonConstant.TEAM_MEMBER);
			WorkItemDTO workItem = workItemService.createWorkItem(workItemRequest, userCode);
			if (workItem != null) {
				dto.setWorkItemRefNo(workItem.getWorkItemReferenceNumber());
				logger.info("WorkItemcreated succesfully");
				if (workItem != null) {
					policy.setWorkItemRefNo(workItem.getWorkItemReferenceNumber());
					logger.info("WorkItemcreated succesfully");
					policyRepo.save(policy);
				}
			} else {
				new RuntimeErrorException(null, "Error while creating WorkItem");
			}
			}
			BankAccount save = bankAccountRepository.save(dto);
			
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
		 List<String> bankNames=List.of("HDFC00001234","HDFC700989","HDFC700989","HDFC700979","HDFC700990");
		return bankNames;
	}

	public PolicyRequest getCustomerDetails(String policyNo, String customerNo, String userCode) {
		PolicyRequest customer = new PolicyRequest();
		if (policyNo != null) {
			Policy policyNum = policyRepo.findByPolicyNum(policyNo);
			if(policyNum != null) {
				Customer byCustomerNoNew = customerRepo.findByCustomerNoNew(policyNum.getCustomerNo());
				customer.setCustName(byCustomerNoNew.getName() + " " + byCustomerNoNew.getSurname());
				customer.setCustomerNo(byCustomerNoNew.getCustomerNo());
				customer.setDateOfBirth(byCustomerNoNew.getDateOfBirth());
				customer.setEmail(byCustomerNoNew.getEmail());
				customer.setuserCode(byCustomerNoNew.getUserCode());	
			}
		}
		return customer;
	}
	
	public VerificationRecordDTO createVerificationRecord(String action, String accountNo, MultipartFile document,
			String status, String userCode, String details, String customerNo, String policyNumber) {
		if (!isValidAction(action)) {
			throw new IllegalArgumentException("Invalid action type. Must be SANCTIONS, ID, or DEATH");
		}
		if (document.isEmpty() || document.getSize() > 5 * 1024 * 1024) {
			throw new IllegalArgumentException("Document is empty or exceeds 5MB");
		}
		if (!isValidFileType(document)) {
			throw new IllegalArgumentException("Unsupported file type. Use PDF, PNG, or JPG");
		}
		if (!isValidStatus(status)) {
			throw new IllegalArgumentException("Invalid status. Must be PENDING, PASS, FAIL, or IN_REVIEW");
		}
		BankAccount bankAccount = bankAccountRepository.findByAccountNo(accountNo);
		if(bankAccount == null) {
			throw new ResourceNotFoundException("Bank account not found for accountNo: " + accountNo);
		}
		VerificationRecord verificationRecord = new VerificationRecord();
		verificationRecord.setBankAccount(bankAccount);
		verificationRecord.setCustomerNo(customerNo != null ? customerNo : bankAccount.getCustomer().getCustomerNo());
		verificationRecord.setPolicyNumber(policyNumber != null ? policyNumber : bankAccount.getPolicy().getPolicyNumber());
		verificationRecord.setUserCode(userCode);
		switch (action.toUpperCase()) {
		case "SANCTIONS":
			verificationRecord.setSanctions(details);
			try {
				verificationRecord.setSanctionsDocs(document.getBytes());
			} catch (IOException e) {
				throw new IllegalArgumentException("Failed to process document");
			}
			verificationRecord.setSancStatus(status);
			break;
		case "ID":
			verificationRecord.setIdentity(details);
			try {
				verificationRecord.setIdentityDocs(document.getBytes());
			} catch (IOException e) {
				throw new IllegalArgumentException("Failed to process document");
			}
			verificationRecord.setIdentityStatus(status);
			break;
		case "DEATH":
			verificationRecord.setDeath(details);
			try {
				verificationRecord.setDeathDocs(document.getBytes());
			} catch (IOException e) {
				throw new IllegalArgumentException("Failed to process document");
			}
			verificationRecord.setDeathStatus(status);
			break;
		default:
			throw new IllegalArgumentException("Invalid action type");
		}

		//create workitem
		WorkItemDTO workItemRequest = new WorkItemDTO();
		workItemRequest.setComment("Verification Record Created for "+action+" Account is created "+ customerNo + "for the customer");
		workItemRequest.setCreatedBy(userCode);
		workItemRequest.setuserCode(userCode);
		workItemRequest.setWorkItemName(CommonConstant.VERIFICATION_RECORD);
		workItemRequest.setCreatedTime(String.valueOf(LocalDateTime.now()));
		workItemRequest.setWorkType(CommonConstant.VERIFICATION_RECORD);
		workItemRequest.setStatus(CommonConstant.OPEN);
		workItemRequest.setQueue(CommonConstant.TEAM_MEMBER);
		WorkItemDTO workItem = workItemService.createWorkItem(workItemRequest, userCode);
		if (workItem != null) {
			verificationRecord.setWorkItemRefNo(workItem.getWorkItemReferenceNumber());
			logger.info("WorkItemcreated succesfully");

		} else {
			new RuntimeErrorException(null, "Error while creating WorkItem");
		}
		
		verificationRecord = verificationRecordRepository.save(verificationRecord);
		return mapToDTO(verificationRecord, action);
	}

	public Page<VerificationRecordDTO> getVerificationRecords(String accountNo, String action, String status,
			String userCode, Pageable pageable) {
// Validate inputs
		if (accountNo == null || accountNo.isEmpty()) {
			throw new IllegalArgumentException("Account number is required");
		}
		if (action != null && !isValidAction(action)) {
			throw new IllegalArgumentException("Invalid action type. Must be SANCTIONS, ID, or DEATH");
		}
		if (status != null && !isValidStatus(status)) {
			throw new IllegalArgumentException("Invalid status. Must be PENDING, PASS, FAIL, or IN_REVIEW");
		}
		BankAccount byAccountNo = bankAccountRepository.findByAccountNo(accountNo);
		if(byAccountNo == null) {
			throw new ResourceNotFoundException("Bank account not found for accountNo: " + accountNo);
		}

		Page<VerificationRecord> verificationRecords;
		if (action != null && status != null) {
			verificationRecords = verificationRecordRepository.findByBankAccountAccountNoAndActionAndStatus(accountNo,
					action.toUpperCase(), status, pageable);
		} else if (action != null) {
			verificationRecords = verificationRecordRepository.findByBankAccountAccountNoAndAction(accountNo,
					action.toUpperCase(), pageable);
		} else if (status != null) {
			verificationRecords = verificationRecordRepository.findByBankAccountAccountNoAndStatus(accountNo, status,
					pageable);
		} else {
			verificationRecords = verificationRecordRepository.findByBankAccountAccountNo(accountNo, pageable);
		}
		return verificationRecords.map(record -> mapToDTO(record, action != null ? action : determineAction(record)));
	}

	public byte[] getVerificationDocument(Long id, String action, String userCode) {
		if (!isValidAction(action)) {
			throw new IllegalArgumentException("Invalid action type. Must be SANCTIONS, ID, or DEATH");
		}
		VerificationRecord verificationRecord = verificationRecordRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Verification record not found for id: " + id));
		switch (action.toUpperCase()) {
		case "SANCTIONS":
			if (verificationRecord.getSanctionsDocs() == null) {
				throw new ResourceNotFoundException("No sanctions document found for id: " + id);
			}
			return verificationRecord.getSanctionsDocs();
		case "ID":
			if (verificationRecord.getIdentityDocs() == null) {
				throw new ResourceNotFoundException("No ID document found for id: " + id);
			}
			return verificationRecord.getIdentityDocs();
		case "DEATH":
			if (verificationRecord.getDeathDocs() == null) {
				throw new ResourceNotFoundException("No death document found for id: " + id);
			}
			return verificationRecord.getDeathDocs();
		default:
			throw new IllegalArgumentException("Invalid action type");
		}
	}
	private boolean isValidAction(String action) {
		return action != null && (action.equalsIgnoreCase("SANCTIONS") || action.equalsIgnoreCase("ID")
				|| action.equalsIgnoreCase("DEATH"));
	}

	private boolean isValidStatus(String status) {
		return status != null && (status.equals("PENDING") || status.equals("PASS") || status.equals("FAIL")
				|| status.equals("IN_REVIEW"));
	}

	private boolean isValidFileType(MultipartFile file) {
		String contentType = file.getContentType();
		return contentType != null && (contentType.equals("application/pdf") || contentType.equals("image/png")
				|| contentType.equals("image/jpeg"));
	}
	private VerificationRecordDTO mapToDTO(VerificationRecord record, String action) {
		VerificationRecordDTO dto = new VerificationRecordDTO();
		dto.setId(record.getId());
		dto.setAccountNo(record.getBankAccount().getAccountNo());
		dto.setCustomerNo(record.getCustomerNo());
		dto.setPolicyNumber(record.getPolicyNumber());
		dto.setAction(action.toUpperCase());
        dto.setUserCode(record.getUserCode());
		switch (action.toUpperCase()) {
		case "SANCTIONS":
			dto.setDetails(record.getSanctions());
			dto.setStatus(record.getSancStatus());
			break;
		case "ID":
			dto.setDetails(record.getIdentity());
			dto.setStatus(record.getIdentityStatus());
			break;
		case "DEATH":
			dto.setDetails(record.getDeath());
			dto.setStatus(record.getDeathStatus());
			break;
		}
		dto.setCreatedBy(record.getCreatedBy());
		dto.setUpdatedBy(record.getUpdatedBy());
		dto.setUpdatedTime(record.getUpdatedTime());
		dto.setCreatedTime(record.getCreatedTime());
		return dto;
	}

	private String determineAction(VerificationRecord record) {
		if (record.getSanctions() != null || record.getSanctionsDocs() != null) {
			return "SANCTIONS";
		} else if (record.getIdentity() != null || record.getIdentityDocs() != null) {
			return "ID";
		} else if (record.getDeath() != null || record.getDeathDocs() != null) {
			return "DEATH";
		}
		return "UNKNOWN";
	}

	public String updateStatus(Long id, VerificationRecordDTO verificationRecordDTO) {
		String message = "";
		try {
			Optional<VerificationRecord> record = verificationRecordRepository.findById(id);
			VerificationRecord verificationRecord = record.get();
			if ("ID".equalsIgnoreCase(verificationRecordDTO.getAction())) {
				verificationRecord.setIdentityStatus(verificationRecordDTO.getStatus());
			} else if ("DEATH".equalsIgnoreCase(verificationRecordDTO.getAction())) {
				verificationRecord.setDeathStatus(verificationRecordDTO.getStatus());
			} else if ("SANCTIONS".equalsIgnoreCase(verificationRecordDTO.getAction())) {
				verificationRecord.setSancStatus(verificationRecordDTO.getStatus());
			}
			verificationRecordRepository.save(verificationRecord);
			//create workitem
			WorkItemDTO workItemRequest = new WorkItemDTO();
			workItemRequest.setComment("Verification for "+ verificationRecordDTO.getAction() + verificationRecordDTO.getStatus() + " for the customer"
					
					+ verificationRecord.getCustomerNo() + "for the customer");
			workItemRequest.setCreatedBy(verificationRecord.getCreatedBy());
			workItemRequest.setuserCode(verificationRecord.getCreatedBy());
			workItemRequest.setWorkItemName(CommonConstant.POLICY_CREATED);
			workItemRequest.setCreatedTime(String.valueOf(LocalDateTime.now()));
			workItemRequest.setWorkType(CommonConstant.ADD_POL);
			workItemRequest.setStatus(CommonConstant.OPEN);
			workItemRequest.setQueue(CommonConstant.TEAM_MEMBER);
			WorkItemDTO workItem = workItemService.createWorkItem(workItemRequest, verificationRecord.getCreatedBy());
			if (workItem != null) {
				verificationRecord.setWorkItemRefNo(workItem.getWorkItemReferenceNumber());
				logger.info("WorkItemcreated succesfully");

			} else {
				new RuntimeErrorException(null, "Error while creating WorkItem");
			}
			message = "Success";
			// workitem create:
			
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return message;
	}

	public List<VerificationRecordDTO> getAllDocuments(String accountNo, String userCode) {
		 List<VerificationRecordDTO> listOfDocument= new ArrayList<>();
		if(accountNo != null) {
			List<VerificationRecord> verRecords = verificationRecordRepository.findByAccountNo(accountNo);
			if(verRecords != null) {
				listOfDocument= bankMapper.mapVerificationRecordList(verRecords);	
			}else {
				throw new RuntimeException("No record Found");
			}
		}else {
			List<VerificationRecord> verRecords = verificationRecordRepository.findAll();
			if(verRecords != null) {
				listOfDocument= bankMapper.mapVerificationRecordList(verRecords);	
			}else {
				throw new RuntimeException("No record Found");
			}
		}
		 return listOfDocument;
	}
}

