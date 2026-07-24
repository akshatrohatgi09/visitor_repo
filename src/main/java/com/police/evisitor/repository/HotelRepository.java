package com.police.evisitor.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.police.evisitor.entity.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

	List<Hotel> findByRecordStatus(String recordStatus);

	List<Hotel> findByDistrictCdAndPsCdAndRecordStatus(Integer districtCd, Integer psCd, String recordStatus);

	Optional<Hotel> findByHotelIdAndRecordStatusNot(Long hotelId, String recordStatus);

	Optional<Hotel> findByHotelId(Long hotelId);

	Hotel findByMobileNoAndRecordStatusNot(String mobileNo, String recordStatus);

	Hotel findByEmailIgnoreCaseAndRecordStatusNot(String email, String recordStatus);

	Optional<Hotel> findByHotelNameIgnoreCaseAndRecordStatusNot(String hotel, String recordStatus);

	@Query(value = """

			SELECT

			h.hotel_id AS hotelId,
			h.hotel_name AS hotelName,
			h.owner_name AS ownerName,
			h.mobile_no AS mobileNo,
			h.email AS email,

			h.hotel_type_id AS hotelTypeId,
			ht.hotel_type_name AS hotelTypeName,

			h.no_of_rooms AS noOfRooms,
			h.no_of_floors AS noOfFloors,

			h.license_number AS licenseNumber,
			h.license_validity AS licenseValidity,

			h.address AS address,
			h.pincode AS pincode,

			h.latitude AS latitude,
			h.longitude AS longitude,

			h.beat_number AS beatNumber,

			h.owner_address AS ownerAddress,
			h.owner_state_cd AS ownerStateCd,
			os.state_name AS ownerStateName,
			h.owner_district_cd AS ownerDistrictCd,
			od.district AS ownerDistrictName,
			h.owner_pincode AS ownerPincode,

			h.manager_name AS managerName,
			h.manager_email AS managerEmail,
			h.manager_phone AS managerPhone,
			h.manager_address AS managerAddress,
			h.manager_state_cd AS managerStateCd,
			ms.state_name AS managerStateName,
			h.manager_district_cd AS managerDistrictCd,
			md.district AS managerDistrictName,
			h.manager_pincode AS managerPincode,

			h.comment AS comment,

			h.state_cd AS stateCd,
			st.state_name AS stateName,

			h.zone_cd AS zoneCd,
			z.zone_name AS zoneName,

			h.range_cd AS rangeCd,
			rg.range_name AS rangeName,

			h.district_cd AS districtCd,
			d.district AS districtName,

			h.spdo_cd AS sdpoCd,
			sd.spdo_name AS sdpoName,

			h.ps_cd AS psCd,
			ps.ps AS policeStationName,

			h.record_status AS recordStatus,
			h.created_by AS createdBy,
			h.created_on AS createdOn

			FROM t_hotels h

			LEFT JOIN m_state st
			ON st.state_cd = h.state_cd

			LEFT JOIN m_zone z
			ON z.zone_cd = h.zone_cd

			LEFT JOIN m_range rg
			ON rg.range_cd = h.range_cd

			LEFT JOIN m_district d
			ON d.district_cd = h.district_cd

			LEFT JOIN m_spdo sd
			ON sd.spdo_cd = h.spdo_cd

			LEFT JOIN m_police_station ps
			ON ps.ps_cd = h.ps_cd

			LEFT JOIN m_hotel_type ht
			ON ht.hotel_type_id = h.hotel_type_id

			LEFT JOIN m_state os
			ON os.state_cd = h.owner_state_cd

			LEFT JOIN m_district od
			ON od.district_cd = h.owner_district_cd

			LEFT JOIN m_state ms
			ON ms.state_cd = h.manager_state_cd

			LEFT JOIN m_district md
			ON md.district_cd = h.manager_district_cd

			WHERE (:stateCd IS NULL OR h.state_cd = :stateCd)
			AND (:zoneCd IS NULL OR h.zone_cd = :zoneCd)
			AND (:rangeCd IS NULL OR h.range_cd = :rangeCd)
			AND (:districtCd IS NULL OR h.district_cd = :districtCd)
			AND (:sdpoCd IS NULL OR h.spdo_cd = :sdpoCd)
			AND (:psCd IS NULL OR h.ps_cd = :psCd)
			AND (:hotelCd IS NULL OR h.hotel_id = :hotelCd)

			AND (
			    COALESCE(:hotelName,'') = ''
			    OR LOWER(h.hotel_name)
			       LIKE LOWER(CONCAT('%',:hotelName,'%'))
			)

			AND (
			    COALESCE(:ownerName,'') = ''
			    OR LOWER(h.owner_name)
			       LIKE LOWER(CONCAT('%',:ownerName,'%'))
			)

			AND (
			    COALESCE(:mobileNo,'') = ''
			    OR h.mobile_no LIKE CONCAT('%',:mobileNo,'%')
			)

			AND (
			    (:fromDate IS NULL OR :toDate IS NULL)
			    OR DATE(h.created_on) BETWEEN :fromDate AND :toDate
			)

			ORDER BY h.created_on DESC

			""",

			countQuery = """

					SELECT COUNT(*)

					FROM t_hotels h

					WHERE (:stateCd IS NULL OR h.state_cd = :stateCd)
					AND (:zoneCd IS NULL OR h.zone_cd = :zoneCd)
					AND (:rangeCd IS NULL OR h.range_cd = :rangeCd)
					AND (:districtCd IS NULL OR h.district_cd = :districtCd)
					AND (:sdpoCd IS NULL OR h.spdo_cd = :sdpoCd)
					AND (:psCd IS NULL OR h.ps_cd = :psCd)
					AND (:hotelCd IS NULL OR h.hotel_id = :hotelCd)

					AND (
					    COALESCE(:hotelName,'') = ''
					    OR LOWER(h.hotel_name)
					       LIKE LOWER(CONCAT('%',:hotelName,'%'))
					)

					AND (
					    COALESCE(:ownerName,'') = ''
					    OR LOWER(h.owner_name)
					       LIKE LOWER(CONCAT('%',:ownerName,'%'))
					)

					AND (
					    COALESCE(:mobileNo,'') = ''
					    OR h.mobile_no LIKE CONCAT('%',:mobileNo,'%')
					)

					AND (
					    (:fromDate IS NULL OR :toDate IS NULL)
					    OR DATE(h.created_on) BETWEEN :fromDate AND :toDate
					)

					""", nativeQuery = true)
	Page<HotelListProjection> listHotels(Integer stateCd, Integer zoneCd, Integer rangeCd, Integer districtCd,
			Integer sdpoCd, Integer psCd, Long hotelCd, String hotelName, String ownerName, String mobileNo,
			LocalDate fromDate, LocalDate toDate, Pageable pageable);

}