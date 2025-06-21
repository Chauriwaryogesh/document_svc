package com.SecureAccessPortal.Service;

import java.util.List;

import com.SecureAccessPortal.Entity.BankAccount;
import com.SecureAccessPortal.Entity.Customer;
import com.SecureAccessPortal.Entity.OtpStore;
import com.SecureAccessPortal.Entity.Policy;
import com.SecureAccessPortal.Entity.VerificationRecord;
import com.SecureAccessPortal.Entity.Workitem;
import com.SecureAccessPortal.Modal.WorkItemCount;
import com.SecureAccessPortal.Modal.WorkItemDTO;

public interface IWorkItemService {

	WorkItemDTO createWorkItem(WorkItemDTO workItemRequest, String userCode);

	List<WorkItemDTO> fetchWorkItems(String id, String userCode);

	WorkItemCount workItemCount(String userCode);

	Workitem mapRequetforWorkItem(String userCode, Policy policy, Customer customer, String workType, 
			String workItemName,String comment, BankAccount bankAccount, VerificationRecord verificationRecord);

	Workitem mapRequetforWorkItemOtpService(String userCode, Customer customer, String workType, String workItemName,
			String comment, OtpStore otpStore2);

}
