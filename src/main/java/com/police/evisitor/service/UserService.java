package com.police.evisitor.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.police.evisitor.dto.request.UserListRequestDTO;
import com.police.evisitor.dto.request.UserRequestDTO;
import com.police.evisitor.dto.response.BulkUploadResponse;
import com.police.evisitor.dto.response.UserListResponseDTO;
import com.police.evisitor.entity.User;
import com.police.evisitor.repository.UserListProjection;

public interface UserService {

	public void saveUser(UserRequestDTO userRequest);

	public void deleteUser(UserRequestDTO userRequest);

	public String activeInactiveUser(UserRequestDTO userRequest);

	public void updateUser(UserRequestDTO userRequest);

	public User getUser(UserRequestDTO userRequest);

	public void updateLoginStatus(Long userId, Boolean loginStatus);

	BulkUploadResponse bulkUploadUsers(MultipartFile file, String loginId);

	Page<UserListProjection> listUsers(UserListRequestDTO request);

}
