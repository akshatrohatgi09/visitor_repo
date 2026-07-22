package com.police.evisitor.service.impl;

import java.time.LocalDateTime;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.police.evisitor.controller.UserController;
import com.police.evisitor.dto.request.LoginRequestDTO;
import com.police.evisitor.dto.response.LoginResponseDTO;
import com.police.evisitor.entity.User;
import com.police.evisitor.repository.UserRepository;
import com.police.evisitor.service.AuthService;
import com.police.evisitor.service.LoginProjection;

import jakarta.transaction.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public LoginResponseDTO login(LoginRequestDTO request) throws BadRequestException {

		LoginProjection user = repository.loginUser(request.getUserLogin())
				.orElseThrow(() -> new BadRequestException("Invalid Login Id"));

		if (!passwordEncoder.matches(request.getPassword(), user.getUserPassword())) {

			throw new BadRequestException("Invalid Password");
		}

		User entity = repository.findById(user.getUserId()).get();

		entity.setLoginStatus(true);
		entity.setLastLoggedIn(LocalDateTime.now());

		repository.save(entity);

		LoginResponseDTO response = new LoginResponseDTO();

		BeanUtils.copyProperties(user, response);

		response.setFullName(user.getFirstName() + " " + user.getLastName());

		response.setMobile(user.getUserMob());

		response.setEmail(user.getUserEmail());

		response.setDistrictName(user.getDistrict());

		response.setPsName(user.getPs());

		return response;
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
