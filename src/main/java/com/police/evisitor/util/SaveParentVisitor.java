package com.police.evisitor.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.police.evisitor.entity.Visitor;
import com.police.evisitor.repository.VisitorRepository;

@Transactional
@Component
public class SaveParentVisitor {
	
	@Autowired
	VisitorRepository visitorRepo;

	@Transactional(propagation = Propagation.REQUIRED)
	public Visitor saveParent(Visitor visitor) {
		return visitorRepo.saveAndFlush(visitor);
	}

}