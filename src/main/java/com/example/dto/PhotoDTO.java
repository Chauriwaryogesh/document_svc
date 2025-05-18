package com.example.dto;

public class PhotoDTO {
	private String id;
	private String docName;
	private String docType;
	private String createdBy;
	private String updatedBy;
	private byte[] data;
	
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	@Override
	public String toString() {
		return "DocumentDTO [id=" + id + ", docName=" + docName + ", docType=" + docType + ", createdBy=" + createdBy
				+ ", updatedBy=" + updatedBy + "]";
	}
	
	
	

}
