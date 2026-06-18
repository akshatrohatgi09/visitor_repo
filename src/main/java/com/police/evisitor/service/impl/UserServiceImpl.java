package com.police.evisitor.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.police.evisitor.dto.request.UserRequestDTO;
import com.police.evisitor.entity.Role;
import com.police.evisitor.entity.User;
import com.police.evisitor.repository.HotelRepository;
import com.police.evisitor.repository.RoleListRepository;
import com.police.evisitor.repository.UserRepository;
import com.police.evisitor.service.UserService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private HotelRepository hotelRepo;

	@Autowired
	private RoleListRepository roleRepository;

	@Transactional
	@Override
	public void saveUser(UserRequestDTO reqDto) {

		try {

			User user = modelMapper.map(reqDto, User.class);

			user.setUserLogin(reqDto.getUserLogin().toLowerCase().trim());

			isUserAlreadyExist(user);

			Role role = roleRepository.findById(reqDto.getRoleId())
					.orElseThrow(() -> new RuntimeException("Role Not Found : " + reqDto.getRoleId()));

			validateRoleData(role.getRoleId(), reqDto);

			// Save role id directly
			user.setUserRoleId(reqDto.getRoleId());

			// Save hotel id directly
			if (reqDto.getHotelId() != null && reqDto.getHotelId() > 0) {

				hotelRepo.findById(reqDto.getHotelId())
						.orElseThrow(() -> new RuntimeException("Hotel Not Found : " + reqDto.getHotelId()));

				user.setHotelCd(reqDto.getHotelId());

			} else {
				user.setHotelCd(null);
			}

			user.setRecordStatus('C');
			user.setLoginStatus(Boolean.FALSE);

			user.setCreatedBy("SYSTEM");
			user.setUpdatedBy("SYSTEM");

			userRepo.save(user);

			log.info("User created successfully. Login : {}", user.getUserLogin());

		} catch (Exception e) {

			log.error("Error while creating user. Request : {}", reqDto, e);

			throw new RuntimeException("Unable to create user : " + e.getMessage(), e);
		}
	}

	private void validateRoleData(Long roleId, UserRequestDTO request) {

		if (roleId == 2 && request.getDistrictCd() == null) {

			throw new RuntimeException("District is required");
		}

		if (roleId == 3 && (request.getDistrictCd() == null || request.getPsCd() == null)) {

			throw new RuntimeException("District and PS are required");
		}

		if ((roleId == 4 || roleId == 5) && request.getHotelId() == null) {

			throw new RuntimeException("Hotel is required");
		}
	}

	private void isUserAlreadyExist(User user) {

		User loginCheck = userRepo.findByUserLoginIgnoreCaseAndRecordStatusNot(user.getUserLogin(), 'D');

		if (loginCheck != null) {
			throw new RuntimeException("User Login Already Exists : " + user.getUserLogin());
		}

		User emailCheck = userRepo.findByUserEmailIgnoreCaseAndRecordStatusNot(user.getUserEmail(), 'D');

		if (emailCheck != null) {
			throw new RuntimeException("Email Already Exists : " + user.getUserEmail());
		}

		User mobileCheck = userRepo.findByUserMobAndRecordStatusNot(user.getUserMob(), 'D');

		if (mobileCheck != null) {
			throw new RuntimeException("Mobile Already Exists : " + user.getUserMob());
		}
	}

}
