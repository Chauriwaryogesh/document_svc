package com.SecureAccessPortal.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.CommonConstants.ErrorConstants;
import com.SecureAccessPortal.Entity.Bank;
import com.SecureAccessPortal.Entity.Customer;
import com.SecureAccessPortal.Entity.Policy;
import com.SecureAccessPortal.Entity.VerificationRecord;
import com.SecureAccessPortal.Exception.ResourceNotFoundException;
import com.SecureAccessPortal.Modal.BankDetailsDTO;
import com.SecureAccessPortal.Modal.DashboardStats;
import com.SecureAccessPortal.Modal.PolicyRequest;
import com.SecureAccessPortal.Modal.VerificationRecordDTO;
import com.SecureAccessPortal.Repo.BankAccountRepo;
import com.SecureAccessPortal.Repo.CustomerRepo;
import com.SecureAccessPortal.Repo.IPolicyRepo;
import com.SecureAccessPortal.Repo.VerificationRecordRepo;
import com.SecureAccessPortal.Repo.WorkItemRepo;
import com.SecureAccessPortal.Transformer.BankMapper;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;

@Service
public class BankService {

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
	private WorkItemRepo workItemRepo;

	@Autowired
	private VerificationRecordRepo verificationRecordRepository;

	@Autowired
	private Environment environment;

	public BankService(BankAccountRepo bankAccountRepository) {
		this.bankAccountRepository = bankAccountRepository;
	}

