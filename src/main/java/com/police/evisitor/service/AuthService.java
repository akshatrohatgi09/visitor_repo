package com.police.evisitor.service;

import org.apache.coyote.BadRequestException;

import com.police.evisitor.dto.request.LoginRequestDTO;
import com.police.evisitor.dto.response.LoginResponseDTO;

public interface AuthService {

	LoginResponseDTO login(LoginRequestDTO request) throws BadRequestException;

	void logout(String userLogin) throws BadRequestException;
}
