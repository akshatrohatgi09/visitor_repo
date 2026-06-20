package com.police.evisitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.police.evisitor.entity.MasterDocument;

@Repository
public interface DocumentRepository extends JpaRepository<MasterDocument, Integer> {

	List<MasterDocument> findByRecordStatus(String status);

}
