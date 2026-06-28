package com.police.evisitor.service;

import java.util.List;

import com.police.evisitor.dto.request.RoleRequestDTO;
import com.police.evisitor.dto.response.RoleResponseDTO;


public interface RoleService {
    public int saveRoleList(RoleRequestDTO request);

    public int updateRoleList(RoleRequestDTO request);

    public List<RoleResponseDTO> fetchRoleList();

    public int deleteRole(RoleRequestDTO request);
}