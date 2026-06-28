package com.police.evisitor.validation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.police.evisitor.dto.request.VisitorRequestDTO;

@Component
public class VisitorRequestValidator {

	public String validateVisitorRequest(VisitorRequestDTO request) {
		if (Objects.isNull(request)) {
			return "Request payload is missing";
		}

		String validationMessage;

		validationMessage = validateUserId(request.getUser());
		if (validationMessage != null) {
			return validationMessage;
		}

		validationMessage = validateHotelId(request.getHotel());
		if (validationMessage != null) {
			return validationMessage;
		}

		validationMessage = validateDocIds(request.getDocIds());
		if (validationMessage != null) {
			return validationMessage;
		}

		validationMessage = validateRoomNumber(request.getRoomNo());
		if (validationMessage != null) {
			return validationMessage;
		}

		validationMessage = validateCheckInDate(request.getCheckInDate());
		if (validationMessage != null) {
			return validationMessage;
		}

		validationMessage = validateCheckOutDate(request.getCheckOutDate());
		if (validationMessage != null) {
			return validationMessage;
		}

		validationMessage = validateVisitReason(request);
		if (validationMessage != null) {
			return validationMessage;
		}

		validationMessage = validateNotes(request.getNote());
		if (validationMessage != null) {
			return validationMessage;
		}

		validationMessage = validateName(request.getVisitorName());
		if (validationMessage != null) {
			return validationMessage;
		}

		validationMessage = validateMobile(request.getVisitorMobile());
		if (validationMessage != null) {
			return validationMessage;
		}

		validationMessage = validateGender(request.getVisitorGender());
		if (validationMessage != null) {
			return validationMessage;
		}

		validationMessage = validateDateOfBirth(request.getVisitorDob());
		if (validationMessage != null) {
			return validationMessage;
		}

		validationMessage = validateEmail(request.getVisitorMail());
		if (validationMessage != null) {
			return validationMessage;
		}

		validationMessage = validateDocumentNumber(request.getDocumentNo(), request.getDocumentType());
		if (validationMessage != null) {
			return validationMessage;
		}

		validationMessage = validateAddressFields(request);
		if (validationMessage != null) {
			return validationMessage;
		}

		return null;
	}

	private String validateUserId(Long userId) {
		if (Objects.isNull(userId)) {
			return "UserId is required";
		}

		return null;
	}

	private String validateHotelId(Long hotelId) {
		if (Objects.isNull(hotelId)) {
			return "HotelId is required";
		}

		return null;
	}

	private String validateDocIds(List<Long> docIds) {
		if (Objects.isNull(docIds) || docIds.isEmpty()) {
			return "DocId is required";
		}

		return null;
	}

	private String validateRoomNumber(String roomNo) {
		if (Objects.isNull(roomNo) || roomNo.trim().isEmpty()) {
			return "Room number is required";
		}

		if (!roomNo.matches("^[0-9]+$")) {
			return "Room number must be numeric";
		}

		return null;
	}

	private String validateCheckInDate(LocalDateTime checkInDate) {
		// Null Check
		if (Objects.isNull(checkInDate)) {
			return "Check-In Date is mandatory";
		}

		LocalDateTime now = LocalDateTime.now();

		// Future Date Not Allowed
		if (checkInDate.isAfter(now)) {
			return "Future Check-In Date is not allowed";
		}

		// Allow back date only till last 1 month
		LocalDateTime oneMonthBack = now.minusMonths(1);

		if (checkInDate.isBefore(oneMonthBack)) {
			return "Back date is allowed only for the last 1 month";
		}

		return null;
	}

	private String validateCheckOutDate(LocalDateTime checkOutDate) {
		// Null Check
		if (!Objects.isNull(checkOutDate)) {
			return "Check-Out Date is not required";
		}

		return null;
	}

	private String validateVisitReason(VisitorRequestDTO request) {
		if (Objects.isNull(request.getVisitReasonType())) {
			return "Visit Reason Type is required";
		}

		// Example: 11 = Other
		if (request.getVisitReasonType() == 11
				&& (request.getVisitReason() == null || request.getVisitReason().trim().isEmpty())) {
			return "Visit Reason is mandatory when Visit Reason Type is Other";
		}

		if (request.getVisitReason().length() > 100) {
			return "Visit Reason must not exceed 100 characters";
		}

		return null;
	}

	private String validateNotes(String notes) {
		if (Objects.isNull(notes) || notes.trim().isEmpty()) {
			return null;
		}

		if (notes.length() > 100) {
			return "Notes must not exceed 100 characters";
		}

		return null;
	}

	private String validateName(String name) {
		if (Objects.isNull(name) || name.trim().isEmpty()) {
			return "Name is required";
		}

		if (name.length() > 50) {
			return "Name must not exceed 50 characters";
		}

		return null;
	}

	private String validateMobile(String mobile) {
		if (Objects.isNull(mobile) || mobile.trim().isEmpty()) {
			return "Mobile number is required";
		}

		if (!mobile.matches("^[6-9][0-9]{9}$")) {
			return "Mobile number must start with 6 to 9 and contain 10 digits";
		}

		return null;
	}

	private String validateGender(String gender) {
		if (Objects.isNull(gender) || gender.trim().isEmpty()) {
			return "Gender is required";
		}

		if (gender.length() > 1) {
			return "Gender must not exceed 10 characters";
		} else {
			if (!("M".equals(gender) || "F".equals(gender) || "O".equals(gender))) {
				return "Gender is not valid";
			}
		}

		return null;
	}

