package com.police.evisitor.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="m_role_menu")
public class RoleMenuList {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_menu_id")
    private Integer roleMenuId;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonBackReference
    private Role roleId;

    @ManyToOne
    @JoinColumn(name = "menu_id")  
    @JsonBackReference
    private Menu menuId;
	
	@Column(name="record_status")
	@JsonIgnore
	private String recordStatus;
	
	@CreatedBy
	@JsonIgnore
	@Column(name="created_by", updatable=false)
	private String createdBy;
	
	@CreationTimestamp
	@JsonIgnore
	@Column(name="created_on", updatable=false)
	private LocalDateTime createdOn;
	
	@LastModifiedBy
	@JsonIgnore
	@Column(name="updated_by")
	private String updatedBy;
	
	@UpdateTimestamp
	@JsonIgnore
	@Column(name="updated_on")
	private LocalDateTime updatedOn;
}