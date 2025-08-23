package com.SecureAccessPortal.Transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.SecureAccessPortal.Entity.DocumentEntity;
import com.SecureAccessPortal.Modal.DocumentDTO;

@Component
public class DocumentMapper {

	public List<DocumentDTO> mapDOcumentDtls(Optional<DocumentEntity> documentOpt) {
		List<DocumentDTO> documentList = new ArrayList<>();
		DocumentEntity document = documentOpt.get();
		return documentList.stream().map(list -> {
			DocumentDTO documentDTO = new DocumentDTO();
			documentDTO.setId(document.getDocumentId());
			documentDTO.setDocName(document.getDocumentName());
			documentDTO.setDocType(document.getType());
			documentDTO.setCreatedBy(document.getCreatedBy());
			documentDTO.setUpdatedBy(document.getUpdatedBy());
			return documentDTO;
		}).collect(Collectors.toList());

	}

	public List<DocumentDTO> mapAllDocuments(List<DocumentEntity> documents) {
		List<DocumentDTO> collect = documents.stream().map(document -> {
			DocumentDTO documentDTO = new DocumentDTO();
			documentDTO.setDocumentId(document.getDocumentId());
			documentDTO.setDocumentName(document.getDocumentName());
			documentDTO.setType(document.getType());
			documentDTO.setPolicyNumber(document.getPolicyNumber());
			documentDTO.setCustomerNumber(document.getCustomerNumber());
			documentDTO.setBankAccountNumber(document.getBankAccountNumber());
			documentDTO.setStatus(document.getStatus());
			documentDTO.setCreatedBy(document.getCreatedBy());
			documentDTO.setUpdatedBy(document.getUpdatedBy());
			documentDTO.setCreatedTime(String.valueOf(document.getCreatedTime()));
			documentDTO.setEndTime(String.valueOf(document.getEndTime()));
			documentDTO.setData(document.getData());
			return documentDTO;
		}).collect(Collectors.toList());
		return collect;
	}
}
