package com.police.evisitor.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.police.evisitor.dto.request.VisitorRequestDTO;
import com.police.evisitor.dto.response.DocumentData;
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
import com.police.evisitor.repository.VisitorRepository;
import com.police.evisitor.service.VisitorService;
import com.police.evisitor.util.Constants;
import com.police.evisitor.util.SaveParentVisitor;
import com.police.evisitor.validation.VisitorRequestValidator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VisitorServiceImpl implements VisitorService {

	@Autowired
	private VisitorRequestValidator visitorRequestValidator;

	@Autowired
	private MasterDocumentRepository masterDocumentRepository;

	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VisitorRepository visitorRepository;

	@Autowired
	private SaveParentVisitor saveParentVisitor;

	@Autowired
	private DocFileRepository fileRepository;

	@Autowired
	private VisitorRepository visitorRepo;

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public String addVisitor(List<VisitorRequestDTO> requestList, String loginId) {

		int flag = 0;
		String response = "Visitor Not Save";

		Hotel hotel = null;
		User user = null;
		Visitor parent = null;

		for (VisitorRequestDTO requestDto : requestList) {

			// Request Validation of Add Visitor
			String validationMessage = visitorRequestValidator.validateVisitorRequest(requestDto);
			if (Objects.nonNull(validationMessage)) {
				return validationMessage;
			}

			String aadharRefNo = requestDto.getDocumentNo();

//			if (requestDto.getDocumentType() != null && requestDto.getDocumentType() == 1) {
//				aadharRefNo = getAadharRefNumber(requestDto.getDocumentNo());
//			}

			if (Objects.nonNull(requestDto.getHotel())) {
				hotel = hotelRepository.findById(requestDto.getHotel())
						.orElseThrow(() -> new NotFound("Hotel not found for ID: " + requestDto.getHotel()));
			}

			if (Objects.nonNull(requestDto.getUser())) {
				user = userRepository.findByUserIdAndRecordStatusNot(requestDto.getUser(), Constants.d)
						.orElseThrow(() -> new NotFound("User not found for ID: " + requestDto.getUser()));
			}

			// Creating Visitor Entity Data From Request DTO And Saving in DB
			Visitor visitor = new Visitor();
			getVisitorDataEntity(requestDto, hotel, user, aadharRefNo, loginId, visitor);

			switch (flag) {
			case 0 -> {
				parent = saveParentVisitor.saveParent(visitor);
				visitorRepo.flush();
				response = "Visitor saved successfully without sub-visitor.";
			}
			default -> {
				// Adding Parent to Child Visitor for Reference
				visitor.setVisitorRef(parent);
				visitorRepo.save(visitor);

				response = "Visitor saved successfully with " + flag + " sub-visitor.";
			}
			}

			flag++;
		}

		response = "Visitor Added Successfully.";
		return response;
	}

//	public List<DocumentData> getVisitorDocuments(List<Long> docIds, Integer documentType, Long visitorId,
//			String operation) {
//
//		List<DocumentData> visitorDocuments = new ArrayList<>();
//		List<DocFile> docFiles = new ArrayList<>();
//
//		docIds.forEach(docId -> {
//			DocFile docFileEle = fileRepository.findByDocIdAndRecordStatusNot(docId, Constants.D);
//			docFileEle.setVisitorId(visitorId);
//			if (documentType != null && operation.equalsIgnoreCase("update")) {
//				MasterDocument documentId = masterDocumentRepository.findByDocumentIdAndRecordStatusNot(documentType,
//						Constants.D);
//				docFileEle.setDocumentType(documentId);
//			}
//			docFiles.add(docFileEle);
//
//			DocumentData docIdObj = new DocumentData();
//			docIdObj.setFileId(Math.toIntExact(docId));
//			docIdObj.setFileName(docFileEle.getFileName());
//			visitorDocuments.add(docIdObj);
//		});
//
//		if (!docFiles.isEmpty()) {
//			try {
//				assert false;
//				fileRepository.saveAll(docFiles);
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//		}
//
//		return visitorDocuments;
//	}

	public void getVisitorDataEntity(VisitorRequestDTO requestDto, Hotel hotel, User user, String aadharRefNo,
			String uId, Visitor visitor) {

		if (Objects.nonNull(requestDto.getVisitorRef())) {

			Visitor visitorRef = visitorRepository.findById(requestDto.getVisitorRef())
					.orElseThrow(() -> new NotFound("VisitorRef not found for ID: " + requestDto.getVisitorRef()));
			visitor.setVisitorRef(visitorRef);
		}
		visitor.setRoomNo(requestDto.getRoomNo());
		visitor.setCheckInDate(requestDto.getCheckInDate());
		visitor.setCheckOutDate(requestDto.getCheckOutDate());
		visitor.setComingLocation(requestDto.getComingLocation());
		visitor.setGoingLocation(requestDto.getGoingLocation());
		visitor.setVisitReasonType(requestDto.getVisitReasonType());
		visitor.setVisitReason(requestDto.getVisitReason());
		visitor.setNote(requestDto.getNote());
		visitor.setVisitorName(requestDto.getVisitorName());
		visitor.setVisitorMobile(requestDto.getVisitorMobile());
		visitor.setVisitorMail(requestDto.getVisitorMail());
		visitor.setVisitorDob(requestDto.getVisitorDob());
		visitor.setNationalityCd(requestDto.getNationalityCd());
		visitor.setNationalityName(requestDto.getNationalityName());
		visitor.setVisitorGender(requestDto.getVisitorGender());
		visitor.setStateCd(requestDto.getStateCd());
		visitor.setStateName(requestDto.getStateName());
		visitor.setDistrictCd(requestDto.getDistrictCd());
		visitor.setDistrictName(requestDto.getDistrictName());
		visitor.setPsCd(requestDto.getPsCd());
		visitor.setPsName(requestDto.getPsName());
		visitor.setVisitorAddress(requestDto.getVisitorAddress());
		visitor.setDocumentNo(aadharRefNo);
		visitor.setUser(user);
		visitor.setHotel(hotel);
		visitor.setCreatedBy(uId);
		visitor.setUpdatedBy(uId);
		visitor.setRecordStatus(Constants.C);
	}

}
