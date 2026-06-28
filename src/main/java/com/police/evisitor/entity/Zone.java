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
@Table(name = "m_zone")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Zone {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "zone_id")
	private Long zoneId;

	@Column(name = "zone_cd")
	private Integer zoneCd;

	@Column(name = "state_cd")
	private Integer stateCd;

	@Column(name = "zone_name")
	private String zoneName;

	@Column(name = "zone_description")
	private String zoneDescription;

	@Column(name = "record_status")
	private String recordStatus;
}
