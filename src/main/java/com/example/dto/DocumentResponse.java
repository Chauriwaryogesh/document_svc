package com.example.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class DocumentResponse {
	private long id;
	private String name;
	private String type;
	private String size;
	private String content;
	private String message;

//	public DocumentResponse(Long id, String name, String type, String size,JsonNode content, String message) {
//		this.id=id;
//		this.name=name;
//		this.type=type;
//		this.size=size;
//		this.content= content;
//		this.message=message;
//		
//	}

	

	public long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
