package com.police.evisitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.police.evisitor.entity.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
	
	List<Menu> findByRecordStatusNot(String status);
}