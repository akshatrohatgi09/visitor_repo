package com.police.evisitor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.police.evisitor.entity.District;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {

	List<District> findByStateCdAndRecordStatusNot(Integer stateCd, String recordStatus);

	Optional<District> findByDistrictCdAndRecordStatusNot(Integer districtCd, String recordStatus);

	List<District> findByRangeCdAndRecordStatusNotOrderByDistrict(Integer rangeCd, String recordStatus);

	Optional<District> findByDistrictIgnoreCaseAndRecordStatusNot(String district, String string);
}