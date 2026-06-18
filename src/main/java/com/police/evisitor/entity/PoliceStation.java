package com.police.evisitor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "m_police_station")
@Data
public class PoliceStation {

	@Id
	private Long id;

	@Column(name = "ps_cd")
	private Integer psCd;

	@Column(name = "state_cd")
	private Integer stateCd;

	@Column(name = "district_cd")
	private Integer districtCd;

	@Column(name = "ps")
	private String ps;

	@Column(name = "record_status")
	private Character recordStatus;
}
