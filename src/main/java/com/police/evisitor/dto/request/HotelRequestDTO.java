package com.police.evisitor.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelRequestDTO {

	// Required only for Update API
	private Long hotelId;

	// Hotel Details
	private String hotelName;
	private String ownerName;
	private String mobileNo;
	private String email;

	// Police Hierarchy
	private Integer stateCd;
	private Integer zoneCd;
	private Integer rangeCd;
	private Integer districtCd;
	private Integer sdpoCd;
	private Integer psCd;

	// Hotel Information
	private Long hotelTypeId;
	private Integer noOfRooms;
	private Integer noOfFloors;

	private String licenseNumber;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate licenseValidity;

	private String address;
	private String pincode;

	private String latitude;
	private String longitude;

	private String beatNumber;

	// Owner Details
	private String ownerAddress;
	private Integer ownerStateCd;
	private Integer ownerDistrictCd;
	private String ownerPincode;

	// Manager Details
	private String managerName;
	private String managerEmail;
	private String managerPhone;
	private String managerAddress;
	private Integer managerStateCd;
	private Integer managerDistrictCd;
	private String managerPincode;

	// Other Details
	private String comment;

	// Logged-in User
	private String loginUser;
	
	private String operation;
}