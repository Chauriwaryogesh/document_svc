package com.SecureAccessPortal.Modal;

import java.util.HashMap;
import java.util.Map;

public class ChatbotResponse {
	private String greeting;
	private Map<String, String> links;

	// Constructors
	public ChatbotResponse() {
		this.links = new HashMap<>();
	}

	public ChatbotResponse(String greeting, Map<String, String> links) {
		this.greeting = greeting;
		this.links = links;
	}

	// Getters and setters
	public String getGreeting() {
		return greeting;
	}

	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}

	public Map<String, String> getLinks() {
		return links;
	}

	public void setLinks(Map<String, String> links) {
		this.links = links;
	}
}
