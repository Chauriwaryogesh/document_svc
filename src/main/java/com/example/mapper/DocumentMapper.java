package com.example.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.dto.AddDocument;
import com.example.dto.DocumentDTO;
import com.example.entity.Document;

@Component
public class DocumentMapper {

	public List<DocumentDTO> mapDOcumentDtls(Optional<Document> documentOpt) {
		List<DocumentDTO> documentList = new ArrayList<>();
		Document document = documentOpt.get();
		return documentList.stream().map(list -> {
			DocumentDTO documentDTO = new DocumentDTO();
			documentDTO.setId(document.getDocId());
			documentDTO.setDocName(document.getDocName());
			documentDTO.setDocType(document.getDocType());
			documentDTO.setCreatedBy(document.getCreatedBy());
			documentDTO.setUpdatedBy(document.getUpdatedBy());
			return documentDTO;
		}).collect(Collectors.toList());

	}

	public List<Document> uploadDoc(List<AddDocument> documentList) {
		// TODO Auto-generated method stub
		return null;
	}

}
