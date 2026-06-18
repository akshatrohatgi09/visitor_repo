package com.police.evisitor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.police.evisitor.service.MasterService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/visitor")
@RequiredArgsConstructor
public class MasterController {

	private final MasterService masterService;

	@GetMapping("/districts/{stateCd}")
	public ResponseEntity<?> getDistricts(@PathVariable Integer stateCd) {

		return ResponseEntity.ok(masterService.getDistricts(stateCd));
	}

	@GetMapping("/policeStations")
	public ResponseEntity<?> getPoliceStations(@RequestParam Integer stateCd, @RequestParam Integer districtCd) {

		return ResponseEntity.ok(masterService.getPoliceStations(stateCd, districtCd));
	}

}
