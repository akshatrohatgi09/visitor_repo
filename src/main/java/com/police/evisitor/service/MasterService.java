package com.police.evisitor.service;

import java.util.List;

import com.police.evisitor.dto.request.DistrictDTO;
import com.police.evisitor.dto.request.PoliceStationDTO;

public interface MasterService {

	List<DistrictDTO> getDistricts(Integer stateCd);

	List<PoliceStationDTO> getPoliceStations(Integer stateCd, Integer districtCd);

}
