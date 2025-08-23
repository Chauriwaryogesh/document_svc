package com.SecureAccessPortal.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.SecureAccessPortal.Entity.DocumentEntity;
import com.SecureAccessPortal.Modal.DocumentDTO;
import com.SecureAccessPortal.Modal.NotesDTO;

public interface IDocumentService {
	public DocumentEntity getDocumentdtls(String id, String docName, String userCode);

	public List<DocumentDTO> getAllDocuments(String userCode);

	public void savePhoto(String name, MultipartFile image) throws IOException;

	public List<DocumentDTO> getCaptureAllDocuments(String userCode);

	public DocumentDTO getCaptureDocumentdtls(String id, String docName, String userCode);

	public String addNotes(NotesDTO note, String userCode);

	public Page<NotesDTO> getList(String customerNo, String priority, String userCode, String startDate, String endDate,
			boolean deleted, String id, int page, int size);

	public String updateStatus(NotesDTO note, String userCode);

	public Page<DocumentDTO> myDocuments(String documentId, String documentName, String policyNumber, String type,
			String customerNumber, String startDate, String endDate, int page, int size, String bankAccountNumber, String status, boolean deleted);

	public ResponseEntity<List<DocumentDTO>> uploadDocument(List<DocumentDTO> documentDTO, String userCode);
	public ResponseEntity<DocumentDTO> updateStatusOfDocument(DocumentDTO documentDTO, String userCode);
}
