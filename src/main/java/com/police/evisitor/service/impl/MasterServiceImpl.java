package com.police.evisitor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.police.evisitor.dto.request.DistrictDTO;
import com.police.evisitor.dto.request.PoliceStationDTO;
import com.police.evisitor.entity.MasterDocument;
import com.police.evisitor.entity.Menu;
import com.police.evisitor.entity.Range;
import com.police.evisitor.entity.Role;
import com.police.evisitor.entity.Sdpo;
import com.police.evisitor.entity.State;
import com.police.evisitor.entity.StateRepository;
import com.police.evisitor.entity.VisitReason;
import com.police.evisitor.entity.Zone;
import com.police.evisitor.repository.DistrictRepository;
import com.police.evisitor.repository.MasterDocumentRepository;
import com.police.evisitor.repository.MenuRepository;
import com.police.evisitor.repository.PoliceStationRepository;
import com.police.evisitor.repository.RangeRepository;
import com.police.evisitor.repository.RoleListRepository;
import com.police.evisitor.repository.SdpoRepository;
import com.police.evisitor.repository.VisitReasonRepository;
import com.police.evisitor.repository.ZoneRepository;
import com.police.evisitor.service.MasterService;
import com.police.evisitor.util.Constants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MasterServiceImpl implements MasterService {

	private final DistrictRepository districtRepository;

	private final PoliceStationRepository policeStationRepository;

	@Autowired
	private MasterDocumentRepository documentRepo;

	@Autowired
	private VisitReasonRepository visitReasonRepo;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private ZoneRepository zoneRepository;

	@Autowired
	private RangeRepository rangeRepository;

	@Autowired
	private SdpoRepository sdpoRepository;

	@Autowired
	private RoleListRepository roleListRepo;

	@Autowired
	private MenuRepository menuRepository;

	@Override
	public List<DistrictDTO> getDistricts(Integer stateCd) {

		return districtRepository.findByStateCdAndRecordStatusNot(stateCd, "D").stream()
				.map(d -> new DistrictDTO(d.getDistrictCd(), d.getDistrict())).toList();
	}

	@Override
	public List<PoliceStationDTO> getPoliceStations(Integer stateCd, Integer districtCd) {

		return policeStationRepository.findByStateCdAndDistrictCdAndRecordStatusNot(stateCd, districtCd, "D").stream()
				.map(ps -> new PoliceStationDTO(ps.getPsCd(), ps.getPs())).toList();
	}

	@Override
	public List<MasterDocument> getDocumentList() {
		return documentRepo.findByRecordStatusNot("D");
	}

	@Override
	public List<VisitReason> getVisitReasonList() {
		return visitReasonRepo.findByRecordStatusNot("D");
	}

	@Override
	public List<State> getStates() {
		return stateRepository.findByRecordStatusNotOrderByStateNameAsc("D");
	}

	@Override
	public List<Zone> getZones(Integer stateCd) {
		return zoneRepository.findByStateCdAndRecordStatusNotOrderByZoneName(stateCd, "D");
	}

	@Override
	public List<Range> getRanges(Integer zoneCd) {
		return rangeRepository.findByZoneCdAndRecordStatusNotOrderByRangeName(zoneCd, "D");
	}

	@Override
	public List<Sdpo> getSdpos(Integer districtCd) {

		return sdpoRepository.findByDistrictCdAndRecordStatusNotOrderBySdpoName(districtCd, "D");
	}

	@Override
	public List<Role> getRoleList() {
		List<Role> roles = roleListRepo.findByRecordStatusNotOrderByRoleIdAsc(Constants.D);
		return roles;
	}

	@Override
	public List<Menu> getMenuList() {
		List<Menu> menuData = menuRepository.findByRecordStatusNot(Constants.D);
		menuData.removeIf(menu -> menu.getMenuId() != null && menu.getMenuId() == 6);
		return menuData;
	}

	@Override
	public List<DistrictDTO> getAllDistricts() {
		return districtRepository.findByRecordStatusNot("D").stream()
				.map(d -> new DistrictDTO(d.getDistrictCd(), d.getDistrict())).toList();
	}
}