	private String validateDateOfBirth(LocalDate dateOfBirth) {
		if (Objects.isNull(dateOfBirth)) {
			return null;
		}

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate.parse(dateOfBirth.toString(), formatter);
		} catch (Exception ex) {
			return "Date of birth must be in yyyy-MM-dd format";
		}

		LocalDate now = LocalDate.now();
		// Future Date Not Allowed
		if (dateOfBirth.isAfter(now)) {
			return "Future Date of birth is not allowed";
		}

		return null;
	}

	private String validateEmail(String email) {
		if (Objects.isNull(email) || email.trim().isEmpty()) {
			return null;
		}

		if (email.length() > 100) {
			return "Email must not exceed 100 characters";
		}

		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		if (!email.matches(emailRegex)) {
			return "Invalid email format";
		}
		return null;
	}

	private String validateDocumentNumber(String documentNumber, Integer docType) {

		if (Objects.isNull(docType)) {
			return "Document Type is mandatory";
		}

		if (docType != 1 && (Objects.isNull(documentNumber) || documentNumber.trim().isEmpty())) {

			return "Document Number is mandatory";
		}

		if (documentNumber.length() > 50) {
			return "Document Number must not exceed 50 characters";
		}

		Map<Integer, String> documentRegexMap = new HashMap<>();

		documentRegexMap.put(2, "^[A-Z]{3}\\d{7}$"); // Voter ID
		documentRegexMap.put(3, "^[A-Z]{5}\\d{4}[A-Z]{1}$"); // PAN
		documentRegexMap.put(4, "^[A-Z0-9]+$"); // Passport
		documentRegexMap.put(5, "^[A-Z0-9/\\-\\s]+$"); // Driving Licence
		documentRegexMap.put(6, "^[A-Z0-9]+$"); // Ration Card
		documentRegexMap.put(7, "^\\d+$"); // Birth Certificate
		documentRegexMap.put(8, "^[A-Z0-9]+$"); // Employee ID
		documentRegexMap.put(9, "^[A-Z0-9]+$"); // Service ID
		documentRegexMap.put(10, "^[A-Z0-9]+$"); // Govt ID
		documentRegexMap.put(11, "^[A-Z0-9]+$"); // College ID
		documentRegexMap.put(12, "^[A-Z0-9]+$"); // Pensioner ID
		documentRegexMap.put(13, "^[A-Z0-9/\\- ]+$"); // Other

		String regex = documentRegexMap.get(docType);

		if (Objects.nonNull(regex) && !documentNumber.toUpperCase().matches(regex)) {

			switch (docType) {

			case 2:
				return "Invalid Voter ID";

			case 3:
				return "Invalid PAN Number";

			case 4:
				return "Invalid Passport Number";

			case 5:
				return "Invalid Driving Licence Number";

			case 6:
				return "Invalid Ration Card Number";

			case 7:
				return "Invalid Birth Certificate Number";

			case 8:
				return "Invalid Employee ID";

			case 9:
				return "Invalid Service ID";

			case 10:
				return "Invalid Government ID";

			case 11:
				return "Invalid College ID";

			case 12:
				return "Invalid Pensioner ID";

			case 13:
				return "Invalid Document Number";

			default:
				return "Invalid Document Number";
			}
		}

		return null;
	}

	private String validateAddressFields(VisitorRequestDTO request) {
		String validationMessage;

		validationMessage = validateRequiredField(request.getNationalityCd(), "Nationality is required");
		if (validationMessage != null) {
			return validationMessage;
		}

		validationMessage = validateStringField(request.getNationalityName(), 50, "Nationality Name");
		if (validationMessage != null) {
			return validationMessage;
		}

		boolean isIndia = request.getNationalityCd() == 80 && "INDIA".equalsIgnoreCase(request.getNationalityName());
		if (isIndia) {
			validationMessage = validateIndianAddress(request);
			if (validationMessage != null) {
				return validationMessage;
			}
		}

		validationMessage = validateStringField(request.getVisitorAddress(), 200, "Full address");
		if (validationMessage != null) {
			return validationMessage;
		}

		if (Objects.nonNull(request.getPsName()) && request.getPsName().length() > 100) {
			return "Police Station must not exceed 100 characters";
		}

		return null;
	}

	private String validateIndianAddress(VisitorRequestDTO request) {
		String validationMessage;

		validationMessage = validateRequiredField(request.getStateCd(), "State is required");
		if (validationMessage != null) {
			return validationMessage;
		}

		validationMessage = validateStringField(request.getStateName(), 50, "State Name");
		if (validationMessage != null) {
			return validationMessage;
		}

		validationMessage = validateRequiredField(request.getDistrictCd(), "District is required");
		if (validationMessage != null) {
			return validationMessage;
		}

		validationMessage = validateStringField(request.getDistrictName(), 50, "District Name");
		if (validationMessage != null) {
			return validationMessage;
		}

		return null;
	}

	private String validateStringField(String value, int maxLength, String fieldName) {

		if (Objects.isNull(value) || value.trim().isEmpty()) {
			return fieldName + " is required";
		}

		if (value.length() > maxLength) {
			return fieldName + " must not exceed " + maxLength + " characters";
		}

		return null;
	}

	private String validateRequiredField(Object value, String message) {
		if (Objects.isNull(value)) {
			return message;
		}

		return null;
	}

}
