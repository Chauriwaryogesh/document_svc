package com.SecureAccessPortal.Transformer;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.SecureAccessPortal.Entity.BankAccount;
import com.SecureAccessPortal.Entity.Customer;
import com.SecureAccessPortal.Entity.Policy;
import com.SecureAccessPortal.Entity.Workitem;
import com.SecureAccessPortal.Modal.BankAccountDTO;
import com.SecureAccessPortal.Modal.PolicyDTO;
import com.SecureAccessPortal.Modal.PolicyList;
import com.SecureAccessPortal.Repo.BankAccountRepo;
import com.SecureAccessPortal.Service.ResponseEntity;

@Component
public class PolicyMapper {

	@Autowired
	private BankAccountRepo bankAccountRepository;
	
	public ResponseEntity<List<PolicyDTO>> mapAllPolicies(List<Customer> customer, List<Policy> policies) {

		ResponseEntity<List<PolicyDTO>> reponse = new ResponseEntity<>();
		List<PolicyDTO> policyListResp=customer.stream().map(cust -> {
			PolicyDTO policyDTO = new PolicyDTO();
			policyDTO.setCustomerNo(cust.getCustomerNo());
			policyDTO.setCustomerName(cust.getName() + "" + cust.getSurname());
			List<PolicyList> policyList = new ArrayList<>();
			policies.stream()
					.filter(policy -> policy.getCustomer().getCustomerNo().equalsIgnoreCase(cust.getCustomerNo()))
					.forEach(policy -> {
						PolicyList pol = new PolicyList();
						pol.setPolicyNumber(policy.getPolicyNumber());
						pol.setPolicyType(policy.getPolicyType());
						pol.setId(policy.getId());
						pol.setFcuFlag(policy.getFcuFlag() != null ? policy.getFcuFlag() : "");
						pol.setPolicyNumber(policy.getPolicyNumber() != null ? policy.getPolicyNumber() : "");
						pol.setPolCompanyName(policy.getPolCompanyName() != null ? policy.getPolCompanyName() : "");
						pol.setPolicyName(policy.getPolicyName() != null ? policy.getPolicyName() : "");
						pol.setProductCode(policy.getProductCode() != null ? policy.getProductCode() : "");
						pol.setCreatedBy(policy.getCreatedBy() != null ? policy.getCreatedBy() : "");
						pol.setCreatedDate(String.valueOf(policy.getCreatedTime()));
						pol.setWorkItemRefNo(policy.getWorkitems() != null
								? policy.getWorkitems().stream().map(Workitem::getWorkItemRefNumber).collect(Collectors.toList())
								: new ArrayList<>());
						pol.setUpdatedBy(policy.getUpdatedBy() != null ? policy.getUpdatedBy() : "");
						pol.setUserCode(policy.getUserCode() != null ? policy.getUserCode() : "");
						pol.setDeletedFlag(policy.getDeletedFlag() != null ? policy.getDeletedFlag() : "");
						pol.setPolicyType(policy.getPolicyType() != null ? policy.getPolicyType() : "");
						pol.setPolicyPremium(policy.getPolicyPremium() != null ? policy.getPolicyPremium() : BigDecimal.ZERO);
						pol.setPremium(policy.getPolicyPremium() != null ? policy.getPolicyPremium() : BigDecimal.ZERO); // Added for
																															// frontend
						pol.setPolicyStatus(policy.getPolicyStatus() != null ? policy.getPolicyStatus() : "");
						pol.setCoverageAmount(policy.getCoverageAmount() != null ? policy.getCoverageAmount() : BigDecimal.ZERO);
						pol.setCustomerNo(policy.getCustomer() != null ? policy.getCustomer().getCustomerNo() : "");
						pol.setBeneficiaryName(policy.getBeneficiaryName() != null ? policy.getBeneficiaryName() : "");
						pol.setBeneficiaryRelationship(
								policy.getBeneficiaryRelationship() != null ? policy.getBeneficiaryRelationship() : "");
						pol.setComplianceFlag(policy.getComplianceFlag() != null ? policy.getComplianceFlag() : "");
						pol.setPaymentFrequency(policy.getPaymentFrequency() != null ? policy.getPaymentFrequency() : "");
						pol.setSmokerStatus(policy.getSmokerStatus() != null ? policy.getSmokerStatus() : "");
						pol.setPolicyAmount(
								String.valueOf(policy.getPolicyPremium() != null ? policy.getPolicyPremium() : BigDecimal.ZERO));
						pol.setInstallmentCount(String.valueOf(policy.getPolicyfrequency() != null ? policy.getPolicyfrequency() : ""));
						pol.setFrequency(policy.getPolicyfrequency() != null ? policy.getPolicyfrequency() : "");
						pol.setPolicyTerm(policy.getPolicyTerm() != null ? policy.getPolicyTerm() : "");
						pol.setTerm(policy.getPolicyTerm() != null ? policy.getPolicyTerm() : "");
						pol.setTotalAmount(policy.getTotalAmount());
						pol.setMonthlyInstallment(policy.getMonthlyInstallment());
						pol.setTotalClaimableAmount(policy.getTotalClaimableAmount());
						pol.setBeneficiaryAadharNumber(
								policy.getBeneficiaryIdentityNumber() != null ? policy.getBeneficiaryIdentityNumber() : "");
						pol.setNomineeContactNumber(
								policy.getBeneficiaryContactNumber() != null ? policy.getBeneficiaryContactNumber() : "");
						pol.setStatus(policy.getPolicyStatus() != null ? policy.getPolicyStatus() : "");
						pol.setType(policy.getPolicyType() != null ? policy.getPolicyType() : "");
						pol.setPremiumDueDate(String.valueOf(policy.getPremiumDueDate()));
						pol.setRenewalDate(String.valueOf(policy.getRenewalDate()));
						pol.setStartDate(String.valueOf(policy.getPolicyStartDate()));
						pol.setEndDate(String.valueOf(policy.getPolicyEndDate()));
						pol.setPolicyDate(String.valueOf(policy.getPolicyStartDate()));
						pol.setDueDate(String.valueOf(policy.getPremiumDueDate()));

						List<BankAccount> bankAccounts = bankAccountRepository.findByPolicyNumber(policy.getPolicyNumber());
						pol.setBankAccounts(
								bankAccounts != null ? bankAccounts.stream().map(this::mapToBankAccountDTO).collect(Collectors.toList())
										: new ArrayList<>());
						policyList.add(pol);
					});
			policyDTO.setPolicyList(policyList);
			return policyDTO;
		}).collect(Collectors.toList());
          reponse.setData(policyListResp);
		return reponse;
	}
	
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

}
