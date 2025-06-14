package com.SecureAccessPortal.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

	@Entity
	@Table(name ="WorkItem")
	public class Workitem {
		@Id
		@Column
		@NotBlank
		private String workItemRefNumber;
		
		@Column
		private String workItemId;
		
		@Column
		private String workItemName;
		@Column
		@NotNull
		private String workType;
		
		@Column
		@NotBlank
		@NotNull
		private String comment;
		
		@Column
		@NotNull
		private String createdBy;
		
		@Column
		private LocalDateTime createdTime;
		
		@Column
		private String userCode;
		
		@Column
		private String status;
		
		@Column
		private String queue;
		
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	/**
	 * @return the queue
	 */
	public String getQueue() {
		return queue;
	}
	/**
	 * @param queue the queue to set
	 */
	public void setQueue(String queue) {
		this.queue = queue;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the workItemReferenceNumber
	 */
	/**
	 * @return the workItemId
	 */
	public String getWorkItemId() {
		return workItemId;
	}
	/**
	 * @return the workItemRefNumber
	 */
	public String getWorkItemRefNumber() {
		return workItemRefNumber;
	}
	/**
	 * @param workItemRefNumber the workItemRefNumber to set
	 */
	public void setWorkItemRefNumber(String workItemRefNumber) {
		this.workItemRefNumber = workItemRefNumber;
	}
	/**
	 * @param workItemId the workItemId to set
	 */
	public void setWorkItemId(String workItemId) {
		this.workItemId = workItemId;
	}
	/**
	 * @return the workItemName
	 */
	public String getWorkItemName() {
		return workItemName;
	}
	/**
	 * @param workItemName the workItemName to set
	 */
	public void setWorkItemName(String workItemName) {
		this.workItemName = workItemName;
	}
	/**
	 * @return the workType
	 */
	public String getWorkType() {
		return workType;
	}
	/**
	 * @param workType the workType to set
	 */
	public void setWorkType(String workType) {
		this.workType = workType;
	}
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the createdTime
	 */
	
	/**
	 * @return the userCode
	 */
	public String getuserCode() {
		return userCode;
	}
	/**
	 * @return the createdTime
	 */
	public LocalDateTime getCreatedTime() {
		return createdTime;
	}
	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}
	/**
	 * @param userCode the userCode to set
	 */
	public void setuserCode(String userCode) {
		this.userCode = userCode;
	}
}
