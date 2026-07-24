package com.police.evisitor.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class VisitorListRequestDTO {

	private Integer pageNo = 0;
	private Integer pageSize = 10;

	// Logged-in User
	private String loginId;

	// Search Filters
	private String visitorName;
	private String visitorMobile;

	private LocalDate fromDate;
	private LocalDate toDate;

	// Optional Location Filters
	private Integer stateCd;
	private Integer zoneCd;
	private Integer rangeCd;
	private Integer districtCd;
	private Integer sdpoCd;
	private Integer psCd;
	private Long hotelCd;

	// A = Checked In
	// C = Checked Out
	private String visitorStatus;

}
