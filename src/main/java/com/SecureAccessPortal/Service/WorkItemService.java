package com.SecureAccessPortal.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Entity.Bank;
import com.SecureAccessPortal.Entity.Customer;
import com.SecureAccessPortal.Entity.OtpStore;
import com.SecureAccessPortal.Entity.Payments;
import com.SecureAccessPortal.Entity.Policy;
import com.SecureAccessPortal.Entity.VerificationRecord;
import com.SecureAccessPortal.Entity.Workitem;
import com.SecureAccessPortal.Modal.Queue;
import com.SecureAccessPortal.Modal.WorkItemCount;
import com.SecureAccessPortal.Modal.WorkItemDTO;
import com.SecureAccessPortal.Repo.CustomerRepo;
import com.SecureAccessPortal.Repo.IPolicyRepo;
import com.SecureAccessPortal.Repo.WorkItemRepo;
import com.SecureAccessPortal.Transformer.WorkItemMapper;

import jakarta.persistence.criteria.Predicate;

@Component
public class WorkItemService implements IWorkItemService {

	private static final Logger logger = LoggerFactory.getLogger(WorkItemService.class);

	@Autowired
	private WorkItemRepo workItemRepo;

	@Autowired
	private WorkItemMapper workItemMapper;

	@Autowired
	private IPolicyRepo policyRepository;

	@Autowired
	private CustomerRepo customerRepository;

