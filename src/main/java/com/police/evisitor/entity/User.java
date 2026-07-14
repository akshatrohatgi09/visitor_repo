package com.police.evisitor.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;

	@Column(name = "user_login")
	private String userLogin;

	@Column(name = "user_password")
	private String userPassword;

	@Column(name = "user_mob")
	private String userMob;

	@Column(name = "user_email")
	private String userEmail;

	@Column(name = "user_role_id")
	private Long userRoleId;

	@Column(name = "nationality_cd")
	private Integer nationalityCd;

	@Column(name = "state_cd")
	private Integer stateCd;

	@Column(name = "zone_cd")
	private Integer zoneCd;

	@Column(name = "range_cd")
	private Integer rangeCd;

	@Column(name = "district_cd")
	private Integer districtCd;

	@Column(name = "sdpo_cd")
	private Integer sdpoCd;

	@Column(name = "ps_cd")
	private Integer psCd;

	@Column(name = "hotel_cd")
	private Long hotelCd;

	@Column(name = "user_address")
	private String userAddress;
	
	@Column(name = "pincode")
	private String pincode;

	@Column(name = "login_status")
	private Boolean loginStatus;

	@Column(name = "last_logged_in")
	private LocalDateTime lastLoggedIn;

	@Column(name = "record_status")
	private Character recordStatus;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_on", updatable = false)
	@CreationTimestamp
	private LocalDateTime createdOn;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_on")
	private LocalDateTime updatedOn;

	@Column(name = "comments")
	private String comment;
	
	@Column(name = "user_state_cd")
	private Integer userStateCd;
	
	@Column(name = "user_district_cd")
	private Integer userDistrictCd;

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin == null ? null : userLogin.trim().toLowerCase();
	}
}