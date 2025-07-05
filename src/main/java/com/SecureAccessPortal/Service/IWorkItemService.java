package com.SecureAccessPortal.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.SecureAccessPortal.Entity.BankAccount;
import com.SecureAccessPortal.Entity.Customer;
import com.SecureAccessPortal.Entity.OtpStore;
import com.SecureAccessPortal.Entity.Payments;
import com.SecureAccessPortal.Entity.Policy;
import com.SecureAccessPortal.Entity.VerificationRecord;
import com.SecureAccessPortal.Entity.Workitem;
import com.SecureAccessPortal.Modal.WorkItemCount;
import com.SecureAccessPortal.Modal.WorkItemDTO;

public interface IWorkItemService {

	WorkItemDTO createWorkItem(WorkItemDTO workItemRequest, String userCode);

	Page<WorkItemDTO> fetchWorkItems(String id, String queue, String filterUserCode, String createdBy, String status,
			String startDate, String endDate, int page, int size, String userCode);

	WorkItemCount workItemCount(String userCode);

	Workitem mapRequetforWorkItem(String userCode, Policy policy, Customer customer, String workType,
			String workItemName, String comment, BankAccount bankAccount, VerificationRecord verificationRecord,
			Payments payments);

	Workitem mapRequetforWorkItemOtpService(String userCode, Customer customer, String workType, String workItemName,
			String comment, OtpStore otpStore2);

	List<String> fetchWorkType(String userCode);

	Page<WorkItemDTO> fetchRelatedWorkitems(String workitemRefNo, int page, int size, String policyRelated,
			String customerRelated, String userCode);

}
