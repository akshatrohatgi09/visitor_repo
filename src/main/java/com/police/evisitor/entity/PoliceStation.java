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
@Table(name = "m_police_station")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoliceStation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "ps_cd")
	private Integer psCd;

	@Column(name = "state_cd")
	private Integer stateCd;

	@Column(name = "zone_cd")
	private Integer zoneCd;

	@Column(name = "range_cd")
	private Integer rangeCd;

	@Column(name = "district_cd")
	private Integer districtCd;

	@Column(name = "sdpo_cd")
	private Integer sdpoCd;

	@Column(name = "ps")
	private String ps;

	@Column(name = "record_status")
	private String recordStatus;
}
