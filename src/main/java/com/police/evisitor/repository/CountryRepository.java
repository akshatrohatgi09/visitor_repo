package com.police.evisitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.police.evisitor.entity.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {

	List<Country> findByRecordStatusOrderByCountryNameAsc(String recordStatus);

}