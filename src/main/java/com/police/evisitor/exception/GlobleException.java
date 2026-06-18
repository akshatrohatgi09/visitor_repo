package com.police.evisitor.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.police.evisitor.dto.response.ApiResponse;

@RestControllerAdvice
public class GlobleException {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<com.police.evisitor.dto.response.ApiResponse<?>> handleResourceNotFoundException(
			ResourceNotFoundException ex) {
		List<String> errors = new ArrayList<String>();
		errors.add(ex.getMessage());
		ApiResponse<?> apiResponse = ApiResponse.builder().errors(errors).status(String.valueOf(HttpStatus.NO_CONTENT))
				.build();

		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
	}

	@ExceptionHandler(NotFound.class)
	public ResponseEntity<ApiResponse<?>> handleNotFoundException(NotFound ex) {
		ApiResponse<?> apiResponse = ApiResponse.builder().message(ex.getMessage())
				.status(String.valueOf(HttpStatus.NOT_FOUND)).build();

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
	}

	@ExceptionHandler(UserAlreadyExists.class)
	public ResponseEntity<ApiResponse<?>> handleUserAlreadyExistsException(UserAlreadyExists ex) {
		ApiResponse<?> apiResponse = ApiResponse.builder().message(ex.getMessage())
				.status(String.valueOf(HttpStatus.CONFLICT)).build();

		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
	}

	@ExceptionHandler(BadRequest.class)
	public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(BadRequest ex) {
		List<String> errors = new ArrayList<String>();
		errors.add(ex.getMessage());
		ApiResponse<?> apiResponse = ApiResponse.builder().status(String.valueOf(HttpStatus.BAD_REQUEST)).errors(errors)
				.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<ApiResponse<?>> handleIOException(IOException ex) {
		List<String> errors = new ArrayList<String>();
		errors.add(ex.getMessage());
		ApiResponse<?> apiResponse = ApiResponse.builder().status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
				.errors(errors).build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
	}

}