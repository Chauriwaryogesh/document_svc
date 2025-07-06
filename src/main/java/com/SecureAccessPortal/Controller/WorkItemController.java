package com.SecureAccessPortal.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
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
	public ResponseEntity<Page<WorkItemDTO>> workitems(
			@RequestParam(value = "workItemRefNum", required = false) String workItemRefNum,
			@RequestParam(value = "queue", required = false) String queue,
			@RequestParam(value = "userCode", required = false) String filterUserCode,
			@RequestParam(value = "createdBy", required = false) String createdBy,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate, 
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
			@RequestHeader(value = "userCode", required = true) String userCode) { 

		ResponseEntity<Page<WorkItemDTO>> response = new ResponseEntity<>();
		Page<WorkItemDTO> workItem = workItemService.fetchWorkItems(workItemRefNum, queue, filterUserCode, createdBy,
				status, startDate, endDate, page, size, userCode);
		if (workItem != null) {
			response.setData(workItem);
		} else {
			response.setErrorMessage("error in fetch WorkItem");
		}
		return response;
	}

	//@Cacheable("WorkItem")
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
	
	@RequestMapping(value = "workType", method = RequestMethod.GET)
	public ResponseEntity<List<String>> workType(
			@RequestHeader(value = "userCode", required = false) String userCode) {
		ResponseEntity<List<String>> response = new ResponseEntity<>();
		List<String> workItem = workItemService.fetchWorkType(userCode);
		if (workItem != null) {
			response.setData(workItem);
		} else {
			response.setErrorMessage("error in fetch WorkItem");
		}
		return response;
	}
	
	@RequestMapping(value = "/related-workitems", method = RequestMethod.GET)
	public ResponseEntity<Page<WorkItemDTO>> relatedWorktems(
			@RequestParam(value = "workItemRefNum", required = false) String workitemRefNo,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
			@RequestParam(value = "policyRelated", required = false) String policyRelated,
			@RequestParam(value = "customerRelated", required = false) String customerRelated,
			@RequestHeader(value = "userCode", required = false) String userCode) {
		ResponseEntity<Page<WorkItemDTO>>response = new ResponseEntity<>();
		Page<WorkItemDTO> workItem = workItemService.fetchRelatedWorkitems(workitemRefNo,page,size,policyRelated,customerRelated,userCode);
		if (workItem != null) {
			response.setData(workItem);
		} else {
			response.setErrorMessage("error in fetch WorkItem");
		}
		return response;
	}
	@RequestMapping(value = "update-WorkItem", method = RequestMethod.POST, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WorkItemDTO> updateWorkItem(@RequestBody WorkItemDTO workItemRequest,
			@RequestHeader(value = "userCode", required = true) String userCode) {
		ResponseEntity<WorkItemDTO> response = new ResponseEntity<>();
		WorkItemDTO workItem = workItemService.updateWorkItem(workItemRequest, userCode);
		if (workItem != null) {
			response.setData(workItem);
			response.setStatus(CommonConstant.SUCCESS);
		} else {
			response.setErrorMessage("failed to Update WorkItem");
			response.setStatus(CommonConstant.FAILURE);
		}

		return response;
	}
}
