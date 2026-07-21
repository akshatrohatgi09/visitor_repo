package com.police.evisitor.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.police.evisitor.dto.request.UserListRequestDTO;
import com.police.evisitor.dto.request.UserRequestDTO;
import com.police.evisitor.dto.response.BulkUploadResponse;
import com.police.evisitor.dto.response.ExcelUserDTO;
import com.police.evisitor.dto.response.FailedUserDTO;
import com.police.evisitor.entity.District;
import com.police.evisitor.entity.Hotel;
import com.police.evisitor.entity.PoliceStation;
import com.police.evisitor.entity.Range;
import com.police.evisitor.entity.Role;
import com.police.evisitor.entity.Sdpo;
import com.police.evisitor.entity.State;
import com.police.evisitor.entity.StateRepository;
import com.police.evisitor.entity.User;
import com.police.evisitor.entity.Zone;
import com.police.evisitor.exception.IOException;
import com.police.evisitor.exception.NotFound;
import com.police.evisitor.repository.DistrictRepository;
import com.police.evisitor.repository.HotelRepository;
import com.police.evisitor.repository.PoliceStationRepository;
import com.police.evisitor.repository.RangeRepository;
import com.police.evisitor.repository.RoleListRepository;
import com.police.evisitor.repository.SdpoRepository;
import com.police.evisitor.repository.UserListProjection;
import com.police.evisitor.repository.UserRepository;
import com.police.evisitor.repository.ZoneRepository;
import com.police.evisitor.service.UserService;
import com.police.evisitor.util.Constants;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private HotelRepository hotelRepo;

	@Autowired
	private RoleListRepository roleRepository;

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

	private static final List<String> EXPECTED_HEADERS = Arrays.asList("Fist Name", "Last Name", "Login", "Password",
			"Mobile", "Email", "Role", "State", "Zone", "Range", "District", "SDPO", "Police Station", "Hotel",
			"Address", "Pincode", "Comment");

	@Transactional
	@Override
	public void saveUser(UserRequestDTO reqDto) {

		try {

			log.info("Create User Request : {}", reqDto);

			// Validate Role
			Role role = roleRepository.findById(reqDto.getRoleId())
					.orElseThrow(() -> new RuntimeException("Role Not Found : " + reqDto.getRoleId()));

			// Validate hierarchy based on role
			validateRoleHierarchy(role.getRoleId(), reqDto);

			// Validate master data
			validateMasterData(reqDto);

			User user = modelMapper.map(reqDto, User.class);

			user.setUserLogin(reqDto.getUserLogin().trim().toLowerCase());

			// Duplicate Validation
			isUserAlreadyExist(user);

			user.setUserRoleId(reqDto.getRoleId());

			user.setStateCd(reqDto.getStateCd());
			user.setZoneCd(reqDto.getZoneCd());
			user.setRangeCd(reqDto.getRangeCd());
			user.setDistrictCd(reqDto.getDistrictCd());
			user.setSdpoCd(reqDto.getSdpoCd());
			user.setPsCd(reqDto.getPsCd());
			user.setHotelCd(reqDto.getHotelId());
			user.setLoginStatus(Boolean.FALSE);
			user.setComment(reqDto.getComment());
			user.setRecordStatus('C');
			user.setCreatedBy(reqDto.getLoginId());

			userRepo.save(user);
			log.info("User Created Successfully : {}", user.getUserLogin());

		} catch (Exception e) {
			log.error("Error while creating user : {}", reqDto, e);
			throw new RuntimeException(e.getMessage());

		}
	}

	private void validateMasterData(UserRequestDTO dto) {

		State state = null;
		Zone zone = null;
		Range range = null;
		District district = null;
		Sdpo sdpo = null;
		PoliceStation ps = null;

		if (dto.getStateCd() != null) {
			state = stateRepository.findByStateCdAndRecordStatusNot(dto.getStateCd(), "D")
					.orElseThrow(() -> new RuntimeException("Invalid State"));
		}

		if (dto.getZoneCd() != null) {
			zone = zoneRepository.findByZoneCdAndRecordStatusNot(dto.getZoneCd(), "D")
					.orElseThrow(() -> new RuntimeException("Invalid Zone"));

			if (!zone.getStateCd().equals(dto.getStateCd())) {
				throw new RuntimeException("Zone does not belong to selected State.");
			}
		}

		if (dto.getRangeCd() != null) {
			range = rangeRepository.findByRangeCdAndRecordStatusNot(dto.getRangeCd(), "D")
					.orElseThrow(() -> new RuntimeException("Invalid Range"));

			if (!range.getZoneCd().equals(dto.getZoneCd())) {
				throw new RuntimeException("Range does not belong to selected Zone.");
			}

		}

		if (dto.getDistrictCd() != null) {
			district = districtRepository.findByDistrictCdAndRecordStatusNot(dto.getDistrictCd(), "D")
					.orElseThrow(() -> new RuntimeException("Invalid District"));

			if (!district.getRangeCd().equals(dto.getRangeCd())) {
				throw new RuntimeException("District does not belong to selected Range.");
			}

		}

		if (dto.getSdpoCd() != null) {
			sdpo = sdpoRepository.findBySdpoCdAndRecordStatusNot(dto.getSdpoCd(), "D")
					.orElseThrow(() -> new RuntimeException("Invalid SDPO"));

			if (!sdpo.getDistrictCd().equals(dto.getDistrictCd())) {
				throw new RuntimeException("SDPO does not belong to selected District.");
			}

		}

		if (dto.getPsCd() != null) {
			ps = policeStationRepository.findByPsCdAndRecordStatusNot(dto.getPsCd(), "D")
					.orElseThrow(() -> new RuntimeException("Invalid Police Station"));

			if (!ps.getSdpoCd().equals(dto.getSdpoCd())) {
				throw new RuntimeException("Police Station does not belong to selected SDPO.");
			}

		}

		if (dto.getHotelId() != null) {
			Hotel hotel = hotelRepo.findByHotelIdAndRecordStatusNot(dto.getHotelId(), Constants.D)
					.orElseThrow(() -> new NotFound("Hotel Not Found With ID: " + dto.getHotelId()));

			if (!hotel.getPsCd().equals(dto.getPsCd())) {
				throw new RuntimeException("Hotel does not belong to selected Police Station.");
			}
		}

	}

	private void validateRoleHierarchy(Long roleId, UserRequestDTO dto) {
		switch (roleId.intValue()) {

		case 1:
			break;
		case 2:
			if (dto.getStateCd() == null)
				throw new RuntimeException("State is required.");
			break;
		case 3:
			if (dto.getStateCd() == null || dto.getZoneCd() == null)
				throw new RuntimeException("State and Zone are required.");
			break;
		case 4:
			if (dto.getStateCd() == null || dto.getZoneCd() == null || dto.getRangeCd() == null)
				throw new RuntimeException("State, Zone and Range are required.");
			break;
		case 5:
			if (dto.getStateCd() == null || dto.getZoneCd() == null || dto.getRangeCd() == null
					|| dto.getDistrictCd() == null)
				throw new RuntimeException("State, Zone, Range and District are required.");
			break;
		case 6:
			if (dto.getStateCd() == null || dto.getZoneCd() == null || dto.getRangeCd() == null
					|| dto.getDistrictCd() == null || dto.getSdpoCd() == null)
				throw new RuntimeException("State, Zone, Range, District and SDPO are required.");
			break;
		case 7:
			if (dto.getStateCd() == null || dto.getZoneCd() == null || dto.getRangeCd() == null
					|| dto.getDistrictCd() == null || dto.getSdpoCd() == null || dto.getPsCd() == null)
				throw new RuntimeException("Complete Police hierarchy is required.");
			break;

		case 8:
			if (dto.getStateCd() == null || dto.getZoneCd() == null || dto.getRangeCd() == null
					|| dto.getDistrictCd() == null || dto.getSdpoCd() == null || dto.getPsCd() == null
					|| dto.getHotelId() == null) {
				throw new RuntimeException(
						"State, Zone, Range, District, SDPO, Police Station and Hotel are required.");
			}
			break;
		case 9:
			if (dto.getStateCd() == null || dto.getZoneCd() == null || dto.getRangeCd() == null
					|| dto.getDistrictCd() == null || dto.getSdpoCd() == null || dto.getPsCd() == null
					|| dto.getHotelId() == null) {
				throw new RuntimeException(
						"State, Zone, Range, District, SDPO, Police Station and Hotel are required.");
			}
			break;

		default:
			throw new RuntimeException("Invalid Role");
		}

	}

	private void isUserAlreadyExist(User user) {

		User loginCheck = userRepo.findByUserLoginIgnoreCaseAndRecordStatusNot(user.getUserLogin(), 'D');

		if (loginCheck != null) {
			throw new RuntimeException("User Login Already Exists : " + user.getUserLogin());
		}

		User emailCheck = userRepo.findByUserEmailIgnoreCaseAndRecordStatusNot(user.getUserEmail(), 'D');

		if (emailCheck != null) {
			throw new RuntimeException("Email Already Exists : " + user.getUserEmail());
		}

		User mobileCheck = userRepo.findByUserMobAndRecordStatusNot(user.getUserMob(), 'D');

		if (mobileCheck != null) {
			throw new RuntimeException("Mobile Already Exists : " + user.getUserMob());
		}
	}

	@Override
	@Transactional
	public void deleteUser(UserRequestDTO userRequest) {
		Long userId = userRequest.getUserId();
		User userData = null;
		userData = userRepo.findByUserIdAndRecordStatusNot(userId, Constants.d)
				.orElseThrow(() -> new NotFound("User Not Found With ID: " + userId));
		userData.setRecordStatus(Constants.d);
		userData.setUpdatedBy(userRequest.getLoginId());

		userRepo.saveAndFlush(userData);
	}

	@Override
	@Transactional
	public String activeInactiveUser(UserRequestDTO userRequest) {

		Long userId = userRequest.getUserId();
		String operation = userRequest.getOperation();
		Long roleId = userRequest.getRoleId();
		String response = "";

		User userData = userRepo.findByUserId(userId)
				.orElseThrow(() -> new NotFound("User Not Found With ID: " + userId));

		if (userData.getHotelCd() != null && userData.getHotelCd() > 0) {
			Long hotelCd = userData.getHotelCd();

			Optional<Hotel> byHotelId = hotelRepo.findByHotelId(hotelCd);

			Hotel hotelData = byHotelId.orElseThrow(() -> new NotFound("Hotel Not Found : " + hotelCd));

			if (Constants.d == (hotelData.getRecordStatus())) {
				throw new NotFound("Associated Hotel is Inactive for hotelId : " + hotelCd);
			}
		}

		if (userData.getRecordStatus() == Constants.d && operation.equals("Activate")) {
			isUserAlreadyExist(userData);

			userData.setRecordStatus(Constants.c);
			response = "User Activated Successfully.";
		} else if (userData.getRecordStatus() == Constants.c && operation.equals("Deactivate")) {

			userData.setRecordStatus(Constants.d);
			response = "User Deactivated Successfully.";

		} else {
			throw new RuntimeException(operation + " operation is not suitable for the userId : " + userId);
		}

		userData.setUpdatedBy(userRequest.getLoginId());
		userData.setComment(userRequest.getComment());

		return response;
	}

	@Transactional
	@Override
	public void updateUser(UserRequestDTO reqDto) {

		try {

			log.info("Update User Request : {}", reqDto);

			if (reqDto.getUserId() == null) {
				throw new RuntimeException("User Id is required.");
			}

			User user = userRepo.findById(reqDto.getUserId())
					.orElseThrow(() -> new RuntimeException("User Not Found : " + reqDto.getUserId()));

			Role role = roleRepository.findById(reqDto.getRoleId())
					.orElseThrow(() -> new RuntimeException("Role Not Found : " + reqDto.getRoleId()));

			validateRoleHierarchy(role.getRoleId(), reqDto);

			validateMasterData(reqDto);

			validateDuplicateForUpdate(reqDto);

			modelMapper.map(reqDto, user);

			user.setUserRoleId(reqDto.getRoleId());

			user.setUserLogin(reqDto.getUserLogin().trim().toLowerCase());

			user.setHotelCd(reqDto.getHotelId());

			user.setUpdatedBy(reqDto.getLoginId());

			user.setUpdatedOn(LocalDateTime.now());

			userRepo.save(user);

			log.info("User Updated Successfully : {}", user.getUserLogin());

		} catch (Exception e) {

			log.error("Error while updating user.", e);

			throw new RuntimeException(e.getMessage());

		}
	}

	private void validateDuplicateForUpdate(UserRequestDTO dto) {

		User login = userRepo.findByUserLoginIgnoreCaseAndRecordStatusNot(dto.getUserLogin(), 'D');

		if (login != null && !login.getUserId().equals(dto.getUserId())) {

			throw new RuntimeException("User Login Already Exists : " + dto.getUserLogin());

		}

		User email = userRepo.findByUserEmailIgnoreCaseAndRecordStatusNot(dto.getUserEmail(), 'D');

		if (email != null && !email.getUserId().equals(dto.getUserId())) {

			throw new RuntimeException("Email Already Exists : " + dto.getUserEmail());

		}

		User mobile = userRepo.findByUserMobAndRecordStatusNot(dto.getUserMob(), 'D');

		if (mobile != null && !mobile.getUserId().equals(dto.getUserId())) {

			throw new RuntimeException("Mobile Already Exists : " + dto.getUserMob());

		}

	}

	@Override
	public User getUser(UserRequestDTO request) {
		Long userId = request.getUserId();
		User userData = null;
		userData = userRepo.findByUserIdAndRecordStatusNot(userId, Constants.d)
				.orElseThrow(() -> new NotFound("User Not Found With ID: " + userId));

		return userData;
	}

	@Override
	@Transactional
	public void updateLoginStatus(Long userId, Boolean loginStatus) {

		User userData = null;
		userData = userRepo.findByUserIdAndRecordStatusNot(userId, Constants.d)
				.orElseThrow(() -> new NotFound("User Not Found With ID: " + userId));
		if (loginStatus) {
			userData.setLoginStatus(Constants.True);
		} else {
			userData.setLoginStatus(Constants.False);
		}
	}

	@Override
	@Transactional
	public BulkUploadResponse bulkUploadUsers(MultipartFile file, String loginId) {

		log.info("Bulk User Upload Started By : {}", loginId);

		List<FailedUserDTO> failedUsers = new ArrayList<>();

		int totalRecords = 0;
		int successCount = 0;
		int failedCount = 0;

		try {

			if (file == null || file.isEmpty()) {
				throw new RuntimeException("Please upload a valid excel file.");
			}

			validateExcelHeader(file);

			List<ExcelUserDTO> excelUsers = readExcel(file);

			if (excelUsers == null || excelUsers.isEmpty()) {
				throw new RuntimeException("No records found in excel.");
			}

			totalRecords = excelUsers.size();

			for (ExcelUserDTO excelUser : excelUsers) {

				try {
					UserRequestDTO request = convertNamesToIds(excelUser);
					request.setLoginId(loginId);
					saveUser(request);
					successCount++;
					log.info("User Created Successfully : {}", request.getUserLogin());
				} catch (Exception ex) {
					failedCount++;
					log.error("Row {} Failed : {}", excelUser.getRowNo(), ex.getMessage());
					failedUsers.add(FailedUserDTO.builder().rowNo(excelUser.getRowNo())
							.firstName(excelUser.getFirstName()).lastName(excelUser.getLastName())
							.login(excelUser.getLogin()).mobile(excelUser.getMobile()).email(excelUser.getEmail())
							.role(excelUser.getRole()).state(excelUser.getState()).zone(excelUser.getZone())
							.range(excelUser.getRange()).district(excelUser.getDistrict()).sdpo(excelUser.getSdpo())
							.policeStation(excelUser.getPoliceStation()).hotel(excelUser.getHotel())
							.address(excelUser.getAddress()).comment(excelUser.getComment()).reason(ex.getMessage())
							.build());
				}
			}

			log.info("Bulk Upload Completed. Total : {}, Success : {}, Failed : {}", totalRecords, successCount,
					failedCount);
			return BulkUploadResponse.builder().totalRecords(totalRecords).successCount(successCount)
					.failedCount(failedCount).failedUsers(failedUsers).build();
		} catch (Exception e) {
			log.error("Bulk Upload Failed.", e);
			throw new RuntimeException("Bulk Upload Failed : " + e.getMessage(), e);
		}

	}

	private void validateExcelHeader(MultipartFile file) {

		log.info("Validating uploaded excel headers.");

		try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {

			Sheet sheet = workbook.getSheetAt(0);

			if (sheet == null) {
				throw new RuntimeException("Excel sheet not found.");
			}

			Row headerRow = sheet.getRow(0);

			if (headerRow == null) {
				throw new RuntimeException("Header row is missing.");
			}

			for (int i = 0; i < EXPECTED_HEADERS.size(); i++) {

				Cell cell = headerRow.getCell(i);

				String actualHeader = (cell == null) ? "" : cell.getStringCellValue().trim();

				String expectedHeader = EXPECTED_HEADERS.get(i);

				if (!expectedHeader.equalsIgnoreCase(actualHeader)) {

					String errorMsg = String.format("Invalid header at column %d. Expected '%s' but found '%s'.", i + 1,
							expectedHeader, actualHeader);

					log.error(errorMsg);

					throw new RuntimeException(errorMsg);
				}
			}

			log.info("Excel header validation completed successfully.");

		} catch (EncryptedDocumentException e) {

			log.error("Uploaded excel file is encrypted.", e);
			throw new RuntimeException("Encrypted excel files are not supported.");

		} catch (IOException e) {

			log.error("Unable to read uploaded excel file.", e);
			throw new RuntimeException("Unable to read uploaded excel file.");

		} catch (Exception e) {

			log.error("Error while validating excel header.", e);
			throw new RuntimeException(e.getMessage(), e);

		}

	}

	private List<ExcelUserDTO> readExcel(MultipartFile file) {

		List<ExcelUserDTO> users = new ArrayList<>();
		try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {

				Row row = sheet.getRow(i);
				if (row == null || isRowEmpty(row)) {
					continue;
				}

				ExcelUserDTO dto = new ExcelUserDTO();

				dto.setRowNo(i + 1);
				dto.setFirstName(getCellValue(row.getCell(0)));
				dto.setLastName(getCellValue(row.getCell(1)));
				dto.setLogin(getCellValue(row.getCell(2)));
				dto.setPassword(getCellValue(row.getCell(3)));
				dto.setMobile(getCellValue(row.getCell(4)));
				dto.setEmail(getCellValue(row.getCell(5)));
				dto.setRole(getCellValue(row.getCell(6)));
				dto.setState(getCellValue(row.getCell(7)));
				dto.setZone(getCellValue(row.getCell(8)));
				dto.setRange(getCellValue(row.getCell(9)));
				dto.setDistrict(getCellValue(row.getCell(10)));
				dto.setSdpo(getCellValue(row.getCell(11)));
				dto.setPoliceStation(getCellValue(row.getCell(12)));
				dto.setHotel(getCellValue(row.getCell(13)));
				dto.setAddress(getCellValue(row.getCell(14)));
				dto.setComment(getCellValue(row.getCell(15)));
				users.add(dto);
			}

		} catch (Exception e) {
			log.error("Error while reading excel.", e);
			throw new RuntimeException("Invalid Excel File.");
		}
		return users;
	}

	private String getCellValue(Cell cell) {

		if (cell == null) {
			return "";
		}

		switch (cell.getCellType()) {

		case STRING:
			return cell.getStringCellValue().trim();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getLocalDateTimeCellValue().toString();
			}
			return BigDecimal.valueOf(cell.getNumericCellValue()).stripTrailingZeros().toPlainString();

		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case FORMULA:
			return cell.getCellFormula();
		case BLANK:
			return "";
		default:
			return "";

		}
	}

	private boolean isRowEmpty(Row row) {
		if (row == null) {
			return true;
		}

		for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
			Cell cell = row.getCell(cellNum);
			if (cell != null && cell.getCellType() != CellType.BLANK && StringUtils.isNotBlank(getCellValue(cell))) {
				return false;
			}
		}
		return true;
	}

	private UserRequestDTO convertNamesToIds(ExcelUserDTO excel) {

		UserRequestDTO dto = new UserRequestDTO();

		dto.setFirstName(excel.getFirstName());
		dto.setLastName(excel.getLastName());
		dto.setUserLogin(excel.getLogin());
		dto.setUserPassword(excel.getPassword());
		dto.setUserMob(excel.getMobile());
		dto.setUserEmail(excel.getEmail());
		dto.setUserAddress(excel.getAddress());
		dto.setPincode(excel.getPincode());
		dto.setComment(excel.getComment());

		dto.setNationalityCd(80);

		Role role = roleRepository.findByRoleNameIgnoreCaseAndRecordStatusNot(excel.getRole(), "D")
				.orElseThrow(() -> new RuntimeException("Invalid Role : " + excel.getRole()));

		dto.setRoleId(role.getRoleId());

		if (StringUtils.isNotBlank(excel.getState())) {
			State state = stateRepository.findByStateNameIgnoreCaseAndRecordStatusNot(excel.getState(), "D")
					.orElseThrow(() -> new RuntimeException("Invalid State : " + excel.getState()));
			dto.setStateCd(state.getStateCd());
		}

		if (StringUtils.isNotBlank(excel.getZone())) {
			Zone zone = zoneRepository.findByZoneNameIgnoreCaseAndRecordStatusNot(excel.getZone(), "D")
					.orElseThrow(() -> new RuntimeException("Invalid Zone : " + excel.getZone()));
			dto.setZoneCd(zone.getZoneCd());
			if (!zone.getStateCd().equals(dto.getStateCd())) {
				throw new RuntimeException("Zone does not belong to selected State.");
			}
		}

		if (StringUtils.isNotBlank(excel.getRange())) {
			Range range = rangeRepository.findByRangeNameIgnoreCaseAndRecordStatusNot(excel.getRange(), "D")
					.orElseThrow(() -> new RuntimeException("Invalid Range : " + excel.getRange()));
			dto.setRangeCd(range.getRangeCd());
			if (!range.getZoneCd().equals(dto.getZoneCd())) {
				throw new RuntimeException("Range does not belong to selected Zone.");
			}
		}

		if (StringUtils.isNotBlank(excel.getDistrict())) {
			District district = districtRepository.findByDistrictIgnoreCaseAndRecordStatusNot(excel.getDistrict(), "D")
					.orElseThrow(() -> new RuntimeException("Invalid District : " + excel.getDistrict()));
			dto.setDistrictCd(district.getDistrictCd());
			if (!district.getRangeCd().equals(dto.getRangeCd())) {
				throw new RuntimeException("District does not belong to selected Range.");
			}
		}

		if (StringUtils.isNotBlank(excel.getSdpo())) {

			Sdpo sdpo = sdpoRepository.findBySdpoNameIgnoreCaseAndRecordStatusNot(excel.getSdpo(), "D")
					.orElseThrow(() -> new RuntimeException("Invalid SDPO : " + excel.getSdpo()));

			dto.setSdpoCd(sdpo.getSdpoCd());

			if (!sdpo.getDistrictCd().equals(dto.getDistrictCd())) {
				throw new RuntimeException("SDPO does not belong to selected District.");
			}
		}

		if (StringUtils.isNotBlank(excel.getPoliceStation())) {

			PoliceStation ps = policeStationRepository
					.findByPsIgnoreCaseAndRecordStatusNot(excel.getPoliceStation(), "D")
					.orElseThrow(() -> new RuntimeException("Invalid Police Station : " + excel.getPoliceStation()));

			dto.setPsCd(ps.getPsCd());

			if (!ps.getSdpoCd().equals(dto.getSdpoCd())) {
				throw new RuntimeException("Police Station does not belong to selected SDPO.");
			}
		}

		if (StringUtils.isNotBlank(excel.getHotel())) {

			Hotel hotel = hotelRepo.findByHotelNameIgnoreCaseAndRecordStatusNot(excel.getHotel(), "D")
					.orElseThrow(() -> new RuntimeException("Invalid Hotel : " + excel.getHotel()));

			dto.setHotelId(hotel.getHotelId());

			if (!hotel.getPsCd().equals(dto.getPsCd())) {
				throw new RuntimeException("Hotel does not belong to selected Police Station.");
			}
		}

		return dto;
	}

	@Override
	@Transactional()
	public Page<UserListProjection> listUsers(UserListRequestDTO request) {

		User loginUser = userRepo.findByUserLoginIgnoreCaseAndRecordStatusNot(request.getLoginId(), 'D');

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
		Long roleId = request.getRoleId();

		switch (loginUser.getUserRoleId().intValue()) {

		case 1:
			// Admin - no restriction
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
			stateCd = loginUser.getStateCd();
			zoneCd = loginUser.getZoneCd();
			rangeCd = loginUser.getRangeCd();
			districtCd = loginUser.getDistrictCd();
			sdpoCd = loginUser.getSdpoCd();
			psCd = loginUser.getPsCd();
			hotelCd = loginUser.getHotelCd();
			break;

		case 9:
			hotelCd = loginUser.getHotelCd();
			break;

		default:
			throw new RuntimeException("Invalid User Role.");
		}

		Pageable pageable = PageRequest.of(request.getPageNo(), request.getPageSize());

		return userRepo.getUsers(stateCd, zoneCd, rangeCd, districtCd, sdpoCd, psCd, hotelCd, roleId, request.getName(),
				request.getUserLogin(), request.getMobile(), request.getRecordStatus(), request.getFromDate(),
				request.getToDate(), pageable);
	}
}
