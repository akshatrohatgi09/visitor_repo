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
public class UserListRequestDTO {

	private String loginId;

	private String name;

	private String userLogin;

	private String mobile;

	private Integer stateCd;

	private Integer zoneCd;

	private Integer rangeCd;

	private Integer districtCd;

	private Integer sdpoCd;

	private Integer psCd;

	private Long hotelCd;

	private Long roleId;

	private Character recordStatus;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate fromDate;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate toDate;

	@Builder.Default
	private Integer pageNo = 0;

	@Builder.Default
	private Integer pageSize = 10;
}