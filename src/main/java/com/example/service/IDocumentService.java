package com.example.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.dto.AddDocument;
import com.example.dto.DocumentDTO;
import com.example.dto.NotesDTO;
import com.example.entity.Document;

public interface IDocumentService {
	public Document getDocumentdtls(String id, String docName, String userId);

	public String uploadDocService(List<AddDocument> document, String userId);

	public Document uploadDocument(MultipartFile file, String docName , String userId) throws IOException;

	public List<DocumentDTO> getAllDocuments(String userId);

	public String save(NotesDTO note);

	public List<NotesDTO> getList(String userId);

	public boolean deleteNoteById(Long id);

	public void savePhoto(String name, MultipartFile image) throws IOException;

	public List<DocumentDTO> getCaptureAllDocuments(String userId);

	public DocumentDTO getCaptureDocumentdtls(String id, String docName, String userId);

}
