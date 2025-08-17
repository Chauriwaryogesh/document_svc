package com.SecureAccessPortal.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Entity.Complaint;
import com.SecureAccessPortal.Entity.Customer;
import com.SecureAccessPortal.Entity.Policy;
import com.SecureAccessPortal.Entity.Workitem;
import com.SecureAccessPortal.Modal.ComplaintDTO;
import com.SecureAccessPortal.Modal.DashboardStats;
import com.SecureAccessPortal.Modal.RoleDTO;
import com.SecureAccessPortal.Repo.ComplaintRepo;
import com.SecureAccessPortal.Repo.CustomerRepo;
import com.SecureAccessPortal.Repo.IPolicyRepo;
import com.SecureAccessPortal.Transformer.ComplaintMapper;

@Service
public class ComplaintService {
	private static final Logger logger = LoggerFactory.getLogger(ComplaintService.class);

	@Autowired
	private ComplaintRepo complaintRepository;

	@Autowired
	private ComplaintMapper complaintMapper;

	@Autowired
	private IPolicyRepo policyRepo;

	@Autowired
	private CustomerRepo customerRepository;

	@Autowired
	private IWorkItemService workItemService;

	public List<ComplaintDTO> searchComplaints(String complaintId, String complaintNumber, String customerNo,
			String policyNumber, String workitemNumber, String type, String usrCode) {
		List<Complaint> complaints = new ArrayList<>();
		if (CommonConstant.ALL.equalsIgnoreCase(type)) {
			complaints = complaintRepository.findByCriteria(complaintId, complaintNumber, customerNo, policyNumber,
					workitemNumber, null, "N");
		} else {
			complaints = complaintRepository.findByCriteria(complaintId, complaintNumber, customerNo, policyNumber,
					workitemNumber, type, "N");
		}
		return complaints.stream().map(this::mapComplaint).collect(Collectors.toList());
	}

	public ComplaintDTO mapComplaint(Complaint complaint) {
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
		dto.setTotalClaimableAmount(
				complaint.getPolicy() != null ? complaint.getPolicy().getTotalClaimableAmount() : null);
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
		dto.setWorkitemNumber(complaint.getWorkitem() != null ? complaint.getWorkitem().getWorkItemRefNumber() : null);
		return dto;
	}

	public RoleDTO fetchRoles(String usrCode) {

		Customer customer = customerRepository.findByUserCodeAndDeletedFlagN(usrCode, "N");

		RoleDTO roleDTO = new RoleDTO();
		roleDTO.setLoggedInTime(LocalDateTime.now());
		roleDTO.setName(customer.getName() + " " + customer.getMiddleName() + " " + customer.getSurname());
		roleDTO.setPhNo(customer.getPhoneNumber());
		roleDTO.setUserCode(customer.getUserCode());
		List<String> rolelist = new ArrayList<>();
		rolelist.add("Complaints Team");
		rolelist.add("Bancs Team");
		rolelist.add("Admin");
		roleDTO.setRoles(rolelist);

		return roleDTO;
	}

	public DashboardStats getComplaintStats(String customerNo, String userCode) {
		DashboardStats stats = new DashboardStats();
		List<Complaint> complaintList = new ArrayList<Complaint>();
		if (customerNo != null) {
			complaintList = complaintRepository.findByCustomerNo(customerNo);
		} else {
			complaintList = complaintRepository.findAll();
		}
		long complaintteam = complaintList.stream()
				.filter(list -> list.getAssignedTo().equalsIgnoreCase("Complaints Team")).count();
		long bancsTeam = complaintList.stream().filter(list -> list.getAssignedTo().equalsIgnoreCase("Bancs Team"))
				.count();
		long adminteam = complaintList.stream().filter(list -> list.getAssignedTo().equalsIgnoreCase("Admin Team"))
				.count();
		long escTeam = complaintList.stream().filter(list -> list.getAssignedTo().equalsIgnoreCase("Escalation Team"))
				.count();

		stats.setComplaintsTeam(complaintteam);
		stats.setBancsTeam(bancsTeam);
		stats.setEscalationTeam(escTeam);
		stats.setAdminTeam(adminteam);

		return stats;
	}

	public List<ComplaintDTO> getComplaint(String complaintNumber, String userCode) {
		List<Complaint> complaints = complaintRepository.findByComplaintNumber(complaintNumber, "N");
		return complaints.stream().map(this::mapComplaint).collect(Collectors.toList());

	}

