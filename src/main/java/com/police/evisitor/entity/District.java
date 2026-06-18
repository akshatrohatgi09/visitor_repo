package com.police.evisitor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "m_district")
@Data
public class District {

	@Id
	private Long id;

	@Column(name = "district_cd")
	private Integer districtCd;

	@Column(name = "state_cd")
	private Integer stateCd;

	@Column(name = "district")
	private String district;

	@Column(name = "record_status")
	private Character recordStatus;
}