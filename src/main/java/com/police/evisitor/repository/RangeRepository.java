package com.police.evisitor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.police.evisitor.entity.Range;

@Repository
public interface RangeRepository extends JpaRepository<Range, Long> {

    Optional<Range> findByRangeCdAndRecordStatusNot(Integer rangeCd, String recordStatus);

    List<Range> findByZoneCdAndRecordStatusNotOrderByRangeName(Integer zoneCd, String recordStatus);

	Optional<Range> findByRangeNameIgnoreCaseAndRecordStatusNot(String range, String string);

}
