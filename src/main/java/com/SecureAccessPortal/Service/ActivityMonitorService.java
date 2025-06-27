package com.SecureAccessPortal.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Entity.ActivityDtls;
import com.SecureAccessPortal.Entity.LoginHistory;
import com.SecureAccessPortal.Modal.ActivityDetailsDTO;
import com.SecureAccessPortal.Modal.LoginHistroryResponse;
import com.SecureAccessPortal.Repo.ActivityDtlsRepo;
import com.SecureAccessPortal.Repo.LoginHistoryRepo;
import com.SecureAccessPortal.Transformer.ActivityMonitorMapper;

@Component
public class ActivityMonitorService {

	@Autowired
	private ActivityDtlsRepo activityDtlsRepository;

	@Autowired
	private LoginHistoryRepo loginHistoryRepository;
	
	@Autowired
	private ActivityMonitorMapper activityMonitorMapper;
	

	public ResponseEntity<List<ActivityDetailsDTO>> getActivityDtls(String userCode) {
		ResponseEntity<List<ActivityDetailsDTO>> response = new ResponseEntity<List<ActivityDetailsDTO>>();
		List<ActivityDtls> activityDtls = activityDtlsRepository.findByuserCode(userCode);
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

	public ResponseEntity<List<LoginHistroryResponse>> getLoginHistory(String email, String userCode) {

		ResponseEntity<List<LoginHistroryResponse>> response = new ResponseEntity<>();
		List<LoginHistory> loginHistoryList = loginHistoryRepository.findByEmailAndDeletedFlag(email, userCode,
				CommonConstant.N);
		if (loginHistoryList.isEmpty()) {
			response.setErrorMessage("No Login found for user " + userCode);
			return response;
		} else {
			response = activityMonitorMapper.mapLoginHistory(loginHistoryList);
		}

		return response;
	}

	public ResponseEntity<LoginHistroryResponse> getLoginActTrac(String email, String userCode) {
		ResponseEntity<LoginHistroryResponse> response = new ResponseEntity<>();
		LoginHistroryResponse resp = new LoginHistroryResponse();
		LoginHistory loginHistoryList = loginHistoryRepository.findByEmailAndDeletedFlagLatest(email, userCode,
				CommonConstant.N);

		resp = activityMonitorMapper.mapLoginHistoryOneRec(loginHistoryList);
		response.setData(resp);

		return response;
	}

}
