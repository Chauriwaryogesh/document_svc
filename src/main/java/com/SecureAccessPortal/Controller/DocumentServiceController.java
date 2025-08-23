package com.SecureAccessPortal.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.CommonConstants.ErrorConstants;
import com.SecureAccessPortal.Entity.DocumentEntity;
import com.SecureAccessPortal.Modal.DocumentDTO;
import com.SecureAccessPortal.Modal.NotesDTO;
import com.SecureAccessPortal.Service.IDocumentService;

@RestController
@RequestMapping("/documentService")
public class DocumentServiceController {

	@Autowired
	private IDocumentService docmentSrvice;

	@RequestMapping(value = "upload-getDocument", method = RequestMethod.GET, produces = { "image/png", "image/jpeg",
			"application/pdf" })
	public ResponseEntity<byte[]> getDocument(@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "docName", required = false) String docName,
			@RequestHeader(value = "userCode", required = false) String userCode) {

		DocumentEntity document = docmentSrvice.getDocumentdtls(id, docName, userCode);
		if (document != null) {
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + document.getDocumentName() + "\"")
					.header(HttpHeaders.CONTENT_TYPE, document.getType()) // Set correct MIME type (image/png,
																				// image/jpeg, etc.)
					.body(document.getData());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	

	@RequestMapping(value = "upload-getAllDocuments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public com.SecureAccessPortal.Service.ResponseEntity<List<DocumentDTO>> getDocument(
			@RequestHeader(value = "userCode", required = false) String userCode) {

		com.SecureAccessPortal.Service.ResponseEntity<List<DocumentDTO>> docslist = new com.SecureAccessPortal.Service.ResponseEntity<>();
		List<DocumentDTO> document = docmentSrvice.getAllDocuments(userCode);
		if (document != null) {
			docslist.setData(document);
		} else {
			docslist.setErrorMessage("documentListisEmpty");

		}
		return docslist;
	}

	@PostMapping("/capture-upload")
	public ResponseEntity<String> uploadPhoto(@RequestParam("image") MultipartFile image,
			@RequestParam("name") String name) {
		try {
			docmentSrvice.savePhoto(name, image);
			return ResponseEntity.ok("Photo saved successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving photo: " + e.getMessage());
		}
	}

	@GetMapping("/capture-getAllDocument")
	public com.SecureAccessPortal.Service.ResponseEntity<List<DocumentDTO>> getCaptureAllDocument(
			@RequestHeader(value = "userCode", required = false) String userCode) {

		com.SecureAccessPortal.Service.ResponseEntity<List<DocumentDTO>> docslist = new com.SecureAccessPortal.Service.ResponseEntity<>();
		List<DocumentDTO> document = docmentSrvice.getCaptureAllDocuments(userCode);
		if (document != null) {
			docslist.setData(document);
		} else {
			docslist.setErrorMessage("documentListisEmpty");

		}
		return docslist;
	}

	@RequestMapping(value = "capture-getDocument", method = RequestMethod.GET, produces = { "image/png", "image/jpeg",
			"application/pdf" })
	public ResponseEntity<byte[]> getCaptureDocument(@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "docName", required = false) String docName,
			@RequestHeader(value = "userCode", required = false) String userCode) {

		DocumentDTO document = docmentSrvice.getCaptureDocumentdtls(id, docName, userCode);
		if (document != null) {
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + document.getDocName() + "\"")
					.header(HttpHeaders.CONTENT_TYPE, document.getDocType()) // Set correct MIME type (image/png,
																				// image/jpeg, etc.)
					.body(document.getData());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@PostMapping(value = "/notes", consumes = MediaType.APPLICATION_JSON_VALUE)
	public com.SecureAccessPortal.Service.ResponseEntity<String> createNote(@RequestBody NotesDTO note,
			@RequestHeader(required = false) String userCode) {
		com.SecureAccessPortal.Service.ResponseEntity<String> responce = new com.SecureAccessPortal.Service.ResponseEntity<>();
		String saved = docmentSrvice.addNotes(note, userCode);
		if (saved.contains(CommonConstant.FAILURE)) {
			responce.setStatus(CommonConstant.FAILURE);
			responce.setErrorMessage("failed to create");
		} else {
			responce.setData(saved);
			responce.setStatus(CommonConstant.SUCCESS);
		}

		return responce;
	}

	@GetMapping("/notes/List")
	public com.SecureAccessPortal.Service.ResponseEntity<Page<NotesDTO>> fetchNotes(
			@RequestParam(value = "customerNo", required = false) String customerNo,
			@RequestParam(value = "priority", required = false) String priority,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "deleted", required = false) boolean deleted,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size,
			@RequestHeader(value = "userCode", required = false) String userCode) {
		com.SecureAccessPortal.Service.ResponseEntity<Page<NotesDTO>> responce = new com.SecureAccessPortal.Service.ResponseEntity<>();
		Page<NotesDTO> saved = docmentSrvice.getList(customerNo, priority, userCode, startDate, endDate, deleted, id,
				page, size);
		if (saved != null) {
			responce.setStatus(CommonConstant.SUCCESS);
			responce.setData(saved);
		} else {
			responce.setStatus(CommonConstant.FAILURE);
			responce.setErrorMessage("No Notes found");
		}

		return responce;
	}

	@PostMapping(value = "/recycleBin")
	public com.SecureAccessPortal.Service.ResponseEntity<String> deleteNote(@RequestBody NotesDTO note,
			@RequestHeader(required = false) String userCode) {
		com.SecureAccessPortal.Service.ResponseEntity<String> response = new com.SecureAccessPortal.Service.ResponseEntity<>();
		String statusUpdate = docmentSrvice.updateStatus(note, userCode);
		if(statusUpdate.contains("Invalid")) {
			response.setStatus(CommonConstant.FAILURE);
			response.setData(statusUpdate);
		}else {
			response.setData(statusUpdate);
			response.setStatus(CommonConstant.SUCCESS);
		}
		
		return response;
	}
	
	@RequestMapping(value = "myDocumentsNew", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public com.SecureAccessPortal.Service.ResponseEntity<Page<DocumentDTO>> myDocuments(
			@RequestParam(required= false) String documentId,
			@RequestParam(required= false) String documentName,
			@RequestParam(required= false) String policyNumber,
			@RequestParam(required= false) String customerNumber,
			@RequestParam(required= false) String bankAccountNumber,
			@RequestParam(required= false) String status,
			@RequestParam(required= false) boolean deleted,
			@RequestParam(required= false) String type,
			@RequestParam(required= false) String startDate,
			@RequestParam(required= false) String endDate,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestHeader(value = "userCode", required = false) String userCode) {
		com.SecureAccessPortal.Service.ResponseEntity<Page<DocumentDTO>> docslist = new com.SecureAccessPortal.Service.ResponseEntity<>();
		Page<DocumentDTO> document = docmentSrvice.myDocuments(documentId, documentName, policyNumber, type,
				customerNumber,startDate,endDate, page, size,bankAccountNumber,status,deleted);
		if (document != null) {
			docslist.setData(document);
			docslist.setStatus(CommonConstant.SUCCESS);
		} else {
			docslist.setStatus(ErrorConstants.FAILURE);
			docslist.setErrorMessage("document List is Empty");
		}
		return docslist;
	}
	
	@PostMapping(value = "/uploadDocument", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public com.SecureAccessPortal.Service.ResponseEntity<List<DocumentDTO>> uploadDocument(@RequestBody List<DocumentDTO>  documentDTO,
			@RequestHeader String userCode) {
		com.SecureAccessPortal.Service.ResponseEntity<List<DocumentDTO>> response = new com.SecureAccessPortal.Service.ResponseEntity<>();
		try {
			response = docmentSrvice.uploadDocument(documentDTO, userCode);
			if (response != null) {
				response.setStatus(CommonConstant.SUCCESS);
			}
		} catch (Exception e) {
			response.setErrorMessage(ErrorConstants.FAILURE);
		}
		return response;
	}
	
	
	@PostMapping(value = "/updateStatus", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public com.SecureAccessPortal.Service.ResponseEntity<DocumentDTO> updateStatus(@RequestBody DocumentDTO  documentDTO,
			@RequestHeader String userCode) {
		com.SecureAccessPortal.Service.ResponseEntity<DocumentDTO> response = new com.SecureAccessPortal.Service.ResponseEntity<>();
		try {
			response = docmentSrvice.updateStatusOfDocument(documentDTO, userCode);
			if (response != null) {
				response.setStatus(CommonConstant.SUCCESS);
			}
		} catch (Exception e) {
			response.setErrorMessage(ErrorConstants.FAILURE);
		}
		return response;
	}
	

}
