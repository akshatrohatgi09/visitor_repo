package com.police.evisitor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.police.evisitor.entity.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

	List<Hotel> findByRecordStatus(String recordStatus);

	List<Hotel> findByDistrictCdAndPsCdAndRecordStatus(Integer districtCd, Integer psCd, String recordStatus);
	
	Optional<Hotel> findByHotelIdAndRecordStatusNot(Long hotelId, String recordStatus);
	
	Optional<Hotel> findByHotelId(Long hotelId);

}