package com.police.evisitor.service.impl;

import org.springframework.stereotype.Service;

import com.police.evisitor.dto.request.DataScopeDTO;
import com.police.evisitor.entity.User;

@Service
public class DataScopeService {

	public DataScopeDTO getDataScope(User loginUser) {

		DataScopeDTO.DataScopeDTOBuilder builder = DataScopeDTO.builder();

		switch (loginUser.getUserRoleId().intValue()) {

		case 1:
			builder.loginRoleId(1L);
			break;

		case 2:
			builder.stateCd(loginUser.getStateCd()).loginRoleId(2L);
			break;

		case 3:
			builder.stateCd(loginUser.getStateCd()).zoneCd(loginUser.getZoneCd()).loginRoleId(3L);
			break;

		case 4:
			builder.stateCd(loginUser.getStateCd()).zoneCd(loginUser.getZoneCd()).rangeCd(loginUser.getRangeCd())
					.loginRoleId(4L);
			break;

		case 5:
			builder.stateCd(loginUser.getStateCd()).zoneCd(loginUser.getZoneCd()).rangeCd(loginUser.getRangeCd())
					.districtCd(loginUser.getDistrictCd()).loginRoleId(5L);
			break;

		case 6:
			builder.stateCd(loginUser.getStateCd()).zoneCd(loginUser.getZoneCd()).rangeCd(loginUser.getRangeCd())
					.districtCd(loginUser.getDistrictCd()).sdpoCd(loginUser.getSdpoCd()).loginRoleId(6L);
			break;

		case 7:
			builder.stateCd(loginUser.getStateCd()).zoneCd(loginUser.getZoneCd()).rangeCd(loginUser.getRangeCd())
					.districtCd(loginUser.getDistrictCd()).sdpoCd(loginUser.getSdpoCd()).psCd(loginUser.getPsCd())
					.loginRoleId(7L);
			break;

		case 8:
			builder.stateCd(loginUser.getStateCd()).zoneCd(loginUser.getZoneCd()).rangeCd(loginUser.getRangeCd())
					.districtCd(loginUser.getDistrictCd()).sdpoCd(loginUser.getSdpoCd()).psCd(loginUser.getPsCd())
					.hotelCd(loginUser.getHotelCd()).loginRoleId(8L);
			break;

		case 9:
			builder.hotelCd(loginUser.getHotelCd()).loginRoleId(9L);
			break;

		default:
			throw new RuntimeException("Invalid User Role");
		}

		return builder.build();
	}
}