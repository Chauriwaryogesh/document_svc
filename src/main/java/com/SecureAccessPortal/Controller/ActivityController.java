package com.SecureAccessPortal.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.SecureAccessPortal.Modal.ActivityDetailsDTO;
import com.SecureAccessPortal.Service.IWorkItemService;
import com.SecureAccessPortal.Service.ResponseEntity;

@RestController
public class ActivityController {
	
	@Autowired
	private IWorkItemService workItemService;
	
	//@PostMapping("/updateActivity")
	@GetMapping("/get-ActivityDtls")
	public ResponseEntity<List<ActivityDetailsDTO>> getActivitydtls(@RequestHeader String userId) {

		ResponseEntity<List<ActivityDetailsDTO>> listOfActiity = new ResponseEntity<List<ActivityDetailsDTO>>();
		listOfActiity = workItemService.getActivityDtls(userId);

		return listOfActiity;
	}

}
