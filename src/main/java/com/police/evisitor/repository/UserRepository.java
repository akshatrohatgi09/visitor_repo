package com.police.evisitor.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.police.evisitor.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserLoginIgnoreCaseAndRecordStatusNot(String userLogin, Character recordStatus);

	User findByUserEmailIgnoreCaseAndRecordStatusNot(String userEmail, Character recordStatus);

	User findByUserMobAndRecordStatusNot(String userMob, Character recordStatus);

	List<User> findByHotelCdAndRecordStatusNot(Long hotelCd, char recordStatus);

	Optional<User> findByUserIdAndRecordStatusNot(Long userId, char recordStatus);

	Optional<User> findByUserId(Long userId);

	@Query(value = """

			SELECT

			u.user_id AS userId,
			u.first_name AS firstName,
			u.last_name AS lastName,
			u.user_login AS userLogin,
			u.user_mob AS userMob,
			u.user_email AS userEmail,
			r.role_name AS roleName,
			u.state_cd AS stateCd,
			st.state_name AS stateName,
			u.zone_cd AS zoneCd,
			z.zone_name AS zoneName,
			u.range_cd AS rangeCd,
			rg.range_name AS rangeName,
			u.district_cd AS districtCd,
			d.district AS districtName,
			u.sdpo_cd AS sdpoCd,
			sd.sdpo_name AS sdpoName,
			u.ps_cd AS psCd,
			ps.ps AS policeStationName,
			u.hotel_cd AS hotelCd,
			h.hotel_name AS hotelName,
			u.record_status AS recordStatus,
			u.login_status AS loginStatus,
			u.created_by AS createdBy,
			u.created_on AS createdOn

			FROM t_users u
			JOIN m_roles r
			ON r.role_id=u.user_role_id
			
			LEFT JOIN m_state st
			ON st.state_cd=u.state_cd

			LEFT JOIN m_zone z
			ON z.zone_cd=u.zone_cd

			LEFT JOIN m_range rg
			ON rg.range_cd=u.range_cd

			LEFT JOIN m_district d
			ON d.district_cd=u.district_cd

			LEFT JOIN m_sdpo sd
			ON sd.sdpo_cd=u.sdpo_cd

			LEFT JOIN m_police_station ps
			ON ps.ps_cd=u.ps_cd

			LEFT JOIN t_hotels h
			ON h.hotel_id=u.hotel_cd

			WHERE u.record_status<>'D'

			AND (:stateCd IS NULL OR u.state_cd=:stateCd)
			AND (:zoneCd IS NULL OR u.zone_cd=:zoneCd)
			AND (:rangeCd IS NULL OR u.range_cd=:rangeCd)
			AND (:districtCd IS NULL OR u.district_cd=:districtCd)
			AND (:sdpoCd IS NULL OR u.sdpo_cd=:sdpoCd)
			AND (:psCd IS NULL OR u.ps_cd=:psCd)
			AND (:hotelCd IS NULL OR u.hotel_cd=:hotelCd)

			AND (
			:roleId IS NULL
			OR u.user_role_id>:roleId
			)

			AND (
			:recordStatus IS NULL
			OR u.record_status=:recordStatus
			)

			AND (
			:name IS NULL
			OR LOWER(CONCAT(u.first_name,' ',u.last_name))
			LIKE LOWER(CONCAT('%',:name,'%'))
			)

			AND (
			:userLogin IS NULL
			OR LOWER(u.user_login)
			LIKE LOWER(CONCAT('%',:userLogin,'%'))
			)

			AND (
			:mobile IS NULL
			OR u.user_mob
			LIKE CONCAT('%',:mobile,'%')
			)

			AND (
			(:fromDate IS NULL OR :toDate IS NULL)
			OR DATE(u.created_on)
			BETWEEN :fromDate AND :toDate
			)

			ORDER BY u.created_on DESC

			""",
			countQuery = """
					SELECT COUNT(*)
					FROM t_users u
					WHERE u.record_status<>'D'
					AND (:stateCd IS NULL OR u.state_cd=:stateCd)
					AND (:zoneCd IS NULL OR u.zone_cd=:zoneCd)
					AND (:rangeCd IS NULL OR u.range_cd=:rangeCd)
					AND (:districtCd IS NULL OR u.district_cd=:districtCd)
					AND (:sdpoCd IS NULL OR u.sdpo_cd=:sdpoCd)
					AND (:psCd IS NULL OR u.ps_cd=:psCd)
					AND (:hotelCd IS NULL OR u.hotel_cd=:hotelCd)
					AND (
					:roleId IS NULL
					OR u.user_role_id>:roleId
					)

					AND (
					:recordStatus IS NULL
					OR u.record_status=:recordStatus
					)

					AND (
					:name IS NULL
					OR LOWER(CONCAT(u.first_name,' ',u.last_name))
					LIKE LOWER(CONCAT('%',:name,'%'))
					)

					AND (
					:userLogin IS NULL
					OR LOWER(u.user_login)
					LIKE LOWER(CONCAT('%',:userLogin,'%'))
					)

					AND (
					:mobile IS NULL
					OR u.user_mob
					LIKE CONCAT('%',:mobile,'%')
					)

					AND (
					(:fromDate IS NULL OR :toDate IS NULL)
					OR DATE(u.created_on)
					BETWEEN :fromDate AND :toDate
					)
					""",
			nativeQuery = true)

	Page<UserListProjection> getUsers(Integer stateCd, Integer zoneCd, Integer rangeCd, Integer districtCd,
			Integer sdpoCd, Integer psCd, Long hotelCd, Long roleId, String name, String userLogin, String mobile,
			Character recordStatus, LocalDate fromDate, LocalDate toDate, Pageable pageable

	);

}
