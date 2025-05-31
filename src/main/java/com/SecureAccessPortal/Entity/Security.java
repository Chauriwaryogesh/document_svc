package com.SecureAccessPortal.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "security")
public class Security {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "email")
    private String email;

    @Column(name = "isEmailVerified")
    private String isEmailVerified;

    @Column(name = "userName")
    private String userName;

    @Column(name = "userCode")
    private String userCode;

    @Column(name = "isUserCodeVerified")
    private String isUserCodeVerified;

    @Column(name = "updateBy")
    private String updateBy;

    @Column(name = "updateTime")
    private String updateTime;

    @Column(name = "endTime")
    private String endTime;

    @Column(name = "deletedFlag")
    private String deletedFlag;

    // WebAuthn fields
    @Column(name = "credentialId", length = 255)
    private String credentialId; // Base64-encoded credential ID

    @Column(name = "publicKey", columnDefinition = "TEXT")
    private String publicKey; // Base64-encoded COSE public key

    @Column(name = "userHandle", length = 255)
    private String userHandle; // Base64-encoded user handle

    @Column(name = "signatureCounter")
    private Long signatureCounter; // Signature counter for anti-replay

    // Getters and Setters
    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getUserHandle() {
        return userHandle;
    }

    public void setUserHandle(String userHandle) {
        this.userHandle = userHandle;
    }

    public Long getSignatureCounter() {
        return signatureCounter;
    }

    public void setSignatureCounter(Long signatureCounter) {
        this.signatureCounter = signatureCounter;
    }

    public String getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(String deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsEmailVerified() {
        return isEmailVerified;
    }

    public void setIsEmailVerified(String isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }

    public String getIsUserCodeVerified() {
        return isUserCodeVerified;
    }

    public void setIsUserCodeVerified(String isUserCodeVerified) {
        this.isUserCodeVerified = isUserCodeVerified;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    @Override
    public String toString() {
        return "Security [id=" + id + ", userName=" + userName + ", userCode=" + userCode + ", credentialId=" + credentialId + "]";
    }
}