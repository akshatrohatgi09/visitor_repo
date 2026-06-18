package com.police.evisitor.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.police.evisitor.util.ApiErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleException(Exception ex) {

		ApiErrorResponse response = new ApiErrorResponse();

		response.setStatus(false);
		response.setMessage(ex.getMessage());
		response.setTimestamp(LocalDateTime.now());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
}
