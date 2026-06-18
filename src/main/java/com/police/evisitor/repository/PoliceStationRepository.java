package com.police.evisitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.police.evisitor.entity.PoliceStation;

@Repository
public interface PoliceStationRepository extends JpaRepository<PoliceStation, Long> {

	List<PoliceStation> findByStateCdAndDistrictCdAndRecordStatus(Integer stateCd, Integer districtCd,
			Character recordStatus);
}