package com.police.evisitor.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.police.evisitor.dto.response.ApiResponse;
import com.police.evisitor.service.FileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/visitor")
public class FileController {

	@Autowired
	private FileService fileService;

	@PostMapping("/uploadFile")
	public ResponseEntity<ApiResponse<?>> uploaddocsHandller(@RequestPart(name = "files") List<MultipartFile> files,
			@RequestParam(name = "request") String request) {

		if (files.size() > 2) {
			ApiResponse<?> apiResponse = ApiResponse.builder().status(String.valueOf(HttpStatus.BAD_REQUEST))
					.message("Maximum two documents are allowed.").data(null).build();
			return ResponseEntity.ok(apiResponse);
		}

		List<Long> listFileIdUploadRes = new ArrayList<>();
		files.forEach(file -> {
			Long fileId = null;
			try {
				fileId = fileService.uploadFiles(file, request);
			} catch (IOException e) {
				e.printStackTrace();
			}
			listFileIdUploadRes.add(fileId);
		});

		ApiResponse<?> apiResponse = ApiResponse.builder().status(String.valueOf(HttpStatus.CREATED))
				.message("File Uploaded Successfully.").data(listFileIdUploadRes).build();
		return ResponseEntity.ok(apiResponse);
	}

	@PostMapping("/downloadFile")
	public ResponseEntity<ApiResponse> downloadDocsHandller(@RequestBody String request) throws IOException {
		JSONObject jsonRequest = new JSONObject(request);
		String fileIds = jsonRequest.optString("fileIdList");
		List<Long> fileIdList = Arrays.stream(fileIds.split(",")).map(String::trim).map(Long::parseLong).toList();

		List<JSONObject> imageResList = new ArrayList<>();
		fileIdList.forEach(fileId -> {
			try {
				JSONObject imgRes = fileService.downloadFile(fileId);
				imageResList.add(imgRes);
			} catch (IOException e) {
				log.error("Error in Downloading Files :: {}", e.getMessage());
			}

		});

		ApiResponse<?> apiResponse = ApiResponse.builder().status(String.valueOf(HttpStatus.OK))
				.message("File Downloaded Successfully.").jsonData(imageResList).build();
		return ResponseEntity.ok(apiResponse);
	}

	@PostMapping("/deleteFile")
	public ResponseEntity<ApiResponse<?>> deleteDocsHandller(@RequestBody String request,
			@RequestHeader(name = "id") String uId) {
		fileService.deleteFile(request, uId);
		ApiResponse<?> apiResponse = ApiResponse.builder().status(String.valueOf(HttpStatus.OK))
				.message("File Deleted Successfully.").build();
		return ResponseEntity.ok(apiResponse);
	}

}
