package com.example.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.WorkItemDTO;
import com.example.entity.WorkItem;
import com.example.mapper.WorkItemMapper;

@Service
public class WorkItemService implements IWorkItemService {

	@Autowired
	private com.example.repo.IWorkItemService repository;

	@Autowired
	private WorkItemMapper workItemMapper;

	@Override
	public WorkItemDTO createWorkItem(WorkItemDTO workItemRequest, String userId) {
		WorkItemDTO workItemDTO = new WorkItemDTO();
		try {
			com.example.entity.WorkItem workItem = new com.example.entity.WorkItem();
			workItem.setWorkItemId(String.valueOf(UUID.randomUUID()));
			workItem.setComment(workItemRequest.getComment());
			workItem.setCreatedBy(workItemRequest.getCreatedBy());
			workItem.setCreatedTime(LocalDateTime.now());
			if(userId != null) {
				workItem.setUserId(userId);
			}else{
			workItem.setUserId(workItemRequest.getCreatedBy());
			}
			workItem.setWorkItemName(workItemRequest.getWorkItemName());
			workItem.setWorkType(userId);

			String refNo = generateRandomWorkItemRefNumber();
			workItem.setWorkItemRefNumber(refNo);
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

}
