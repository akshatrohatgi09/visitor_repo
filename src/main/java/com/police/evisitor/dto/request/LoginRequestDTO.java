package com.police.evisitor.dto.request;

import lombok.Data;

@Data
public class LoginRequestDTO {

	private String userLogin;

	private String password;
}