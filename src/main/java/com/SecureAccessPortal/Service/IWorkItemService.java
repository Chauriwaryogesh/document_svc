package com.SecureAccessPortal.Service;

import java.util.List;

import com.SecureAccessPortal.Entity.BankAccount;
import com.SecureAccessPortal.Entity.Customer;
import com.SecureAccessPortal.Entity.Policy;
import com.SecureAccessPortal.Entity.VerificationRecord;
import com.SecureAccessPortal.Entity.Workitem;
import com.SecureAccessPortal.Modal.ActivityDetailsDTO;
import com.SecureAccessPortal.Modal.WorkItemCount;
import com.SecureAccessPortal.Modal.WorkItemDTO;

public interface IWorkItemService {

	WorkItemDTO createWorkItem(WorkItemDTO workItemRequest, String userCode);

	ResponseEntity<List<ActivityDetailsDTO>> getActivityDtls(String userCode);

	List<WorkItemDTO> fetchWorkItems(String id, String userCode);

	WorkItemCount workItemCount(String userCode);

	Workitem mapRequetforWorkItem(String userCode, Policy policy, Customer customer, String workType, 
			String workItemName,String comment, BankAccount bankAccount, VerificationRecord verificationRecord);

}
