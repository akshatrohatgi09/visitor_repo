package com.police.evisitor.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.police.evisitor.dto.request.VisitorCheckoutRequestDTO;
import com.police.evisitor.dto.request.VisitorListRequestDTO;
import com.police.evisitor.dto.request.VisitorRequestDTO;
import com.police.evisitor.dto.response.ApiResponse;
import com.police.evisitor.repository.VisitorListProjection;

public interface VisitorService {

	String addVisitor(List<VisitorRequestDTO> request);

	Object checkoutVisitor(VisitorCheckoutRequestDTO request);

	ApiResponse<?> updateVisitor(VisitorRequestDTO request);

	Page<VisitorListProjection> getVisitorList(VisitorListRequestDTO request);
	
	Page<VisitorListProjection> getVisitorCheckOutList(VisitorListRequestDTO request);

}
