package com.SecureAccessPortal.Modal;

public class SecurityDTO {
	private String id;
	private String email;
	private String userName;
	private String userCode;
	private String isEmailVerified;
	private String isUserCodeVerified;
	private String createdTime;
	private String remainingTime;
	
	// WebAuthn fields
    private String credentialId;
    private String publicKey;
    private String userHandle;
    private Long signatureCounter;
    private String authenticatorData;
    
	/**
	 * @return the authenticatorData
	 */
	public String getAuthenticatorData() {
		return authenticatorData;
	}
	/**
	 * @param authenticatorData the authenticatorData to set
	 */
	public void setAuthenticatorData(String authenticatorData) {
		this.authenticatorData = authenticatorData;
	}
	/**
	 * @return the credentialId
	 */
	public String getCredentialId() {
		return credentialId;
	}
	/**
	 * @param credentialId the credentialId to set
	 */
	public void setCredentialId(String credentialId) {
		this.credentialId = credentialId;
	}
	/**
	 * @return the publicKey
	 */
	public String getPublicKey() {
		return publicKey;
	}
	/**
	 * @param publicKey the publicKey to set
	 */
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	/**
	 * @return the userHandle
	 */
	public String getUserHandle() {
		return userHandle;
	}
	/**
	 * @param userHandle the userHandle to set
	 */
	public void setUserHandle(String userHandle) {
		this.userHandle = userHandle;
	}
	/**
	 * @return the signatureCounter
	 */
	public Long getSignatureCounter() {
		return signatureCounter;
	}
	/**
	 * @param signatureCounter the signatureCounter to set
	 */
	public void setSignatureCounter(Long signatureCounter) {
		this.signatureCounter = signatureCounter;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getRemainingTime() {
		return remainingTime;
	}
	public void setRemainingTime(String remainingTime) {
		this.remainingTime = remainingTime;
	}
	public String getIsUserCodeVerified() {
		return isUserCodeVerified;
	}
	public void setIsUserCodeVerified(String isUserCodeVerified) {
		this.isUserCodeVerified = isUserCodeVerified;
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
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	

}
