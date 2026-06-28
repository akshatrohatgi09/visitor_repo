package com.police.evisitor.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.police.evisitor.dto.request.RoleRequestDTO;
import com.police.evisitor.dto.response.RoleMenuResponseDTO;
import com.police.evisitor.dto.response.RoleResponseDTO;
import com.police.evisitor.entity.Menu;
import com.police.evisitor.entity.Role;
import com.police.evisitor.entity.RoleMenuList;
import com.police.evisitor.exception.NotFound;
import com.police.evisitor.exception.ResourceNotFoundException;
import com.police.evisitor.repository.MenuRepository;
import com.police.evisitor.repository.RoleListRepository;
import com.police.evisitor.repository.RoleMenuRepository;
import com.police.evisitor.service.RoleService;
import com.police.evisitor.util.Constants;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleListRepository roleListRepo;

	@Autowired
	private MenuRepository menuRepo;

	@Autowired
	private RoleMenuRepository roleMenuRepo;

	@Transactional
	@Override
	public int saveRoleList(RoleRequestDTO reqDto) {
		if (reqDto == null || reqDto.getRole() == null) {
			throw new IllegalArgumentException("Role ID is missing in the request");
		}

		Role role = roleListRepo.findById(reqDto.getRole().longValue())
				.orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + reqDto.getRole()));

		List<Menu> menuList = new ArrayList<>();
		for (Integer menuId : reqDto.getMenu()) {
			Menu menu = menuRepo.findById(menuId)
					.orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + menuId));
			menuList.add(menu);
		}

		int check = 0;
		for (Menu menu : menuList) {
			RoleMenuList roleList = new RoleMenuList();
			roleList.setRoleId(role);
			roleList.setRecordStatus("C");
			roleList.setCreatedBy(reqDto.getLoginId());
			roleList.setUpdatedBy(reqDto.getLoginId());
			roleList.setMenuId(menu);
			roleMenuRepo.save(roleList);

			check++;
		}
		if (menuList.size() == check) {
			return 1;
		} else {
			return 0;
		}
	}

	@Transactional
	@Override
	public int updateRoleList(RoleRequestDTO reqDto) {
		Role role = roleListRepo.findById(reqDto.getRole())
				.orElseThrow(() -> new NotFound("Role Not Found With ID: " + reqDto.getRole()));

		try {
			List<RoleMenuList> existingMenus = roleMenuRepo.findByRoleIdAndRecordStatusNot(role, "D");

			Set<Integer> dbMenu = existingMenus.stream().map(rm -> rm.getMenuId().getMenuId()).filter(Objects::nonNull)
					.collect(Collectors.toSet());

			Set<Integer> reqMenu = Optional.ofNullable(reqDto.getMenu()).orElse(Collections.emptyList()).stream()
					.filter(Objects::nonNull).collect(Collectors.toSet());

			Set<Integer> menuDelete = dbMenu.stream().filter(id -> id != null && id != 6 && !reqMenu.contains(id))
					.collect(Collectors.toSet());

			existingMenus.stream().filter(rm -> menuDelete.contains(rm.getMenuId().getMenuId())).forEach(rm -> {
				rm.setRecordStatus(Constants.D);
				rm.setUpdatedBy(reqDto.getLoginId());
			});

			roleMenuRepo.saveAll(existingMenus);

			Set<Integer> newMenu = reqMenu.stream().filter(id -> !dbMenu.contains(id)).collect(Collectors.toSet());

			for (Integer menuId : newMenu) {

				Menu menu = menuRepo.findById(menuId)
						.orElseThrow(() -> new NotFound("Menu Not Found With ID: " + menuId));

				RoleMenuList newEntry = new RoleMenuList();
				newEntry.setRoleId(role);
				newEntry.setMenuId(menu);
				newEntry.setRecordStatus(Constants.C);
				newEntry.setCreatedBy(reqDto.getLoginId());
				newEntry.setUpdatedBy(reqDto.getLoginId());

				roleMenuRepo.save(newEntry);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
		return 1;
	}

	@Override
	public List<RoleResponseDTO> fetchRoleList() {
		List<Role> roles = roleListRepo.findByRecordStatusNot(Constants.D);
		if (roles.isEmpty()) {
			return List.of();
		}

		List<RoleMenuList> roleMenus = roleMenuRepo.findByRecordStatusNot(Constants.D);
		Map<Role, List<RoleMenuList>> menuByRole = roleMenus.stream()
				.collect(Collectors.groupingBy(RoleMenuList::getRoleId));

		List<RoleResponseDTO> responseList = new ArrayList<>();
		for (Role role : roles) {
			RoleResponseDTO dto = new RoleResponseDTO();
			dto.setRole(role);

			List<RoleMenuList> menuLinks = menuByRole.getOrDefault(role, List.of());

			List<RoleMenuResponseDTO> menuDtos = menuLinks.stream().filter(rm -> rm.getMenuId() != null
					&& rm.getMenuId().getMenuId() != null && rm.getMenuId().getMenuId() != 6).map(rm -> {
						RoleMenuResponseDTO m = new RoleMenuResponseDTO();
						m.setMenu(rm.getMenuId().getMenuId());
						m.setMenuName(rm.getMenuId().getMenuName());
						return m;
					}).toList();

			dto.setMenu(menuDtos);
			responseList.add(dto);
		}

		return responseList;
	}

	@Override
	public int deleteRole(RoleRequestDTO reqDto) {
		Role role = roleListRepo.findById(reqDto.getRole()).orElseThrow(() -> new NotFound("Role Not Found With ID: "));

		List<RoleMenuList> roleMenu = roleMenuRepo.findByRoleIdAndRecordStatusNot(role, "D");

		roleMenu.forEach(rm -> {
			rm.setRecordStatus(Constants.D);
			rm.setUpdatedBy(reqDto.getLoginId());
		});

		List<RoleMenuList> response = roleMenuRepo.saveAll(roleMenu);

		if (response == null || response.isEmpty()) {
			return 0;
		}

		return 1;
	}

}