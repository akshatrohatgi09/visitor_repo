package com.police.evisitor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.police.evisitor.entity.Menu;
import com.police.evisitor.repository.MenuRepository;
import com.police.evisitor.service.MenuService;
import com.police.evisitor.util.Constants;

@Service
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuRepository menuRepository;

	@Override
	public List<Menu> getMenuList() {
		List<Menu> menuData = menuRepository.findByRecordStatusNot(Constants.D);
		menuData.removeIf(menu -> menu.getMenuId() != null && menu.getMenuId() == 6);
		return menuData;
	}

}
