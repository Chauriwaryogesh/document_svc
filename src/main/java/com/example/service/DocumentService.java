package com.example.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dto.AddDocument;
import com.example.dto.DocumentDTO;
import com.example.entity.Document;
import com.example.mapper.DocumentMapper;
import com.example.repo.IDocumentRepo;

@Service
public class DocumentService implements IDocumentService {

	@Autowired
	private IDocumentRepo docRepo;

	@Autowired
	private DocumentMapper documentMapper;

	@Override
	public Document getDocumentdtls(String id, String docName, String userId) {
		// List<DocumentDTO> documentDto = new ArrayList<>();
		Optional<Document> documents = docRepo.findById(Long.valueOf(id));
		Document document = null;
		if (documents.isPresent()) {
			document = documents.get();
			// documentDto = documentMapper.mapDOcumentDtls(documents);
		}
		return document;
	}

	@Override
	public String uploadDocService(List<AddDocument> documentList, String userId) {

		List<Document> document = documentMapper.uploadDoc(documentList);

		docRepo.save(document.get(0));

		String str = "Document uplaod successfully";

		return str;
	}

	public Document uploadDocument(MultipartFile file, String userId) throws IOException {
		Document document = new Document();
		document.setDocId(String.valueOf(UUID.randomUUID()));
		document.setDocName(file.getOriginalFilename());
		document.setDocType(file.getContentType());
		document.setCreatedBy(userId);
		document.setUpdatedBy(userId);
		document.setData(file.getBytes());

		return docRepo.save(document);
	}

	@Override
	public List<DocumentDTO> getAllDocuments(String userId) {
		List<Document> documents = docRepo.findAll();
		return  documents.stream().map(file ->{
			DocumentDTO document= new DocumentDTO();
			document.setId(String.valueOf(file.getId()));
			document.setDocName(file.getDocName());
			document.setDocType(file.getDocType());
			document.setCreatedBy(file.getCreatedBy());
			document.setUpdatedBy(file.getUpdatedBy());
			document.setData(file.getData());
			return document;	
		}).collect(Collectors.toList());
	}

}
