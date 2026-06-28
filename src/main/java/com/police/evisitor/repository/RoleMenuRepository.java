package com.police.evisitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.police.evisitor.entity.Role;
import com.police.evisitor.entity.RoleMenuList;

@Repository
public interface RoleMenuRepository extends JpaRepository<RoleMenuList, Integer> {

	public List<RoleMenuList> findByRecordStatusNot(String recordStatus);

	public List<RoleMenuList> findByRoleIdAndRecordStatusNot(Role roleId, String recordStatus);
}