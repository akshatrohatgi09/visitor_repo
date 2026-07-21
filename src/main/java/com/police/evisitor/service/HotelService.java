package com.police.evisitor.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.police.evisitor.dto.request.HotelListRequestDTO;
import com.police.evisitor.dto.request.HotelRequestDTO;
import com.police.evisitor.entity.Hotel;
import com.police.evisitor.repository.HotelListProjection;

public interface HotelService {

	void createHotel(HotelRequestDTO request);

	List<Hotel> getHotels();

	List<Hotel> getHotels(Integer districtCd, Integer psCd);

	String deleteHotel(HotelRequestDTO request);

	String activeInactiveHotel(HotelRequestDTO request);

	Page<HotelListProjection> listHotels(HotelListRequestDTO request);

	void updateHotel(HotelRequestDTO request);

}
