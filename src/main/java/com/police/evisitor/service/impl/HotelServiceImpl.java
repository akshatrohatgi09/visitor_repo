package com.police.evisitor.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.police.evisitor.dto.request.HotelListRequestDTO;
import com.police.evisitor.dto.request.HotelRequestDTO;
import com.police.evisitor.entity.Hotel;
import com.police.evisitor.entity.StateRepository;
import com.police.evisitor.entity.User;
import com.police.evisitor.exception.NotFound;
import com.police.evisitor.repository.DistrictRepository;
import com.police.evisitor.repository.HotelListProjection;
import com.police.evisitor.repository.HotelRepository;
import com.police.evisitor.repository.HotelTypeRepository;
import com.police.evisitor.repository.PoliceStationRepository;
import com.police.evisitor.repository.RangeRepository;
import com.police.evisitor.repository.SdpoRepository;
import com.police.evisitor.repository.UserRepository;
import com.police.evisitor.repository.ZoneRepository;
import com.police.evisitor.service.HotelService;
import com.police.evisitor.util.Constants;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

	private final HotelRepository hotelRepo;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private ZoneRepository zoneRepository;

	@Autowired
	private RangeRepository rangeRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private SdpoRepository sdpoRepository;

	@Autowired
	private PoliceStationRepository policeStationRepository;

	@Autowired
	private HotelTypeRepository hotelTypeRepository;

	@Transactional
	@Override
	public void createHotel(HotelRequestDTO req) {

		try {

			log.info("Create Hotel Request : {}", req);

			validateRequest(req);

			validateMasterData(req);

			validateDuplicate(req, null);

			Integer hotelTypeId = req.getHotelTypeId().intValue();
			Hotel hotel = Hotel.builder()

					.hotelName(req.getHotelName().trim()).ownerName(req.getOwnerName().trim())
					.mobileNo(req.getMobileNo().trim())
					.email(req.getEmail() == null ? null : req.getEmail().trim().toLowerCase())

					.stateCd(req.getStateCd()).zoneCd(req.getZoneCd()).rangeCd(req.getRangeCd())
					.districtCd(req.getDistrictCd()).sdpoCd(req.getSdpoCd()).psCd(req.getPsCd())

					.hotelTypeId(hotelTypeId)

					.noOfRooms(req.getNoOfRooms()).noOfFloors(req.getNoOfFloors())

					.licenseNumber(req.getLicenseNumber()).licenseValidity(req.getLicenseValidity())

					.address(req.getAddress()).pincode(req.getPincode())

					.latitude(req.getLatitude()).longitude(req.getLongitude())

					.beatNumber(req.getBeatNumber())

					.ownerAddress(req.getOwnerAddress()).ownerStateCd(req.getOwnerStateCd())
					.ownerDistrictCd(req.getOwnerDistrictCd()).ownerPincode(req.getOwnerPincode())

					.managerName(req.getManagerName())
					.managerEmail(req.getManagerEmail() == null ? null : req.getManagerEmail().trim().toLowerCase())
					.managerPhone(req.getManagerPhone()).managerAddress(req.getManagerAddress())
					.managerStateCd(req.getManagerStateCd()).managerDistrictCd(req.getManagerDistrictCd())
					.managerPincode(req.getManagerPincode())

					.comment(req.getComment())

					.recordStatus("C")

					.createdBy(req.getLoginUser())

					.build();

			hotelRepo.save(hotel);

			log.info("Hotel Created Successfully : {}", hotel.getHotelName());

		} catch (Exception e) {

			log.error("Error while creating Hotel : {}", req, e);

			throw new RuntimeException("Error while creating Hotel");

		}

	}

	private void validateDuplicate(HotelRequestDTO req, Long hotelId) {

		Hotel hotel = hotelRepo.findByMobileNoAndRecordStatusNot(req.getMobileNo(), Constants.D);

		if (hotel != null) {

			if (hotelId == null || !hotel.getHotelId().equals(hotelId)) {

				throw new RuntimeException("Mobile Number already exists.");

			}
		}

		if (StringUtils.isNotBlank(req.getEmail())) {

			hotel = hotelRepo.findByEmailIgnoreCaseAndRecordStatusNot(req.getEmail(), Constants.D);

			if (hotel != null) {

				if (hotelId == null || !hotel.getHotelId().equals(hotelId)) {

					throw new RuntimeException("Email already exists.");

				}
			}
		}
	}

	private void validateMasterData(HotelRequestDTO req) {

		stateRepository.findByStateCdAndRecordStatusNot(req.getStateCd(), "D")
				.orElseThrow(() -> new RuntimeException("Invalid State"));

		zoneRepository.findByZoneCdAndRecordStatusNot(req.getZoneCd(), "D")
				.orElseThrow(() -> new RuntimeException("Invalid Zone"));

		rangeRepository.findByRangeCdAndRecordStatusNot(req.getRangeCd(), "D")
				.orElseThrow(() -> new RuntimeException("Invalid Range"));

		districtRepository.findByDistrictCdAndRecordStatusNot(req.getDistrictCd(), "D")
				.orElseThrow(() -> new RuntimeException("Invalid District"));

		sdpoRepository.findBySdpoCdAndRecordStatusNot(req.getSdpoCd(), "D")
				.orElseThrow(() -> new RuntimeException("Invalid SDPO"));

		policeStationRepository.findByPsCdAndRecordStatusNot(req.getPsCd(), "D")
				.orElseThrow(() -> new RuntimeException("Invalid Police Station"));

		stateRepository.findByStateCdAndRecordStatusNot(req.getOwnerStateCd(), "D")
				.orElseThrow(() -> new RuntimeException("Invalid Owner State"));

		districtRepository.findByDistrictCdAndRecordStatusNot(req.getOwnerDistrictCd(), "D")
				.orElseThrow(() -> new RuntimeException("Invalid Owner District"));

		stateRepository.findByStateCdAndRecordStatusNot(req.getManagerStateCd(), "D")
				.orElseThrow(() -> new RuntimeException("Invalid Manager State"));

		districtRepository.findByDistrictCdAndRecordStatusNot(req.getManagerDistrictCd(), "D")
				.orElseThrow(() -> new RuntimeException("Invalid Manager District"));

		hotelTypeRepository.findByHotelTypeIdAndRecordStatusNot(req.getHotelTypeId(), 'D')
				.orElseThrow(() -> new RuntimeException("Invalid Hotel Type"));

	}

	private void validateRequest(HotelRequestDTO req) {

		if (StringUtils.isBlank(req.getHotelName()))
			throw new RuntimeException("Hotel Name is required.");

		if (StringUtils.isBlank(req.getOwnerName()))
			throw new RuntimeException("Owner Name is required.");

		if (StringUtils.isBlank(req.getMobileNo()))
			throw new RuntimeException("Mobile Number is required.");

		if (req.getStateCd() == null)
			throw new RuntimeException("State is required.");

		if (req.getZoneCd() == null)
			throw new RuntimeException("Zone is required.");

		if (req.getRangeCd() == null)
			throw new RuntimeException("Range is required.");

		if (req.getDistrictCd() == null)
			throw new RuntimeException("District is required.");

		if (req.getSdpoCd() == null)
			throw new RuntimeException("SDPO is required.");

		if (req.getPsCd() == null)
			throw new RuntimeException("Police Station is required.");

		if (req.getHotelTypeId() == null)
			throw new RuntimeException("Hotel Type is required.");

		if (req.getNoOfRooms() == null)
			throw new RuntimeException("Number of Rooms is required.");

		if (req.getNoOfFloors() == null)
			throw new RuntimeException("Number of Floors is required.");

		if (StringUtils.isBlank(req.getLicenseNumber()))
			throw new RuntimeException("License Number is required.");

		if (req.getLicenseValidity() == null)
			throw new RuntimeException("License Validity is required.");

		if (StringUtils.isBlank(req.getAddress()))
			throw new RuntimeException("Hotel Address is required.");

		if (StringUtils.isBlank(req.getPincode()))
			throw new RuntimeException("Pincode is required.");

		if (req.getOwnerStateCd() == null)
			throw new RuntimeException("Owner State is required.");

		if (req.getOwnerDistrictCd() == null)
			throw new RuntimeException("Owner District is required.");

		if (StringUtils.isBlank(req.getManagerName()))
			throw new RuntimeException("Manager Name is required.");

		if (StringUtils.isBlank(req.getManagerPhone()))
			throw new RuntimeException("Manager Phone is required.");

	}

	@Override
	public List<Hotel> getHotels() {
		return hotelRepo.findByRecordStatus("C");
	}

	@Override
	public List<Hotel> getHotels(Integer districtCd, Integer psCd) {

		return hotelRepo.findByDistrictCdAndPsCdAndRecordStatus(districtCd, psCd, "C");
	}

	@Override
	public String deleteHotel(HotelRequestDTO request) {
		Long hotelId = request.getHotelId();
		if (hotelId == 0 || hotelId == null)
			return "Hotel Id Can't be NULL";

		try {

			Hotel hotel = hotelRepo.findByHotelIdAndRecordStatusNot(hotelId, Constants.D)
					.orElseThrow(() -> new NotFound("Hotel Not Found With ID: " + hotelId));
			hotel.setRecordStatus(Constants.D);
			hotel.setUpdatedBy(request.getLoginUser());
			hotelRepo.save(hotel);

			List<User> hotelUsers = userRepository.findByHotelCdAndRecordStatusNot(hotel.getHotelId(), Constants.d);
			List<User> hotelUsersToDelete = new ArrayList<>();
			hotelUsers.forEach(user -> {
				user.setRecordStatus(Constants.d);
				hotelUsersToDelete.add(user);
			});
			userRepository.saveAll(hotelUsersToDelete);

		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}

		return "Hotel Deleted Successfully.";
	}

	@Transactional
	@Override
	public String activeInactiveHotel(HotelRequestDTO request) {
		Long hotelId = request.getHotelId();
		if (hotelId == 0 || hotelId == null)
			return "Hotel Id Can't be NULL";

		String operation = request.getOperation();
		String response = "";
		try {

			Hotel hotel = hotelRepo.findByHotelId(hotelId)
					.orElseThrow(() -> new NotFound("Hotel Not Found With ID: " + hotelId));

			if (hotel.getRecordStatus().equals(Constants.D) && operation.equals("Activate")) {

				hotel.setRecordStatus(Constants.C);
				response = "Hotel Activated Successfully.";
			} else if (hotel.getRecordStatus().equals(Constants.C) && operation.equals("Deactivate")) {
				hotel.setRecordStatus(Constants.D);
				response = "Hotel Deactivated Successfully.";
			} else {
				throw new RuntimeException(operation + " operation is not suitable for the hotelId : " + hotelId);
			}

			hotel.setUpdatedBy(request.getLoginUser());
			hotel.setComment(request.getComment());
			hotelRepo.save(hotel);

			if (operation.equals("Deactivate")) {

				List<User> hotelUsers = userRepository.findByHotelCdAndRecordStatusNot(hotel.getHotelId(), Constants.d);
				List<User> hotelUsersToDelete = new ArrayList<>();
				hotelUsers.forEach(user -> {

					user.setRecordStatus(Constants.d);
					hotelUsersToDelete.add(user);
				});
				userRepository.saveAll(hotelUsersToDelete);

			}
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
		return response;
	}

	@Override
	@Transactional()
	public Page<HotelListProjection> listHotels(HotelListRequestDTO request) {

		User loginUser = userRepository.findByUserLoginIgnoreCaseAndRecordStatusNot(request.getLoginId(), 'D');

		if (loginUser == null) {
			throw new RuntimeException("Invalid Login User.");
		}

		Integer stateCd = request.getStateCd();
		Integer zoneCd = request.getZoneCd();
		Integer rangeCd = request.getRangeCd();
		Integer districtCd = request.getDistrictCd();
		Integer sdpoCd = request.getSdpoCd();
		Integer psCd = request.getPsCd();
		Long hotelCd = request.getHotelCd();

		switch (loginUser.getUserRoleId().intValue()) {

		case 1:
			// Admin
			break;

		case 2:
			stateCd = loginUser.getStateCd();
			break;

		case 3:
			stateCd = loginUser.getStateCd();
			zoneCd = loginUser.getZoneCd();
			break;

		case 4:
			stateCd = loginUser.getStateCd();
			zoneCd = loginUser.getZoneCd();
			rangeCd = loginUser.getRangeCd();
			break;

		case 5:
			stateCd = loginUser.getStateCd();
			zoneCd = loginUser.getZoneCd();
			rangeCd = loginUser.getRangeCd();
			districtCd = loginUser.getDistrictCd();
			break;

		case 6:
			stateCd = loginUser.getStateCd();
			zoneCd = loginUser.getZoneCd();
			rangeCd = loginUser.getRangeCd();
			districtCd = loginUser.getDistrictCd();
			sdpoCd = loginUser.getSdpoCd();
			break;

		case 7:
			stateCd = loginUser.getStateCd();
			zoneCd = loginUser.getZoneCd();
			rangeCd = loginUser.getRangeCd();
			districtCd = loginUser.getDistrictCd();
			sdpoCd = loginUser.getSdpoCd();
			psCd = loginUser.getPsCd();
			break;

		case 8:
		case 9:
			hotelCd = loginUser.getHotelCd();
			break;

		default:
			throw new RuntimeException("Invalid User Role.");
		}

		Pageable pageable = PageRequest.of(request.getPageNo(), request.getPageSize());

		return hotelRepo.listHotels(stateCd, zoneCd, rangeCd, districtCd, sdpoCd, psCd, hotelCd, request.getHotelName(),
				request.getOwnerName(), request.getMobileNo(), request.getFromDate(), request.getToDate(), pageable);
	}

	@Transactional
	@Override
	public void updateHotel(HotelRequestDTO req) {

		try {

			log.info("Update Hotel Request : {}", req);

			if (req.getHotelId() == null) {
				throw new RuntimeException("Hotel Id is required.");
			}

			Hotel hotel = hotelRepo.findById(req.getHotelId())
					.orElseThrow(() -> new RuntimeException("Hotel not found with Id : " + req.getHotelId()));

			validateRequest(req);

			validateMasterData(req);

			validateDuplicate(req, req.getHotelId());

			hotel.setHotelName(req.getHotelName().trim());
			hotel.setOwnerName(req.getOwnerName().trim());
			hotel.setMobileNo(req.getMobileNo().trim());

			hotel.setEmail(StringUtils.isBlank(req.getEmail()) ? null : req.getEmail().trim().toLowerCase());

			hotel.setStateCd(req.getStateCd());
			hotel.setZoneCd(req.getZoneCd());
			hotel.setRangeCd(req.getRangeCd());
			hotel.setDistrictCd(req.getDistrictCd());
			hotel.setSdpoCd(req.getSdpoCd());
			hotel.setPsCd(req.getPsCd());

			Integer hotelTypeId = req.getHotelTypeId().intValue();
			hotel.setHotelTypeId(hotelTypeId);

			hotel.setNoOfRooms(req.getNoOfRooms());
			hotel.setNoOfFloors(req.getNoOfFloors());

			hotel.setLicenseNumber(req.getLicenseNumber());
			hotel.setLicenseValidity(req.getLicenseValidity());

			hotel.setAddress(req.getAddress());
			hotel.setPincode(req.getPincode());

			hotel.setLatitude(req.getLatitude());
			hotel.setLongitude(req.getLongitude());

			hotel.setBeatNumber(req.getBeatNumber());

			hotel.setOwnerAddress(req.getOwnerAddress());
			hotel.setOwnerStateCd(req.getOwnerStateCd());
			hotel.setOwnerDistrictCd(req.getOwnerDistrictCd());
			hotel.setOwnerPincode(req.getOwnerPincode());

			hotel.setManagerName(req.getManagerName());

			hotel.setManagerEmail(
					StringUtils.isBlank(req.getManagerEmail()) ? null : req.getManagerEmail().trim().toLowerCase());

			hotel.setManagerPhone(req.getManagerPhone());
			hotel.setManagerAddress(req.getManagerAddress());

			hotel.setManagerStateCd(req.getManagerStateCd());
			hotel.setManagerDistrictCd(req.getManagerDistrictCd());
			hotel.setManagerPincode(req.getManagerPincode());

			hotel.setComment(req.getComment());

			hotel.setUpdatedBy(req.getLoginUser());

			hotelRepo.save(hotel);

			log.info("Hotel Updated Successfully : {}", hotel.getHotelName());

		} catch (Exception e) {

			log.error("Error while updating Hotel : {}", req, e);

			throw new RuntimeException(e.getMessage());

		}

	}

}
