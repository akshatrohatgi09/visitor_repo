package com.police.evisitor.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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

	@Column(name = "user_name")
	private String userName;

	@Column(name = "user_login")
	private String userLogin;

	@Column(name = "user_password")
	private String userPassword;

	@Column(name = "user_mob")
	private String userMob;

	@Column(name = "user_email")
	private String userEmail;

	@Column(name = "nationality_cd")
	private Integer nationalityCd;

	@Column(name = "state_cd")
	private Integer stateCd;

	@Column(name = "district_cd")
	private Integer districtCd;

	@Column(name = "ps_cd")
	private Integer psCd;

	@Column(name = "user_address")
	private String userAddress;

	@Column(name = "user_role_id")
	private Long userRoleId;

	@Column(name = "hotel_cd")
	private Long hotelCd;

	@Column(name = "login_status")
	private Boolean loginStatus;

	@Column(name = "last_logged_in")
	private LocalDateTime lastLoggedIn;

	@Column(name = "record_status")
	private Character recordStatus;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_on")
	private LocalDateTime createdOn;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_on")
	private LocalDateTime updatedOn;

	@Column(name = "comment")
	private String comment;

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin != null ? userLogin.toLowerCase().trim() : null;
	}
}