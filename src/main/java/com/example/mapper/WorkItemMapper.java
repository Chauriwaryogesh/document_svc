package com.example.mapper;

import org.springframework.stereotype.Component;

import com.example.dto.WorkItemDTO;
import com.example.entity.WorkItem;

@Component
public class WorkItemMapper {

	public WorkItemDTO workItemMApper(WorkItem repoData) {
		WorkItemDTO workItem = new WorkItemDTO();
		workItem.setWorkItemId(repoData.getWorkItemId());
		workItem.setComment(repoData.getComment());
		workItem.setCreatedBy(repoData.getCreatedBy());
		workItem.setCreatedTime(String.valueOf( repoData.getCreatedTime()));
		workItem.setUserId(repoData.getUserId());
		workItem.setWorkItemName(repoData.getWorkItemName());
		workItem.setWorkType(repoData.getWorkType());
		workItem.setWorkItemReferenceNumber(repoData.getWorkItemRefNumber());
		return workItem;
	}
	

}
