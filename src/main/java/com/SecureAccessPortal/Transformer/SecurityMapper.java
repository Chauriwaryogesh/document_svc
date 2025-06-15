package com.SecureAccessPortal.Transformer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.SecureAccessPortal.Entity.Security;
import com.SecureAccessPortal.Modal.SecurityDTO;
import com.SecureAccessPortal.util.DateUtil;

@Component
public class SecurityMapper {
	
	@Autowired
	private DateUtil dateUtil;

	public List<SecurityDTO> mapSecurity(List<Security> security, String userCode) {
		return security.stream().map(securityMap -> {
			SecurityDTO securityDT = new SecurityDTO();
			securityDT.setId(String.valueOf(securityMap.getId()));
			securityDT.setUserCode(securityMap.getUserCode());
			securityDT.setUserName(securityMap.getUserName());
			securityDT.setEmail(securityMap.getEmail());
			securityDT.setIsEmailVerified(securityMap.getIsEmailVerified());
			securityDT.setIsUserCodeVerified(securityMap.getIsUserCodeVerified());
			securityDT.setRemainingTime(String.valueOf(securityMap.getEndTime()));
			securityDT.setCreatedTime(String.valueOf(securityMap.getUpdateTime()));
			return securityDT;
		}).collect(Collectors.toList());
	}

}
