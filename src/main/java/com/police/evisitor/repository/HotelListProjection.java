package com.police.evisitor.repository;

import java.sql.Timestamp;
import java.time.LocalDate;

public interface HotelListProjection {

	Long getHotelId();

	String getHotelName();

	String getOwnerName();

	String getMobileNo();

	String getEmail();

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

	Long getHotelTypeId();

	String getHotelTypeName();

	Integer getNoOfRooms();

	Integer getNoOfFloors();

	String getLicenseNumber();

	LocalDate getLicenseValidity();

	String getAddress();

	String getPincode();

	String getLatitude();

	String getLongitude();

	String getBeatNumber();

	String getOwnerAddress();

	Integer getOwnerStateCd();

	String getOwnerStateName();

	Integer getOwnerDistrictCd();

	String getOwnerDistrictName();

	String getOwnerPincode();

	String getManagerName();

	String getManagerEmail();

	String getManagerPhone();

	String getManagerAddress();

	Integer getManagerStateCd();

	String getManagerStateName();

	Integer getManagerDistrictCd();

	String getManagerDistrictName();

	String getManagerPincode();

	String getComment();

	String getRecordStatus();

	String getCreatedBy();

	Timestamp getCreatedOn();

}
