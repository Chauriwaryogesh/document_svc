package com.SecureAccessPortal.Transformer;

import org.springframework.stereotype.Component;

import com.SecureAccessPortal.Entity.Complaint;
import com.SecureAccessPortal.Modal.ComplaintDTO;

@Component
public class ComplaintMapper {

	public ComplaintDTO mapComplaint(Complaint complaintRepo) {
		ComplaintDTO complaintDTO = new ComplaintDTO();
		complaintDTO.setComplaintNumber(complaintRepo.getComplaintNumber());
		complaintDTO.setStatus(complaintRepo.getStatus());

		return complaintDTO;
	}

}
