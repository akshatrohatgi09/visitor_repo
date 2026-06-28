package com.police.evisitor.dto.response;

import java.util.List;

import com.police.evisitor.entity.Role;

import lombok.Data;

@Data
public class RoleResponseDTO {

	Role role;
	List<RoleMenuResponseDTO> menu;
}