	public Page<BankDetailsDTO> getBankDetails(String bankAccNo, String policyNo, String customerNo, String holderName,
			String bankName, String accountType, String status, LocalDate createdDateFrom, LocalDate createdDateTo,
			String globalSearch, String userCode, int page, int size, String deletedFlag, String bankId) {

		Pageable pageable = PageRequest.of(page, size);

		Specification<Bank> spec = (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			// --- Joins (for policy and customer)
			Join<Bank, Policy> policyJoin = root.join("policy", JoinType.LEFT);
			Join<Bank, Customer> customerJoin = root.join("customer", JoinType.LEFT);

			if (bankId != null && !bankId.isEmpty()) {
			    predicates.add(cb.equal(cb.lower(root.get("bankId")), bankId.toLowerCase()));
			}
			
			// --- Search by Account No
			if (bankAccNo != null && !bankAccNo.isEmpty()) {
			    predicates.add(cb.equal(cb.lower(root.get("accountNumber")), bankAccNo.toLowerCase()));
			}


			// --- Search by Policy No
			if (policyNo != null && !policyNo.isEmpty()) {
				predicates.add(cb.like(cb.lower(policyJoin.get("policyNumber")), policyNo.toLowerCase()));
			}

			// --- Search by Customer No
			if (customerNo != null && !customerNo.isEmpty()) {
				predicates.add(cb.like(cb.lower(customerJoin.get("customerNo")), customerNo.toLowerCase()));
			}

			// --- Search by Holder Name
			if (holderName != null && !holderName.isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("accountHolderName")), holderName.toLowerCase()));
			}

			// --- Search by Bank Name
			if (bankName != null && !bankName.isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("bankName")), bankName.toLowerCase() ));
			}

			// --- Search by Account Type
			if (accountType != null && !accountType.isEmpty()) {
				predicates.add(cb.equal(root.get("accountType"), accountType));
			}

			// --- Deleted flag always N
			if(CommonConstant.Y.equalsIgnoreCase(deletedFlag)) {
				predicates.add(cb.equal(root.get("deletedFlag"), "Y"));
			}else {
				predicates.add(cb.equal(root.get("deletedFlag"), "N"));
			}
			// --- Status filter (multiple comma-separated values supported)
			if (status != null && !status.isEmpty()) {
					predicates.add(cb.equal(root.get("status"), status.trim()));
			}
			// --- Created Date Range
			if (createdDateFrom != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate"), createdDateFrom.atStartOfDay()));
			}
			if (createdDateTo != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("createdDate"),
						createdDateTo.plusDays(1).atStartOfDay().minusNanos(1)));
			}

			// --- Global Search across multiple fields
			if (globalSearch != null && !globalSearch.isEmpty()) {
				String searchLike = "%" + globalSearch.toLowerCase() + "%";
				Predicate globalPredicate = cb.or(cb.like(cb.lower(root.get("accountNumber")), searchLike),
						cb.like(cb.lower(policyJoin.get("policyNumber")), searchLike),
						cb.like(cb.lower(customerJoin.get("customerNo")), searchLike),
						cb.like(cb.lower(root.get("accountHolderName")), searchLike),
						cb.like(cb.lower(root.get("bankName")), searchLike));
				predicates.add(globalPredicate);
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};

		Page<Bank> bankPage = bankAccountRepository.findAll(spec, pageable);
		return bankPage.map(this::convertToDTO);
	}

	public BankDetailsDTO convertToDTO(Bank bankAccount) {
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



	public String addBankDetails(BankDetailsDTO bankAccount, String userCode) {
	String message = "";
	Customer customer = null;
	Policy policy = null;
	try {
	    if (bankAccount.getAccountNumber() == null) {
	        return "Account number is mandatory.";
	    }
	    Bank existing = bankAccountRepository.findByAccountNo(bankAccount.getAccountNumber(), "N");
	    if (existing == null) {
	        // CREATE
	        Bank dto = new Bank();
	        dto.setBankId(generateBankId()); // New Bank ID
	        dto.setAccountNumber(bankAccount.getAccountNumber());
	        dto.setStatus(CommonConstant.IN_PROGRESS);
	        dto.setDeletedFlag("N");

	        // Core Details
	        if (bankAccount.getBankName() != null) dto.setBankName(bankAccount.getBankName());
	        if (bankAccount.getIfscCode() != null) dto.setIfscCode(bankAccount.getIfscCode());
	        if (bankAccount.getSwiftCode() != null) dto.setSwiftCode(bankAccount.getSwiftCode());
	        if (bankAccount.getBranchCode() != null) dto.setBranchCode(bankAccount.getBranchCode());
	        if (bankAccount.getAccountType() != null) dto.setAccountType(bankAccount.getAccountType());
	        if (bankAccount.getCurrency() != null) dto.setCurrency(bankAccount.getCurrency());
	        if (bankAccount.getAccountBalance() != null) dto.setAccountBalance(bankAccount.getAccountBalance());
	        if (bankAccount.getIsDefaultAccount() != null) dto.setIsDefaultAccount(bankAccount.getIsDefaultAccount());
	        if (bankAccount.getAccountHolderName() != null) dto.setAccountHolderName(bankAccount.getAccountHolderName());
	        if (bankAccount.getAccountHolderType() != null) dto.setAccountHolderType(bankAccount.getAccountHolderType());
	        if (bankAccount.getAccountOpeningDate() != null) dto.setAccountOpeningDate(bankAccount.getAccountOpeningDate());
	        if (bankAccount.getAccountClosingDate() != null) dto.setAccountClosingDate(bankAccount.getAccountClosingDate());
	        else dto.setAccountOpeningDate(LocalDateTime.now()); // default

	        // Relations
	        if (bankAccount.getPolicyNumber() != null) {
	            policy = policyRepo.findByPolicyNum(bankAccount.getPolicyNumber(), "N");
	            dto.setPolicy(policy);
	            if (policy != null) {
	                dto.setCustomer(policy.getCustomer());
	            }
	        }
	        // KYC
	        try {
	        if (bankAccount.getKycDocument() != null) {
	            byte[] kycBytes = Base64.getDecoder().decode(bankAccount.getKycDocument());
	            dto.setKycDocument(kycBytes);
	            dto.setLastVerificationDate(LocalDateTime.now());
		        dto.setVerificationAttempts(1);
		        dto.setKycStatus(bankAccount.getKycStatus() != null ? bankAccount.getKycStatus() : CommonConstant.IN_PROGRESS);
		        dto.setKycDocumentStatus(bankAccount.getKycDocumentStatus() != null ? bankAccount.getKycDocumentStatus() : CommonConstant.IN_PROGRESS);
		        dto.setAmlStatus(CommonConstant.APPROVED);
		        dto.setPaymentMethodStatus(CommonConstant.BANK_TRANSFER);
		        dto.setLinkedPaymentMethod(CommonConstant.BANK_TRANSFER);
	        }
	    } catch (IllegalArgumentException ex) {
	        return "Invalid KYC document format (not valid Base64).";
	    }
	        // Payment
	        if (bankAccount.getLastPaymentDate() != null) dto.setLastPaymentDate(bankAccount.getLastPaymentDate());
	        if (bankAccount.getVerifierComment() != null) dto.setVerifierComment(bankAccount.getVerifierComment());
	        if (bankAccount.getCustomerComment() != null) dto.setCustomerComment(bankAccount.getCustomerComment());
	        dto.setCreatedBy(userCode);
	        dto.setCreatedDate(LocalDateTime.now());
	        Bank saved = bankAccountRepository.save(dto);
	        message = "Successfully Added bank account " + dto.getAccountNumber();
	    } else {
	        // UPDATE
	        Bank dto = existing;
	        // Core Details
	        if (bankAccount.getBankName() != null) dto.setBankName(bankAccount.getBankName());
	        if (bankAccount.getIfscCode() != null) dto.setIfscCode(bankAccount.getIfscCode());
	        if (bankAccount.getSwiftCode() != null) dto.setSwiftCode(bankAccount.getSwiftCode());
	        if (bankAccount.getBranchCode() != null) dto.setBranchCode(bankAccount.getBranchCode());
	        if (bankAccount.getAccountType() != null) dto.setAccountType(bankAccount.getAccountType());
	        if (bankAccount.getCurrency() != null) dto.setCurrency(bankAccount.getCurrency());
	        if (bankAccount.getStatus() != null) dto.setStatus(bankAccount.getStatus());
	        if (bankAccount.getAccountBalance() != null) dto.setAccountBalance(bankAccount.getAccountBalance());
	        if (bankAccount.getIsDefaultAccount() != null) dto.setIsDefaultAccount(bankAccount.getIsDefaultAccount());
	        if (bankAccount.getAccountHolderName() != null) dto.setAccountHolderName(bankAccount.getAccountHolderName());
	        if (bankAccount.getAccountHolderType() != null) dto.setAccountHolderType(bankAccount.getAccountHolderType());
	        if (bankAccount.getAccountOpeningDate() != null) dto.setAccountOpeningDate(bankAccount.getAccountOpeningDate());
	        if (bankAccount.getAccountClosingDate() != null) dto.setAccountClosingDate(bankAccount.getAccountClosingDate());

	        // Relations
	        if (bankAccount.getPolicyNumber() != null) {
	            policy = policyRepo.findByPolicyNum(bankAccount.getPolicyNumber(), "N");
	            dto.setPolicy(policy);
	            if (policy != null) {
	                dto.setCustomer(policy.getCustomer());
	            }
	        }

	        // KYC
	        try {
	        if (bankAccount.getKycStatus() != null) dto.setKycStatus(bankAccount.getKycStatus());
	        if (bankAccount.getKycDocumentStatus() != null) dto.setKycDocumentStatus(bankAccount.getKycDocumentStatus());
	        if (bankAccount.getKycDocument() != null) {
	            byte[] kycBytes = Base64.getDecoder().decode(bankAccount.getKycDocument());
	            dto.setKycDocument(kycBytes);
	            dto.setAmlStatus("Y");
	            dto.setVerificationAttempts(1);
		        dto.setLastVerificationDate(LocalDateTime.now());
		        dto.setPaymentMethodStatus(CommonConstant.BANK_TRANSFER);
		        dto.setLinkedPaymentMethod(CommonConstant.BANK_TRANSFER);
	        }
	        } catch (IllegalArgumentException ex) {
	            return "Invalid KYC document format (not valid Base64).";
	        }
	        // Payment
	        if (bankAccount.getLastPaymentDate() != null) dto.setLastPaymentDate(bankAccount.getLastPaymentDate());
	        if (bankAccount.getVerifierComment() != null) dto.setVerifierComment(bankAccount.getVerifierComment());
	        if (bankAccount.getCustomerComment() != null) dto.setCustomerComment(bankAccount.getCustomerComment());

	        // Audit
	        dto.setUpdatedBy(userCode);
	        dto.setUpdatedDate(LocalDateTime.now());
	        bankAccountRepository.save(dto);
	        message = "Successfully updated bank account " + dto.getAccountNumber();
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	    message = "Error: " + e.getMessage();
	}
	return message;
}


	public List<String> getBankNames(String userCode) {
		List<String> bankDetails = new ArrayList<>();
		String[] bankNames = { "HDFC Bank", "ICICI Bank", "State Bank of India", "Axis Bank", "Kotak Mahindra Bank",
				"Punjab National Bank", "Bank of Baroda", "Canara Bank", "Union Bank of India", "Yes Bank" };
		for (String bank : bankNames) {
			for (int i = 1; i <= 10; i++) {
				String ifsc = String.format("%sIFSC%04d", bank.substring(0, 4).toUpperCase(), i);
				String branchCode = String.format("%sBR%04d", bank.substring(0, 4).toUpperCase(), i);
				bankDetails.add(String.format("%s - IFSC: %s - Branch: %s", bank, ifsc, branchCode));
			}
		}
		Map<String, List<String>> groupedByBank = bankDetails.stream()
				.collect(Collectors.groupingBy(detail -> detail.split(" - ")[0], // Extract bank name from the string
						Collectors.toList()));
		List<String> result = new ArrayList<>();
		groupedByBank.forEach((bank, details) -> {
			result.add("Bank: " + bank);
			details.forEach(detail -> result.add("  " + detail));
		});
		return result;
	}

	public List<String> getBranchCodes(String userCode) {
		List<String> bankNames = List.of("HDFC00001234", "HDFC700989", "HDFC700989", "HDFC700979", "HDFC700990");
		return bankNames;
	}

	public PolicyRequest getCustomerDetails(String policyNo, String customerNo, String userCode) {
		PolicyRequest customer = new PolicyRequest();
		if (policyNo != null) {
			Policy policyNum = policyRepo.findByPolicyNum(policyNo, "N");
			if (policyNum != null) {
				Customer byCustomerNoNew = customerRepo.findByCustomerNoNew(policyNum.getCustomer().getCustomerNo(),
						"N");
				customer.setCustomerName(byCustomerNoNew.getName() + " " + byCustomerNoNew.getSurname());
				customer.setCustomerNo(byCustomerNoNew.getCustomerNo());
				customer.setDateOfBirth(byCustomerNoNew.getDateOfBirth());
				customer.setEmail(byCustomerNoNew.getEmail());
				customer.setUserCode(byCustomerNoNew.getUserCode());
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
		Bank bankAccount = bankAccountRepository.findByAccountNo(accountNo, "N");
		if (bankAccount == null) {
			throw new ResourceNotFoundException("Bank account not found for accountNo: " + accountNo);
		}
		VerificationRecord verificationRecord = new VerificationRecord();
		verificationRecord.setBank(bankAccount);
		verificationRecord.setCustomer(bankAccount.getCustomer());
		verificationRecord.setPolicy(bankAccount.getPolicy());
		verificationRecord.setUserCode(userCode);
		verificationRecord.setCreatedBy(userCode);
		verificationRecord.setUpdatedBy(userCode);
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
		verificationRecord.setVerId(generateVerId()); // Set ver
		VerificationRecord verificationRecordNew = verificationRecordRepository.save(verificationRecord);
		// call workitem Service to generate WIrefNo.
		String workType = CommonConstant.VERIFICATION_RECORD;
		String workItemName = CommonConstant.VER_REC_WORKITEM;
		String comment = "Verification Record  is created " + verificationRecordNew.getId() + " and customer Number is"
				+ bankAccount.getCustomer().getCustomerNo();
		String status1 = CommonConstant.OPEN;
		workItemService.mapRequetforWorkItem(userCode, bankAccount.getPolicy(), bankAccount.getCustomer(), workType,
				workItemName, comment, verificationRecordNew.getBank(), verificationRecordNew, null, status1);

		return mapToDTO(verificationRecordNew, action);
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
		Bank byAccountNo = bankAccountRepository.findByAccountNo(accountNo, "N");
		if (byAccountNo == null) {
			throw new ResourceNotFoundException("Bank account not found for accountNo: " + accountNo);
		}

		Page<VerificationRecord> verificationRecords;
		if (action != null && status != null) {
			verificationRecords = verificationRecordRepository.findByBankAccountNumberAndActionAndStatus(accountNo,
					action.toUpperCase(), status, pageable);
		} else if (action != null) {
			verificationRecords = verificationRecordRepository.findByBankAccountNumberAndAction(accountNo,
					action.toUpperCase(), pageable);
		} else if (status != null) {
			verificationRecords = verificationRecordRepository.findByBankAccountNumberAndStatus(accountNo, status,
					pageable);
		} else {
			verificationRecords = verificationRecordRepository.findByBankAccountNumber(accountNo, pageable);
		}
		return verificationRecords.map(record -> mapToDTO(record, action != null ? action : determineAction(record)));
	}

	public byte[] getVerificationDocument(String id, String action, String userCode) {
		if (!isValidAction(action)) {
			throw new IllegalArgumentException("Invalid action type. Must be SANCTIONS, ID, or DEATH");
		}
		Optional<VerificationRecord> verRec = verificationRecordRepository.findByVerId(id);
		if (verRec.isEmpty()) {
			throw new ResourceNotFoundException("Verification record not found for id: " + id);
		}
		VerificationRecord verificationRecord = verRec.get();
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
		dto.setId(record.getVerId());
		dto.setAccountNo(record.getBank().getAccountNumber());
		dto.setCustomerNo(record.getCustomer().getCustomerNo());
		dto.setPolicyNumber(record.getPolicy().getPolicyNumber());
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
		dto.setUpdatedDate(record.getUpdatedTime());
		dto.setCreatedDate(record.getCreatedTime());
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

	public String updateStatus(String id, VerificationRecordDTO verificationRecordDTO, String userCode) {
		String message = "";
		try {
			Optional<VerificationRecord> record = verificationRecordRepository.findByVerId(id);
			VerificationRecord verificationRecord = record.get();
			if ("ID".equalsIgnoreCase(verificationRecordDTO.getAction())) {
				verificationRecord.setIdentityStatus(verificationRecordDTO.getStatus());
			} else if ("DEATH".equalsIgnoreCase(verificationRecordDTO.getAction())) {
				verificationRecord.setDeathStatus(verificationRecordDTO.getStatus());
			} else if ("SANCTIONS".equalsIgnoreCase(verificationRecordDTO.getAction())) {
				verificationRecord.setSancStatus(verificationRecordDTO.getStatus());
			}
			verificationRecordRepository.save(verificationRecord);
			String workType = CommonConstant.VERIFICATION_RECORD;
			String workItemName = CommonConstant.VER_REC_WORKITEM;
			String status = CommonConstant.APPROVED;
			message = "Verification Record is created " + verificationRecord.getId() + " and customer Number is"
					+ verificationRecord.getCustomer().getCustomerNo() + "action is" + verificationRecordDTO.getAction()
					+ "Status is" + verificationRecordDTO.getStatus();
			workItemService.mapRequetforWorkItem(userCode, verificationRecord.getPolicy(),
					verificationRecord.getCustomer(), workType, workItemName, message,
					verificationRecord.getBank(), verificationRecord, null, status);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return message;
	}

	public List<VerificationRecordDTO> getAllDocuments(String accountNo, String userCode) {
		List<VerificationRecordDTO> listOfDocument = new ArrayList<>();
		if (accountNo != null) {
			List<VerificationRecord> verRecords = verificationRecordRepository.findByBankAccountNumber(accountNo);
			if (verRecords != null) {
				listOfDocument = bankMapper.mapVerificationRecordList(verRecords);
			} else {
				throw new RuntimeException("No record Found");
			}
		} else {
			List<VerificationRecord> verRecords = verificationRecordRepository.findAll();
			if (verRecords != null) {
				listOfDocument = bankMapper.mapVerificationRecordList(verRecords);
			} else {
				throw new RuntimeException("No record Found");
			}
		}
		return listOfDocument;
	}

	@Transactional
	public synchronized String generateVerId() {
		String lastVerId = verificationRecordRepository.findTopVerId();
		int nextSequenceNumber = 1;
		if (lastVerId != null && lastVerId.startsWith("REC_")) {
			try {
				String numberPart = lastVerId.substring(4); // Extract number after "REC_"
				nextSequenceNumber = Integer.parseInt(numberPart) + 1;
			} catch (NumberFormatException e) {
				logger.warn("Could not parse sequence number from last ver_Id: {}. Starting sequence from 1.",
						lastVerId, e);
				nextSequenceNumber = 1;
			}
		}
		String newVerId = String.format("REC_%d", nextSequenceNumber);
		logger.info("Generated new ver_Id: {}", newVerId);
		return newVerId;
	}

	public DashboardStats getCount(String userCode) {
		DashboardStats dashboardStats = new DashboardStats();
		dashboardStats.setTotalAccounts(12);
		dashboardStats.setVerifiedAccounts(12);
		dashboardStats.setPendingVerifications(22);
		dashboardStats.setRejectedBankAccount(9);
		return dashboardStats;
	}

	public String verifyBankAccount(BankDetailsDTO bankDetailsDTO, String userCode) {
		String message = CommonConstant.FAILURE;
//		Bank bankAccount = bankAccountRepository.findByAccountNo(bankDetailsDTO.getAccountNumber(), "N");
//		bankAccount.setAccountNumber(bankDetailsDTO.getAccountNumber());
//		bankAccount.setIfscCode(bankDetailsDTO.getIfscCode());
//		bankAccount.setBankName(bankDetailsDTO.getBankName());
//		bankAccount.setAccountHolderName(bankDetailsDTO.getCustomerName());
//		bankAccount.setVerificationAttempts(bankDetailsDTO.getVerificationAttempts());
//		bankAccount.setLastVerificationDate(bankDetailsDTO.getLastVerificationDate());
//		bankAccount.setAccountType(bankDetailsDTO.getAccountType());
//		bankAccount.setStatus(bankDetailsDTO.getStatus());
//		bankAccount.setUpdatedBy(userCode);
//		bankAccount.setUpdatedDate(LocalDateTime.now());
//
//		if (bankDetailsDTO.getAction().equalsIgnoreCase("update")) {
//			bankAccount.setVerifierComment(bankDetailsDTO.getVerifierComment());
//			if ("PASS".equalsIgnoreCase(bankDetailsDTO.getVerificationStatus())) {
//				bankAccount.setAmlStatus(bankDetailsDTO.getVerificationStatus());
//				bankAccount.setStatus(bankDetailsDTO.getStatus());
//				bankAccountRepository.save(bankAccount);
//				message = " Successfully account verified";
//				return message;
//			} else {
//				bankAccount.setAmlStatus("FAILED");
//				bankAccount.setStatus(bankDetailsDTO.getStatus());
//				bankAccountRepository.save(bankAccount);
//				message = " Failed account Verification";
//				return message;
//			}
//		}
//		if (bankDetailsDTO.getAction().equalsIgnoreCase("verify")) {
//			String verify = environment.getProperty("VERIFICATION");
//			return verify;
//		}

		return message;
	}

	public ResponseEntity<BankDetailsDTO> updateBankAccountStatus(BankDetailsDTO bankDetailsDTO, String userCode) {
		ResponseEntity<BankDetailsDTO> response = new ResponseEntity<BankDetailsDTO>();
		Bank bankAccount= null;
		BankDetailsDTO bankDetails= new BankDetailsDTO();
		try {
		if(CommonConstant.RESTORE.equalsIgnoreCase(bankDetailsDTO.getAction())) {
			 bankAccount = bankAccountRepository.findByAccountNo(bankDetailsDTO.getAccountNumber(), "Y");
			 bankAccount.setDeletedFlag(CommonConstant.N);
				bankAccount.setStatus(CommonConstant.REJECTED);
				bankAccount.setVerifierComment(CommonConstant.REJECTED);
		}else {
			bankAccount = bankAccountRepository.findByAccountNo(bankDetailsDTO.getAccountNumber(), "N");
			if(CommonConstant.Y.equalsIgnoreCase(bankDetailsDTO.getDeletedFlag())) {
				bankAccount.setDeletedFlag(CommonConstant.Y);
				bankAccount.setStatus(CommonConstant.REJECTED);
				bankAccount.setVerifierComment(CommonConstant.REJECTED);
			}else {
				bankAccount.setStatus(bankDetailsDTO.getStatus());
				bankAccount.setVerifierComment(CommonConstant.APPROVED);
			}
		}
		bankAccount.setUpdatedBy(bankDetailsDTO.getUpdatedBy());
		bankAccount.setUpdatedDate(LocalDateTime.now());
		Bank save = bankAccountRepository.save(bankAccount);
		bankDetails.setAccountNumber(save.getAccountNumber());
		bankDetails.setStatus(save.getStatus());
		response.setData(bankDetails);
		response.setStatus(CommonConstant.SUCCESS);
		}catch(Exception e) {
			response.setStatus(ErrorConstants.FAILURE);
			e.printStackTrace();
		}
		return response;

	}
	
	@Transactional
	public synchronized String generateBankId() {
	    String currentYear = String.valueOf(Year.now().getValue()); // e.g., "2025"
	    String lastPolicyNo = bankAccountRepository.findTopBankForCurrentYear(currentYear);
	    int nextSequenceNumber = 1;
	    if (lastPolicyNo != null && lastPolicyNo.contains("/")) {
	        try {
	            String numberPart = lastPolicyNo.split("/")[1]; 
	            nextSequenceNumber = Integer.parseInt(numberPart) + 1;
	        } catch (NumberFormatException e) {
	            logger.warn("Could not parse sequence number from last policy number: {}. Resetting to 001.", lastPolicyNo, e);
	            nextSequenceNumber = 1;
	        }
	    }
	    String newPolicyNumber = String.format("BANK/%03d/%s", nextSequenceNumber, currentYear);
	    logger.info("Generated new policy number: {}", newPolicyNumber);

	    return newPolicyNumber;
	}
}
