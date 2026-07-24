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
@Table(name = "m_spdo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sdpo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "spdo_id")
	private Long sdpoId;

	@Column(name = "spdo_cd")
	private Integer sdpoCd;

	@Column(name = "state_cd")
	private Integer stateCd;

	@Column(name = "zone_cd")
	private Integer zoneCd;

	@Column(name = "range_cd")
	private Integer rangeCd;

	@Column(name = "district_cd")
	private Integer districtCd;

	@Column(name = "spdo_name")
	private String sdpoName;

	@Column(name = "spdo_description")
	private String sdpoDescription;

	@Column(name = "record_status")
	private String recordStatus;
}
