package com.police.evisitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@PostMapping("deleteUser")
	public ResponseEntity<ApiResponse<?>> deleteUser(@RequestBody UserRequestDTO request){
		
		userService.deleteUser(request);
		ApiResponse<?> apiResponse = ApiResponse.builder()
		        .status(HttpStatus.CREATED.toString())
		        .message("User Deleted SuccessFully.")
		        .build();

		return ResponseEntity.ok(apiResponse);
	}


	@PostMapping("actveInactiveUser")
	public ResponseEntity<ApiResponse<?>> activeInactiveHotelData(@RequestBody UserRequestDTO request){
		
		String result = userService.activeInactiveUser(request);
		ApiResponse<?> apiResponse = ApiResponse.builder()
				.status(HttpStatus.OK.toString())
				.message(result)
				.build();

		return ResponseEntity.ok(apiResponse);
	}


//	@PostMapping("updateUser")
//	public ResponseEntity<ApiResponse<?>> updateUser(@RequestBody UserRequestDTO request){
//		
//		userService.updateUser(request);
//		ApiResponse<?> apiResponse = ApiResponse.builder()
//		        .status(HttpStatus.OK.toString())
//		        .message("User Data Updated SuccessFully.")
//		        .build();
//
//		return ResponseEntity.ok(apiResponse);
//	}
	
	
	@PostMapping("/updateLoginStatus")
	public ResponseEntity<ApiResponse<?>> updateLoginStatus(@RequestParam("userId") Long userId, @RequestParam("loginStatus") Boolean loginStatus){
		
		userService.updateLoginStatus(userId,loginStatus);
		ApiResponse<?> apiResponse = ApiResponse.builder()
		        .status(HttpStatus.CREATED.toString())
		        .message("User Login Data Updated SuccessFully.")
		        .build();

		return ResponseEntity.ok(apiResponse);
	}


	@PostMapping("/getUser")
	public ResponseEntity<ApiResponse<?>> getUsers(@RequestBody UserRequestDTO request){
		
		ApiResponse<?> apiResponse = ApiResponse.builder()
		        .status(HttpStatus.CREATED.toString())
		        .data(userService.getUser(request))
		        .message("User List Fetched SuccessFully.")
		        .build();

		return ResponseEntity.ok(apiResponse);
	}

}
