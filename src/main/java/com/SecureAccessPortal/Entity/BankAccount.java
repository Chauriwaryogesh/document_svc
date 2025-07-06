package com.SecureAccessPortal.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "BankAccount")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "accountNo", unique = true)
    private String accountNo;

    @Column(name = "ifscCode")
    private String ifscCode;

    @Column(name = "bankName")
    private String bankName;

    @Column(name = "accountType")
    private String accountType;

    @Column(name = "status")
    private String status;
    
    @Column(name = "deletedFlag")
    private String deletedFlag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerNo", referencedColumnName = "customerNo")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_number", referencedColumnName = "policyNumber")
    private Policy policy;

    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<VerificationRecord> verificationRecords = new ArrayList<>();

    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Workitem> workitems = new ArrayList<>();

    @Column(name = "last_verification_date")
    private LocalDateTime lastVerificationDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
    
    @Column(name = "updated_by")
    private String updatedBy;
    
    @Column(name = "updated_Time")
    private LocalDateTime updatedTime;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "account_holder_type")
    private String accountHolderType;

    @Column(name = "branch_code")
    private String branchCode;

    @Column(name = "swift_code")
    private String swiftCode;

    @Column(name = "payment_method_status")
    private String paymentMethodStatus;

    @Column(name = "last_payment_date")
    private LocalDateTime lastPaymentDate;

    @Column(name = "aml_status")
    private String amlStatus;

    @Column(name = "account_balance")
    private BigDecimal accountBalance;

    @Column(name = "linked_payment_method")
    private String linkedPaymentMethod;

    @Column(name = "verification_attempts")
    private Integer verificationAttempts;
    
    @Column(name = "comment")
    private String comment;
    
    @Column(name = "account_holder_name")
    private String accountHolderName;
    

    public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public LocalDateTime getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(LocalDateTime updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	// Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<VerificationRecord> getVerificationRecords() {
        return verificationRecords;
    }

    public void setVerificationRecords(List<VerificationRecord> verificationRecords) {
        this.verificationRecords = verificationRecords;
    }

    public List<Workitem> getWorkitems() {
        return workitems;
    }

    public void setWorkitems(List<Workitem> workitems) {
        this.workitems = workitems;
    }

    public LocalDateTime getLastVerificationDate() {
        return lastVerificationDate;
    }

    public void setLastVerificationDate(LocalDateTime lastVerificationDate) {
        this.lastVerificationDate = lastVerificationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAccountHolderType() {
        return accountHolderType;
    }

    public void setAccountHolderType(String accountHolderType) {
        this.accountHolderType = accountHolderType;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getPaymentMethodStatus() {
        return paymentMethodStatus;
    }

    public void setPaymentMethodStatus(String paymentMethodStatus) {
        this.paymentMethodStatus = paymentMethodStatus;
    }

    public LocalDateTime getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(LocalDateTime lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public String getAmlStatus() {
        return amlStatus;
    }

    public void setAmlStatus(String amlStatus) {
        this.amlStatus = amlStatus;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getLinkedPaymentMethod() {
        return linkedPaymentMethod;
    }

    public void setLinkedPaymentMethod(String linkedPaymentMethod) {
        this.linkedPaymentMethod = linkedPaymentMethod;
    }

    public Integer getVerificationAttempts() {
        return verificationAttempts;
    }

    public void setVerificationAttempts(Integer verificationAttempts) {
        this.verificationAttempts = verificationAttempts;
    }

    // Helper methods to maintain bidirectional relationships
    public void addVerificationRecord(VerificationRecord verificationRecord) {
        verificationRecords.add(verificationRecord);
        verificationRecord.setBankAccount(this);
    }

    public void addWorkitem(Workitem workitem) {
        workitems.add(workitem);
        workitem.setBankAccount(this);
    }
}