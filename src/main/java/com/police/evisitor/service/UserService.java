package com.police.evisitor.service;

import org.springframework.web.multipart.MultipartFile;

import com.police.evisitor.dto.request.UserRequestDTO;
import com.police.evisitor.dto.response.BulkUploadResponse;
import com.police.evisitor.entity.User;

public interface UserService {

	public void saveUser(UserRequestDTO userRequest);

	public void deleteUser(UserRequestDTO userRequest);

	public String activeInactiveUser(UserRequestDTO userRequest);

	public void updateUser(UserRequestDTO userRequest);

	public User getUser(UserRequestDTO userRequest);

	public void updateLoginStatus(Long userId, Boolean loginStatus);

	BulkUploadResponse bulkUploadUsers(MultipartFile file, String loginId);

}
