package com.police.evisitor.repository;

import java.time.LocalDateTime;

public interface UserListProjection {

	Long getUserId();

	Long getRoleId();

	String getRoleName();

	String getFirstName();

	String getLastName();

	String getUserLogin();

	String getUserMob();

	String getUserEmail();

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

	Long getHotelCd();

	String getHotelName();

	Integer getUserStateCd();

	String getUserStateName();

	Integer getUserDistrictCd();

	String getUserDistrictName();

	String getUserAddress();

	String getPincode();

	String getComments();

	Character getRecordStatus();

	Boolean getLoginStatus();

	String getCreatedBy();

	LocalDateTime getCreatedOn();

}
