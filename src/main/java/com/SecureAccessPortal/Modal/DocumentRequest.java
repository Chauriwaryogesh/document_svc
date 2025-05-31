package com.SecureAccessPortal.Modal;

import com.fasterxml.jackson.databind.JsonNode;

public class DocumentRequest {
	private String name;
	private String type;
	private String userId;
	private JsonNode content;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public JsonNode getContent() {
		return content;
	}
	public void setContent(JsonNode content) {
		this.content = content;
	}
	}


