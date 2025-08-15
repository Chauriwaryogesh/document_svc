package com.SecureAccessPortal.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "SURRENDER_WORKFLOW_STEP")
public class SurrenderWorkflowStep {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "surrRefNo", referencedColumnName = "surrRefNo")
	private SurrenderEntity surrenderEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "claimRefNo", referencedColumnName = "claimRefNo")
	private ClaimEntity claimEntity;

	@Column
	private String stepName;
	@Column
	private String status;
	@Column
	private LocalDateTime stepDate;
	@Column
	private String assignedToTeam;
	@Column
	@Lob
	private String comment;

	public ClaimEntity getClaimEntity() {
		return claimEntity;
	}

	public void setClaimEntity(ClaimEntity claimEntity) {
		this.claimEntity = claimEntity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SurrenderEntity getSurrenderEntity() {
		return surrenderEntity;
	}

	public void setSurrenderEntity(SurrenderEntity surrenderEntity) {
		this.surrenderEntity = surrenderEntity;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getStepDate() {
		return stepDate;
	}

	public void setStepDate(LocalDateTime stepDate) {
		this.stepDate = stepDate;
	}

	public String getAssignedToTeam() {
		return assignedToTeam;
	}

	public void setAssignedToTeam(String assignedToTeam) {
		this.assignedToTeam = assignedToTeam;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
