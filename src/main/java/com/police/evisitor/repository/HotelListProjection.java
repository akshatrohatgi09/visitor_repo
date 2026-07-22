package com.police.evisitor.repository;

import java.time.LocalDateTime;

public interface HotelListProjection {

	Long getHotelId();

	String getHotelName();

	String getOwnerName();

	String getMobileNo();

	String getEmail();

	String getAddress();

	Integer getStateCd();

	String getStateName();

	Integer getZoneCd();

	String getZoneName();

	Integer getRangeCd();

	String getRangeName();

	Integer getDistrictCd();

	String getDistrictName();

	Integer getSdpoCd();

	String getSdpoName();

	Integer getPsCd();

	String getPoliceStationName();

	String getRecordStatus();

	String getCreatedBy();

	LocalDateTime getCreatedOn();

}
