package com.police.evisitor.dto.request;

import lombok.Data;

@Data
public class UserRequestDTO {

	private Long userId;

	private String userName;

	private String userLogin;

	private String userPassword;

	private String userMob;

	private String userEmail;

	private Integer nationalityCd;

	private Integer stateCd;

	private Integer districtCd;

	private Integer psCd;

	private Long hotelId;

	private Long roleId;

	private String userAddress;

	private String comment;
	
	private String operation;
	
	private String loginId;
	
	private Long role;
}
