package com.police.evisitor.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.police.evisitor.dto.request.DataScopeDTO;
import com.police.evisitor.dto.request.VisitorCheckoutRequestDTO;
import com.police.evisitor.dto.request.VisitorListRequestDTO;
import com.police.evisitor.dto.request.VisitorRequestDTO;
import com.police.evisitor.dto.response.ApiResponse;
import com.police.evisitor.entity.DocFile;
import com.police.evisitor.entity.Hotel;
import com.police.evisitor.entity.MasterDocument;
import com.police.evisitor.entity.User;
import com.police.evisitor.entity.Visitor;
import com.police.evisitor.exception.NotFound;
import com.police.evisitor.repository.DocFileRepository;
import com.police.evisitor.repository.HotelRepository;
import com.police.evisitor.repository.MasterDocumentRepository;
import com.police.evisitor.repository.UserRepository;
import com.police.evisitor.repository.VisitorListProjection;
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

	@Autowired
	private DataScopeService dataScopeService;

	@Autowired
	private VisitorRepository visitorRepository;

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public String addVisitor(List<VisitorRequestDTO> requestList) {

		String response = null;

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

		String errorMessage = null;
		try {
			Visitor visitor = visitorRepo.findByIdAndRecordStatus(request.getVisitorId(), Constants.C);

			if (visitor == null) {
				errorMessage = "Visitor not exist";
			}

			if (visitor.getCheckOutDate() != null) {
				errorMessage = "Visitor is already checked out.";
			}

			if (request.getCheckOutDate() == null) {
				errorMessage = "Check-Out Date is required.";
			}

			if (request.getCheckOutDate().isBefore(visitor.getCheckInDate())) {
				errorMessage = "Check-Out Date cannot be earlier than Check-In Date.";
			}

			if (StringUtils.isBlank(errorMessage)) {
				visitor.setCheckOutDate(request.getCheckOutDate());
				visitor.setUpdatedBy(request.getLoginId());

				visitorRepo.save(visitor);

				return ApiResponse.builder().status("Success").message("Visitor checked out successfully.").build();
			} else {
				return ApiResponse.builder().status("Failed").message(errorMessage).build();
			}

		} catch (Exception e) {
			return ApiResponse.builder().status("Failed").message(errorMessage).build();
		}
	}

	@Override
	@Transactional
	public ApiResponse<?> updateVisitor(VisitorRequestDTO requestDto) {

		String validationMessage = visitorRequestValidator.validateVisitorRequest(requestDto);

		if (Objects.nonNull(validationMessage)) {
			return ApiResponse.builder().status("Failed").message("Visitor Updated Failed.").data(validationMessage)
					.build();
		}

		if (Objects.isNull(requestDto.getId())) {
			return ApiResponse.builder().status("Failed").message("Visitor Updated Failed.")
					.data("Visitor Id is required.").build();
		}

		Optional<Visitor> visitorOptional = visitorRepo.findById(requestDto.getId());

		if (visitorOptional.isEmpty()) {
			return ApiResponse.builder().status("Failed").message("Visitor Updated Failed.")
					.data("Visitor not found for ID : " + requestDto.getId()).build();
		}

		Visitor visitor = visitorOptional.get();

		Hotel hotel = null;
		User user = null;

		if (Objects.nonNull(requestDto.getHotel())) {

			Optional<Hotel> hotelOptional = hotelRepository.findById(requestDto.getHotel());

			if (hotelOptional.isEmpty()) {
				return ApiResponse.builder().status("Failed").message("Visitor Updated Failed.")
						.data("Hotel not found for ID : " + requestDto.getHotel()).build();
			}

			hotel = hotelOptional.get();
		}

		if (Objects.nonNull(requestDto.getUser())) {

			Optional<User> userOptional = userRepository.findByUserIdAndRecordStatusNot(requestDto.getUser(),
					Constants.d);

			if (userOptional.isEmpty()) {
				return ApiResponse.builder().status("Failed").message("Visitor Updated Failed.")
						.data("User not found for ID : " + requestDto.getUser()).build();
			}

			user = userOptional.get();
		}

		if (Objects.nonNull(requestDto.getDocumentType())) {

			Optional<MasterDocument> documentOptional = documentRepository
					.findByDocumentIdAndRecordStatusNot(requestDto.getDocumentType(), Constants.D);

			if (documentOptional.isEmpty()) {
				return ApiResponse.builder().status("Failed").message("Visitor Updated Failed.")
						.data("Document Type not found for ID : " + requestDto.getDocumentType()).build();
			}
		}

		mapVisitorData(visitor, requestDto, hotel, user);

		visitor.setUpdatedBy(requestDto.getLoginId());
		visitor.setUpdatedOn(LocalDateTime.now());

		visitorRepo.save(visitor);

		updateVisitorIdForDocuments(visitor.getId(), requestDto);

		return ApiResponse.builder().status("Success").message("Visitor Updated Successfully.")
				.data("Visitor updated successfully.").build();
	}

	private void mapVisitorData(Visitor visitor, VisitorRequestDTO dto, Hotel hotel, User user) {

		visitor.setHotel(hotel);
		visitor.setUser(user);

		visitor.setRoomNo(dto.getRoomNo());
		visitor.setCheckInDate(dto.getCheckInDate());
		visitor.setCheckOutDate(dto.getCheckOutDate());

		visitor.setComingLocation(dto.getComingLocation());
		visitor.setGoingLocation(dto.getGoingLocation());

		visitor.setVisitReasonType(dto.getVisitReasonType());
		visitor.setVisitReason(dto.getVisitReason());
		visitor.setNote(dto.getNote());

		visitor.setVisitorFirstName(dto.getVisitorFirstName());
		visitor.setVisitorLastName(dto.getVisitorLastName());

		visitor.setVisitorMobile(dto.getVisitorMobile());
		visitor.setVisitorMail(dto.getVisitorMail());

		visitor.setVisitorRelativeName(dto.getVisitorRelativeName());

		visitor.setVisitorDob(dto.getVisitorDob());
		visitor.setVisitorGender(dto.getVisitorGender());

		visitor.setNationalityCd(dto.getNationalityCd());
		visitor.setNationalityName(dto.getNationalityName());

		visitor.setStateCd(dto.getStateCd());
		visitor.setStateName(dto.getStateName());

		visitor.setDistrictCd(dto.getDistrictCd());
		visitor.setDistrictName(dto.getDistrictName());

		visitor.setPsCd(dto.getPsCd());
		visitor.setPsName(dto.getPsName());

		visitor.setVisitorAddress(dto.getVisitorAddress());

		visitor.setPincode(dto.getPincode());

		visitor.setDocumentNo(dto.getDocumentNo());
		visitor.setDocumentType(dto.getDocumentType());

		if (dto.getDocIds() != null) {
			visitor.setFilesId(dto.getDocIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
		}

		if (dto.getPhotoIds() != null) {
			visitor.setPhotoId(dto.getPhotoIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
		}
	}

	@Override
	public Page<VisitorListProjection> getVisitorList(VisitorListRequestDTO request) {

		User loginUser = userRepository.findByUserLoginIgnoreCaseAndRecordStatusNot(request.getLoginId(), Constants.d);

		if (Objects.isNull(loginUser)) {
			throw new RuntimeException("Invalid Login User.");
		}

		DataScopeDTO scope = dataScopeService.getDataScope(loginUser);

		Integer stateCd = scope.getStateCd();
		Integer zoneCd = scope.getZoneCd();
		Integer rangeCd = scope.getRangeCd();
		Integer districtCd = scope.getDistrictCd();
		Integer sdpoCd = scope.getSdpoCd();
		Integer psCd = scope.getPsCd();
		Long hotelCd = scope.getHotelCd();

		if (loginUser.getUserRoleId() == 1) {

			if (request.getStateCd() != null)
				stateCd = request.getStateCd();

			if (request.getZoneCd() != null)
				zoneCd = request.getZoneCd();

			if (request.getRangeCd() != null)
				rangeCd = request.getRangeCd();

			if (request.getDistrictCd() != null)
				districtCd = request.getDistrictCd();

			if (request.getSdpoCd() != null)
				sdpoCd = request.getSdpoCd();

			if (request.getPsCd() != null)
				psCd = request.getPsCd();

			if (request.getHotelCd() != null)
				hotelCd = request.getHotelCd();
		}

		Pageable pageable = PageRequest.of(request.getPageNo(), request.getPageSize());

		return visitorRepository.getVisitorList(stateCd, zoneCd, rangeCd, districtCd, sdpoCd, psCd, hotelCd,
				request.getVisitorName(), request.getVisitorMobile(), request.getVisitorStatus(), request.getFromDate(),
				request.getToDate(), pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<VisitorListProjection> getVisitorCheckOutList(VisitorListRequestDTO request) {

		User loginUser = userRepository.findByUserLoginIgnoreCaseAndRecordStatusNot(request.getLoginId(), Constants.d);

		if (Objects.isNull(loginUser)) {
			throw new RuntimeException("Invalid Login User.");
		}

		DataScopeDTO scope = dataScopeService.getDataScope(loginUser);

		Integer stateCd = scope.getStateCd();
		Integer zoneCd = scope.getZoneCd();
		Integer rangeCd = scope.getRangeCd();
		Integer districtCd = scope.getDistrictCd();
		Integer sdpoCd = scope.getSdpoCd();
		Integer psCd = scope.getPsCd();
		Long hotelCd = scope.getHotelCd();

		if (loginUser.getUserRoleId().intValue() == 1) {

			stateCd = request.getStateCd();
			zoneCd = request.getZoneCd();
			rangeCd = request.getRangeCd();
			districtCd = request.getDistrictCd();
			sdpoCd = request.getSdpoCd();
			psCd = request.getPsCd();
			hotelCd = request.getHotelCd();
		}

		Pageable pageable = PageRequest.of(request.getPageNo(), request.getPageSize());

		return visitorRepo.getCheckOutVisitorList(stateCd, zoneCd, rangeCd, districtCd, sdpoCd, psCd, hotelCd,
				request.getVisitorName(), request.getVisitorMobile(), request.getFromDate(), request.getToDate(),
				pageable);
	}
}
