package com.SecureAccessPortal.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Entity.ActivityDtls;
import com.SecureAccessPortal.Entity.BankAccount;
import com.SecureAccessPortal.Entity.Customer;
import com.SecureAccessPortal.Entity.OtpStore;
import com.SecureAccessPortal.Entity.Policy;
import com.SecureAccessPortal.Entity.VerificationRecord;
import com.SecureAccessPortal.Entity.Workitem;
import com.SecureAccessPortal.Modal.ActivityDetailsDTO;
import com.SecureAccessPortal.Modal.Queue;
import com.SecureAccessPortal.Modal.WorkItemCount;
import com.SecureAccessPortal.Modal.WorkItemDTO;
import com.SecureAccessPortal.Repo.ActivityDtlsRepo;
import com.SecureAccessPortal.Repo.WorkItemRepo;
import com.SecureAccessPortal.Transformer.WorkItemMapper;

@Component
public class WorkItemService implements IWorkItemService {

	@Autowired
	private WorkItemRepo workItemRepo;

	@Autowired
	private WorkItemMapper workItemMapper;

	@Autowired
	private ActivityDtlsRepo activityDtlsRepo;

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

	@Override
	public ResponseEntity<List<ActivityDetailsDTO>> getActivityDtls(String userCode) {
		ResponseEntity<List<ActivityDetailsDTO>> response = new ResponseEntity<List<ActivityDetailsDTO>>();
		List<ActivityDtls> activityDtls = activityDtlsRepo.findByuserCode(userCode);
		if (activityDtls != null && !activityDtls.isEmpty()) {
			List<ActivityDetailsDTO> activityList = activityDtls.stream().map(activity -> {
				ActivityDetailsDTO activityDetailsDTO = new ActivityDetailsDTO();
				activityDetailsDTO.setActivityTime(activity.getActivityTime());
				activityDetailsDTO.setActivityType(activity.getActivityType());
				activityDetailsDTO.setDetails(activity.getDetails());
				activityDetailsDTO.setEmail(activity.getEmail());
				activityDetailsDTO.setId(String.valueOf(activity.getId()));
				activityDetailsDTO.setIpAddress(activity.getIpAddress());
				activityDetailsDTO.setScreenName(activity.getScreenName());
				activityDetailsDTO.setTimeSpentSeconds(activity.getTimeSpentSeconds());
				activityDetailsDTO.setuserCode(activity.getuserCode());
				return activityDetailsDTO;
			}).collect(Collectors.toList());

			response.setData(activityList);

		} else {
			response.setErrorMessage("Not activity Found");
		}

		return response;
	}

	@Override
	public List<WorkItemDTO> fetchWorkItems(String wiRefNum, String userCode) {
		List<WorkItemDTO> workItemDTOList = new ArrayList<>();
		try {
			if (wiRefNum != null) {
				Optional<Workitem> workItemsList = workItemRepo.findByWiRefNum(wiRefNum);
				workItemDTOList = workItemsList.stream().map(workItems -> {
					WorkItemDTO workItem = new WorkItemDTO();
					workItem.setWorkItemId(workItems.getWorkItemId());
					workItem.setComment(workItems.getComment());
					workItem.setCreatedBy(workItems.getCreatedBy());
					workItem.setCreatedTime(String.valueOf(workItems.getCreatedTime()));
					workItem.setuserCode(workItems.getUserCode());
					workItem.setWorkItemName(workItems.getWorkItemName());
					workItem.setWorkType(workItems.getWorkType());
					workItem.setWorkItemReferenceNumber(workItems.getWorkItemRefNumber());
					workItem.setStatus(workItems.getStatus());
					workItem.setQueue(workItems.getQueue());
					return workItem;
				}).collect(Collectors.toList());
			} else {
				List<Workitem> workItemsList = workItemRepo.findAll();
				workItemDTOList = workItemsList.stream().map(workItems -> {
					WorkItemDTO workItem = new WorkItemDTO();
					workItem.setWorkItemId(workItems.getWorkItemId());
					workItem.setComment(workItems.getComment());
					workItem.setCreatedBy(workItems.getCreatedBy());
					workItem.setCreatedTime(String.valueOf(workItems.getCreatedTime()));
					workItem.setuserCode(workItems.getUserCode());
					workItem.setWorkItemName(workItems.getWorkItemName());
					workItem.setWorkType(workItems.getWorkType());
					workItem.setWorkItemReferenceNumber(workItems.getWorkItemRefNumber());
					workItem.setStatus(workItems.getStatus());
					workItem.setQueue(workItems.getQueue());
					return workItem;
				}).collect(Collectors.toList());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		List<WorkItemDTO> sortedList = workItemDTOList.stream().filter(item -> item.getCreatedTime() != null)
				.sorted(Comparator.comparing(WorkItemDTO::getCreatedTime).reversed()).collect(Collectors.toList());
		return sortedList;
	}

	@Override
	public WorkItemCount workItemCount(String userCode) {
		WorkItemCount workItemCount = new WorkItemCount();

		Queue queue = new Queue();
		List<Workitem> workItemsList = workItemRepo.findAll();
        //add pending
		// hold 1 week
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
		workItemCount.setPent_internal(String.valueOf(pendInternal));
		workItemCount.setRejected(String.valueOf(rejected));
		workItemCount.setQueue(queue);
		return workItemCount;
	}

	@Override
	public Workitem mapRequetforWorkItem(String userCode, Policy policy, Customer customer, String workType,
			String workItemName, String comment, BankAccount bankAccount,VerificationRecord verificationRecord) {
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
		if(customer != null) {
			workItem.setCustomer(customer);
		}
		if(policy != null) {
			workItem.setPolicy(policy);
		}
		if(bankAccount != null) {
			workItem.setBankAccount(bankAccount);
		}
		if(verificationRecord != null) {
			workItem.setVerificationRecord(verificationRecord);
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
		if(customer != null) {
			workItem.setCustomer(customer);
		}
		if(otpStore != null) {
			workItem.setOtpStore(otpStore);
		}
		Workitem repoData = workItemRepo.save(workItem);
		if (repoData == null) {
			new RuntimeErrorException(null, "Error while creating WorkItem");
		}
		return repoData;
	}

}
