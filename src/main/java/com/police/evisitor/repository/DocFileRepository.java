package com.police.evisitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.police.evisitor.entity.DocFile;

@Repository
public interface DocFileRepository extends JpaRepository<DocFile, Integer> {

	DocFile findByDocIdAndRecordStatusNot(Long docId, String recordStatus);

	List<DocFile> findByVisitorIdAndRecordStatusNot(Long visitorId, String recordStatus);

	List<DocFile> findByDocIdInAndRecordStatusNot(List<Long> docIdList, String d);

}
