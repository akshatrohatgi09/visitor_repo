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

	private Long hotelId;

	private String hotelName;
	private String ownerName;
	private String mobileNo;
	private String email;

	private Integer stateCd;
	private Integer zoneCd;
	private Integer rangeCd;
	private Integer districtCd;
	private Integer sdpoCd;
	private Integer psCd;

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

	private String ownerAddress;
	private Integer ownerStateCd;
	private Integer ownerDistrictCd;
	private String ownerPincode;

	private String managerName;
	private String managerEmail;
	private String managerPhone;
	private String managerAddress;
	private Integer managerStateCd;
	private Integer managerDistrictCd;
	private String managerPincode;

	private String comment;

	private String loginUser;

	private String operation;
}