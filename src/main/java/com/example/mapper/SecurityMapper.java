package com.example.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.dto.SecurityDTO;
import com.example.entity.Security;

@Component
public class SecurityMapper {

	public List<SecurityDTO> mapSecurity(List<Security> security, String userId) {
		return security.stream().map(securityMap -> {
			SecurityDTO securityDT = new SecurityDTO();
			securityDT.setId(String.valueOf(securityMap.getId()));
			securityDT.setUserCode(securityMap.getUserCode());
			securityDT.setUserName(securityMap.getUserName());
			securityDT.setEmail(securityMap.getEmail());
			securityDT.setIsEmailVerified(securityMap.getIsEmailVerified());
			securityDT.setIsUserCodeVerified(securityMap.getIsUserCodeVerified());
			securityDT.setRemainingTime(securityMap.getEndTime());
			securityDT.setCreatedTime(securityMap.getUpdateTime());
			return securityDT;
		}).collect(Collectors.toList());
	}

}
