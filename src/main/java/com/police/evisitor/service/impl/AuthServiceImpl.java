package com.police.evisitor.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.police.evisitor.dto.request.LoginRequestDTO;
import com.police.evisitor.dto.response.LoginResponseDTO;
import com.police.evisitor.dto.response.RoleMenuResponseDTO;
import com.police.evisitor.entity.Role;
import com.police.evisitor.entity.RoleMenuList;
import com.police.evisitor.entity.User;
import com.police.evisitor.repository.RoleListRepository;
import com.police.evisitor.repository.RoleMenuRepository;
import com.police.evisitor.repository.UserRepository;
import com.police.evisitor.service.AuthService;
import com.police.evisitor.service.LoginProjection;
import com.police.evisitor.util.Constants;
import com.police.evisitor.util.JWTUtility;

import jakarta.transaction.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JWTUtility jwtUtility;

	@Autowired
	private RoleMenuRepository roleMenuRepo;

	@Autowired
	private RoleListRepository roleListRepo;

	@Override
	@Transactional
	public LoginResponseDTO login(LoginRequestDTO request) {

		try {
			LoginProjection user = repository.loginUser(request.getUserLogin())
					.orElseThrow(() -> new BadRequestException("Invalid Login Id"));

			if (!passwordEncoder.matches(request.getPassword(), user.getUserPassword())) {

				throw new BadRequestException("Invalid Password");
			}

			User entity = repository.findById(user.getUserId()).get();

			entity.setLoginStatus(true);
			entity.setLastLoggedIn(LocalDateTime.now());

			repository.save(entity);

			Optional<Role> userRole = roleListRepo.findByRoleIdAndRecordStatusNot(user.getRoleId(), Constants.D);

			List<RoleMenuList> menus = roleMenuRepo.findByRoleIdAndRecordStatusNot(userRole.get(), Constants.D);

			List<RoleMenuResponseDTO> menuDtos = menus.stream()
					.filter(rm -> rm.getMenuId() != null && rm.getMenuId().getMenuId() != 6).map(rm -> {
						RoleMenuResponseDTO dto = new RoleMenuResponseDTO();
						dto.setMenu(rm.getMenuId().getMenuId());
						dto.setMenuName(rm.getMenuId().getMenuName());
						return dto;
					}).distinct().toList();

			LoginResponseDTO response = new LoginResponseDTO();

			BeanUtils.copyProperties(user, response);

			response.setFullName(user.getFirstName() + " " + user.getLastName());

			response.setMobile(user.getUserMob());

			response.setEmail(user.getUserEmail());

			response.setDistrictName(user.getDistrict());

			response.setPsName(user.getPs());

			String token = jwtUtility.generateTokenBySSOId(user.getUserLogin());

			response.setToken(token);
			response.setMenu(menuDtos);

			return response;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	@Transactional
	public void logout(String userLogin) throws BadRequestException {

		User user = repository.findByUserLoginIgnoreCaseAndRecordStatusNot(userLogin, 'D');
		if (user == null) {
			throw new BadRequestException("User not found");
		}

		user.setLoginStatus(false);
		user.setUpdatedOn(LocalDateTime.now());

		repository.save(user);
	}

}
