package com.police.evisitor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.police.evisitor.entity.MasterDocument;

@Repository
public interface MasterDocumentRepository extends JpaRepository<MasterDocument, Integer> {

	List<MasterDocument> findByRecordStatusNot(String status);

	Optional<MasterDocument> findByDocumentIdAndRecordStatusNot(Integer docId, String status);

}
