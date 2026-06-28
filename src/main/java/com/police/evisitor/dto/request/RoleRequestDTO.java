package com.police.evisitor.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class RoleRequestDTO {
	private Integer roleMenu;
	private Long role;
	private List<Integer> menu;
	private String loginId;
}