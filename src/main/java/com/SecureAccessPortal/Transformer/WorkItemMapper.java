package com.SecureAccessPortal.Transformer;

import org.springframework.stereotype.Component;

import com.SecureAccessPortal.Entity.Workitem;
import com.SecureAccessPortal.Modal.WorkItemDTO;

@Component
public class WorkItemMapper {

	public WorkItemDTO workItemMApper(Workitem repoData) {
		WorkItemDTO workItem = new WorkItemDTO();
		workItem.setWorkItemId(repoData.getWorkItemId());
		workItem.setComment(repoData.getComment());
		workItem.setCreatedBy(repoData.getCreatedBy());
		workItem.setCreatedTime(String.valueOf( repoData.getCreatedTime()));
		workItem.setUserCode(repoData.getUserCode());
		workItem.setWorkItemName(repoData.getWorkItemName());
		workItem.setWorkType(repoData.getWorkType());
		workItem.setWorkItemReferenceNumber(repoData.getWorkItemRefNumber());
		workItem.setPolicyNumber(repoData.getPolicy().getPolicyNumber());
		return workItem;
	}
	

}
