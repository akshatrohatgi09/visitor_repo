package com.police.evisitor.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelListRequestDTO {

	private String loginId;

	private String hotelName;
	private String ownerName;
	private String mobileNo;

	private Integer stateCd;
	private Integer zoneCd;
	private Integer rangeCd;
	private Integer districtCd;
	private Integer sdpoCd;
	private Integer psCd;

	private Long hotelCd;

	private Character recordStatus;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate fromDate;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate toDate;

	private Integer pageNo;
	private Integer pageSize;

}