package com.police.evisitor.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.police.evisitor.dto.request.HotelRequestDTO;
import com.police.evisitor.dto.response.ApiResponse;
import com.police.evisitor.entity.Hotel;
import com.police.evisitor.service.HotelService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/visitor")
@RequiredArgsConstructor
public class HotelController {

	private final HotelService hotelService;

	@PostMapping("/createHotel")
	public ResponseEntity<ApiResponse<?>> createHotel(@RequestBody HotelRequestDTO request) {

		hotelService.createHotel(request);

		return ResponseEntity.ok(ApiResponse.builder().status(HttpStatus.CREATED.toString())
				.message("Hotel Created Successfully").build());
	}

	@GetMapping("/listHotel")
	public ResponseEntity<List<Hotel>> getHotels() {

		return ResponseEntity.ok(hotelService.getHotels());
	}

	@GetMapping("/listHotelByPs")
	public ResponseEntity<List<Hotel>> getHotelsByPs(@RequestParam Integer districtCd, @RequestParam Integer psCd) {

		return ResponseEntity.ok(hotelService.getHotels(districtCd, psCd));
	}

}
