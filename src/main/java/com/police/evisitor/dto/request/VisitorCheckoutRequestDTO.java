package com.police.evisitor.dto.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class VisitorCheckoutRequestDTO {

	private Long visitorId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime checkOutDate;

	private String loginId;

}