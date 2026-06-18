package com.police.evisitor.service;

import java.util.List;

import com.police.evisitor.dto.request.HotelRequestDTO;
import com.police.evisitor.entity.Hotel;

public interface HotelService {

	void createHotel(HotelRequestDTO request);

	List<Hotel> getHotels();

	List<Hotel> getHotels(Integer districtCd, Integer psCd);

}
