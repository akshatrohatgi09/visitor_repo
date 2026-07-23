package com.police.evisitor.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.police.evisitor.dto.request.VisitorCheckoutRequestDTO;
import com.police.evisitor.dto.request.VisitorRequestDTO;
import com.police.evisitor.dto.response.ApiResponse;
import com.police.evisitor.entity.DocFile;
import com.police.evisitor.entity.Hotel;
import com.police.evisitor.entity.User;
import com.police.evisitor.entity.Visitor;
import com.police.evisitor.exception.NotFound;
import com.police.evisitor.repository.DocFileRepository;
import com.police.evisitor.repository.HotelRepository;
import com.police.evisitor.repository.MasterDocumentRepository;
import com.police.evisitor.repository.UserRepository;
import com.police.evisitor.repository.VisitorRepository;
import com.police.evisitor.service.VisitorService;
import com.police.evisitor.util.Constants;
import com.police.evisitor.validation.VisitorRequestValidator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VisitorServiceImpl implements VisitorService {

	@Autowired
	private VisitorRequestValidator visitorRequestValidator;

	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MasterDocumentRepository documentRepository;

	@Autowired
	private DocFileRepository fileRepository;

	@Autowired
	private VisitorRepository visitorRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public String addVisitor(List<VisitorRequestDTO> requestList, String loginId) {

		String response = "Visitor Not Save";

		Hotel hotel = null;
		User user = null;

		for (VisitorRequestDTO requestDto : requestList) {

			String validationMessage = visitorRequestValidator.validateVisitorRequest(requestDto);
			if (Objects.nonNull(validationMessage)) {
				return validationMessage;
			}

			if (Objects.nonNull(requestDto.getHotel())) {
				hotel = hotelRepository.findById(requestDto.getHotel())
						.orElseThrow(() -> new NotFound("Hotel not found for ID: " + requestDto.getHotel()));
			}

			if (Objects.nonNull(requestDto.getUser())) {
				user = userRepository.findByUserIdAndRecordStatusNot(requestDto.getUser(), Constants.d)
						.orElseThrow(() -> new NotFound("User not found for ID: " + requestDto.getUser()));
			}

			if (Objects.nonNull(requestDto.getDocumentType())) {
				documentRepository.findByDocumentIdAndRecordStatusNot(requestDto.getDocumentType(), Constants.D)
						.orElseThrow(
								() -> new NotFound("Document type not found for ID: " + requestDto.getDocumentType()));
			}
			// Creating Visitor Entity Data From Request DTO And Saving in DB

			Visitor visitor = getVisitorDataEntity(requestDto, hotel, user);

			visitor = visitorRepo.save(visitor);

			updateVisitorIdForDocuments(visitor.getId(), requestDto);

			response = "Visitor saved successfully";

		}
		return response;
	}

	private void updateVisitorIdForDocuments(Long visitorId, VisitorRequestDTO requestDto) {

		List<Long> allDocIds = new ArrayList<>();

		if (requestDto.getDocIds() != null) {
			allDocIds.addAll(requestDto.getDocIds());
		}

		if (requestDto.getPhotoIds() != null) {
			allDocIds.addAll(requestDto.getPhotoIds());
		}

		if (allDocIds.isEmpty()) {
			return;
		}

		List<DocFile> documents = fileRepository.findAllById(allDocIds);

		documents.forEach(doc -> doc.setVisitorId(visitorId));

		fileRepository.saveAll(documents);
	}

	public Visitor getVisitorDataEntity(VisitorRequestDTO requestDto, Hotel hotel, User user) {

		Visitor visitor = modelMapper.map(requestDto, Visitor.class);

		visitor.setUser(user);
		visitor.setHotel(hotel);

		if (requestDto.getDocIds() != null && !requestDto.getDocIds().isEmpty()) {
			visitor.setFilesId(requestDto.getDocIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
		}

		if (requestDto.getPhotoIds() != null && !requestDto.getPhotoIds().isEmpty()) {
			visitor.setPhotoId(requestDto.getPhotoIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
		}

		visitor.setCreatedBy(requestDto.getLoginId());
		visitor.setRecordStatus(Constants.C);

		return visitor;
	}

	@Override
	@Transactional
	public ApiResponse<?> checkoutVisitor(VisitorCheckoutRequestDTO request) {

//		try {
//			Visitor visitor = visitorRepo.findByIdAndRecordStatus(request.getVisitorId(), Constants.C)
//					.orElseThrow(() -> new NotFound("Visitor not found"));
//
//			if (visitor.getCheckOutDate() != null) {
//				throw new BadRequestException("Visitor is already checked out.");
//			}
//
//			if (request.getCheckOutDate() == null) {
//				throw new BadRequestException("Check-Out Date is required.");
//			}
//
//			if (request.getCheckOutDate().isBefore(visitor.getCheckInDate())) {
//				throw new BadRequestException("Check-Out Date cannot be earlier than Check-In Date.");
//			}
//
//			visitor.setCheckOutDate(request.getCheckOutDate());
//			visitor.setUpdatedBy(request.getLoginId());
//
//			visitorRepo.save(visitor);
//
//			return ApiResponse.builder().status("Success").message("Visitor checked out successfully.").build();
//		} catch (Exception e) {
//			return ApiResponse.builder().status("Failed").message("Visitor checkout failed.").build();
//		}
		return null;
	}

}
