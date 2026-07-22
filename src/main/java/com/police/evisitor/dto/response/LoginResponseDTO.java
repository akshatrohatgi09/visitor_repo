package com.police.evisitor.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginResponseDTO {

	private Long userId;

	private String firstName;
	private String lastName;
	private String fullName;

	private String userLogin;
	private String mobile;
	private String email;

	private Long roleId;
	private String roleName;

	private Integer nationalityCd;

	private Integer stateCd;
	private String stateName;

	private Integer zoneCd;
	private String zoneName;

	private Integer rangeCd;
	private String rangeName;

	private Integer districtCd;
	private String districtName;

	private Integer sdpoCd;
	private String sdpoName;

	private Integer psCd;
	private String psName;

	private Long hotelCd;
	private String hotelName;

	private String address;
	private Integer userStateCd;
	private Integer userDistrictCd;
	private String pincode;

	private Boolean loginStatus;
	private LocalDateTime lastLoggedIn;

	private String token;
}