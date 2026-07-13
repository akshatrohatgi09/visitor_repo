package com.police.evisitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.police.evisitor.dto.request.RoleRequestDTO;
import com.police.evisitor.dto.response.ApiResponse;
import com.police.evisitor.dto.response.RoleResponseDTO;
import com.police.evisitor.service.RoleService;

@RestController
@RequestMapping("/visitor")
public class RoleController {

	@Autowired
	RoleService roleService;

	@PostMapping("createRole")
	public ResponseEntity<ApiResponse<?>> createRoleData(@RequestBody RoleRequestDTO request) {

		int check = roleService.saveRoleList(request);
		HttpStatus status = (check == 1) ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		String message = (check == 1) ? "Role Saved Successfully." : "Error In Saving Data";

		ApiResponse<?> apiResponse = ApiResponse.builder().status(status.toString()).message(message).build();

		return ResponseEntity.ok(apiResponse);
	}

	@PostMapping("updateRole")
	public ResponseEntity<ApiResponse<?>> updateRole(@RequestBody RoleRequestDTO request) {
		
		int response = roleService.updateRoleList(request);
		HttpStatus status = (response == 1) ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		String message = (response == 1) ? "Role Updated Successfully." : "Something Error to Save Data";

		ApiResponse<?> apiResponse = ApiResponse.builder().status(status.toString()).message(message).build();

		return ResponseEntity.ok(apiResponse);

	}

	@PostMapping("fetchRole")
	public ResponseEntity<ApiResponse<?>> fetchRole() {
		List<RoleResponseDTO> response = roleService.fetchRoleList();

		ApiResponse<?> apiResponse = ApiResponse.builder().status(HttpStatus.OK.toString())
				.message("Role Fetch successuflly.").data(response).build();

		return ResponseEntity.ok(apiResponse);
	}

	@PostMapping("deleteRole")
	public ResponseEntity<ApiResponse<?>> deleteRole(@RequestBody RoleRequestDTO request) {
		Integer response = roleService.deleteRole(request);
		HttpStatus status = (response == 1) ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		String message = (response == 1) ? "Role Delete Successfully." : "Something Error to Delete Data";

		ApiResponse<?> apiResponse = ApiResponse.builder().status(status.toString()).message(message).build();

		return ResponseEntity.ok(apiResponse);
	}

}