	@Override
	public WorkItemDTO createWorkItem(WorkItemDTO workItemRequest, String userCode) {
		WorkItemDTO workItemDTO = new WorkItemDTO();
		try {
			com.SecureAccessPortal.Entity.Workitem workItem = new com.SecureAccessPortal.Entity.Workitem();
			workItem.setWorkItemId(String.valueOf(UUID.randomUUID()));
			workItem.setComment(workItemRequest.getComment());
			workItem.setCreatedBy(userCode);
			workItem.setCreatedTime(LocalDateTime.now());
			if (userCode != null) {
				workItem.setUserCode(userCode);
			} else {
				workItem.setUserCode(workItemRequest.getCreatedBy());
			}
			if (workItemRequest.getPolicyNumber() != null) {
				Policy policy = policyRepository.findByPolicyNum(workItemRequest.getPolicyNumber(), "N");
				if (policy == null) {
					logger.error("No policy No found");
				} else {
					workItem.setPolicy(policy);
					workItem.setCustomer(policy.getCustomer());
				}

			}
			workItem.setWorkItemName(workItemRequest.getWorkItemName());
			workItem.setWorkType(workItemRequest.getWorkType());

			String refNo = generateRandomWorkItemRefNumber();
			workItem.setWorkItemRefNumber(refNo);
			workItem.setQueue(CommonConstant.TEAM_MEMBER);
			if (workItemRequest.getStatus() != null) {
				workItem.setStatus(CommonConstant.OPEN);
			} else {
				workItem.setStatus(workItemRequest.getStatus());
			}
			Workitem repoData = workItemRepo.save(workItem);
			if (repoData == null) {
				workItemDTO.setComment("failed to create WorkItem");
			} else {
				workItemDTO = workItemMapper.workItemMApper(repoData);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return workItemDTO;
	}

	public String generateRandomWorkItemRefNumber() {
		String year = String.valueOf(LocalDate.now().getYear());

		long randomNumber = 1000000000L + new Random().nextLong(9000000000L); // ensures 10 digits
		return "WI" + year + randomNumber;
	}

	public Page<WorkItemDTO> fetchWorkItems(String wiRefNum, String queue, String customerNo, String policyNo,
			String filterUserCode, String createdBy, String status, String startDate, String endDate, int page,
			int size, String userCodeHeader) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdTime").descending());

		Specification<Workitem> spec = (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (wiRefNum != null && !wiRefNum.isEmpty()) {
				predicates.add(cb.equal(root.get("workItemRefNumber"), wiRefNum));
			}
			if (policyNo != null && !policyNo.isEmpty()) {
				predicates.add(
						cb.like(cb.lower(root.get("policy").get("policyNumber")), "%" + policyNo.toLowerCase() + "%"));
			}
			if (customerNo != null && !customerNo.isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("customer").get("customerNo")),
						"%" + customerNo.toLowerCase() + "%"));
			}
			if (queue != null && !queue.isEmpty()) {
				predicates.add(cb.equal(root.get("queue"), queue));
			}
			if (filterUserCode != null && !filterUserCode.isEmpty()) {
				predicates.add(cb.equal(root.get("userCode"), filterUserCode));
			}
			if (createdBy != null && !createdBy.isEmpty()) {
				predicates.add(cb.equal(root.get("createdBy"), createdBy));
			}
			if (status != null && !status.isEmpty()) {
				predicates.add(cb.equal(root.get("status"), status));
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Assuming format from UI
			if (startDate != null && !startDate.isEmpty()) {
				try {
					LocalDateTime startDateTime = LocalDateTime.parse(startDate + " 00:00:00",
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					predicates.add(cb.greaterThanOrEqualTo(root.get("createdTime"), startDateTime));
				} catch (DateTimeParseException e) {
					System.err.println("Invalid startDate format: " + startDate + ". Skipping date filter.");
				}
			}
			if (endDate != null && !endDate.isEmpty()) {
				try {
					LocalDateTime endDateTime = LocalDateTime.parse(endDate + " 23:59:59",
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					predicates.add(cb.lessThanOrEqualTo(root.get("createdTime"), endDateTime));
				} catch (DateTimeParseException e) {
					System.err.println("Invalid endDate format: " + endDate + ". Skipping date filter.");
				}
			}
			return cb.and(predicates.toArray(new Predicate[0]));
		};
		try {
			Page<Workitem> workItemsPage = workItemRepo.findAll(spec, pageable);
			return workItemsPage.map(workItems -> {
				WorkItemDTO workItem = new WorkItemDTO();
				workItem.setWorkItemId(workItems.getWorkItemId());
				workItem.setComment(workItems.getComment());
				workItem.setCreatedBy(workItems.getCreatedBy());
				workItem.setCreatedTime(String.valueOf(workItems.getCreatedTime()));
				workItem.setUserCode(workItems.getUserCode());
				workItem.setWorkItemName(workItems.getWorkItemName());
				workItem.setWorkType(workItems.getWorkType());
				workItem.setWorkItemReferenceNumber(workItems.getWorkItemRefNumber());
				workItem.setStatus(workItems.getStatus());
				workItem.setQueue(workItems.getQueue());
				if (workItems.getPolicy() != null) {
					workItem.setPolicyNumber(Optional.of(workItems.getPolicy().getPolicyNumber()).orElse(null));
				}
				if (workItems.getCustomer() != null) {
					workItem.setCustomerNo(Optional.ofNullable(workItems.getCustomer().getCustomerNo()).orElse(null));
				}
				return workItem;
			});
		} catch (Exception e) {
			e.printStackTrace();
			return Page.empty(pageable);
		}
	}

	@Override
	public WorkItemCount workItemCount(String customerNo, String userCode) {
		WorkItemCount workItemCount = new WorkItemCount();
		List<Workitem> workItemsList = new ArrayList<>();
		Queue queue = new Queue();
		if (customerNo != null && !customerNo.isEmpty()) {
			workItemsList = workItemRepo.findByCustomerNo(customerNo);
		} else {
			workItemsList = workItemRepo.findAll();
		}
		long pendExternal = workItemsList.stream().filter(sttus -> sttus.getStatus().equalsIgnoreCase("PEND_EXTERNAL"))
				.count();
		long pendInternal = workItemsList.stream().filter(sttus -> sttus.getStatus().equalsIgnoreCase("PEND_INTERNAL"))
				.count();
		long rejected = workItemsList.stream().filter(sttus -> sttus.getStatus().equalsIgnoreCase("REJECTED")).count();
		long open = workItemsList.stream().filter(sttus -> sttus.getStatus().equalsIgnoreCase("OPEN")).count();
		long closed = workItemsList.stream().filter(sttus -> sttus.getStatus().equalsIgnoreCase("CLOSED")).count();
		long inProgress = workItemsList.stream().filter(sttus -> sttus.getStatus().equalsIgnoreCase("IN_PROGRESS"))
				.count();
		long completed = workItemsList.stream().filter(sttus -> sttus.getStatus().equalsIgnoreCase("COMPLETED"))
				.count();
		long passed = workItemsList.stream().filter(sttus -> sttus.getStatus().equalsIgnoreCase("PASSED")).count();

		long complaintsTeam = workItemsList.stream()
				.filter(que -> que.getQueue() != null && que.getQueue().equalsIgnoreCase("COMPLAINTS_TEAM")).count();
		long adminTeam = workItemsList.stream()
				.filter(que -> que.getQueue() != null && que.getQueue().equalsIgnoreCase("ADMIN_TEAM")).count();
		long workflowTeam = workItemsList.stream()
				.filter(que -> que.getQueue() != null && que.getQueue().equalsIgnoreCase("WORKFLOW_TEAM")).count();
		long teamMember = workItemsList.stream()
				.filter(que -> que.getQueue() != null && que.getQueue().equalsIgnoreCase("TEAM_MEMBER")).count();
		long total = workItemsList.stream().count();

		queue.setComplaintsTeam(String.valueOf(complaintsTeam));
		queue.setAdminTeam(String.valueOf(adminTeam));
		queue.setTeamMember(String.valueOf(teamMember));
		queue.setWorkflowTeam(String.valueOf(workflowTeam));

		workItemCount.setTotal(String.valueOf(total));
		workItemCount.setClosed(String.valueOf(closed));
		workItemCount.setCompleted(String.valueOf(completed));
		workItemCount.setIn_progress(String.valueOf(inProgress));
		workItemCount.setOpen(String.valueOf(open));
		workItemCount.setPassed(String.valueOf(passed));
		workItemCount.setPend_external(String.valueOf(pendExternal));
		workItemCount.setPend_internal(String.valueOf(pendInternal));
		workItemCount.setRejected(String.valueOf(rejected));
		workItemCount.setQueue(queue);
		return workItemCount;
	}

	@Override
	public Workitem mapRequetforWorkItem(String userCode, Policy policy, Customer customer, String workType,
			String workItemName, String comment, Bank bankAccount, VerificationRecord verificationRecord,
			Payments payments, String status) {
		com.SecureAccessPortal.Entity.Workitem workItem = new com.SecureAccessPortal.Entity.Workitem();
		workItem.setWorkItemId(String.valueOf(UUID.randomUUID()));
		workItem.setComment(comment);
		workItem.setCreatedBy(userCode);
		workItem.setCreatedTime(LocalDateTime.now());
		workItem.setUserCode(userCode);

		workItem.setWorkItemName(workItemName);
		workItem.setWorkType(workType);
		String refNo = generateRandomWorkItemRefNumber();
		workItem.setWorkItemRefNumber(refNo);
		workItem.setQueue(CommonConstant.TEAM_MEMBER);
		workItem.setStatus(status);
		if (customer != null) {
			workItem.setCustomer(customer);
		}
		if (policy != null) {
			workItem.setPolicy(policy);
		}
		if (bankAccount != null) {
			workItem.setBank(bankAccount);
		}
		if (verificationRecord != null) {
			workItem.setVerificationRecord(verificationRecord);
		}
		if (payments != null) {
			workItem.setPayment(payments);
		}
		Workitem repoData = workItemRepo.save(workItem);
		if (repoData == null) {
			new RuntimeErrorException(null, "Error while creating WorkItem");
		}
		return repoData;
	}

	@Override
	public Workitem mapRequetforWorkItemOtpService(String userCode, Customer customer, String workType,
			String workItemName, String comment, OtpStore otpStore) {

		com.SecureAccessPortal.Entity.Workitem workItem = new com.SecureAccessPortal.Entity.Workitem();
		workItem.setWorkItemId(String.valueOf(UUID.randomUUID()));
		workItem.setComment(comment);
		workItem.setCreatedBy(userCode);
		workItem.setCreatedTime(LocalDateTime.now());
		workItem.setUserCode(userCode);

		workItem.setWorkItemName(workItemName);
		workItem.setWorkType(workType);
		String refNo = generateRandomWorkItemRefNumber();
		workItem.setWorkItemRefNumber(refNo);
		workItem.setQueue(CommonConstant.TEAM_MEMBER);
		workItem.setStatus(CommonConstant.OPEN);
		if (customer != null) {
			workItem.setCustomer(customer);
		}
		if (otpStore != null) {
			workItem.setOtpStore(otpStore);
		}
		Workitem repoData = workItemRepo.save(workItem);
		if (repoData == null) {
			new RuntimeErrorException(null, "Error while creating WorkItem");
		}
		return repoData;
	}

	@Override
	public List<String> fetchWorkType(String userCode) {
		List<String> workTypes = List.of("COMPLAINT", "FCU", "POLICY_CREATE");
		return workTypes;
	}

	@Override
	public Page<WorkItemDTO> fetchRelatedWorkitems(String workitemRefNo, int page, int size, String policyRelated,
			String customerRelated, String userCode) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdTime").descending());
		Page<Workitem> workItemsPage = null;
		if (CommonConstant.Y.equalsIgnoreCase(customerRelated)) {
			workItemsPage = workItemRepo.findWorkItemsByRefNumberCustomerNo(workitemRefNo, pageable);
		} else {
			workItemsPage = workItemRepo.findWorkItemsByRefNumberPolicy(workitemRefNo, pageable);
		}

		return workItemsPage.map(workItems -> {
			WorkItemDTO workItem = new WorkItemDTO();
			workItem.setWorkItemId(workItems.getWorkItemId());
			workItem.setComment(workItems.getComment());
			workItem.setCreatedBy(workItems.getCreatedBy());
			workItem.setCreatedTime(String.valueOf(workItems.getCreatedTime()));
			workItem.setUserCode(workItems.getUserCode());
			workItem.setWorkItemName(workItems.getWorkItemName());
			workItem.setWorkType(workItems.getWorkType());
			workItem.setWorkItemReferenceNumber(workItems.getWorkItemRefNumber());
			workItem.setStatus(workItems.getStatus());
			workItem.setQueue(workItems.getQueue());
			return workItem;
		});
	}

