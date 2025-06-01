package com.SecureAccessPortal.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Modal.WorkItemCount;
import com.SecureAccessPortal.Modal.WorkItemDTO;
import com.SecureAccessPortal.Service.IWorkItemService;
import com.SecureAccessPortal.Service.ResponseEntity;

@RestController
@RequestMapping(value = "workItem_Service")
public class WorkItemController {

	@Autowired
	private CommonConstant constant;

	@Autowired
	private IWorkItemService workItemService;

	@RequestMapping(value = "create-WorkItem", method = RequestMethod.POST, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WorkItemDTO> createWorkItem(@RequestBody WorkItemDTO workItemRequest,
			@RequestHeader(value = "userCode", required = true) String userCode) {
		ResponseEntity<WorkItemDTO> response = new ResponseEntity<>();
		WorkItemDTO workItem = workItemService.createWorkItem(workItemRequest, userCode);

		if (workItem != null) {
			response.setData(workItem);
		} else {
			response.setErrorMessage("error in create WorkItem");
		}

		return response;
	}

	@RequestMapping(value = "workitems", method = RequestMethod.GET)
	public ResponseEntity<List<WorkItemDTO>> createWorkItem(
			@RequestParam(value = "workItemRefNum", required = false) String workItemRefNum,
			@RequestHeader(value = "userCode", required = true) String userCode) {
		ResponseEntity<List<WorkItemDTO>> response = new ResponseEntity<>();
		List<WorkItemDTO> workItem = workItemService.fetchWorkItems(workItemRefNum, userCode);
		if (workItem != null) {
			response.setData(workItem);
		} else {
			response.setErrorMessage("error in fetch WorkItem");
		}
		return response;
	}

	@RequestMapping(value = "workItem-count", method = RequestMethod.GET)
	public ResponseEntity<WorkItemCount> workItemCount(
			@RequestHeader(value = "userCode", required = true) String userCode) {
		ResponseEntity<WorkItemCount> response = new ResponseEntity<>();
		WorkItemCount workItem = workItemService.workItemCount(userCode);
		if (workItem != null) {
			response.setData(workItem);
		} else {
			response.setErrorMessage("error in fetch WorkItem");
		}
		return response;
	}
}
