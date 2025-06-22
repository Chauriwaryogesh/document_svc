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
@Table(name = "WorkItem")
public class Workitem {

    @Id
    @Column
    @NotBlank
    private String workItemRefNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerNo", referencedColumnName = "customerNo")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_number", referencedColumnName = "policyNumber")
    private Policy policy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number", referencedColumnName = "accountNo")
    private BankAccount bankAccount;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentId", referencedColumnName = "payment_id")
    private Payments payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verificationRecordVerId", referencedColumnName = "verId")
    private VerificationRecord verificationRecord;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "otpEmail", referencedColumnName = "email")
    private OtpStore otpStore;

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
    
    public Payments getPayment() {
		return payment;
	}

	public void setPayment(Payments payment) {
		this.payment = payment;
	}

	public OtpStore getOtpStore() {
		return otpStore;
	}

	public void setOtpStore(OtpStore otpStore) {
		this.otpStore = otpStore;
	}

	// Getters and Setters
    public String getWorkItemRefNumber() {
        return workItemRefNumber;
    }

    public void setWorkItemRefNumber(String workItemRefNumber) {
        this.workItemRefNumber = workItemRefNumber;
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

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public VerificationRecord getVerificationRecord() {
        return verificationRecord;
    }

    public void setVerificationRecord(VerificationRecord verificationRecord) {
        this.verificationRecord = verificationRecord;
    }

    public String getWorkItemId() {
        return workItemId;
    }

    public void setWorkItemId(String workItemId) {
        this.workItemId = workItemId;
    }

    public String getWorkItemName() {
        return workItemName;
    }

    public void setWorkItemName(String workItemName) {
        this.workItemName = workItemName;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }
}