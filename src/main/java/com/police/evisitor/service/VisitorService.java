package com.police.evisitor.service;

import java.util.List;

import com.police.evisitor.dto.request.VisitorCheckoutRequestDTO;
import com.police.evisitor.dto.request.VisitorRequestDTO;

public interface VisitorService {

	String addVisitor(List<VisitorRequestDTO> request, String loginId);

	Object checkoutVisitor(VisitorCheckoutRequestDTO request);

}
