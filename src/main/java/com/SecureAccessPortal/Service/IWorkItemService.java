package com.SecureAccessPortal.Service;

import java.util.List;

import com.SecureAccessPortal.Modal.ActivityDetailsDTO;
import com.SecureAccessPortal.Modal.WorkItemCount;
import com.SecureAccessPortal.Modal.WorkItemDTO;

public interface IWorkItemService {

	WorkItemDTO createWorkItem(WorkItemDTO workItemRequest, String userId);

	ResponseEntity<List<ActivityDetailsDTO>> getActivityDtls(String userId);

	List<WorkItemDTO> fetchWorkItems(String id, String userId);

	WorkItemCount workItemCount(String userId);

}
