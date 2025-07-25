package com.SecureAccessPortal.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Modal.ComplaintDTO;
import com.SecureAccessPortal.Modal.DashboardStats;
import com.SecureAccessPortal.Modal.RoleDTO;
import com.SecureAccessPortal.Service.ComplaintService;
import com.SecureAccessPortal.Service.ResponseEntity;

@RestController
@RequestMapping("Complaint")
public class ComplaintsController {

	@Autowired
	private ComplaintService complaintService;

	@GetMapping("/search-complaint")
	public ResponseEntity<List<ComplaintDTO>> searchComplaints(@RequestParam(required = false) String complaintId,
			@RequestParam(required = false) String complaintNumber, @RequestParam(required = false) String customerNo,
			@RequestParam(required = false) String policyNumber, @RequestParam(required = false) String workitemNumber,
			@RequestParam(required = false) String type,
			@RequestHeader(required = false) String userCode) {

		ResponseEntity<List<ComplaintDTO>> response = new ResponseEntity<>();
		List<ComplaintDTO> complaints = complaintService.searchComplaints(complaintId, complaintNumber, customerNo,
				policyNumber, workitemNumber,type, userCode);
		response.setData(complaints);
		return response;
	}

	@GetMapping("/fetch-Team")
	public ResponseEntity<RoleDTO> fetchRoles(@RequestParam(required = true) String userCode) {
		ResponseEntity<RoleDTO> response = new ResponseEntity<>();
		RoleDTO complaints = complaintService.fetchRoles(userCode);
		response.setData(complaints);
		return response;
	}

	@GetMapping("/get-stats")
	public ResponseEntity<DashboardStats> getStats(@RequestParam(required = false) String customerNo,@RequestParam(required = false) String userCode) {		ResponseEntity<DashboardStats> response = new ResponseEntity<>();
		DashboardStats dashboardStats = complaintService.getComplaintStats(customerNo,userCode);
		response.setData(dashboardStats);
		return response;
	}
	
	@GetMapping("/get-complaint")
	public ResponseEntity<List<ComplaintDTO>> getComplaint(@RequestParam(required = false) String complaintNumber,
			@RequestHeader(required = false) String userCode) {

		ResponseEntity<List<ComplaintDTO>> response = new ResponseEntity<>();
		List<ComplaintDTO> complaints = complaintService.getComplaint(complaintNumber, userCode);
		if(complaints.isEmpty()) {
			response.setErrorMessage("No Complaint found");
			response.setStatus(CommonConstant.FAILURE);
		}else {
			response.setData(complaints);
			response.setStatus(CommonConstant.SUCCESS);
		}
		
		return response;
	}
	@PostMapping(value="create-complaint", consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public com.SecureAccessPortal.Service.ResponseEntity<ComplaintDTO> getCustomerlDetails(
			@RequestBody ComplaintDTO complaintDTO, @RequestHeader String userCode) {
		com.SecureAccessPortal.Service.ResponseEntity<ComplaintDTO> resp = new com.SecureAccessPortal.Service.ResponseEntity<>();
		ComplaintDTO complaint = complaintService.createComplaint(complaintDTO, userCode);
		if (complaint != null ) {
			resp.setData(complaint);
			resp.setStatus(CommonConstant.SUCCESS);
		} else {
			resp.setStatus(CommonConstant.FAILURE);
			resp.setErrorMessage("failed to create Complaint");
		}
		return resp;
	}
	
	

}
