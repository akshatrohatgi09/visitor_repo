package com.police.evisitor.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.police.evisitor.entity.Visitor;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

	Visitor findByIdAndRecordStatus(Long id, String recordStatus);

	@Query(value = """

			SELECT
			    v.visitor_id AS id,

			    v.users AS user,
			    v.hotel AS hotel,
			    h.hotel_name AS hotelName,

			    v.room_no AS roomNo,
			    v.check_in_date AS checkInDate,
			    v.check_out_date AS checkOutDate,

			    v.coming_location AS comingLocation,
			    v.going_location AS goingLocation,

			    v.visit_reason_id AS visitReasonType,
			    v.visit_reason AS visitReason,
			    v.note AS note,

			    v.visitor_first_name AS visitorFirstName,
			    v.visitor_last_name AS visitorLastName,
			    v.visitor_mobile AS visitorMobile,
			    v.visitor_mail AS visitorMail,
			    v.visitor_relative_name AS visitorRelativeName,
			    v.visitor_dob AS visitorDob,
			    v.visitor_gender AS visitorGender,

			    v.nationality_cd AS nationalityCd,
			    v.nationality_name AS nationalityName,

			    v.state_cd AS stateCd,
			    v.state_name AS stateName,

			    v.district_cd AS districtCd,
			    v.district_name AS districtName,

			    v.ps_cd AS psCd,
			    v.ps_name AS psName,

			    v.visitor_address AS visitorAddress,
			    v.pincode AS pincode,

			    v.document_no AS documentNo,
			    v.document_type AS documentType,

			    v.files_id AS docIds,
			    v.photo_id AS photoIds,

			    v.record_status AS recordStatus,

			    v.created_by AS createdBy,
			    v.created_on AS createdOn,
			    v.updated_by AS updatedBy,
			    v.updated_on AS updatedOn

			FROM t_visitors v

			LEFT JOIN t_hotels h
			    ON h.hotel_id = v.hotel

			WHERE v.record_status <> 'D'

			AND v.check_in_date IS NOT NULL
			AND v.check_out_date IS NULL

			AND (:stateCd IS NULL OR h.state_cd = :stateCd)

			AND (:zoneCd IS NULL OR h.zone_cd = :zoneCd)

			AND (:rangeCd IS NULL OR h.range_cd = :rangeCd)

			AND (:districtCd IS NULL OR h.district_cd = :districtCd)

			AND (:sdpoCd IS NULL OR h.spdo_cd = :sdpoCd)

			AND (:psCd IS NULL OR h.ps_cd = :psCd)

			AND (:hotelCd IS NULL OR h.hotel_id = :hotelCd)

			AND (
			    COALESCE(:visitorName, '') = ''
			    OR LOWER(
			        CONCAT(
			            COALESCE(v.visitor_first_name, ''),
			            ' ',
			            COALESCE(v.visitor_last_name, '')
			        )
			    )
			    LIKE LOWER(CONCAT('%', :visitorName, '%'))
			)

			AND (
			    COALESCE(:visitorMobile, '') = ''
			    OR v.visitor_mobile LIKE CONCAT('%', :visitorMobile, '%')
			)

			AND (
			    :visitorStatus IS NULL
			    OR :visitorStatus = ''
			    OR v.record_status = :visitorStatus
			)

			AND (
			    :fromDate IS NULL
			    OR DATE(v.check_in_date) >= :fromDate
			)

			AND (
			    :toDate IS NULL
			    OR DATE(v.check_in_date) <= :toDate
			)

			ORDER BY v.check_in_date DESC

			""",

			countQuery = """

					SELECT COUNT(*)

					FROM t_visitors v

					LEFT JOIN t_hotels h
					    ON h.hotel_id = v.hotel

					WHERE v.record_status <> 'D'

					AND v.check_in_date IS NOT NULL
					AND v.check_out_date IS NULL

					AND (:stateCd IS NULL OR h.state_cd = :stateCd)

					AND (:zoneCd IS NULL OR h.zone_cd = :zoneCd)

					AND (:rangeCd IS NULL OR h.range_cd = :rangeCd)

					AND (:districtCd IS NULL OR h.district_cd = :districtCd)

					AND (:sdpoCd IS NULL OR h.spdo_cd = :sdpoCd)

					AND (:psCd IS NULL OR h.ps_cd = :psCd)

					AND (:hotelCd IS NULL OR h.hotel_id = :hotelCd)

					AND (
					    COALESCE(:visitorName, '') = ''
					    OR LOWER(
					        CONCAT(
					            COALESCE(v.visitor_first_name, ''),
					            ' ',
					            COALESCE(v.visitor_last_name, '')
					        )
					    )
					    LIKE LOWER(CONCAT('%', :visitorName, '%'))
					)

					AND (
					    COALESCE(:visitorMobile, '') = ''
					    OR v.visitor_mobile LIKE CONCAT('%', :visitorMobile, '%')
					)

					AND (
					    :visitorStatus IS NULL
					    OR :visitorStatus = ''
					    OR v.record_status = :visitorStatus
					)

					AND (
					    :fromDate IS NULL
					    OR DATE(v.check_in_date) >= :fromDate
					)

					AND (
					    :toDate IS NULL
					    OR DATE(v.check_in_date) <= :toDate
					)

					""", nativeQuery = true)
	Page<VisitorListProjection> getVisitorList(@Param("stateCd") Integer stateCd, @Param("zoneCd") Integer zoneCd,
			@Param("rangeCd") Integer rangeCd, @Param("districtCd") Integer districtCd, @Param("sdpoCd") Integer sdpoCd,
			@Param("psCd") Integer psCd, @Param("hotelCd") Long hotelCd, @Param("visitorName") String visitorName,
			@Param("visitorMobile") String visitorMobile, @Param("visitorStatus") String visitorStatus,
			@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

	@Query(value = """

			SELECT
				v.visitor_id AS id,

				v.users AS user,
				v.hotel AS hotel,
				h.hotel_name AS hotelName,

				v.room_no AS roomNo,
				v.check_in_date AS checkInDate,
				v.check_out_date AS checkOutDate,

				v.coming_location AS comingLocation,
				v.going_location AS goingLocation,

				v.visit_reason_id AS visitReasonType,
				v.visit_reason AS visitReason,
				v.note AS note,

				v.visitor_first_name AS visitorFirstName,
				v.visitor_last_name AS visitorLastName,
				v.visitor_mobile AS visitorMobile,
				v.visitor_mail AS visitorMail,
				v.visitor_relative_name AS visitorRelativeName,
				v.visitor_dob AS visitorDob,
				v.visitor_gender AS visitorGender,

				v.nationality_cd AS nationalityCd,
				v.nationality_name AS nationalityName,

				v.state_cd AS stateCd,
				v.state_name AS stateName,

				v.district_cd AS districtCd,
				v.district_name AS districtName,

				v.ps_cd AS psCd,
				v.ps_name AS psName,

				v.visitor_address AS visitorAddress,
				v.pincode AS pincode,

				v.document_no AS documentNo,
				v.document_type AS documentType,

				v.files_id AS docIds,
				v.photo_id AS photoIds,

				v.record_status AS recordStatus,

				v.created_by AS createdBy,
				v.created_on AS createdOn,
				v.updated_by AS updatedBy,
				v.updated_on AS updatedOn

			FROM t_visitors v

			LEFT JOIN t_hotels h
				ON h.hotel_id = v.hotel

			WHERE v.record_status <> 'D'

			AND v.check_in_date IS NOT NULL
			AND v.check_out_date IS NOT NULL

			AND (:stateCd IS NULL OR h.state_cd = :stateCd)

			AND (:zoneCd IS NULL OR h.zone_cd = :zoneCd)

			AND (:rangeCd IS NULL OR h.range_cd = :rangeCd)

			AND (:districtCd IS NULL OR h.district_cd = :districtCd)

			AND (:sdpoCd IS NULL OR h.spdo_cd = :sdpoCd)

			AND (:psCd IS NULL OR h.ps_cd = :psCd)

			AND (:hotelCd IS NULL OR h.hotel_id = :hotelCd)

			AND (
				COALESCE(:visitorName, '') = ''
				OR LOWER(
					CONCAT(
						COALESCE(v.visitor_first_name, ''),
						' ',
						COALESCE(v.visitor_last_name, '')
					)
				)
				LIKE LOWER(CONCAT('%', :visitorName, '%'))
			)

			AND (
				COALESCE(:visitorMobile, '') = ''
				OR v.visitor_mobile LIKE CONCAT('%', :visitorMobile, '%')
			)

			AND (
				:fromDate IS NULL
				OR DATE(v.check_out_date) >= :fromDate
			)

			AND (
				:toDate IS NULL
				OR DATE(v.check_out_date) <= :toDate
			)

			ORDER BY v.check_out_date DESC

			""",

			countQuery = """

					SELECT COUNT(*)

					FROM t_visitors v

					LEFT JOIN t_hotels h
						ON h.hotel_id = v.hotel

					WHERE v.record_status <> 'D'

					AND v.check_in_date IS NOT NULL
					AND v.check_out_date IS NOT NULL

					AND (:stateCd IS NULL OR h.state_cd = :stateCd)

					AND (:zoneCd IS NULL OR h.zone_cd = :zoneCd)

					AND (:rangeCd IS NULL OR h.range_cd = :rangeCd)

					AND (:districtCd IS NULL OR h.district_cd = :districtCd)

					AND (:sdpoCd IS NULL OR h.spdo_cd = :sdpoCd)

					AND (:psCd IS NULL OR h.ps_cd = :psCd)

					AND (:hotelCd IS NULL OR h.hotel_id = :hotelCd)

					AND (
						COALESCE(:visitorName, '') = ''
						OR LOWER(
							CONCAT(
								COALESCE(v.visitor_first_name, ''),
								' ',
								COALESCE(v.visitor_last_name, '')
							)
						)
						LIKE LOWER(CONCAT('%', :visitorName, '%'))
					)

					AND (
						COALESCE(:visitorMobile, '') = ''
						OR v.visitor_mobile LIKE CONCAT('%', :visitorMobile, '%')
					)

					AND (
						:fromDate IS NULL
						OR DATE(v.check_out_date) >= :fromDate
					)

					AND (
						:toDate IS NULL
						OR DATE(v.check_out_date) <= :toDate
					)

					""", nativeQuery = true)
	Page<VisitorListProjection> getCheckOutVisitorList(@Param("stateCd") Integer stateCd,
			@Param("zoneCd") Integer zoneCd, @Param("rangeCd") Integer rangeCd, @Param("districtCd") Integer districtCd,
			@Param("sdpoCd") Integer sdpoCd, @Param("psCd") Integer psCd, @Param("hotelCd") Long hotelCd,
			@Param("visitorName") String visitorName, @Param("visitorMobile") String visitorMobile,
			@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);
}
