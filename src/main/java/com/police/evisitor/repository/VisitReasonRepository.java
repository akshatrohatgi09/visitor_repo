package com.police.evisitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.police.evisitor.entity.VisitReason;

@Repository
public interface VisitReasonRepository extends JpaRepository<VisitReason, Integer> {

	List<VisitReason> findByRecordStatus(String status);
}
