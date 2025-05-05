package com.example.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostalResponse {
	    @JsonProperty("Message") 
	    public String message;
	    @JsonProperty("Status") 
	    public String status;
	    @JsonProperty("PostOffice") 
	    public List<PostOffice> postOffice;
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public List<PostOffice> getPostOffice() {
			return postOffice;
		}
		public void setPostOffice(List<PostOffice> postOffice) {
			this.postOffice = postOffice;
		}
	    
	}

