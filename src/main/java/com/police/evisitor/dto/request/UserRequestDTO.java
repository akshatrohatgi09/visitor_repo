package com.police.evisitor.dto.request;

import lombok.Data;

@Data
public class UserRequestDTO {

	private Long userId;

	private String firstName;
	
	private String lastName;

	private String userLogin;

	private String userPassword;

	private String userMob;

	private String userEmail;

	private Integer nationalityCd;

	private Integer stateCd;

	private Integer zoneCd;

	private Integer rangeCd;

	private Integer districtCd;

	private Integer sdpoCd;

	private Integer psCd;

	private Long hotelId;

	private Long roleId;

	private String userAddress;
	
	private String pincode;

	private String comment;

	private String operation;

	private String loginId;

	private Long role;
	
	private Integer userStateCd;

	private Integer userDistrictCd;
}
