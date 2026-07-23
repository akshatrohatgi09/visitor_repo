package com.police.evisitor.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.police.evisitor.dto.request.HotelTypeResponseDTO;
import com.police.evisitor.dto.response.ApiResponse;
import com.police.evisitor.entity.Menu;
import com.police.evisitor.entity.Role;
import com.police.evisitor.service.MasterService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/visitor")
@RequiredArgsConstructor
public class MasterController {

	private final MasterService masterService;

	@GetMapping("/countries")
	public ResponseEntity<ApiResponse<?>> getCountries() {

		return ResponseEntity.ok(masterService.getCountries());
	}

	@GetMapping("/states")
	public ResponseEntity<?> getStates() {
		return ResponseEntity.ok(masterService.getStates());
	}

	@GetMapping("/zones/{stateCd}")
	public ResponseEntity<?> getZones(@PathVariable Integer stateCd) {
		return ResponseEntity.ok(masterService.getZones(stateCd));
	}

	@GetMapping("/ranges/{zoneCd}")
	public ResponseEntity<?> getRanges(@PathVariable Integer zoneCd) {
		return ResponseEntity.ok(masterService.getRanges(zoneCd));
	}

	@GetMapping("/districts/{stateCd}")
	public ResponseEntity<?> getDistricts(@PathVariable Integer stateCd) {

		return ResponseEntity.ok(masterService.getDistricts(stateCd));
	}

	@GetMapping("/sdpos/{districtCd}")
	public ResponseEntity<?> getSdpos(@PathVariable Integer districtCd) {
		return ResponseEntity.ok(masterService.getSdpos(districtCd));
	}

	@GetMapping("/policeStations")
	public ResponseEntity<?> getPoliceStations(@RequestParam Integer stateCd, @RequestParam Integer districtCd) {

		return ResponseEntity.ok(masterService.getPoliceStations(stateCd, districtCd));
	}

	@GetMapping("/documentList")
	public ResponseEntity<?> getDocumentList() {

		return ResponseEntity.ok(masterService.getDocumentList());
	}

	@GetMapping("/visitReasonList")
	public ResponseEntity<?> getVisitReasonList() {

		return ResponseEntity.ok(masterService.getVisitReasonList());
	}

	@GetMapping("/roleList")
	public ResponseEntity<ApiResponse<?>> getRoleList() {

		List<Role> roleList = masterService.getRoleList();
		ApiResponse<?> apiResponse = ApiResponse.builder().message("Record Fetched Successfully.")
				.status(String.valueOf(HttpStatus.OK)).data(roleList).build();
		return ResponseEntity.ok(apiResponse);
	}

	@GetMapping("/menuList")
	public ResponseEntity<ApiResponse<?>> getMenuList() {

		List<Menu> menuList = masterService.getMenuList();
		ApiResponse<?> apiResponse = ApiResponse.builder().message("Record Fetched Successfully.")
				.status(String.valueOf(HttpStatus.OK)).data(menuList).build();
		return ResponseEntity.ok(apiResponse);
	}

	@GetMapping("/allDistricts")
	public ResponseEntity<?> getAllDistricts() {

		return ResponseEntity.ok(masterService.getAllDistricts());
	}

	@GetMapping("/hotelTypeList")
	public ResponseEntity<ApiResponse<List<HotelTypeResponseDTO>>> getHotelTypeList() {

		List<HotelTypeResponseDTO> response = masterService.getHotelTypes();

		return ResponseEntity.ok(ApiResponse.<List<HotelTypeResponseDTO>>builder().status("SUCCESS")
				.message("Hotel Type List Fetched Successfully").data(response).build());
	}

}
