package com.police.evisitor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "m_range")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Range {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "range_id")
	private Long rangeId;

	@Column(name = "range_cd")
	private Integer rangeCd;

	@Column(name = "state_cd")
	private Integer stateCd;

	@Column(name = "zone_cd")
	private Integer zoneCd;

	@Column(name = "range_name")
	private String rangeName;

	@Column(name = "range_description")
	private String rangeDescription;

	@Column(name = "record_status")
	private String recordStatus;
}
