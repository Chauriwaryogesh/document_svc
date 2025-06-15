package com.SecureAccessPortal.Entity;

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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "Verification_Records")
public class VerificationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private Long id;
    
    @Column
    private String verId;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerNo", referencedColumnName = "customerNo")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_number", referencedColumnName = "policyNumber")
    private Policy policy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number", referencedColumnName = "accountNo")
    private BankAccount bankAccount;

    @OneToMany(mappedBy = "verificationRecord", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Workitem> workitems = new ArrayList<>();

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

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
        updatedTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
    }
    

	public String getVerId() {
		return verId;
	}

	public void setVerId(String verId) {
		this.verId = verId;
	}

	// Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Workitem> getWorkitems() {
        return workitems;
    }

    public void setWorkitems(List<Workitem> workitems) {
        this.workitems = workitems;
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

    // Helper method to maintain bidirectional relationship
    public void addWorkitem(Workitem workitem) {
        workitems.add(workitem);
        workitem.setVerificationRecord(this);
    }
}