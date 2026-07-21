package com.police.evisitor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.police.evisitor.entity.HotelTypes;
import com.police.evisitor.entity.State;

@Repository
public interface HotelTypeRepository extends JpaRepository<HotelTypes, Long> {

	List<HotelTypes> findByRecordStatusOrderByHotelTypeNameAsc(Character recordStatus);

	Optional<State> findByHotelTypeIdAndRecordStatusNot(Integer hotelTypeId, String string);

}