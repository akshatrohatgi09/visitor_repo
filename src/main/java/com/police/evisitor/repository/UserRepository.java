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
import com.police.evisitor.service.LoginProjection;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserLoginIgnoreCaseAndRecordStatusNot(String userLogin, Character recordStatus);

	User findByUserEmailIgnoreCaseAndRecordStatusNot(String userEmail, Character recordStatus);

	User findByUserMobAndRecordStatusNot(String userMob, Character recordStatus);

	List<User> findByHotelCdAndRecordStatusNot(Long hotelCd, Character recordStatus);

	Optional<User> findByUserIdAndRecordStatusNot(Long userId, Character recordStatus);

	Optional<User> findByUserId(Long userId);

	@Query(value = """
						SELECT
			u.user_id AS userId,
			u.user_role_id AS roleId,
			r.role_name AS roleName,

			u.first_name AS firstName,
			u.last_name AS lastName,

			u.user_login AS userLogin,
			u.user_mob AS userMob,
			u.user_email AS userEmail,

			u.state_cd AS stateCd,
			st.state_name AS stateName,

			u.zone_cd AS zoneCd,
			z.zone_name AS zoneName,

			u.range_cd AS rangeCd,
			rg.range_name AS rangeName,

			u.district_cd AS districtCd,
			d.district AS districtName,

			u.spdo_cd AS sdpoCd,
			sd.spdo_name AS sdpoName,

			u.ps_cd AS psCd,
			ps.ps AS policeStationName,

			u.hotel_cd AS hotelCd,
			h.hotel_name AS hotelName,

			u.user_state_cd AS userStateCd,
			ust.state_name AS userStateName,

			u.user_district_cd AS userDistrictCd,
			ud.district AS userDistrictName,

			u.user_address AS userAddress,
			u.pincode AS pincode,
			u.comments AS comments,

			u.record_status AS recordStatus,
			u.login_status AS loginStatus,

			u.created_by AS createdBy,
			u.created_on AS createdOn

			FROM t_users u

			INNER JOIN m_roles r
			    ON r.role_id = u.user_role_id

			LEFT JOIN m_state st
			    ON st.state_cd = u.state_cd

			LEFT JOIN m_zone z
			    ON z.zone_cd = u.zone_cd

			LEFT JOIN m_range rg
			    ON rg.range_cd = u.range_cd

			LEFT JOIN m_district d
			    ON d.district_cd = u.district_cd

			LEFT JOIN m_spdo sd
			    ON sd.spdo_cd = u.spdo_cd

			LEFT JOIN m_police_station ps
			    ON ps.ps_cd = u.ps_cd

			LEFT JOIN t_hotels h
			    ON h.hotel_id = u.hotel_cd

			LEFT JOIN m_state ust
			    ON ust.state_cd = u.user_state_cd

			LEFT JOIN m_district ud
			    ON ud.district_cd = u.user_district_cd

			WHERE (:stateCd IS NULL OR u.state_cd = :stateCd)
			AND (:zoneCd IS NULL OR u.zone_cd = :zoneCd)
			AND (:rangeCd IS NULL OR u.range_cd = :rangeCd)
			AND (:districtCd IS NULL OR u.district_cd = :districtCd)
			AND (:sdpoCd IS NULL OR u.spdo_cd = :sdpoCd)
			AND (:psCd IS NULL OR u.ps_cd = :psCd)
			AND (:hotelCd IS NULL OR u.hotel_cd = :hotelCd)

			AND (
			    :loginRoleId IS NULL
			    OR u.user_role_id > :loginRoleId
			)

			AND (
			    :roleId IS NULL
			    OR u.user_role_id = :roleId
			)

			AND (
			    COALESCE(:name,'') = ''
			    OR LOWER(CONCAT(COALESCE(u.first_name,''),' ',COALESCE(u.last_name,'')))
			       LIKE LOWER(CONCAT('%',:name,'%'))
			)

			AND (
			    COALESCE(:userLogin,'') = ''
			    OR LOWER(u.user_login)
			       LIKE LOWER(CONCAT('%',:userLogin,'%'))
			)

			AND (
			    COALESCE(:mobile,'') = ''
			    OR u.user_mob LIKE CONCAT('%',:mobile,'%')
			)

			AND (
			    (:fromDate IS NULL OR :toDate IS NULL)
			    OR DATE(u.created_on) BETWEEN :fromDate AND :toDate
			)

			ORDER BY u.created_on DESC

						""",

			countQuery = """

										SELECT COUNT(*)

					FROM t_users u

					WHERE (:stateCd IS NULL OR u.state_cd = :stateCd)
					AND (:zoneCd IS NULL OR u.zone_cd = :zoneCd)
					AND (:rangeCd IS NULL OR u.range_cd = :rangeCd)
					AND (:districtCd IS NULL OR u.district_cd = :districtCd)
					AND (:sdpoCd IS NULL OR u.spdo_cd = :sdpoCd)
					AND (:psCd IS NULL OR u.ps_cd = :psCd)
					AND (:hotelCd IS NULL OR u.hotel_cd = :hotelCd)

					AND (
					    :loginRoleId IS NULL
					    OR u.user_role_id > :loginRoleId
					)

					AND (
					    :roleId IS NULL
					    OR u.user_role_id = :roleId
					)

					AND (
					    COALESCE(:name,'') = ''
					    OR LOWER(CONCAT(COALESCE(u.first_name,''),' ',COALESCE(u.last_name,'')))
					       LIKE LOWER(CONCAT('%',:name,'%'))
					)

					AND (
					    COALESCE(:userLogin,'') = ''
					    OR LOWER(u.user_login)
					       LIKE LOWER(CONCAT('%',:userLogin,'%'))
					)

					AND (
					    COALESCE(:mobile,'') = ''
					    OR u.user_mob LIKE CONCAT('%',:mobile,'%')
					)

					AND (
					    (:fromDate IS NULL OR :toDate IS NULL)
					    OR DATE(u.created_on) BETWEEN :fromDate AND :toDate
					)

										""", nativeQuery = true)

	Page<UserListProjection> getUsers(Integer stateCd, Integer zoneCd, Integer rangeCd, Integer districtCd,
			Integer sdpoCd, Integer psCd, Long hotelCd, Long loginRoleId, Long roleId, String name, String userLogin,
			String mobile, LocalDate fromDate, LocalDate toDate, Pageable pageable);

	@Query(value = """
			SELECT

			u.user_id AS userId,
			u.first_name AS firstName,
			u.last_name AS lastName,
			u.user_login AS userLogin,
			u.user_mob AS userMob,
			u.user_email AS userEmail,

			u.user_password AS userPassword,

			r.role_id AS roleId,
			r.role_name AS roleName,

			u.nationality_cd AS nationalityCd,

			s.state_cd AS stateCd,
			s.state_name AS stateName,

			z.zone_cd AS zoneCd,
			z.zone_name AS zoneName,

			rg.range_cd AS rangeCd,
			rg.range_name AS rangeName,

			d.district_cd AS districtCd,
			d.district AS district,

			sd.spdo_cd AS sdpoCd,
			sd.spdo_name AS sdpoName,

			ps.ps_cd AS psCd,
			ps.ps AS ps,

			h.hotel_id AS hotelCd,
			h.hotel_name AS hotelName,

			u.user_address AS userAddress,
			u.user_state_cd AS userStateCd,
			u.user_district_cd AS userDistrictCd,
			u.pincode,

			u.login_status AS loginStatus,
			u.last_logged_in AS lastLoggedIn

			FROM t_users u

			LEFT JOIN m_roles r
			ON u.user_role_id=r.role_id

			LEFT JOIN m_state s
			ON u.state_cd=s.state_cd

			LEFT JOIN m_zone z
			ON u.zone_cd=z.zone_cd

			LEFT JOIN m_range rg
			ON u.range_cd=rg.range_cd

			LEFT JOIN m_district d
			ON u.district_cd=d.district_cd

			LEFT JOIN m_spdo sd
			ON u.spdo_cd=sd.spdo_cd

			LEFT JOIN m_police_station ps
			ON u.ps_cd=ps.ps_cd

			LEFT JOIN t_hotels h
			ON u.hotel_cd=h.hotel_id

			WHERE u.user_login=:login
			AND u.record_status='C'
			""", nativeQuery = true)
	Optional<LoginProjection> loginUser(String login);

}
