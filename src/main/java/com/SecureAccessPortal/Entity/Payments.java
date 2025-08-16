package com.SecureAccessPortal.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "PAYMENTS")
public class Payments {
	@Id
	@Column(name = "payment_id")
	private String paymentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "policyNumber", referencedColumnName = "policyNumber")
	private Policy policy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customerNo", referencedColumnName = "customerNo")
	private Customer customer;

	@OneToMany(mappedBy = "payment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Workitem> workItem = new ArrayList<>();
	
	@OneToMany(mappedBy = "payment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<SurrenderEntity> surrenderEntity = new ArrayList<>();
	
	@OneToMany(mappedBy = "payment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ClaimEntity> claimEntity = new ArrayList<>();

	@Column(name = "product_code")
	private String productCode;

	@Column(name = "policy_name")
	private String policyName;

	@Column(name = "installment_amount")
	private BigDecimal installmentAmount;

	@Column(name = "installment_count")
	private int installmentCount;

	@Column(name = "due_date")
	private LocalDate dueDate;

	@Column(name = "payment_date")
	private LocalDate paymentDate;

	@Column(name = "status")
	private String status;

	@Column(name = "transaction_id")
	private String transactionId;

	@Column(name = "payment_method")
	private String paymentMethod;

	@Column(name = "email_status")
	private String emailStatus;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "created_time")
	private LocalDateTime createdTime;

	@Column(name = "updated_time")
	private LocalDateTime updatedTime;

	@Column(name = "total_installments")
	private int totalInstallments;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accountNo", referencedColumnName = "accountNo")
	private BankAccount bankAccount;
	
	@Column(name = "total_AmountPaid")
	private BigDecimal totalAmountPaid;

	public List<SurrenderEntity> getSurrenderEntity() {
		return surrenderEntity;
	}

	public void setSurrenderEntity(List<SurrenderEntity> surrenderEntity) {
		this.surrenderEntity = surrenderEntity;
	}

	public List<ClaimEntity> getClaimEntity() {
		return claimEntity;
	}

	public void setClaimEntity(List<ClaimEntity> claimEntity) {
		this.claimEntity = claimEntity;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public BigDecimal getTotalAmountPaid() {
		return totalAmountPaid;
	}

	public void setTotalAmountPaid(BigDecimal totalAmountPaid) {
		this.totalAmountPaid = totalAmountPaid;
	}

	public int getTotalInstallments() {
		return totalInstallments;
	}

	public void setTotalInstallments(int totalInstallments) {
		this.totalInstallments = totalInstallments;
	}

	public List<Workitem> getWorkItem() {
		return workItem;
	}

	public int getInstallmentCount() {
		return installmentCount;
	}

	public void setInstallmentCount(int installmentCount) {
		this.installmentCount = installmentCount;
	}

	public void setWorkItem(List<Workitem> workItem) {
		this.workItem = workItem;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
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

	public BigDecimal getInstallmentAmount() {
		return installmentAmount;
	}

	public void setInstallmentAmount(BigDecimal installmentAmount) {
		this.installmentAmount = installmentAmount;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public LocalDateTime getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(LocalDateTime updatedTime) {
		this.updatedTime = updatedTime;
	}

}
