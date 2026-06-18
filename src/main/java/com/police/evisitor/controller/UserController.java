package com.police.evisitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.police.evisitor.dto.request.UserRequestDTO;
import com.police.evisitor.dto.response.ApiResponse;
import com.police.evisitor.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/visitor")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/createUser")
	public ResponseEntity<ApiResponse<?>> saveUser(@RequestBody UserRequestDTO request) {

		userService.saveUser(request);

		return ResponseEntity.ok(ApiResponse.builder().status("SUCCESS").message("User Created Successfully").build());
	}

}
