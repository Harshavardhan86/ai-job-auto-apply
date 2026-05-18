package com.harsha.autoapply.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {
	
	private final String SCREENSHOT_PATH="uploads/screenshots/";
	
	private final String RESUME_PATH="uploads/resumes";
	
	public String uploadScreenshot(MultipartFile file) throws IOException {

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        Path path = Paths.get(SCREENSHOT_PATH + fileName);

        Files.createDirectories(path.getParent());

        Files.write(path, file.getBytes());

        return fileName;
    }

	public String uploadResume(MultipartFile file) throws IOException {
		
		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        Path path = Paths.get(RESUME_PATH + fileName);

        Files.createDirectories(path.getParent());

        Files.write(path, file.getBytes());

		return fileName;
	}
}
