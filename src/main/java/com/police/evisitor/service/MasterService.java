package com.police.evisitor.service;

import java.util.List;

import com.police.evisitor.dto.request.DistrictDTO;
import com.police.evisitor.dto.request.HotelTypeResponseDTO;
import com.police.evisitor.dto.request.PoliceStationDTO;
import com.police.evisitor.dto.response.ApiResponse;
import com.police.evisitor.entity.MasterDocument;
import com.police.evisitor.entity.Menu;
import com.police.evisitor.entity.Range;
import com.police.evisitor.entity.Role;
import com.police.evisitor.entity.Sdpo;
import com.police.evisitor.entity.State;
import com.police.evisitor.entity.VisitReason;
import com.police.evisitor.entity.Zone;

public interface MasterService {

	List<DistrictDTO> getDistricts(Integer stateCd);

	List<PoliceStationDTO> getPoliceStations(Integer stateCd, Integer districtCd);

	List<MasterDocument> getDocumentList();

	List<VisitReason> getVisitReasonList();
	
	List<State> getStates();
	
	List<Zone> getZones(Integer stateCd);

	List<Range> getRanges(Integer zoneCd);

	List<Sdpo> getSdpos(Integer districtCd);

	List<Role> getRoleList();
	
	public List<Menu> getMenuList();

	List<DistrictDTO> getAllDistricts();
	
	List<HotelTypeResponseDTO> getHotelTypes();
	
	ApiResponse<?> getCountries();

}
