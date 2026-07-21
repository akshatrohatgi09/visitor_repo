package com.police.evisitor.entity;

import java.time.LocalDate;
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

	@Column(name = "hotel_type_id")
	private Integer hotelTypeId;

	@Column(name = "no_of_rooms")
	private Integer noOfRooms;

	@Column(name = "no_of_floors")
	private Integer noOfFloors;

	@Column(name = "license_number")
	private String licenseNumber;

	@Column(name = "license_validity")
	private LocalDate licenseValidity;

	@Column(name = "address")
	private String address;

	@Column(name = "pincode")
	private String pincode;

	@Column(name = "latitude")
	private String latitude;

	@Column(name = "longitude")
	private String longitude;

	@Column(name = "beat_number")
	private String beatNumber;

	// Owner Details

	@Column(name = "owner_address")
	private String ownerAddress;

	@Column(name = "owner_state_cd")
	private Integer ownerStateCd;

	@Column(name = "owner_district_cd")
	private Integer ownerDistrictCd;

	@Column(name = "owner_pincode")
	private String ownerPincode;

	// Manager Details

	@Column(name = "manager_name")
	private String managerName;

	@Column(name = "manager_email")
	private String managerEmail;

	@Column(name = "manager_phone")
	private String managerPhone;

	@Column(name = "manager_address")
	private String managerAddress;

	@Column(name = "manager_state_cd")
	private Integer managerStateCd;

	@Column(name = "manager_district_cd")
	private Integer managerDistrictCd;

	@Column(name = "manager_pincode")
	private String managerPincode;

	@Column(name = "comment")
	private String comment;

	@Column(name = "record_status")
	private Character recordStatus;

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