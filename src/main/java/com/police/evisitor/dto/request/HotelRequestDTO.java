package com.police.evisitor.dto.request;

import lombok.Data;

@Data
public class HotelRequestDTO {

	private Long hotelId;

	private String hotelName;
	private String ownerName;
	private String mobileNo;
	private String email;

	private Integer stateCd;
	private Integer districtCd;
	private Integer psCd;

	private String address;

	private Integer hotelTypeId;

	private String hotelPsName;
	private String hotelDistrictName;

	private String comment;

	private String operation;
	private String loginUser;
}
