package com.SecureAccessPortal.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SecureAccessPortal.CommonConstants.CommonConstant;
import com.SecureAccessPortal.Modal.ChatbotRequest;
import com.SecureAccessPortal.Modal.ChatbotResponse;
import com.SecureAccessPortal.Service.ChatbotService;
import com.SecureAccessPortal.Service.ResponseEntity;

@RestController
@RequestMapping("/chatbot")
public class ChatbotController {

	private final ChatbotService chatbotService;

	@Autowired
	public ChatbotController(ChatbotService chatbotService) {
		this.chatbotService = chatbotService;
	}

	@PostMapping
	public ResponseEntity<ChatbotResponse> handleChatbotRequest(@RequestBody ChatbotRequest request) {
		ResponseEntity resp = new ResponseEntity<>();
		ChatbotResponse response = chatbotService.processMessage(request);
		if (response != null) {
			resp.setData(response);
			resp.setStatus(CommonConstant.SUCCESS);
		} else {
			resp.setStatus(CommonConstant.FAILURE);
		}

		return resp;
	}
}