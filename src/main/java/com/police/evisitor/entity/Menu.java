package com.police.evisitor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Table(name = "m_menu")
public class Menu {
	@Id
	@Column(name = "menu_id")
	private Integer menuId;

	@Column(name = "menu_name")
	private String menuName;

	@Column(name = "menu_description")
	private String menuDescription;

	@Column(name = "record_status")
	@JsonIgnore
	private String recordStatus;
}