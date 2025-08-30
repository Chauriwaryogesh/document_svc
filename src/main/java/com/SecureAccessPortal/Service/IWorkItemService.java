package com.SecureAccessPortal.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.SecureAccessPortal.Entity.Bank;
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

	Page<WorkItemDTO> fetchWorkItems(String id, String queue, String customerNo, String policyNo, String filterUserCode,
			String createdBy, String status, String startDate, String endDate, int page, int size, String userCode);

	WorkItemCount workItemCount(String customerNo, String userCode);

	Workitem mapRequetforWorkItem(String userCode, Policy policy, Customer customer, String workType,
			String workItemName, String comment, Bank bankAccount, VerificationRecord verificationRecord,
			Payments payments, String status);

	Workitem mapRequetforWorkItemOtpService(String userCode, Customer customer, String workType, String workItemName,
			String comment, OtpStore otpStore2);

	List<String> fetchWorkType(String userCode);

	Page<WorkItemDTO> fetchRelatedWorkitems(String workitemRefNo, int page, int size, String policyRelated,
			String customerRelated, String userCode);

	WorkItemDTO updateWorkItem(WorkItemDTO workItemRequest, String userCode);

	List<WorkItemDTO> getAllWorkitems(List<Workitem> workitems);

}