	public ComplaintDTO createComplaint(ComplaintDTO dto, String userCode) {
		Complaint complaint = new Complaint();
		try {
			if (dto.getComplaintNumber() == null) {
				String complaintNumber = generateComplaintNumber();
				complaint.setComplaintNumber(complaintNumber);
				String[] parts = complaintNumber.split("/");
				String complaintId = parts[1];
				complaint.setComplaintId(complaintId);
			} else {
				complaint.setComplaintNumber(dto.getComplaintNumber());
				complaint.setComplaintId(dto.getComplaintId());
			}
			Customer customer = customerRepository.findByCustomerNoNew(dto.getCustomerNo(), "N");
			complaint.setCustomer(customer);
			complaint.setUserCode(customer.getUserCode());

			Policy policy = policyRepo.findByPolicyNum(dto.getPolicyNumber(), "N");
			complaint.setPolicy(policy);

			// Create work item
			if (dto.getWorkitemNumber() == null) {
				String workType = CommonConstant.COMPLAINT_CREATE;
				String workItemName = CommonConstant.COMPLAINT_CREATE_NEW;
				String status = CommonConstant.OPEN;
				String comment = "Complaint is created " + policy.getPolicyNumber() + " for the customer";
				Workitem mapRequetforWorkItem = workItemService.mapRequetforWorkItem(userCode, policy, customer,
						workType, workItemName, comment, null, null, null, status);
				complaint.setWorkitem(mapRequetforWorkItem);
			} else {
				// code for link workitemwill implement soon.
				String workType = CommonConstant.COMPLAINT_UPDATE;
				String workItemName = CommonConstant.COMPLAINT_CREATE_UPDATE;
				String status = CommonConstant.APPROVED;
				String comment = "Complaint is updateing  " + policy.getPolicyNumber() + " for the customer";
				Workitem mapRequetforWorkItem = workItemService.mapRequetforWorkItem(userCode, policy, customer,
						workType, workItemName, comment, null, null, null, status);
				complaint.setWorkitem(mapRequetforWorkItem);
			}
			complaint.setCategory(dto.getCategory());
			complaint.setStatus(dto.getStatus());
			complaint.setPriority(dto.getPriority());
			complaint.setSlaStatus(dto.getSlaStatus());
			complaint.setSlaProgress(dto.getSlaProgress());
			complaint.setDateFiled(LocalDate.parse(dto.getDateFiled()));
			if (dto.getAction().equalsIgnoreCase("update")) {
				complaint.setLastUpdated(LocalDate.parse(dto.getLastUpdated()));
				complaint.setUpdatedBy(userCode);
			} else {
				complaint.setCreatedTime(LocalDate.parse(dto.getCreatedAt()));
				complaint.setCreatedBy(dto.getCreatedBy());
			}
			complaint.setDescription(dto.getDescription());
			complaint.setAttachmentPath(dto.getAttachmentPath());

			complaint.setAssignedTo(dto.getAssignedTo());
			complaint.setDepartmentId(dto.getDepartmentId());
			complaint.setEscalationLevel(dto.getEscalationLevel());
			complaint.setRelatedComplaintId(dto.getRelatedComplaintId());
			complaint.setSourceChannel(dto.getSourceChannel());
			complaint.setSeverity(dto.getSeverity());
			complaint.setCustomerFeedback(dto.getCustomerFeedback());
			if ("Yes".equalsIgnoreCase(dto.getIsReopened())) {
				complaint.setReopened(true);
			} else {
				complaint.setReopened(false);
			}

			complaint.setTags(dto.getTags());
			complaint.setSlaDueDate(dto.getSlaDueDate());
			complaint.setReason(dto.getReason());
			complaint.setDeletedFlag("N");
		} catch (Exception e) {
			e.printStackTrace();
		}

		Complaint complaintRepo = complaintRepository.save(complaint);
		logger.info("Complaint no{}", complaintRepo.getComplaintNumber());
		ComplaintDTO complaintDTO = complaintMapper.mapComplaint(complaintRepo);
		return complaintDTO;
	}

	public String generateComplaintNumber() {
		String prefix = "CMPLT/";
		int currentYear = LocalDate.now().getYear();
		String latestComplaintNumber = complaintRepository.findLatestComplaintNumber();
		int nextNumber = 1;
		if (latestComplaintNumber != null && latestComplaintNumber.startsWith(prefix)
				&& latestComplaintNumber.endsWith("/" + currentYear)) {
			String numberPart = latestComplaintNumber.replace(prefix, "").replace("/" + currentYear, "");
			try {
				nextNumber = Integer.parseInt(numberPart) + 1;
			} catch (NumberFormatException e) {
				// Fallback to 1 if parsing fails
			}
		}
		return String.format("%s%06d/%d", prefix, nextNumber, currentYear);
	}
	

}
