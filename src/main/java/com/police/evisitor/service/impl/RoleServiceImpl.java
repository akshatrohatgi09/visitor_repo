package com.police.evisitor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.police.evisitor.entity.Role;
import com.police.evisitor.repository.RoleListRepository;
import com.police.evisitor.service.RoleService;
import com.police.evisitor.util.Constants;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleListRepository roleListRepository;

	@Override
	public List<Role> getRoleList() {
		List<Role> roleData = roleListRepository.findByRecordStatusNot(Constants.D);
		return roleData;
	}


}
