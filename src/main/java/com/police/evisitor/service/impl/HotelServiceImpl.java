package com.police.evisitor.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.police.evisitor.dto.request.HotelRequestDTO;
import com.police.evisitor.entity.Hotel;
import com.police.evisitor.entity.User;
import com.police.evisitor.exception.NotFound;
import com.police.evisitor.repository.HotelRepository;
import com.police.evisitor.repository.UserRepository;
import com.police.evisitor.service.HotelService;
import com.police.evisitor.util.Constants;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

	private final HotelRepository hotelRepo;

	@Autowired
	private UserRepository userRepository;

	@Transactional
	@Override
	public void createHotel(HotelRequestDTO req) {

		Hotel hotel = new Hotel();

		hotel.setHotelName(req.getHotelName());
		hotel.setOwnerName(req.getOwnerName());
		hotel.setMobileNo(req.getMobileNo());
		hotel.setEmail(req.getEmail());

		hotel.setStateCd(req.getStateCd());
		hotel.setDistrictCd(req.getDistrictCd());
		hotel.setPsCd(req.getPsCd());

		hotel.setAddress(req.getAddress());

		hotel.setHotelTypeId(req.getHotelTypeId());

		hotel.setHotelPsName(req.getHotelPsName());
		hotel.setHotelDistrictName(req.getHotelDistrictName());

		hotel.setComment(req.getComment());

		hotel.setRecordStatus("C");
		hotel.setCreatedBy(req.getLoginUser());

		hotelRepo.save(hotel);
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
