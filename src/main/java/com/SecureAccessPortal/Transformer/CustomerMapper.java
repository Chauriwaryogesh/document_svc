package com.SecureAccessPortal.Transformer;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Entity.BankAccount;
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
			List<Complaint> complaints, List<BankAccount> bankAccountList, List<Payments> payments, String userCode) {
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

	private List<BankDetailsDTO> mapBankDetailsOfCustomer(List<BankAccount> bankAccountList, String userCode) {
		List<BankDetailsDTO> bankList = bankAccountList.stream().filter(Objects::nonNull).map(bankAccount -> {
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
				dto.setCustomerName(bankAccount.getCustomer().getName() + " "
						+ bankAccount.getCustomer().getMiddleName() + " " + bankAccount.getCustomer().getSurname());
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
			dto.setAmlStatus("Active");
			dto.setAccountBalance(bankAccount.getAccountBalance());
			dto.setLinkedPaymentMethod(bankAccount.getLinkedPaymentMethod());
			dto.setVerificationAttempts(bankAccount.getVerificationAttempts());
			dto.setComment(bankAccount.getComment());
			return dto;
		}).collect(Collectors.toList());
		return bankList;
	}

}
