package com.police.evisitor.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListResponseDTO {

	private Long userId;

	private String userName;

	private String userLogin;

	private String userMob;

	private String userEmail;

	private String roleName;

	private String hotelName;

	private String loginStatus;

	private Character recordStatus;

	private String createdBy;

	private LocalDateTime createdOn;

}