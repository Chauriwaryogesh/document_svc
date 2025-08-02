package com.SecureAccessPortal.Controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.SecureAccessPortal.Entity.BusinessDocument;
import com.SecureAccessPortal.Entity.CapturePhoto;
import com.SecureAccessPortal.Modal.DocumentRequest;
import com.SecureAccessPortal.Modal.DocumentResponse;
import com.SecureAccessPortal.Modal.NotesDTO;
import com.SecureAccessPortal.Modal.PhotoDTO;
import com.SecureAccessPortal.Service.IDocumentService;

@RestController
@RequestMapping("/DocumentService")
public class PhotoCaptureController {

	@Autowired
	private IDocumentService docmentSrvice;

	@RequestMapping(value = "upload-getDocument", method = RequestMethod.GET, produces = { "image/png", "image/jpeg",
			"application/pdf" })
	public ResponseEntity<byte[]> getDocument(@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "docName", required = false) String docName,
			@RequestHeader(value = "userCode", required = false) String userCode) {

		CapturePhoto document = docmentSrvice.getDocumentdtls(id, docName, userCode);
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

	@PostMapping(value = "/uploadDocument", consumes = "multipart/form-data")
	public ResponseEntity<String> uploadDocument(@RequestParam("file") MultipartFile file,
			@RequestParam("Doc Name") String docName, @RequestHeader String userCode) {
		try {
			CapturePhoto document = docmentSrvice.uploadDocument(file, docName, userCode);
			return ResponseEntity.ok("Document uploaded successfully. ID: " + document.getId());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to upload document: " + e.getMessage());
		}
	}

	@RequestMapping(value = "upload-getAllDocuments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public com.SecureAccessPortal.Service.ResponseEntity<List<PhotoDTO>> getDocument(
			@RequestHeader(value = "userCode", required = false) String userCode) {

		com.SecureAccessPortal.Service.ResponseEntity<List<PhotoDTO>> docslist = new com.SecureAccessPortal.Service.ResponseEntity<>();
		List<PhotoDTO> document = docmentSrvice.getAllDocuments(userCode);
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
	public com.SecureAccessPortal.Service.ResponseEntity<List<PhotoDTO>> getCaptureAllDocument(
			@RequestHeader(value = "userCode", required = false) String userCode) {

		com.SecureAccessPortal.Service.ResponseEntity<List<PhotoDTO>> docslist = new com.SecureAccessPortal.Service.ResponseEntity<>();
		List<PhotoDTO> document = docmentSrvice.getCaptureAllDocuments(userCode);
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

		PhotoDTO document = docmentSrvice.getCaptureDocumentdtls(id, docName, userCode);
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

	@PostMapping("/notes")
	public com.SecureAccessPortal.Service.ResponseEntity<String> createNote(@RequestBody NotesDTO note) {
		com.SecureAccessPortal.Service.ResponseEntity<String> responce = new com.SecureAccessPortal.Service.ResponseEntity<>();
		String saved = docmentSrvice.save(note);
		responce.setData(saved);
		return responce;
	}

	@GetMapping("/notes/List")
	public com.SecureAccessPortal.Service.ResponseEntity<List<NotesDTO>> fetchNotes(
			@RequestHeader(value = "userCode", required = false) String userCode) {
		com.SecureAccessPortal.Service.ResponseEntity<List<NotesDTO>> responce = new com.SecureAccessPortal.Service.ResponseEntity<>();
		List<NotesDTO> saved = docmentSrvice.getList(userCode);
		responce.setData(saved);
		return responce;
	}

	@DeleteMapping("notes/delete/{id}")
	public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
		boolean deleted = docmentSrvice.deleteNoteById(id);
		return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}

}
