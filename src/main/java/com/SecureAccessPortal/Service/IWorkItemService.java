package com.SecureAccessPortal.Service;

import java.util.List;

import com.SecureAccessPortal.Modal.ActivityDetailsDTO;
import com.SecureAccessPortal.Modal.WorkItemCount;
import com.SecureAccessPortal.Modal.WorkItemDTO;

public interface IWorkItemService {

	WorkItemDTO createWorkItem(WorkItemDTO workItemRequest, String userCode);

	ResponseEntity<List<ActivityDetailsDTO>> getActivityDtls(String userCode);

	List<WorkItemDTO> fetchWorkItems(String id, String userCode);

	WorkItemCount workItemCount(String userCode);

}
