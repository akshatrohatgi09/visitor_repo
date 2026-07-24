package com.police.evisitor.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface VisitorListProjection {

	Long getId();

	Long getUser();

	Long getHotel();

	String getHotelName();

	String getRoomNo();

	LocalDateTime getCheckInDate();

	LocalDateTime getCheckOutDate();

	String getComingLocation();

	String getGoingLocation();

	Integer getVisitReasonType();

	String getVisitReason();

	String getNote();

	String getVisitorFirstName();

	String getVisitorLastName();

	String getVisitorMobile();

	String getVisitorMail();

	String getVisitorRelativeName();

	LocalDate getVisitorDob();

	String getVisitorGender();

	Integer getNationalityCd();

	String getNationalityName();

	Integer getStateCd();

	String getStateName();

	Integer getDistrictCd();

	String getDistrictName();

	Integer getPsCd();

	String getPsName();

	String getVisitorAddress();

	Integer getPincode();

	String getDocumentNo();

	Integer getDocumentType();

	String getDocIds();

	String getPhotoIds();

	String getRecordStatus();

	String getCreatedBy();

	LocalDateTime getCreatedOn();

	String getUpdatedBy();

	LocalDateTime getUpdatedOn();

}
