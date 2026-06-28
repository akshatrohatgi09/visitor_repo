package com.police.evisitor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.police.evisitor.entity.Sdpo;

@Repository
public interface SdpoRepository extends JpaRepository<Sdpo, Long> {

    Optional<Sdpo> findBySdpoCdAndRecordStatusNot(Integer sdpoCd, String recordStatus);

    List<Sdpo> findByDistrictCdAndRecordStatusNotOrderBySdpoName(Integer districtCd, String recordStatus);

	Optional<Sdpo> findBySdpoNameIgnoreCaseAndRecordStatusNot(String sdpo, String string);

}
