package com.example.service;

import java.util.List;

import com.example.dto.ActivityDetailsDTO;
import com.example.dto.WorkItemCount;
import com.example.dto.WorkItemDTO;

public interface IWorkItemService {

	WorkItemDTO createWorkItem(WorkItemDTO workItemRequest, String userId);

	ResponseEntity<List<ActivityDetailsDTO>> getActivityDtls(String userId);

	List<WorkItemDTO> fetchWorkItems(String id, String userId);

	WorkItemCount workItemCount(String userId);

}
