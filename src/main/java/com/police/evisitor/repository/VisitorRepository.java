package com.police.evisitor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.police.evisitor.entity.Visitor;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Integer> {

	//Optional<Visitor> findByIdAndRecordStatus(Long id, String recordStatus);

}
