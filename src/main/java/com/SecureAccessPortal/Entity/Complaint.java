package com.SecureAccessPortal.Entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "COMPLAINTS")
public class Complaint {
	
	@Id
	@Column
	private String complaintId;
	@Column
	private String complaintNumber;
	@Column
	private String userCode;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition = "customerNo", referencedColumnName = "customerNo")
	private Customer customer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition = "policyNumber", referencedColumnName = "policyNumber")
	private Policy policy;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workItemRefNumber", referencedColumnName = "workItemRefNumber")
	private Workitem workitem;
	
	@Column
	private String category; 
	@Column
	private String status;
	@Column
	private String priority; // Priority level (e.g., High, Medium, Low)
	@Column
	private String slaStatus; // SLA status (e.g., Within SLA, Nearing SLA, Breached)
	@Column
	private int slaProgress; // SLA progress percentage (0-100)
	@Column
	private LocalDate dateFiled; // Date the complaint was filed (ISO format: YYYY-MM-DD)
	@Column
	private LocalDate lastUpdated; // Date of last update (ISO format: YYYY-MM-DD)
	@Column
	private String description; // Detailed description of the complaint
	@Column
	private String resolutionNotes; // Notes on resolution, if any
	@Column
	private String attachmentPath; // File path for any attachments
	@Column
	private String createdBy; // User code of the creator
	@Column
	private String updatedBy; // User code of the last updater
	@Column
	private LocalDate createdTime; // Record creation timestamp (ISO format)
	@Column
	private LocalDate updatedTime; // Record update timestamp (ISO format)
	@Column
	private String assignedTo; // User or team assigned to handle the complaint
	@Column
	private String departmentId; // Department responsible for the complaint
	@Column
	private String escalationLevel; // Escalation level (e.g., Level 1, Level 2)
	@Column
	private String relatedComplaintId; // Reference to a related complaint, if any
	@Column
	private String sourceChannel; // Source of the complaint (e.g., Email, Phone, Web)
	@Column
	private String severity; // Severity level (e.g., Critical, Major, Minor)
	@Column
	private String customerFeedback; // Feedback provided by the customer post-resolution
	@Column
	private boolean isReopened; // Flag indicating if the complaint was reopened
	@Column
	private String tags; // Comma-separated tags for categorization or analytics
	@Column
	private String slaDueDate;
	@Column
	private String reason;
	@Column
	private String deletedFlag;
	
	public String getDeletedFlag() {
		return deletedFlag;
	}
	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}
	public String getComplaintId() {
		return complaintId;
	}
	public void setComplaintId(String complaintId) {
		this.complaintId = complaintId;
	}
	public String getComplaintNumber() {
		return complaintNumber;
	}
	public void setComplaintNumber(String complaintNumber) {
		this.complaintNumber = complaintNumber;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Policy getPolicy() {
		return policy;
	}
	public void setPolicy(Policy policy) {
		this.policy = policy;
	}
	public Workitem getWorkitem() {
		return workitem;
	}
	public void setWorkitem(Workitem workitem) {
		this.workitem = workitem;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getSlaStatus() {
		return slaStatus;
	}
	public void setSlaStatus(String slaStatus) {
		this.slaStatus = slaStatus;
	}
	public int getSlaProgress() {
		return slaProgress;
	}
	public void setSlaProgress(int slaProgress) {
		this.slaProgress = slaProgress;
	}
	public LocalDate getDateFiled() {
		return dateFiled;
	}
	public void setDateFiled(LocalDate dateFiled) {
		this.dateFiled = dateFiled;
	}
	public LocalDate getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(LocalDate lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getResolutionNotes() {
		return resolutionNotes;
	}
	public void setResolutionNotes(String resolutionNotes) {
		this.resolutionNotes = resolutionNotes;
	}
	public String getAttachmentPath() {
		return attachmentPath;
	}
	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
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
	public LocalDate getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(LocalDate createdTime) {
		this.createdTime = createdTime;
	}
	public LocalDate getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(LocalDate updatedTime) {
		this.updatedTime = updatedTime;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public String getEscalationLevel() {
		return escalationLevel;
	}
	public void setEscalationLevel(String escalationLevel) {
		this.escalationLevel = escalationLevel;
	}
	public String getRelatedComplaintId() {
		return relatedComplaintId;
	}
	public void setRelatedComplaintId(String relatedComplaintId) {
		this.relatedComplaintId = relatedComplaintId;
	}
	public String getSourceChannel() {
		return sourceChannel;
	}
	public void setSourceChannel(String sourceChannel) {
		this.sourceChannel = sourceChannel;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getCustomerFeedback() {
		return customerFeedback;
	}
	public void setCustomerFeedback(String customerFeedback) {
		this.customerFeedback = customerFeedback;
	}
	public boolean isReopened() {
		return isReopened;
	}
	public void setReopened(boolean isReopened) {
		this.isReopened = isReopened;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getSlaDueDate() {
		return slaDueDate;
	}
	public void setSlaDueDate(String slaDueDate) {
		this.slaDueDate = slaDueDate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
}
