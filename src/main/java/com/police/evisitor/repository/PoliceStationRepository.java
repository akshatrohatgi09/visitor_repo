package com.police.evisitor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.police.evisitor.entity.PoliceStation;

@Repository
public interface PoliceStationRepository extends JpaRepository<PoliceStation, Long> {

	List<PoliceStation> findByStateCdAndDistrictCdAndRecordStatusNot(Integer stateCd, Integer districtCd,
			String recordStatus);

	Optional<PoliceStation> findByPsCdAndRecordStatusNot(Integer psCd, String c);

	List<PoliceStation> findBySdpoCdAndRecordStatusNotOrderByPs(Integer sdpoCd, String recordStatus);

	Optional<PoliceStation> findByPsIgnoreCaseAndRecordStatusNot(String policeStation, String string);
}