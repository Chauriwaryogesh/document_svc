package com.example.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.ActivityDetailsDTO;
import com.example.dto.Queue;
import com.example.dto.WorkItemCount;
import com.example.dto.WorkItemDTO;
import com.example.entity.ActivityDtls;
import com.example.entity.WorkItem;
import com.example.mapper.WorkItemMapper;
import com.example.repo.ActivityDtlsRepo;

@Service
public class WorkItemService implements IWorkItemService {

	@Autowired
	private com.example.repo.IWorkItemService repository;

	@Autowired
	private WorkItemMapper workItemMapper;

	@Autowired
	private ActivityDtlsRepo activityDtlsRepo;

	@Override
	public WorkItemDTO createWorkItem(WorkItemDTO workItemRequest, String userId) {
		WorkItemDTO workItemDTO = new WorkItemDTO();
		try {
			com.example.entity.WorkItem workItem = new com.example.entity.WorkItem();
			workItem.setWorkItemId(String.valueOf(UUID.randomUUID()));
			workItem.setComment(workItemRequest.getComment());
			workItem.setCreatedBy(userId);
			workItem.setCreatedTime(LocalDateTime.now());
			if (userId != null) {
				workItem.setUserId(userId);
			} else {
				workItem.setUserId(workItemRequest.getCreatedBy());
			}
			workItem.setWorkItemName(workItemRequest.getWorkItemName());
			workItem.setWorkType(userId);

			String refNo = generateRandomWorkItemRefNumber();
			workItem.setWorkItemRefNumber(refNo);
			workItem.setStatus(workItemRequest.getStatus());
			WorkItem repoData = repository.save(workItem);
			if (repoData == null) {
				workItemDTO.setComment("failed to create WorkItem");
			} else {
				// MApping for Response
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
	public ResponseEntity<List<ActivityDetailsDTO>> getActivityDtls(String userId) {
		ResponseEntity<List<ActivityDetailsDTO>> response = new ResponseEntity<List<ActivityDetailsDTO>>();
		List<ActivityDtls> activityDtls = activityDtlsRepo.findByUserId(userId);
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
				activityDetailsDTO.setUserId(activity.getUserId());
				return activityDetailsDTO;
			}).collect(Collectors.toList());

			response.setData(activityList);

		} else {
			response.setErrorMessage("Not activity Found");
		}

		return response;
	}

	@Override
	public List<WorkItemDTO> fetchWorkItems(String wiRefNum, String userId) {
		List<WorkItemDTO> workItemDTOList = new ArrayList<>();
		try {
			if (wiRefNum != null) {
				Optional<WorkItem> workItemsList = repository.findByWiRefNum(wiRefNum);
				workItemDTOList = workItemsList.stream().map(workItems -> {
					WorkItemDTO workItem = new WorkItemDTO();
					workItem.setWorkItemId(workItems.getWorkItemId());
					workItem.setComment(workItems.getComment());
					workItem.setCreatedBy(workItems.getCreatedBy());
					workItem.setCreatedTime(String.valueOf(workItems.getCreatedTime()));
					workItem.setUserId(workItems.getUserId());
					workItem.setWorkItemName(workItems.getWorkItemName());
					workItem.setWorkType(workItems.getWorkType());
					workItem.setWorkItemReferenceNumber(workItems.getWorkItemRefNumber());
					workItem.setStatus(workItems.getStatus());
					workItem.setQueue(workItems.getQueue());
					return workItem;
				}).collect(Collectors.toList());
			} else {
				List<WorkItem> workItemsList = repository.findAll();
				workItemDTOList = workItemsList.stream().map(workItems -> {
					WorkItemDTO workItem = new WorkItemDTO();
					workItem.setWorkItemId(workItems.getWorkItemId());
					workItem.setComment(workItems.getComment());
					workItem.setCreatedBy(workItems.getCreatedBy());
					workItem.setCreatedTime(String.valueOf(workItems.getCreatedTime()));
					workItem.setUserId(workItems.getUserId());
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
		return workItemDTOList;
	}

	@Override
	public WorkItemCount workItemCount(String userId) {
		WorkItemCount workItemCount = new WorkItemCount();

		Queue queue = new Queue();
		List<WorkItem> workItemsList = repository.findAll();

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

		long complaintsTeam = workItemsList.stream().filter(que -> que.getQueue()!= null && que.getQueue().equalsIgnoreCase("COMPLAINTS_TEAM")).count();
		long adminTeam = workItemsList.stream().filter(que -> que.getQueue()!= null &&que.getQueue().equalsIgnoreCase("ADMIN_TEAM")).count();
		long workflowTeam = workItemsList.stream().filter(que -> que.getQueue()!= null &&que.getQueue().equalsIgnoreCase("WORKFLOW_TEAM"))
				.count();
		long teamMember = workItemsList.stream().filter(que -> que.getQueue()!= null &&que.getQueue().equalsIgnoreCase("TEAM_MEMBER")).count();
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

}
