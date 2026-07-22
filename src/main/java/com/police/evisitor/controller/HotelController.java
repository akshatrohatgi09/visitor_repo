package com.police.evisitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.police.evisitor.dto.request.HotelListRequestDTO;
import com.police.evisitor.dto.request.HotelRequestDTO;
import com.police.evisitor.dto.response.ApiResponse;
import com.police.evisitor.entity.Hotel;
import com.police.evisitor.service.HotelService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/visitor")

public class HotelController {

	@Autowired
	private HotelService hotelService;

	@PostMapping("/createHotel")
	public ResponseEntity<ApiResponse<?>> createHotel(@RequestBody HotelRequestDTO request) {

		hotelService.createHotel(request);

		return ResponseEntity.ok(ApiResponse.builder().status("SUCCESS").message("Hotel Created Successfully").build());
	}

	@PostMapping("/listHotels")
	public ResponseEntity<ApiResponse<?>> listHotels(@RequestBody HotelListRequestDTO request) {

		return ResponseEntity.ok(ApiResponse.builder().status("SUCCESS").message("Hotels fetched successfully.")
				.data(hotelService.listHotels(request)).build());

	}

	@PostMapping("/updateHotel")
	public ResponseEntity<ApiResponse<?>> updateHotel(@RequestBody HotelRequestDTO request) {

		hotelService.updateHotel(request);

		return ResponseEntity.ok(ApiResponse.builder().status("SUCCESS").message("Hotel Updated Successfully").build());
	}

	@PostMapping("deleteHotel")
	public ResponseEntity<ApiResponse<?>> deleteHotelData(@RequestBody HotelRequestDTO request) {
		String result = hotelService.deleteHotel(request);
		ApiResponse<?> apiResponse = ApiResponse.builder().status(String.valueOf(HttpStatus.OK.value()))
				.message("Hotel Deleted Successfully").data(result).build();

		return ResponseEntity.ok(apiResponse);
	}

	@PostMapping("actveInactiveHotel")
	public ResponseEntity<ApiResponse<?>> activeInactiveHotelData(@RequestBody HotelRequestDTO request) {

		String result = hotelService.activeInactiveHotel(request);

		ApiResponse<?> apiResponse = ApiResponse.builder().status(HttpStatus.OK.toString()).message(result).build();

		return ResponseEntity.ok(apiResponse);
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
