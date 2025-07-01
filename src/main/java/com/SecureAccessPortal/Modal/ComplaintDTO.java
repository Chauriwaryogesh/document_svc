package com.SecureAccessPortal.Modal;

public class ComplaintDTO {
	private String action;
	    private String complaintId;
	    private String complaintNumber;
	    private String userCode;
	    private String customerNo;
	    private String customerName; 
	    private String policyNumber;
	    private String policyType;
	    private int totalClaimableAmount;
	    private String category; // Complaint category (e.g., Billing Issue, Claim Issue)
	    private String status; // Complaint status (e.g., Open, In Progress, Resolved)
	    private String priority; // Priority level (e.g., High, Medium, Low)
	    private String slaStatus; // SLA status (e.g., Within SLA, Nearing SLA, Breached)
	    private int slaProgress; // SLA progress percentage (0-100)
	    private String dateFiled; // Date the complaint was filed (ISO format: YYYY-MM-DD)
	    private String lastUpdated; // Date of last update (ISO format: YYYY-MM-DD)
	    private String description; // Detailed description of the complaint
	    private String resolutionNotes; // Notes on resolution, if any
	    private String attachmentPath; // File path for any attachments
	    private String createdBy; // User code of the creator
	    private String updatedBy; // User code of the last updater
	    private String createdAt; // Record creation timestamp (ISO format)
	    private String updatedAt; // Record update timestamp (ISO format)
	    private String assignedTo; // User or team assigned to handle the complaint
	    private String departmentId; // Department responsible for the complaint
	    private String escalationLevel; // Escalation level (e.g., Level 1, Level 2)
	    private String relatedComplaintId; // Reference to a related complaint, if any
	    private String sourceChannel; // Source of the complaint (e.g., Email, Phone, Web)
	    private String severity; // Severity level (e.g., Critical, Major, Minor)
	    private String customerFeedback; // Feedback provided by the customer post-resolution
	    private String isReopened; // Flag indicating if the complaint was reopened
	    private String tags; // Comma-separated tags for categorization or analytics
	    private String slaDueDate; 
	    private String reason; 
	    private String workitemNumber;
	    
		public String getAction() {
			return action;
		}
		public void setAction(String action) {
			this.action = action;
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
		public String getCustomerNo() {
			return customerNo;
		}
		public void setCustomerNo(String customerNo) {
			this.customerNo = customerNo;
		}
		public String getCustomerName() {
			return customerName;
		}
		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}
		public String getPolicyNumber() {
			return policyNumber;
		}
		public void setPolicyNumber(String policyNumber) {
			this.policyNumber = policyNumber;
		}
		public String getPolicyType() {
			return policyType;
		}
		public void setPolicyType(String policyType) {
			this.policyType = policyType;
		}
		public int getTotalClaimableAmount() {
			return totalClaimableAmount;
		}
		public void setTotalClaimableAmount(int totalClaimableAmount) {
			this.totalClaimableAmount = totalClaimableAmount;
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
		public String getDateFiled() {
			return dateFiled;
		}
		public void setDateFiled(String dateFiled) {
			this.dateFiled = dateFiled;
		}
		public String getLastUpdated() {
			return lastUpdated;
		}
		public void setLastUpdated(String lastUpdated) {
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
		public String getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(String createdAt) {
			this.createdAt = createdAt;
		}
		public String getUpdatedAt() {
			return updatedAt;
		}
		public void setUpdatedAt(String updatedAt) {
			this.updatedAt = updatedAt;
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
		
		public String getIsReopened() {
			return isReopened;
		}
		public void setIsReopened(String isReopened) {
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
		public String getWorkitemNumber() {
			return workitemNumber;
		}
		public void setWorkitemNumber(String workitemNumber) {
			this.workitemNumber = workitemNumber;
		}    
	}

