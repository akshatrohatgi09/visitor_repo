package com.police.evisitor.service;

import java.time.LocalDateTime;

public interface LoginProjection {

	Long getUserId();

	String getFirstName();

	String getLastName();

	String getUserLogin();

	String getUserMob();

	String getUserEmail();

	Long getRoleId();

	String getRoleName();

	Integer getNationalityCd();

	Integer getStateCd();

	String getStateName();

	Integer getZoneCd();

	String getZoneName();

	Integer getRangeCd();

	String getRangeName();

	Integer getDistrictCd();

	String getDistrict();

	Integer getSdpoCd();

	String getSdpoName();

	Integer getPsCd();

	String getPs();

	Long getHotelCd();

	String getHotelName();

	String getUserAddress();

	Integer getUserStateCd();

	Integer getUserDistrictCd();

	String getPincode();

	Boolean getLoginStatus();

	LocalDateTime getLastLoggedIn();

	String getUserPassword();
}
