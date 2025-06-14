package com.SecureAccessPortal.Entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Verification_Records")
public class VerificationRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column
	private long id;

	@ManyToOne
	@JoinColumn(name = "accountNo", nullable = false, referencedColumnName = "account_number")
	private BankAccount bankAccount;

	@Column
	private String customerNo;

	@Column
	private String policyNumber;

	@Column
	private String sanctions;

	@Column(columnDefinition = "LONGBLOB")
	@Lob
	private byte[] sanctionsDocs;
	@Column
	private String sancStatus;

	@Column
	private String identity;

	@Column(columnDefinition = "LONGBLOB")
	@Lob
	private byte[] identityDocs;
	@Column
	private String identityStatus;

	@Column
	private String death;

	@Column(columnDefinition = "LONGBLOB")
	@Lob
	private byte[] deathDocs;
	@Column
	private String deathStatus;

	@Column
	private String userCode;
	@Column
	private LocalDateTime createdTime;

	@Column
	private String createdBy;
	@Column
	private LocalDateTime updatedTime;
	@Column
	private String updatedBy;

	@Column(name = "work_item_ref_no")
	private String workItemRefNo;

	public String getWorkItemRefNo() {
		return workItemRefNo;
	}

	public void setWorkItemRefNo(String workItemRefNo) {
		this.workItemRefNo = workItemRefNo;
	}

	@PrePersist
	protected void onCreate() {
		createdTime = LocalDateTime.now();
		updatedTime = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedTime = LocalDateTime.now();
	}

	public String getSanctions() {
		return sanctions;
	}

	public void setSanctions(String sanctions) {
		this.sanctions = sanctions;
	}

	public byte[] getSanctionsDocs() {
		return sanctionsDocs;
	}

	public void setSanctionsDocs(byte[] sanctionsDocs) {
		this.sanctionsDocs = sanctionsDocs;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(LocalDateTime updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getSancStatus() {
		return sancStatus;
	}

	public void setSancStatus(String sancStatus) {
		this.sancStatus = sancStatus;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public byte[] getIdentityDocs() {
		return identityDocs;
	}

	public void setIdentityDocs(byte[] identityDocs) {
		this.identityDocs = identityDocs;
	}

	public String getIdentityStatus() {
		return identityStatus;
	}

	public void setIdentityStatus(String identityStatus) {
		this.identityStatus = identityStatus;
	}

	public String getDeath() {
		return death;
	}

	public void setDeath(String death) {
		this.death = death;
	}

	public byte[] getDeathDocs() {
		return deathDocs;
	}

	public void setDeathDocs(byte[] deathDocs) {
		this.deathDocs = deathDocs;
	}

	public String getDeathStatus() {
		return deathStatus;
	}

	public void setDeathStatus(String deathStatus) {
		this.deathStatus = deathStatus;
	}
}
