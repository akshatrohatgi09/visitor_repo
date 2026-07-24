package com.police.evisitor.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class VisitorListRequestDTO {

	private Integer pageNo = 0;
	private Integer pageSize = 10;

	private String loginId;

	private String visitorName;
	private String visitorMobile;

	private LocalDate fromDate;
	private LocalDate toDate;

	private Integer stateCd;
	private Integer zoneCd;
	private Integer rangeCd;
	private Integer districtCd;
	private Integer sdpoCd;
	private Integer psCd;
	private Long hotelCd;

	private String visitorStatus;

}
