package com.police.evisitor.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.police.evisitor.dto.request.HotelRequestDTO;
import com.police.evisitor.entity.Hotel;
import com.police.evisitor.repository.HotelRepository;
import com.police.evisitor.service.HotelService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

	private final HotelRepository hotelRepo;

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
		hotel.setCreatedBy("SYSTEM");
		hotel.setUpdatedBy("SYSTEM");

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

}
