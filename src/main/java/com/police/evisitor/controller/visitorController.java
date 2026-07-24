package com.police.evisitor.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.police.evisitor.dto.request.VisitorCheckoutRequestDTO;
import com.police.evisitor.dto.request.VisitorListRequestDTO;
import com.police.evisitor.dto.request.VisitorRequestDTO;
import com.police.evisitor.dto.response.ApiResponse;
import com.police.evisitor.repository.VisitorListProjection;
import com.police.evisitor.service.VisitorService;

@RestController
@RequestMapping("/visitor")
public class visitorController {

	@Autowired
	private VisitorService visitorService;

	@PostMapping("addVisitor")
	public ResponseEntity<ApiResponse<?>> addVisitor(@RequestBody List<VisitorRequestDTO> request) {
		String response = visitorService.addVisitor(request);

		ApiResponse<?> apiResponse;
		if (StringUtils.isBlank(response)) {
			apiResponse = ApiResponse.builder().status("Success").message("Visitor Added Successfully.")
					.data("Visitor saved successfully").build();
		} else {
			apiResponse = ApiResponse.builder().status("Failed").message("Visitor Added Error.").data(response).build();
		}
		return ResponseEntity.ok(apiResponse);
	}

	@PostMapping("/checkout")
	public ResponseEntity<Object> checkoutVisitor(@RequestBody VisitorCheckoutRequestDTO request) {

		return ResponseEntity.ok(visitorService.checkoutVisitor(request));
	}

	@PostMapping("updateVisitor")
	public ResponseEntity<ApiResponse<?>> updateVisitor(@RequestBody VisitorRequestDTO request) {

		ApiResponse<?> apiResponse = visitorService.updateVisitor(request);

		return ResponseEntity.ok(apiResponse);
	}

	@PostMapping("visitorList")
	public ResponseEntity<ApiResponse<?>> getVisitorList(@RequestBody VisitorListRequestDTO request) {

		Page<VisitorListProjection> response = visitorService.getVisitorList(request);

		ApiResponse<?> apiResponse = ApiResponse.builder().status("Success")
				.message("Visitor List fetched successfully.").data(response).build();

		return ResponseEntity.ok(apiResponse);
	}

	
	@PostMapping("searchVisitorList")
	public ResponseEntity<ApiResponse<?>> visitorList(@RequestBody VisitorListRequestDTO request) {

		Page<VisitorListProjection> response = visitorService.getVisitorCheckOutList(request);

		ApiResponse<?> apiResponse = ApiResponse.builder().status("Success")
				.message("Visitor List fetched successfully.").data(response).build();

		return ResponseEntity.ok(apiResponse);
	}

}
