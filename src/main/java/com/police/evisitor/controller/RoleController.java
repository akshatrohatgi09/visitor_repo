package com.police.evisitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.police.evisitor.dto.response.ApiResponse;
import com.police.evisitor.entity.Role;
import com.police.evisitor.service.impl.RoleServiceImpl;

@RestController
@RequestMapping("/visitor")
public class RoleController {

	@Autowired
	private RoleServiceImpl roleServiceImpl;

	@PostMapping("/roleList")
	public ResponseEntity<ApiResponse<?>> getRoleList() {

		List<Role> roleList = roleServiceImpl.getRoleList();

		com.police.evisitor.dto.response.ApiResponse<?> apiResponse = ApiResponse.builder()
				.message("Record Fetched Successfully.").status(String.valueOf(HttpStatus.OK)).data(roleList).build();

		return ResponseEntity.ok(apiResponse);
	}

}
