package com.SecureAccessPortal.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment_sequence")
public class PaymentSequence {

	@Id
	@Column(name = "year", length = 6)
	private String yearMonth;

	@Column(name = "sequence_number", nullable = false)
	private Long sequenceNumber;

	// Constructors
	public PaymentSequence() {
	}

	public PaymentSequence(String yearMonth, Long sequenceNumber) {
		this.yearMonth = yearMonth;
		this.sequenceNumber = sequenceNumber;
	}

	// Getters and Setters
	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public Long getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
}