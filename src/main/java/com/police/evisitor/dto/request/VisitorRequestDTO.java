package com.police.evisitor.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class VisitorRequestDTO {

	private Long id;

	private Long user;

	private Long hotel;

	private String roomNo;

	private LocalDateTime checkInDate;

	private LocalDateTime checkOutDate;

	private String comingLocation;

	private String goingLocation;

	private Integer visitReasonType;

	private String visitReason;

	private String note;

	private String visitorFirstName;

	private String visitorLastName;

	private String visitorMobile;

	private String visitorMail;

	private String visitorRelativeName;

	private LocalDate visitorDob;

	private String visitorGender;

	private Integer nationalityCd;

	private String nationalityName;

	private Integer stateCd;

	private String stateName;

	private Integer districtCd;

	private String districtName;

	private Integer psCd;

	private String psName;

	private String visitorAddress;

	private Integer pincode;

	private String documentNo;

	private Integer documentType;

	private String loginId;

	private List<Long> docIds;

	private List<Long> photoIds;
}