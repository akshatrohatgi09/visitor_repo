package com.police.evisitor.service.impl;

import org.springframework.stereotype.Service;

import com.police.evisitor.dto.request.DataScopeDTO;
import com.police.evisitor.entity.User;

@Service
public class DataScopeService {

	public DataScopeDTO getDataScope(User loginUser) {

		DataScopeDTO.DataScopeDTOBuilder builder = DataScopeDTO.builder();

		switch (loginUser.getUserRoleId().intValue()) {

		case 1: // Admin
			builder.loginRoleId(1L);
			break;

		case 2: // State
			builder.stateCd(loginUser.getStateCd()).loginRoleId(2L);
			break;

		case 3: // Zone
			builder.stateCd(loginUser.getStateCd()).zoneCd(loginUser.getZoneCd()).loginRoleId(3L);
			break;

		case 4: // Range
			builder.stateCd(loginUser.getStateCd()).zoneCd(loginUser.getZoneCd()).rangeCd(loginUser.getRangeCd())
					.loginRoleId(4L);
			break;

		case 5: // District
			builder.stateCd(loginUser.getStateCd()).zoneCd(loginUser.getZoneCd()).rangeCd(loginUser.getRangeCd())
					.districtCd(loginUser.getDistrictCd()).loginRoleId(5L);
			break;

		case 6: // SDPO
			builder.stateCd(loginUser.getStateCd()).zoneCd(loginUser.getZoneCd()).rangeCd(loginUser.getRangeCd())
					.districtCd(loginUser.getDistrictCd()).sdpoCd(loginUser.getSdpoCd()).loginRoleId(6L);
			break;

		case 7: // Police Station
			builder.stateCd(loginUser.getStateCd()).zoneCd(loginUser.getZoneCd()).rangeCd(loginUser.getRangeCd())
					.districtCd(loginUser.getDistrictCd()).sdpoCd(loginUser.getSdpoCd()).psCd(loginUser.getPsCd())
					.loginRoleId(7L);
			break;

		case 8: // Hotel Manager
			builder.stateCd(loginUser.getStateCd()).zoneCd(loginUser.getZoneCd()).rangeCd(loginUser.getRangeCd())
					.districtCd(loginUser.getDistrictCd()).sdpoCd(loginUser.getSdpoCd()).psCd(loginUser.getPsCd())
					.hotelCd(loginUser.getHotelCd()).loginRoleId(8L);
			break;

		case 9: // Hotel User
			builder.hotelCd(loginUser.getHotelCd()).loginRoleId(9L);
			break;

		default:
			throw new RuntimeException("Invalid User Role");
		}

		return builder.build();
	}
}