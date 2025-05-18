package com.example.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.dto.DocumentRequest;
import com.example.dto.DocumentResponse;
import com.example.entity.BusinessDocument;
import com.example.repo.DocumentRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NewBusinessService {

	@Autowired
	private DocumentRepo documentRepo;

	public com.example.service.ResponseEntity<DocumentResponse> createDocument(DocumentRequest request, String userId) {

		com.example.service.ResponseEntity<DocumentResponse> response = new com.example.service.ResponseEntity<DocumentResponse>();
		BusinessDocument doc = new BusinessDocument();
		doc.setName(request.getName() + getExtension(request.getType()));
		doc.setType(normalizeType(request.getType()));
		doc.setSize(calculateSize(request.getContent()));
		doc.setUserId(userId);
		storeContent(doc, request.getContent());
		BusinessDocument savedDoc = documentRepo.save(doc);
//        if (savedDoc == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new DocumentResponse("Failed to save document"));
//        }

		// Prepare response
		DocumentResponse documentResponse = new DocumentResponse();

		documentResponse.setMessage("Document created successfully");

//				(savedDoc.getId(), savedDoc.getName(),
//				savedDoc.getType(),null, savedDoc.getSize(), );
		response.setData(documentResponse);
		return response;
	}

	public void storeContent(BusinessDocument doc, JsonNode content) {
		if (content == null) {
			return;
		}

		String contentStr;
		String contentType = doc.getType().toLowerCase();

		// Convert content to string for storage
		try {
			if ("excel".equals(contentType)) {
				// For XLS, content is a JSON array
				if (!content.isArray()) {
					throw new IllegalArgumentException("Excel content must be a JSON array");
				}
				contentStr = new ObjectMapper().writeValueAsString(content);
			} else {
				// For PDF/TXT/DAT/Word, content is a string
				if (!content.isTextual()) {
					throw new IllegalArgumentException(contentType + " content must be a string");
				}
				contentStr = content.asText();
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Failed to serialize content", e);
		}

		// Option 1: Store content in database
		doc.setContent(contentStr);
	}

	private String getExtension(String type) {
		return switch (type) {
		case "XLS" -> ".xlsx";
		case "PDF" -> ".pdf";
		case "Word" -> ".docx";
		case "DAT" -> ".dat";
		case "TXT" -> ".txt";
		default -> "";
		};
	}

	private String normalizeType(String type) {
		return type.equals("XLS") ? "Excel" : type;
	}

	private String calculateSize(Object content) {
		if (content == null) {
			return "0 MB";
		}
		try {
			String contentStr = content instanceof String ? (String) content
					: new ObjectMapper().writeValueAsString(content);
			long bytes = contentStr.getBytes().length;
			return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
		} catch (JsonProcessingException e) {
			return "0.1 MB"; // Fallback
		}
	}

	// New method: Fetch all documents for a user
	public com.example.service.ResponseEntity<List<DocumentResponse>> getAllDocuments(String userId) {
		com.example.service.ResponseEntity<List<DocumentResponse>> documentResponseList = new ResponseEntity<>();

		if (userId == null || userId.trim().isEmpty()) {
			documentResponseList.setErrorMessage("User ID is required");

		}

		List<BusinessDocument> userDocs = documentRepo.findByUserId(userId);
		List<DocumentResponse> responses = userDocs.stream().map(doc -> {
			DocumentResponse documentResponse = new DocumentResponse();
			documentResponse.setId(doc.getId());
			documentResponse.setName(doc.getName());
			documentResponse.setType(doc.getType());
			documentResponse.setSize(doc.getSize());
			documentResponse.setMessage("Document retrieved");
			return documentResponse;
		}).collect(Collectors.toList());

		if (responses.isEmpty()) {
			documentResponseList.setErrorMessage("No documents found");
		} else {
			documentResponseList.setData(responses);
		}

		return documentResponseList;
	}

	public com.example.service.ResponseEntity<List<DocumentResponse>> getDocument(Long id, String userId) {
		com.example.service.ResponseEntity<List<DocumentResponse>> documentResponse = new ResponseEntity<>();
		BusinessDocument doc = documentRepo.findById(id).orElse(null);
		if (id == null || id <= 0) {
			documentResponse.setErrorMessage("Invalid document ID");
		}
		if (userId == null || userId.trim().isEmpty()) {
			documentResponse.setErrorMessage("User ID is required");
		}
		if (doc == null) {
			documentResponse.setErrorMessage("Document not found");
		}

		if (!userId.equals(doc.getUserId())) {
			documentResponse.setErrorMessage("You are not authorized to access this document");
		}

		DocumentResponse document = new DocumentResponse();
		document.setId(doc.getId());
		document.setName(doc.getName());
		document.setType(doc.getType());
		document.setSize(doc.getSize());
		try {
			JsonNode contentNode = doc.getContent() != null ? new ObjectMapper().readTree(doc.getContent()) : null;
			document.setContent(contentNode);
		} catch (JsonProcessingException e) {
			documentResponse.setErrorMessage("Failed to parse document content");
		}
		document.setMessage("Document retrieved");
		documentResponse.setData(Arrays.asList(document));
		return documentResponse;
	}
}