package com.police.evisitor.service;

import java.util.List;

import com.police.evisitor.dto.request.DistrictDTO;
import com.police.evisitor.dto.request.PoliceStationDTO;
import com.police.evisitor.entity.MasterDocument;
import com.police.evisitor.entity.VisitReason;

public interface MasterService {

	List<DistrictDTO> getDistricts(Integer stateCd);

	List<PoliceStationDTO> getPoliceStations(Integer stateCd, Integer districtCd);

	List<MasterDocument> getDocumentList();

	List<VisitReason> getVisitReasonList();

}
