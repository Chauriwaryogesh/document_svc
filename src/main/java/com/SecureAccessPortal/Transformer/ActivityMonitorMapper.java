package com.SecureAccessPortal.Transformer;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Entity.LoginHistory;
import com.SecureAccessPortal.Modal.LoginHistroryResponse;
import com.SecureAccessPortal.Service.ResponseEntity;

@Component
public class ActivityMonitorMapper {

	public ResponseEntity<List<LoginHistroryResponse>> mapLoginHistory(List<LoginHistory> loginHistoryList) {
		ResponseEntity<List<LoginHistroryResponse>> response = new ResponseEntity<>();

		List<LoginHistroryResponse> listResponse = loginHistoryList.stream().filter(Objects::nonNull).map(history -> {
			LoginHistroryResponse loginHistroryResponse = new LoginHistroryResponse();
			loginHistroryResponse.setCreatedBy(Optional.ofNullable(history.getCreatedBy()).orElse(""));
			loginHistroryResponse.setCreatedTime(Optional.ofNullable(history.getCreatedTime()).orElse(null));
			loginHistroryResponse.setDeviceInfo(Optional.ofNullable(history.getDeviceInfo()).orElse(""));
			loginHistroryResponse.setId(Optional.ofNullable(history.getId()).orElse(null));
			loginHistroryResponse.setIpAddress(Optional.ofNullable(history.getIpAddress()).orElse(""));
			loginHistroryResponse.setLocation(Optional.ofNullable(history.getLocation()).orElse(""));
			Timestamp loginTimestamp = history.getLoginTime();
			if (loginTimestamp != null) {
				LocalDateTime loginTime = loginTimestamp.toLocalDateTime();
				LocalDateTime logoutTime = loginTime.plusMinutes(30);
				loginHistroryResponse.setLoginTime(history.getLoginTime());
				loginHistroryResponse.setLogoutTime(history.getLogoutTime());
				Duration duration = Duration.between(loginTime, logoutTime);
				long hours = duration.toHours();
				long minutes = duration.toMinutesPart();
				long seconds = duration.toSecondsPart();
				String totalScreenTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
				loginHistroryResponse.setTotalScreenTime(totalScreenTime);
			} else {
				loginHistroryResponse.setLoginTime(null);
				loginHistroryResponse.setLogoutTime(null);
				loginHistroryResponse.setTotalScreenTime("");
			}
			loginHistroryResponse.setLoggedinStatus(CommonConstant.YES);

			loginHistroryResponse.setLoginMethod(Optional.ofNullable(history.getLoginMethod()).orElse(""));
			loginHistroryResponse.setMfaUsed(Optional.ofNullable(history.isMfaUsed()).orElse(false));
			loginHistroryResponse.setRiskScore(Optional.ofNullable(history.getRiskScore()).orElse(null));
			loginHistroryResponse.setSessionId(Optional.ofNullable(history.getSessionId()).orElse(""));

			loginHistroryResponse.setUpdatedBy(Optional.ofNullable(history.getUpdatedBy()).orElse(""));
			loginHistroryResponse.setUpdatedTime(Optional.ofNullable(history.getUpdatedTime()).orElse(null));
			loginHistroryResponse.setUserCode(Optional.ofNullable(history.getUserCode()).orElse(""));

			return loginHistroryResponse;
		}).collect(Collectors.toList());
		response.setData(listResponse);
		response.setStatus(CommonConstant.SUCCESS);
		return response;
	}

}
