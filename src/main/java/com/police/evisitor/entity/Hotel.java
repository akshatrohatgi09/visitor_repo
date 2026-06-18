package com.police.evisitor.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "t_hotels")
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

	@Column(name = "district_cd")
	private Integer districtCd;

	@Column(name = "ps_cd")
	private Integer psCd;

	@Column(name = "address")
	private String address;

	@Column(name = "hotel_type_id")
	private Integer hotelTypeId;

	@Column(name = "hotel_ps_name")
	private String hotelPsName;

	@Column(name = "hotel_district_name")
	private String hotelDistrictName;

	@Column(name = "comment")
	private String comment;

	@Column(name = "record_status")
	private String recordStatus;

	@Column(name = "created_by")
	private String createdBy;

	@CreationTimestamp
	@Column(name = "created_on")
	private LocalDateTime createdOn;

	@Column(name = "updated_by")
	private String updatedBy;

	@UpdateTimestamp
	@Column(name = "updated_on")
	private LocalDateTime updatedOn;
}