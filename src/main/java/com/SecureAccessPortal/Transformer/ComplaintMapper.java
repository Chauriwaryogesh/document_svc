package com.SecureAccessPortal.Transformer;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.SecureAccessPortal.Entity.Complaint;
import com.SecureAccessPortal.Modal.ComplaintDTO;

@Component
public class ComplaintMapper {

	public ComplaintDTO mapComplaint(Complaint complaintRepo) {
		ComplaintDTO complaintDTO = new ComplaintDTO();
		complaintDTO.setComplaintNumber(complaintRepo.getComplaintNumber());
		complaintDTO.setStatus(complaintRepo.getStatus());
		complaintDTO.setWorkitemNumber(complaintRepo.getWorkitem().getWorkItemRefNumber());
		complaintDTO.setUserCode(complaintRepo.getUserCode());
		return complaintDTO;
	}

	public List<ComplaintDTO> mapComplaintData(List<Complaint> complaints) {

		List<ComplaintDTO> complaintList = complaints.stream().filter(Objects::nonNull).map(complaint -> {
			ComplaintDTO dto = new ComplaintDTO();
			dto.setComplaintId(complaint.getComplaintId());
			dto.setComplaintNumber(complaint.getComplaintNumber());
			dto.setUserCode(complaint.getUserCode());
			dto.setCustomerNo(complaint.getCustomer() != null ? complaint.getCustomer().getCustomerNo() : null);
			dto.setCustomerName(complaint.getCustomer() != null
					? complaint.getCustomer().getName() + " " + complaint.getCustomer().getSurname()
					: null);
			dto.setPolicyNumber(complaint.getPolicy() != null ? complaint.getPolicy().getPolicyNumber() : null);
			dto.setPolicyType(complaint.getPolicy() != null ? complaint.getPolicy().getPolicyType() : null);
			dto.setCategory(complaint.getCategory());
			dto.setStatus(complaint.getStatus());
			dto.setPriority(complaint.getPriority());
			dto.setSlaStatus(complaint.getSlaStatus());
			dto.setSlaProgress(complaint.getSlaProgress());
			dto.setDateFiled(complaint.getDateFiled() != null ? complaint.getDateFiled().toString() : null);
			dto.setLastUpdated(complaint.getLastUpdated() != null ? complaint.getLastUpdated().toString() : null);
			dto.setDescription(complaint.getDescription());
			dto.setResolutionNotes(complaint.getResolutionNotes());
			dto.setAttachmentPath(complaint.getAttachmentPath());
			dto.setCreatedBy(complaint.getCreatedBy());
			dto.setUpdatedBy(complaint.getUpdatedBy());
			dto.setCreatedAt(complaint.getCreatedTime() != null ? complaint.getCreatedTime().toString() : null);
			dto.setUpdatedAt(complaint.getUpdatedTime() != null ? complaint.getUpdatedTime().toString() : null);
			dto.setAssignedTo(complaint.getAssignedTo());
			dto.setDepartmentId(complaint.getDepartmentId());
			dto.setEscalationLevel(complaint.getEscalationLevel());
			dto.setRelatedComplaintId(complaint.getRelatedComplaintId());
			dto.setSourceChannel(complaint.getSourceChannel());
			dto.setSeverity(complaint.getSeverity());
			dto.setCustomerFeedback(complaint.getCustomerFeedback());
			if (Boolean.TRUE.equals(complaint.isReopened())) {
				dto.setIsReopened("Yes");
			} else {
				dto.setIsReopened("No");
			}
			dto.setTags(complaint.getTags());
			dto.setSlaDueDate(complaint.getSlaDueDate());
			dto.setReason(complaint.getReason());
			dto.setWorkitemNumber(
					complaint.getWorkitem() != null ? complaint.getWorkitem().getWorkItemRefNumber() : null);
			return dto;
		}).collect(Collectors.toList());
		return complaintList;
	}

}
