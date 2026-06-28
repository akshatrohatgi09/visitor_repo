package com.police.evisitor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.police.evisitor.entity.Zone;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {

	Optional<Zone> findByZoneCdAndRecordStatusNot(Integer zoneCd, String recordStatus);

	List<Zone> findByStateCdAndRecordStatusNotOrderByZoneName(Integer stateCd, String recordStatus);

	Optional<Zone> findByZoneNameIgnoreCaseAndRecordStatusNot(String zone, String string);

}