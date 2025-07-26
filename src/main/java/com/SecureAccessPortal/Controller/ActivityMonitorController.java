package com.SecureAccessPortal.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SecureAccessPortal.Modal.ActivityDetailsDTO;
import com.SecureAccessPortal.Modal.LoginHistroryResponse;
import com.SecureAccessPortal.Service.ActivityMonitorService;
import com.SecureAccessPortal.Service.ResponseEntity;

@RestController
public class ActivityMonitorController {
	
	@Autowired
	private ActivityMonitorService activityMonitorService;
	
	@GetMapping("/get-ActivityDtls")
	public ResponseEntity<List<ActivityDetailsDTO>> getActivitydtls(@RequestHeader String userCode) {

		ResponseEntity<List<ActivityDetailsDTO>> listOfActiity = new ResponseEntity<List<ActivityDetailsDTO>>();
		listOfActiity = activityMonitorService.getActivityDtls(userCode);

		return listOfActiity;
	}
	
	@GetMapping(value="/login-history", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LoginHistroryResponse>> getActivitydtls(
			@RequestParam(value="email",required= true ) String email,
			@RequestHeader String userCode) {

		ResponseEntity<List<LoginHistroryResponse>> listOfActiity = new ResponseEntity<>();
		listOfActiity = activityMonitorService.getLoginHistory(email,userCode);

		return listOfActiity;
	}
	
	@GetMapping(value="/activity-tracing",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoginHistroryResponse> getActivityTrac(
			@RequestParam(value="email",required= true ) String email,
			@RequestHeader String userCode) {

		ResponseEntity<LoginHistroryResponse> listOfActiity = new ResponseEntity<>();
		listOfActiity = activityMonitorService.getLoginActTrac(email,userCode);

		return listOfActiity;
	}

}
