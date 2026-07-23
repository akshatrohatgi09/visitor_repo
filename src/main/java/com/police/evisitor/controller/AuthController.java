package com.police.evisitor.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.police.evisitor.dto.request.LoginRequestDTO;
import com.police.evisitor.dto.request.LogoutRequestDTO;
import com.police.evisitor.dto.response.ApiResponse;
import com.police.evisitor.dto.response.LoginResponseDTO;
import com.police.evisitor.service.AuthService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/visitor")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody LoginRequestDTO request) {

		System.out.println("Login API called");
		System.out.println(request);

		LoginResponseDTO response = null;
		try {
			response = authService.login(request);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity
				.ok(ApiResponse.builder().status("SUCCESS").message("Login Successfully").data(response).build());
	}

	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<Object>> logout(@RequestBody LogoutRequestDTO request)
			throws BadRequestException {

		authService.logout(request.getUserLogin());

		return ResponseEntity.ok(ApiResponse.builder().status("SUCCESS").message("Logout Successfully").build());
	}
}