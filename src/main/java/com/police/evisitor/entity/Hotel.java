package com.police.evisitor.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_hotels")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "hotel_id")
	private Long hotelId;

	@Column(name = "hotel_name")
	private String hotelName;

	@Column(name = "owner_name")
	private String ownerName;

	@Column(name = "mobile_no")
	private String mobileNo;

	@Column(name = "email")
	private String email;

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

	@Column(name = "ps_cd")
	private Integer psCd;

	@Column(name = "address")
	private String address;

	@Column(name = "comment")
	private String comment;

	@Column(name = "record_status")
	private String recordStatus;

	@Column(name = "created_by")
	private String createdBy;

	@CreationTimestamp
	@Column(name = "created_on", updatable = false)
	private LocalDateTime createdOn;

	@Column(name = "updated_by")
	private String updatedBy;

	@UpdateTimestamp
	@Column(name = "updated_on")
	private LocalDateTime updatedOn;
}