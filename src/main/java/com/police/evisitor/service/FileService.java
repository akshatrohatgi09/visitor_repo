package com.police.evisitor.service;

import java.io.IOException;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	Long uploadFiles(MultipartFile file, String request) throws IOException;

	JSONObject downloadFile(Long docId) throws java.io.IOException;

	void deleteFile(String request, String uId);

}
