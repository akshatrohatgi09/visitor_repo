package com.police.evisitor.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.police.evisitor.dto.request.HotelRequestDTO;
import com.police.evisitor.entity.Hotel;
import com.police.evisitor.entity.StateRepository;
import com.police.evisitor.entity.User;
import com.police.evisitor.exception.NotFound;
import com.police.evisitor.repository.DistrictRepository;
import com.police.evisitor.repository.HotelRepository;
import com.police.evisitor.repository.PoliceStationRepository;
import com.police.evisitor.repository.RangeRepository;
import com.police.evisitor.repository.SdpoRepository;
import com.police.evisitor.repository.UserRepository;
import com.police.evisitor.repository.ZoneRepository;
import com.police.evisitor.service.HotelService;
import com.police.evisitor.util.Constants;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

	private final HotelRepository hotelRepo;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private ZoneRepository zoneRepository;

	@Autowired
	private RangeRepository rangeRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private SdpoRepository sdpoRepository;

	@Autowired
	private PoliceStationRepository policeStationRepository;

	@Transactional
	@Override
	public void createHotel(HotelRequestDTO req) {
		try {
			validateRequest(req);
			validateMasterData(req);
			isHotelAlreadyExist(req);

			Hotel hotel = Hotel.builder().hotelName(req.getHotelName().trim()).ownerName(req.getOwnerName().trim())
					.mobileNo(req.getMobileNo().trim())
					.email(req.getEmail() == null ? null : req.getEmail().trim().toLowerCase())
					.stateCd(req.getStateCd()).zoneCd(req.getZoneCd()).rangeCd(req.getRangeCd())
					.districtCd(req.getDistrictCd()).sdpoCd(req.getSdpoCd()).psCd(req.getPsCd())
					.address(req.getAddress()).comment(req.getComment()).recordStatus("C").createdBy(req.getLoginUser())
					.build();

			hotelRepo.save(hotel);
			log.info("Hotel Created Successfully : {}", hotel.getHotelName());

		} catch (Exception e) {
			log.error("Error while creating hotel : {}", req, e);
			throw new RuntimeException(e.getMessage());

		}
	}

	private void isHotelAlreadyExist(HotelRequestDTO req) {

		Hotel hotel = hotelRepo.findByMobileNoAndRecordStatusNot(req.getMobileNo(), "D");

		if (hotel != null)
			throw new RuntimeException("Mobile Number already exists.");

		if (StringUtils.isNotBlank(req.getEmail())) {

			hotel = hotelRepo.findByEmailIgnoreCaseAndRecordStatusNot(req.getEmail(), "D");

			if (hotel != null)
				throw new RuntimeException("Email already exists.");
		}
	}

	private void validateMasterData(HotelRequestDTO req) {

		stateRepository.findByStateCdAndRecordStatusNot(req.getStateCd(), "D")
				.orElseThrow(() -> new RuntimeException("Invalid State"));

		zoneRepository.findByZoneCdAndRecordStatusNot(req.getZoneCd(), "D")
				.orElseThrow(() -> new RuntimeException("Invalid Zone"));

		rangeRepository.findByRangeCdAndRecordStatusNot(req.getRangeCd(), "D")
				.orElseThrow(() -> new RuntimeException("Invalid Range"));

		districtRepository.findByDistrictCdAndRecordStatusNot(req.getDistrictCd(), "D")
				.orElseThrow(() -> new RuntimeException("Invalid District"));

		sdpoRepository.findBySdpoCdAndRecordStatusNot(req.getSdpoCd(), "D")
				.orElseThrow(() -> new RuntimeException("Invalid SDPO"));

		policeStationRepository.findByPsCdAndRecordStatusNot(req.getPsCd(), "D")
				.orElseThrow(() -> new RuntimeException("Invalid Police Station"));

	}

	private void validateRequest(HotelRequestDTO req) {

		if (StringUtils.isBlank(req.getHotelName()))
			throw new RuntimeException("Hotel Name is required.");

		if (StringUtils.isBlank(req.getOwnerName()))
			throw new RuntimeException("Owner Name is required.");

		if (StringUtils.isBlank(req.getMobileNo()))
			throw new RuntimeException("Mobile Number is required.");

		if (req.getStateCd() == null)
			throw new RuntimeException("State is required.");

		if (req.getZoneCd() == null)
			throw new RuntimeException("Zone is required.");

		if (req.getRangeCd() == null)
			throw new RuntimeException("Range is required.");

		if (req.getDistrictCd() == null)
			throw new RuntimeException("District is required.");

		if (req.getSdpoCd() == null)
			throw new RuntimeException("SDPO is required.");

		if (req.getPsCd() == null)
			throw new RuntimeException("Police Station is required.");

	}

	@Override
	public List<Hotel> getHotels() {
		return hotelRepo.findByRecordStatus("C");
	}

	@Override
	public List<Hotel> getHotels(Integer districtCd, Integer psCd) {

		return hotelRepo.findByDistrictCdAndPsCdAndRecordStatus(districtCd, psCd, "C");
	}

	@Override
	public String deleteHotel(HotelRequestDTO request) {
		Long hotelId = request.getHotelId();
		if (hotelId == 0 || hotelId == null)
			return "Hotel Id Can't be NULL";

		try {
			// Soft Delete Hotel from DataBase
			Hotel hotel = hotelRepo.findByHotelIdAndRecordStatusNot(hotelId, Constants.D)
					.orElseThrow(() -> new NotFound("Hotel Not Found With ID: " + hotelId));
			hotel.setRecordStatus(Constants.D);
			hotel.setUpdatedBy(request.getLoginUser());
			hotelRepo.save(hotel);

			// Soft Delete Respective Hotel Users from DataBase
			List<User> hotelUsers = userRepository.findByHotelCdAndRecordStatusNot(hotel.getHotelId(), Constants.d);
			List<User> hotelUsersToDelete = new ArrayList<>();
			hotelUsers.forEach(user -> {
				user.setRecordStatus(Constants.d);
				hotelUsersToDelete.add(user);
			});
			userRepository.saveAll(hotelUsersToDelete);

		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}

		return "Hotel Deleted Successfully.";
	}

	@Override
	public String activeInactiveHotel(HotelRequestDTO request) {
		Long hotelId = request.getHotelId();
		if (hotelId == 0 || hotelId == null)
			return "Hotel Id Can't be NULL";

		String operation = request.getOperation();
		String response = "";
		try {
			// Soft Delete Hotel from DataBase
			Hotel hotel = hotelRepo.findByHotelId(hotelId)
					.orElseThrow(() -> new NotFound("Hotel Not Found With ID: " + hotelId));

			if (hotel.getRecordStatus().equals(Constants.D) && operation.equals("Activate")) {

				hotel.setRecordStatus(Constants.C);
				response = "Hotel Activated Successfully.";
			} else if (hotel.getRecordStatus().equals(Constants.C) && operation.equals("Deactivate")) {
				hotel.setRecordStatus(Constants.D);
				response = "Hotel Deactivated Successfully.";
			} else {
				throw new RuntimeException(operation + " operation is not suitable for the hotelId : " + hotelId);
			}

			hotel.setUpdatedBy(request.getLoginUser());
			hotel.setComment(request.getComment());
			hotelRepo.save(hotel);

			// Soft Delete Respective Hotel Users from DataBase
			if (operation.equals("Deactivate")) {

				List<User> hotelUsers = userRepository.findByHotelCdAndRecordStatusNot(hotel.getHotelId(), Constants.d);
				List<User> hotelUsersToDelete = new ArrayList<>();
				hotelUsers.forEach(user -> {

					user.setRecordStatus(Constants.d);
					hotelUsersToDelete.add(user);
				});
				userRepository.saveAll(hotelUsersToDelete);

			}
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
		return response;
	}

}
