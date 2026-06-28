package com.police.evisitor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "m_state")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class State {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "state_id")
	private Long stateId;

	@Column(name = "state_cd")
	private Integer stateCd;

	@Column(name = "state_name")
	private String stateName;

	@Column(name = "state_short_name")
	private String stateShortName;

	@Column(name = "state_description")
	private String stateDescription;

	@Column(name = "record_status")
	private String recordStatus;
}