	@Override
	public WorkItemDTO updateWorkItem(WorkItemDTO workItemRequest, String userCode) {
		WorkItemDTO workItemDTO = new WorkItemDTO();
		String workItemReferenceNumber = workItemRequest.getWorkItemReferenceNumber();
		Optional<Workitem> byWiRefNum = workItemRepo.findByWiRefNum(workItemReferenceNumber);
		Workitem workitem = byWiRefNum.get();
		workitem.setStatus(workItemRequest.getStatus());
		if (workItemRequest.getComment() != null) {
			String comment = workItemRequest.getComment();
			workitem.setComment(comment);
		}
		if (workItemRequest.getQueue() != null) {
			workitem.setQueue(workItemRequest.getQueue());
		}
		workitem.setUpdatedBy(userCode);
		workitem.setUpdatedTime(LocalDateTime.now());
		Workitem saveWorkItem = workItemRepo.save(workitem);
		workItemDTO.setWorkItemReferenceNumber(saveWorkItem.getWorkItemRefNumber());
		workItemDTO.setComment("Saved Successfully");
		return workItemDTO;
	}

//	public WorkItemCount workItemCount(String userCode) {
//        WorkItemCount workItemCount = new WorkItemCount();
//        Queue queue = new Queue();
//
//        // Status counts
//        long pendExternal = workItemRepo.countByStatusAndUserCode("PEND_EXTERNAL", userCode);
//        long pendInternal = workItemRepo.countByStatusAndUserCode("PEND_INTERNAL", userCode);
//        long rejected = workItemRepo.countByStatusAndUserCode("REJECTED", userCode);
//        long open = workItemRepo.countByStatusAndUserCode("OPEN", userCode);
//        long closed = workItemRepo.countByStatusAndUserCode("CLOSED", userCode);
//        long inProgress = workItemRepo.countByStatusAndUserCode("IN_PROGRESS", userCode);
//        long completed = workItemRepo.countByStatusAndUserCode("COMPLETED", userCode);
//        long passed = workItemRepo.countByStatusAndUserCode("PASSED", userCode);
//
//        // Queue counts
//        long complaintsTeam = workItemRepo.countByQueueAndUserCode("COMPLAINTS_TEAM", userCode);
//        long adminTeam = workItemRepo.countByQueueAndUserCode("ADMIN_TEAM", userCode);
//        long workflowTeam = workItemRepo.countByQueueAndUserCode("WORKFLOW_TEAM", userCode);
//        long teamMember = workItemRepo.countByQueueAndUserCode("TEAM_MEMBER", userCode);
//
//        // Total count
//        long total = workItemRepo.countAllByUserCode(userCode);
//
//        // Set queue counts
//        queue.setComplaintsTeam(String.valueOf(complaintsTeam));
//        queue.setAdminTeam(String.valueOf(adminTeam));
//        queue.setWorkflowTeam(String.valueOf(workflowTeam));
//        queue.setTeamMember(String.valueOf(teamMember));
//
//        // Set work item counts
//        workItemCount.setTotal(String.valueOf(total));
//        workItemCount.setClosed(String.valueOf(closed));
//        workItemCount.setCompleted(String.valueOf(completed));
//        workItemCount.setIn_progress(String.valueOf(inProgress));
//        workItemCount.setOpen(String.valueOf(open));
//        workItemCount.setPassed(String.valueOf(passed));
//        workItemCount.setPend_external(String.valueOf(pendExternal));
//        workItemCount.setPent_internal(String.valueOf(pendInternal));
//        workItemCount.setRejected(String.valueOf(rejected));
//        workItemCount.setQueue(queue);
//
//        return workItemCount;
//    }
	
	@Override
	public List<WorkItemDTO> getAllWorkitems(List<Workitem> workitems) {
	//	List<WorkItemDTO> response= new ArrayList<>();
		return workitems.stream().filter(Objects ::nonNull).map(workItems -> {
			WorkItemDTO workItem = new WorkItemDTO();
			workItem.setWorkItemId(workItems.getWorkItemId());
			workItem.setComment(workItems.getComment());
			workItem.setCreatedBy(workItems.getCreatedBy());
			workItem.setCreatedTime(String.valueOf(workItems.getCreatedTime()));
			workItem.setUserCode(workItems.getUserCode());
			workItem.setWorkItemName(workItems.getWorkItemName());
			workItem.setWorkType(workItems.getWorkType());
			workItem.setWorkItemReferenceNumber(workItems.getWorkItemRefNumber());
			workItem.setStatus(workItems.getStatus());
			workItem.setQueue(workItems.getQueue());
			return workItem;
		}).collect(Collectors.toList());
		
		
	//	return response;
	}

}
