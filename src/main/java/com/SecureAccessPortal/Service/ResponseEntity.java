package com.SecureAccessPortal.Service;

import java.io.Serializable;

public class ResponseEntity<T> implements Serializable {

	public ResponseEntity() {
		super();
	}

	T data;

	String status;
	String errorMessage;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
}
