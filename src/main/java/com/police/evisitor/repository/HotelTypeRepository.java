package com.police.evisitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.police.evisitor.entity.HotelTypes;

@Repository
public interface HotelTypeRepository extends JpaRepository<HotelTypes, Long> {

	List<HotelTypes> findByRecordStatusOrderByHotelTypeNameAsc(Character recordStatus);

}