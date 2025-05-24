package com.example.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.DocumentRequest;
import com.example.dto.DocumentResponse;
import com.example.service.NewBusinessService;
import com.example.service.ResponseEntity;

@RestController
@RequestMapping("/NewBusinessController")
public class NewBusinessController {

	@Autowired
	private NewBusinessService businessService;

	@PostMapping("/createDocument")
	public com.example.service.ResponseEntity<DocumentResponse> createDocument(@RequestHeader("userId") String userId,
			@RequestBody DocumentRequest request) {
		com.example.service.ResponseEntity<DocumentResponse> documentResponse = new ResponseEntity<DocumentResponse>();
		if (request.getName().isEmpty() || !isValidType(request.getType()) || request.getContent() == null) {
			documentResponse.setErrorMessage("Invalid name, type, or content");
		}
		documentResponse = businessService.createDocument(request, userId);

		return documentResponse;
	}

	private boolean isValidType(String type) {
		return Arrays.asList("XLS", "PDF", "Word", "DAT", "TXT").contains(type);
	}

	@GetMapping("/documents")
	public ResponseEntity<List<DocumentResponse>> getAllDocuments(@RequestHeader("userId") String userId) {
		com.example.service.ResponseEntity<List<DocumentResponse>> documentResponse = new ResponseEntity<>();

		try {
			documentResponse = businessService.getAllDocuments(userId);
		} catch (IllegalArgumentException e) {
			documentResponse.setErrorMessage("Something went wrong");
		} catch (Exception e) {

		}
		return documentResponse;
	}

	@GetMapping("/getDocument")
	public com.example.service.ResponseEntity<List<DocumentResponse>> getDocument(@RequestParam("id") Long id,
			@RequestHeader("userId") String userId) {
		com.example.service.ResponseEntity<List<DocumentResponse>> documentResponse = new ResponseEntity<>();

		try {
			documentResponse = businessService.getDocument(id, userId);
		} catch (IllegalArgumentException e) {
			documentResponse.setErrorMessage("Something went wrong");
		} catch (Exception e) {
			documentResponse.setErrorMessage("Something went wrong");
		}
		return documentResponse;

	}
	@DeleteMapping("documents/delete/{id}")
	public org.springframework.http.ResponseEntity<Void> deleteNote(@PathVariable(value="id",required=true) Long id,
			@RequestHeader("userId") String userId) {
		boolean deleted = businessService.deleteNoteById(id);
		return deleted ? org.springframework.http.ResponseEntity.noContent().build() : org.springframework.http.ResponseEntity.notFound().build();
	}
}
