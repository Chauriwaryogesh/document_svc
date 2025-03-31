package com.example.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.dto.SecurityDTO;
import com.example.entity.Security;

@Component
public class SecurityMapper {

	public List<SecurityDTO> mapSecurity(List<Security> security) {
		return security.stream().map(securityMap -> {
			SecurityDTO securityDT = new SecurityDTO();
			securityDT.setId(securityMap.getId());
			securityDT.setUserCode(securityMap.getUserCode());
			securityDT.setUserName(securityMap.getUserName());
			return securityDT;
		}).collect(Collectors.toList());
	}

}
