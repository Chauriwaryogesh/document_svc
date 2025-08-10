package com.SecureAccessPortal.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.SecureAccessPortal.Entity.CapturePhoto;
import com.SecureAccessPortal.Modal.AddDocument;
import com.SecureAccessPortal.Modal.NotesDTO;
import com.SecureAccessPortal.Modal.PhotoDTO;

public interface IDocumentService {
	public CapturePhoto getDocumentdtls(String id, String docName, String userCode);

	public String uploadDocService(List<AddDocument> document, String userCode);

	public CapturePhoto uploadDocument(MultipartFile file, String docName, String userCode) throws IOException;

	public List<PhotoDTO> getAllDocuments(String userCode);

	public void savePhoto(String name, MultipartFile image) throws IOException;

	public List<PhotoDTO> getCaptureAllDocuments(String userCode);

	public PhotoDTO getCaptureDocumentdtls(String id, String docName, String userCode);

	public String addNotes(NotesDTO note, String userCode);

	public Page<NotesDTO> getList(String customerNo, String priority, String userCode, String startDate, String endDate,boolean deleted, String id, int page, int size);

	public String updateStatus(NotesDTO note, String userCode);
}
