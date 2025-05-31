package com.SecureAccessPortal.Service;

import java.io.Serializable;

public class ResponseEntity<T> implements Serializable {

	public ResponseEntity() {
		super();
	}
	T data;
	String links;
	String metaData;
	String errorMessage;
	public String getLinks() {
		return links;
	}
	public void setLinks(String links) {
		this.links = links;
	}
	public String getMetaData() {
		return metaData;
	}
	public void setMetaData(String metaData) {
		this.metaData = metaData;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "ResponseEntity [data=" + data + ", links=" + links + ", metaData=" + metaData + ", errorMessage="
				+ errorMessage + "]";
	}
	
	
	
}
