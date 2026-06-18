package com.police.evisitor.util;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ApiErrorResponse {

	private Boolean status;
	private String message;
	private LocalDateTime timestamp;
}