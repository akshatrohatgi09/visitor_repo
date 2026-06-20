package com.police.evisitor.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "m_visit_reason")
public class VisitReason {

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "visit_reason")
	private String visitReason;

	@Column(name = "record_status")
	private String recordStatus;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_on")
	private LocalDateTime createdOn;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_on")
	private LocalDateTime updatedOn;
}