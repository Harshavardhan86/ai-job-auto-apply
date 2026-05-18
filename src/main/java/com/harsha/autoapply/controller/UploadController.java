package com.harsha.autoapply.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.harsha.autoapply.dto.UploadResponse;
import com.harsha.autoapply.service.FileUploadService;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin("*")
public class UploadController {
	
	 @Autowired
	 private FileUploadService fileUploadService;
	 
	 @PostMapping("/screenshot")
	 public ResponseEntity<UploadResponse> uploadScreenshot(
			 @RequestParam("file") MultipartFile file) throws IOException{
		 
		 String fileName=fileUploadService.uploadScreenshot(file);
		 
		 return ResponseEntity.ok(new UploadResponse("Resume Uploaded Successfully", fileName));
	 }
}
