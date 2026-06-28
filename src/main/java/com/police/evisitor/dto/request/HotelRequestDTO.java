package com.police.evisitor.dto.request;

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

	private String address;

	private String comment;

	private String loginUser;
	
	private String operation;
}