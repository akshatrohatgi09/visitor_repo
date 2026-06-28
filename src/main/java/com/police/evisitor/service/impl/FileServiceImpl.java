package com.police.evisitor.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.Calendar;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.police.evisitor.entity.DocFile;
import com.police.evisitor.entity.MasterDocument;
import com.police.evisitor.exception.BadRequest;
import com.police.evisitor.exception.NotFound;
import com.police.evisitor.exception.ResourceNotFoundException;
import com.police.evisitor.repository.DocFileRepository;
import com.police.evisitor.repository.MasterDocumentRepository;
import com.police.evisitor.service.FileService;
import com.police.evisitor.util.Constants;
import com.police.evisitor.util.FileCompressionUtil;

import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

	@Value("${upload.file.max.size}")
	private long uploadMaxFileSize;

	@Value("${storage.file.path}")
	private String envFilePath;

	@Autowired
	private FileCompressionUtil fileCompressionUtil;

	@Autowired
	private MasterDocumentRepository documentRepo;

	@Autowired
	private DocFileRepository fileRepository;

	private static final long KB = 1024;
	private static final long MB = 1024 * KB;

	@Override
	public Long uploadFiles(MultipartFile file, String request) throws IOException {
		log.info("Start file upload process for request :: {}", request);

		JSONObject jsonObject = new JSONObject(request);

		Integer docType = null;
		String loginId = null;

		if (Objects.nonNull(request)) {
			docType = jsonObject.getInt("docType");
			loginId = jsonObject.getString("loginId");
		}

		if (Objects.isNull(file) || file.isEmpty()) {
			throw new IOException("File is empty. Please upload at least one file.");
		}

		if (file.getSize() > uploadMaxFileSize) {
			throw new IOException("File size must not exceed " + (uploadMaxFileSize / MB) + " MB");
		}

		Set<String> allowedExtensions = Set.of("pdf", "jpg", "jpeg", "png");
		Set<String> allowedMimeTypes = Set.of("application/pdf", "image/jpeg", "image/png");

		String completeFileName = file.getOriginalFilename();
		if (Objects.isNull(completeFileName) || !completeFileName.contains(".")) {
			throw new IOException("Invalid file name.");
		}

		String fileExtension = FilenameUtils.getExtension(completeFileName).toLowerCase();
		String mimeType = file.getContentType();

		if (!allowedExtensions.contains(fileExtension)) {
			throw new IOException("File not allowed Upload Only pdf, jpg, jpeg, png.");
		}

		if (!allowedMimeTypes.contains(mimeType)) {
			throw new IOException("File Type not allowed Upload Only application/pdf, image/jpeg, image/png.");
		}

		String path = getFilePathFromProp(loginId);
		log.info("Created directory is :: {}", path);
		String zipPassword = getZipPassword(completeFileName);

		File directory = new File(path);
		if (!directory.exists()) {
			log.info("Creating directory with path :: {}", path);
			directory.mkdirs();
		}

		String originalFileName = completeFileName.substring(0, completeFileName.lastIndexOf("."));
		long millis = System.currentTimeMillis();
		String last4Millis = String.valueOf(millis % 10000);
		String zipFileName = originalFileName + "_" + last4Millis + ".zip";
		String zipFullPath = path + File.separator + zipFileName;

		DocFile docFile = new DocFile();
		Path processedFilePath;
		try {

			byte[] fileContent = fileCompressionUtil.compressIfRequired(file);
			log.info("Original File :: Name={}, Size={} KB", originalFileName, file.getSize() / 1024);
			log.info("Processed File :: Name={}, Size={} KB", originalFileName, fileContent.length / 1024);

			processedFilePath = Paths.get(path, completeFileName);
			Files.write(processedFilePath, fileContent, StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);

			ZipParameters zipParameters = new ZipParameters();
			zipParameters.setEncryptFiles(true);
			zipParameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
			zipParameters.setFileNameInZip(processedFilePath.getFileName().toString());

			ZipFile zipFile = new ZipFile(zipFullPath, zipPassword.toCharArray());
			zipFile.addFile(processedFilePath.toFile(), zipParameters);

			Files.delete(processedFilePath);
			log.info("Zip file is :: {}", zipFile);

			docFile.setFileName(zipFileName);
			docFile.setFilePath(zipFullPath);
			docFile.setFileContentType("application/zip");
			docFile.setFileSize((Files.size(Paths.get(zipFullPath)) / 1024) + " KB");
			docFile.setCreatedBy(loginId);
			docFile.setRecordStatus(Constants.C);
			docFile.setFilePass(zipPassword);
			if (Objects.nonNull(docType)) {
				MasterDocument doc = documentRepo.findById(docType).orElseThrow(
						() -> new NotFound("Document Type not found with ID: " + jsonObject.getInt("docType")));
				docFile.setDocumentType(doc);
			}

			log.info("Doc fileName is  :: {}", zipFileName);
		} catch (java.io.IOException ex) {
			throw new IOException("File not save.");
		}

		docFile = fileRepository.save(docFile);
		log.info("File Uploaded successfully with docId  :: {}", docFile.getDocId());
		return docFile.getDocId();
	}

	private String getZipPassword(String completeFileName) {
		String rawPassword = completeFileName + System.currentTimeMillis();
		return Base64.getEncoder().encodeToString(rawPassword.getBytes(StandardCharsets.UTF_8));
	}

	public String getFilePathFromProp(String loginId) {

		Calendar calendar = Calendar.getInstance();

		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
		String day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));

		String yearMonth = year + month;

		String filePath = envFilePath.replace("%YYYY%MM%", yearMonth).replace("%YY%", year).replace("%MM%", month)
				.replace("%DD%", day).replace("%LOGIND%", loginId);

		log.info("Generated File Path : {}", filePath);

		return filePath;
	}

	@Override
	public JSONObject downloadFile(Long docId) throws java.io.IOException {
		DocFile docFile = fileRepository.findByDocIdAndRecordStatusNot(docId, Constants.D);
		String zipFilePath = docFile.getFilePath();
		String zipPassword = docFile.getFilePass();

		Path tempDir = Files.createTempDirectory("extract-");
		JSONObject jsonObjRes = new JSONObject();
		try {
			ZipFile zipFile = new ZipFile(zipFilePath, zipPassword.toCharArray());
			zipFile.extractAll(tempDir.toString());
			File extractedFile = tempDir.toFile().listFiles()[0];
			FileInputStream fis = new FileInputStream(extractedFile);

			byte[] fileBytes = fis.readAllBytes();
			String base64Data = Base64.getEncoder().encodeToString(fileBytes);
			jsonObjRes.put(docId.toString(), base64Data);

			return jsonObjRes;
		} catch (RuntimeException e) {
			log.error("Error in Extracting the data due to :: {}", e.getMessage());
		} finally {
			tempDir.toFile().deleteOnExit();
		}

		return jsonObjRes;
	}

	@Override
	public void deleteFile(String request, String uId) {
		
		JSONObject jsonObject = new JSONObject(request);
		if (request == null) {
			throw new BadRequest("Please provide DocId");
		}
		Long docId = jsonObject.getLong("docId");

		DocFile docFile = fileRepository.findByDocIdAndRecordStatusNot(docId, Constants.D);
		if (docFile != null) {
			docFile.setRecordStatus(Constants.D);
			docFile.setUpdatedBy(uId);
		} else {
			throw new NotFound("DocId does not exist.");
		}

		try {
			fileRepository.save(docFile);
		} catch (Exception ex) {
			log.error("Error in Deleting the File :: {}", ex.getMessage());
			throw new ResourceNotFoundException("Error in Deleting the File" + ex.getMessage());
		}

	}
}
