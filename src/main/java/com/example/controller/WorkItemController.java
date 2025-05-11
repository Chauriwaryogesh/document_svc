package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.CommonConstants.CommonConstant;
import com.example.dto.WorkItemDTO;
import com.example.service.IWorkItemService;
import com.example.service.ResponseEntity;

@RestController
@RequestMapping(value = "workItem_Service")
public class WorkItemController {

	@Autowired
	private CommonConstant constant;

	@Autowired
	private IWorkItemService workItemService;

	@RequestMapping(value = "create-WorkItem", method = RequestMethod.POST, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WorkItemDTO> createWorkItem(@RequestBody WorkItemDTO workItemRequest,
			@RequestHeader(value = "userId", required = true) String userId) {
		ResponseEntity<WorkItemDTO> response = new ResponseEntity<>();
		WorkItemDTO workItem = workItemService.createWorkItem(workItemRequest, userId);

		if (workItem != null) {
			response.setData(workItem);
		} else {
			response.setErrorMessage("error in create WorkItem");
		}

		return response;

	}

}
