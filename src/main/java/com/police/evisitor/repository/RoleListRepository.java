package com.police.evisitor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.police.evisitor.entity.Role;

@Repository
public interface RoleListRepository extends JpaRepository<Role, Long> {
	
	List<Role> findByRecordStatusNot(String status);

	Optional<Role> findByRoleIdAndRecordStatusNot(Long Id, String recordStatus);

	Optional<Role> findByRoleNameIgnoreCaseAndRecordStatusNot(String role, String string);
}