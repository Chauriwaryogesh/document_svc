package com.example.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.dto.AddDocument;
import com.example.dto.DocumentDTO;
import com.example.entity.Document;

public interface IDocumentService {
	public Document getDocumentdtls(String id, String docName, String userId);

	public String uploadDocService(List<AddDocument> document, String userId);

	public Document uploadDocument(MultipartFile file, String userId) throws IOException;

}
