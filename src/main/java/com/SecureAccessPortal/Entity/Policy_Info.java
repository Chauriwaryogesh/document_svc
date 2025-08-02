package com.SecureAccessPortal.Entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "POLICY_INFO")
public class Policy_Info {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "policy_id")
	private Long policy_id;
	@Column
	private String policy_name;
	@Column
	private String product_code;
	@Column
	private String policy_company_name;
	@Column
	private String supported_terms;
	@Column
	private String frequency;
	@Column
	private BigDecimal min_total_amount;
	@Column
	private BigDecimal max_total_amount;
	@Column
	private BigDecimal coverage_amount;
	@Column
	private int default_duration_months;
	@Column
	private LocalDate policy_start_date;
	@Column
	private LocalDate policy_end_date;
	@Column
	private LocalDate policy_renewal_date;
	@Column
	private LocalDate policy_expiry_date;
	@Column
	private String policy_type;
	@Column
	private String status;
	@Column
	private Timestamp created_at;
	@Column
	private Timestamp updated_at;

	@PrePersist
	protected void onCreate() {
		created_at = Timestamp.valueOf(LocalDateTime.now());

	}

	@PreUpdate
	protected void onUpdate() {
		updated_at = Timestamp.valueOf(LocalDateTime.now());
	}

	public Long getPolicy_id() {
		return policy_id;
	}

	public void setPolicy_id(Long policy_id) {
		this.policy_id = policy_id;
	}

	public String getPolicy_name() {
		return policy_name;
	}

	public void setPolicy_name(String policy_name) {
		this.policy_name = policy_name;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public String getPolicy_company_name() {
		return policy_company_name;
	}

	public void setPolicy_company_name(String policy_company_name) {
		this.policy_company_name = policy_company_name;
	}

	public String getSupported_terms() {
		return supported_terms;
	}

	public void setSupported_terms(String supported_terms) {
		this.supported_terms = supported_terms;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public BigDecimal getMin_total_amount() {
		return min_total_amount;
	}

	public void setMin_total_amount(BigDecimal min_total_amount) {
		this.min_total_amount = min_total_amount;
	}

	public BigDecimal getMax_total_amount() {
		return max_total_amount;
	}

	public void setMax_total_amount(BigDecimal max_total_amount) {
		this.max_total_amount = max_total_amount;
	}

	public BigDecimal getCoverage_amount() {
		return coverage_amount;
	}

	public void setCoverage_amount(BigDecimal coverage_amount) {
		this.coverage_amount = coverage_amount;
	}

	public int getDefault_duration_months() {
		return default_duration_months;
	}

	public void setDefault_duration_months(int default_duration_months) {
		this.default_duration_months = default_duration_months;
	}

	public LocalDate getPolicy_start_date() {
		return policy_start_date;
	}

	public void setPolicy_start_date(LocalDate policy_start_date) {
		this.policy_start_date = policy_start_date;
	}

	public LocalDate getPolicy_end_date() {
		return policy_end_date;
	}

	public void setPolicy_end_date(LocalDate policy_end_date) {
		this.policy_end_date = policy_end_date;
	}

	public LocalDate getPolicy_renewal_date() {
		return policy_renewal_date;
	}

	public void setPolicy_renewal_date(LocalDate policy_renewal_date) {
		this.policy_renewal_date = policy_renewal_date;
	}

	public LocalDate getPolicy_expiry_date() {
		return policy_expiry_date;
	}

	public void setPolicy_expiry_date(LocalDate policy_expiry_date) {
		this.policy_expiry_date = policy_expiry_date;
	}

	public String getPolicy_type() {
		return policy_type;
	}

	public void setPolicy_type(String policy_type) {
		this.policy_type = policy_type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}

	public Timestamp getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Timestamp updated_at) {
		this.updated_at = updated_at;
	}

}
