package com.police.evisitor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.police.evisitor.dto.request.DistrictDTO;
import com.police.evisitor.dto.request.PoliceStationDTO;
import com.police.evisitor.entity.MasterDocument;
import com.police.evisitor.entity.VisitReason;
import com.police.evisitor.repository.DistrictRepository;
import com.police.evisitor.repository.DocumentRepository;
import com.police.evisitor.repository.PoliceStationRepository;
import com.police.evisitor.repository.VisitReasonRepository;
import com.police.evisitor.service.MasterService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MasterServiceImpl implements MasterService {

	private final DistrictRepository districtRepository;

	private final PoliceStationRepository policeStationRepository;

	@Autowired
	private DocumentRepository documentRepo;

	@Autowired
	private VisitReasonRepository visitReasonRepo;

	@Override
	public List<DistrictDTO> getDistricts(Integer stateCd) {

		return districtRepository.findByStateCdAndRecordStatus(stateCd, 'C').stream()
				.map(d -> new DistrictDTO(d.getDistrictCd(), d.getDistrict())).toList();
	}

	@Override
	public List<PoliceStationDTO> getPoliceStations(Integer stateCd, Integer districtCd) {

		return policeStationRepository.findByStateCdAndDistrictCdAndRecordStatus(stateCd, districtCd, 'C').stream()
				.map(ps -> new PoliceStationDTO(ps.getPsCd(), ps.getPs())).toList();
	}

	@Override
	public List<MasterDocument> getDocumentList() {
		return documentRepo.findByRecordStatus("C");
	}

	@Override
	public List<VisitReason> getVisitReasonList() {
		return visitReasonRepo.findByRecordStatus("C");
	}
}