package com.SecureAccessPortal.Transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.SecureAccessPortal.Entity.VerificationRecord;
import com.SecureAccessPortal.Modal.VerificationRecordDTO;

@Component
public class BankMapper {
	public List<VerificationRecordDTO> mapVerificationRecordList(List<VerificationRecord> verRecords) {
		List<VerificationRecordDTO> listOfDocument = new ArrayList<>();
		listOfDocument = verRecords.parallelStream().map(record -> {
			VerificationRecordDTO dto = new VerificationRecordDTO();
			dto.setId(record.getVerId());
			dto.setAccountNo(record.getBank().getAccountNumber());
			dto.setCustomerNo(record.getCustomer().getCustomerNo());
			dto.setPolicyNumber(record.getPolicy().getPolicyNumber());
			dto.setUserCode(record.getUserCode());
			dto.setIdCertNo(record.getIdentity());
			dto.setIdentityStatus(record.getIdentityStatus());
			dto.setIdCertificate(record.getIdentityDocs());
			dto.setDeathCertNo(record.getDeath());
			dto.setDeathStatus(record.getDeathStatus());
			dto.setDeathCertificate(record.getDeathDocs());
			dto.setSancStatus(record.getSanctions());
			dto.setSanctionsCertNo(record.getSanctions());
			dto.setSanctionsCertificate(record.getSanctionsDocs());
			dto.setCreatedBy(record.getCreatedBy());
			dto.setUpdatedBy(record.getUpdatedBy());
			dto.setUpdatedDate(record.getUpdatedTime());
			dto.setCreatedDate(record.getCreatedTime());
			return dto;
		}).collect(Collectors.toList());

		return listOfDocument;
	}
}
