package com.SecureAccessPortal.Transformer;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Entity.Bank;
import com.SecureAccessPortal.Entity.Complaint;
import com.SecureAccessPortal.Entity.Customer;
import com.SecureAccessPortal.Entity.Payments;
import com.SecureAccessPortal.Entity.Policy;
import com.SecureAccessPortal.Entity.Workitem;
import com.SecureAccessPortal.Modal.Address;
import com.SecureAccessPortal.Modal.BankDetailsDTO;
import com.SecureAccessPortal.Modal.ComplaintDTO;
import com.SecureAccessPortal.Modal.ContactDetails;
import com.SecureAccessPortal.Modal.CustomerDTO;
import com.SecureAccessPortal.Modal.PaymentList;
import com.SecureAccessPortal.Modal.PaymentResponse;
import com.SecureAccessPortal.Modal.PolicyDTO;
import com.SecureAccessPortal.Modal.WorkItemDTO;
import com.SecureAccessPortal.Service.IWorkItemService;
import com.SecureAccessPortal.Service.PolicyService;

@Component
public class CustomerMapper {

	@Autowired
	private PolicyService policyServices;

	@Autowired
	private IWorkItemService workItemService;

	@Autowired
	private ComplaintMapper complaintMapper;

	public CustomerDTO mapCustomerDetails(Customer customer, List<Policy> policy, List<Workitem> workitems,
			List<Complaint> complaints, List<Bank> bankAccountList, List<Payments> payments, String userCode) {
		CustomerDTO custDTO = new CustomerDTO();
		if (customer != null) {
			custDTO.setAdminAccess(customer.getAdminAccess());
			custDTO.setAge(customer.getAge());
			custDTO.setEmail(customer.getEmail());
			custDTO.setGender(customer.getGender());
			custDTO.setCustomerNo(customer.getCustomerNo());
			custDTO.setMiddleName(customer.getMiddleName());
			custDTO.setName(customer.getName());
			custDTO.setPhoneNumber(customer.getPhoneNumber());
			custDTO.setSurname(customer.getSurname());
			custDTO.setUserCode(customer.getUserCode());
			custDTO.setSmokerStatus(customer.getSmokerStatus());
			custDTO.setDateOfBirth(customer.getDateOfBirth());
			// mapping for address
			Address address = new Address();
			address.setCity(customer.getCity());
			address.setCountry(customer.getCountry());
			address.setState(customer.getState());
			address.setStreet(customer.getStreet());
			address.setZipCode(customer.getZipCode());
			// mapping for contactDetails
			ContactDetails contact = new ContactDetails();
			contact.setAlternateEmail(customer.getAlternateEmail());
			contact.setEmergencyContactName(customer.getEmergencyContactName());
			contact.setEmergencyContactPhone(customer.getEmergencyContactPhone());
			contact.setPhoneCountryCode(customer.getPhoneCountryCode());
			contact.setPhoneNumber(customer.getPhoneNumber());
			custDTO.setAddress(address);
			custDTO.setContactDetails(contact);
			custDTO.setRoles(customer.getRoles());
		}
		if (!policy.isEmpty()) {
			List<PolicyDTO> policyList = policyServices.mapPolicyListDetails(policy, userCode);
			custDTO.setPolicy(policyList);
		}
		if (!workitems.isEmpty()) {
			List<WorkItemDTO> allWorkitems = workItemService.getAllWorkitems(workitems);
			custDTO.setWorkitems(allWorkitems);
		}
		if (!bankAccountList.isEmpty()) {
			List<BankDetailsDTO> bankAccountDTO = mapBankDetailsOfCustomer(bankAccountList, userCode);
			custDTO.setBank(bankAccountDTO);
		}
		if (!complaints.isEmpty()) {
			List<ComplaintDTO> complaintList = complaintMapper.mapComplaintData(complaints);
			custDTO.setComplaint(complaintList);
		}
		if (!payments.isEmpty()) {
			List<PaymentResponse> paymentResponse = mapForPayment(payments);
			custDTO.setPayments(paymentResponse);
		}
		return custDTO;
	}

	private List<PaymentResponse> mapForPayment(List<Payments> payments) {
		Map<String, List<Payments>> groupedByPolicy = payments.stream()
				.collect(Collectors.groupingBy(p -> p.getPolicy().getPolicyNumber()));
		List<PaymentResponse> response = groupedByPolicy.entrySet().stream().map(entry -> {
			PaymentResponse pr = new PaymentResponse();
			pr.setResponseStatus(CommonConstant.SUCCESS);
			// Get first payment to set policy details
			Payments firstPayment = entry.getValue().get(0);
			pr.setPolicyNumber(entry.getKey());
			pr.setCustomerNumber(firstPayment.getCustomer().getCustomerNo());
			pr.setEmail(firstPayment.getCustomer().getEmail());
			pr.setPolicyName(firstPayment.getPolicyName());
			pr.setPolicyType(firstPayment.getPolicy().getPolicyType());

			// Map all payments to PaymentList
			List<PaymentList> paymentList = entry.getValue().stream().map(p -> {
				PaymentList pl = new PaymentList();
				pl.setPaymentId(p.getPaymentId());
				pl.setInstallmentAmount(p.getInstallmentAmount());
				pl.setDueDate(p.getDueDate());
				pl.setPaymentDate(p.getPaymentDate());
				pl.setStatus(p.getStatus());
				pl.setTransactionId(p.getTransactionId());
				pl.setEmailStatus(p.getEmailStatus());
				return pl;
			})
					.collect(Collectors.toList());
			pr.setPaymentList(paymentList);
			return pr;
		}).collect(Collectors.toList());
		return response;
	}

	private List<BankDetailsDTO> mapBankDetailsOfCustomer(List<Bank> bankAccountList, String userCode) {
		List<BankDetailsDTO> bankList = bankAccountList.stream().filter(Objects::nonNull).map(bankAccount -> {

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
		
		}).collect(Collectors.toList());
		return bankList;
	}

}
