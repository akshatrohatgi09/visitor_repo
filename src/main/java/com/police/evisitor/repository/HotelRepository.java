package com.police.evisitor.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

	Optional<Hotel> findByHotelNameIgnoreCaseAndRecordStatusNot(String hotel, String string);

	Hotel findByMobileNoAndRecordStatusNot(String mobileNo, Character recordStatus);

	Hotel findByEmailIgnoreCaseAndRecordStatusNot(String email, Character recordStatus);

	@Query(value = """

			SELECT

			h.hotel_id AS hotelId,
			h.hotel_name AS hotelName,
			h.owner_name AS ownerName,
			h.mobile_no AS mobileNo,
			h.email AS email,
			h.address AS address,

			h.state_cd AS stateCd,
			st.state_name AS stateName,

			h.zone_cd AS zoneCd,
			z.zone_name AS zoneName,

			h.range_cd AS rangeCd,
			rg.range_name AS rangeName,

			h.district_cd AS districtCd,
			d.district AS districtName,

			h.sdpo_cd AS sdpoCd,
			sd.sdpo_name AS sdpoName,

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

			LEFT JOIN m_sdpo sd
			ON sd.sdpo_cd = h.sdpo_cd

			LEFT JOIN m_police_station ps
			ON ps.ps_cd = h.ps_cd

			WHERE h.record_status <> 'D'

			AND (:stateCd IS NULL OR h.state_cd = :stateCd)
			AND (:zoneCd IS NULL OR h.zone_cd = :zoneCd)
			AND (:rangeCd IS NULL OR h.range_cd = :rangeCd)
			AND (:districtCd IS NULL OR h.district_cd = :districtCd)
			AND (:sdpoCd IS NULL OR h.sdpo_cd = :sdpoCd)
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

					WHERE h.record_status <> 'D'

					AND (:stateCd IS NULL OR h.state_cd = :stateCd)
					AND (:zoneCd IS NULL OR h.zone_cd = :zoneCd)
					AND (:rangeCd IS NULL OR h.range_cd = :rangeCd)
					AND (:districtCd IS NULL OR h.district_cd = :districtCd)
					AND (:sdpoCd IS NULL OR h.sdpo_cd = :sdpoCd)
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
	Page<HotelListProjection> listHotels(@Param("stateCd") Integer stateCd, @Param("zoneCd") Integer zoneCd,
			@Param("rangeCd") Integer rangeCd, @Param("districtCd") Integer districtCd, @Param("sdpoCd") Integer sdpoCd,
			@Param("psCd") Integer psCd, @Param("hotelCd") Long hotelCd, @Param("hotelName") String hotelName,
			@Param("ownerName") String ownerName, @Param("mobileNo") String mobileNo,
			@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

}