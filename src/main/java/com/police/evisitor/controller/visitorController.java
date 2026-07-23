package com.police.evisitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.police.evisitor.dto.request.VisitorCheckoutRequestDTO;
import com.police.evisitor.dto.request.VisitorRequestDTO;
import com.police.evisitor.dto.response.ApiResponse;
import com.police.evisitor.service.VisitorService;

@RestController
@RequestMapping("/visitor")
public class visitorController {

	@Autowired
	private VisitorService visitorService;

	@PostMapping("addVisitor")
	public ResponseEntity<ApiResponse<?>> addVisitor(@RequestBody List<VisitorRequestDTO> request,
			@RequestHeader("loginId") String id) {
		String response = visitorService.addVisitor(request, id);

		ApiResponse<?> apiResponse = ApiResponse.builder().status(String.valueOf(HttpStatus.CREATED))
				.message("Visitor Added Successfully.").data(response).build();
		return ResponseEntity.ok(apiResponse);
	}
	
	@PostMapping("/checkout")
	public ResponseEntity<Object> checkoutVisitor(
	        @RequestBody VisitorCheckoutRequestDTO request) {

	    return ResponseEntity.ok(visitorService.checkoutVisitor(request));
	}

}
