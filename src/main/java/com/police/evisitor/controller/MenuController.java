package com.police.evisitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.police.evisitor.dto.response.ApiResponse;
import com.police.evisitor.entity.Menu;
import com.police.evisitor.service.MenuService;

@RestController
@RequestMapping("/visitor")
public class MenuController {

	@Autowired
	private MenuService menuService;

	@PostMapping("/menuList")
	public ResponseEntity<ApiResponse<?>> getMenuList() {

		List<Menu> menuList = menuService.getMenuList();
		ApiResponse<?> apiResponse = ApiResponse.builder().message("Record Fetched Successfully.")
				.status(String.valueOf(HttpStatus.OK)).data(menuList).build();
		return ResponseEntity.ok(apiResponse);
	}

}
