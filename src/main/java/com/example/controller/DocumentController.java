package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.dto.AddDocument;
import com.example.entity.Document;
import com.example.service.IDocumentService;

@RestController
@RequestMapping("/DocumentService")
public class DocumentController {

	@Autowired
	private IDocumentService docmentSrvice;

	@RequestMapping(value = "getDocumentDtls", method = RequestMethod.GET,produces = {"image/png", "image/jpeg", "application/pdf"})
	public ResponseEntity<byte[]> getDocument(@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "docName", required = true) String docName,
			@RequestHeader(value = "userId", required = true) String userId) {
		Document document = docmentSrvice.getDocumentdtls(id, docName, userId);
		if(document != null) {
		return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + document.getDocName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, document.getDocType()) // Set correct MIME type (image/png, image/jpeg, etc.)
                .body(document.getData());
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
	}

	@RequestMapping(value = "/uploadDoc", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE
	/* , produces = MediaType.APPLICATION_JSON_VALUE */)
	public String uploadDoc(@RequestBody List<AddDocument> document,
			@RequestHeader String userId) {
	
		String message= docmentSrvice.uploadDocService(document, userId);
		
		return message;
		
	}
	
	@PostMapping(value ="/upload",consumes = "multipart/form-data")
    public ResponseEntity<String> uploadDocument(@RequestParam("file") MultipartFile file,
    		@RequestHeader String userId) {
        try {
            Document document = docmentSrvice.uploadDocument(file,userId);
            return ResponseEntity.ok("Document uploaded successfully. ID: " + document.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload document: " + e.getMessage());
        }
    }

}
