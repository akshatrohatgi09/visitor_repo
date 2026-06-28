package com.police.evisitor.entity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

	List<State> findByRecordStatusNotOrderByStateNameAsc(String recordStatus);

	Optional<State> findByStateCdAndRecordStatusNot(Integer stateCd, String recordStatus);

	Optional<State> findByStateNameIgnoreCaseAndRecordStatusNot(String state, String string);

}
