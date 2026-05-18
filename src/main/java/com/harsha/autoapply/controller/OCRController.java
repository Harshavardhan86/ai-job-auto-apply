package com.harsha.autoapply.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.harsha.autoapply.service.OCRService;

@RestController
@RequestMapping("api/ocr")
@CrossOrigin("*")
public class OCRController {
	
	@Autowired
	private OCRService ocrService;
	
	@GetMapping("/extract")
	public String extractText(@RequestParam String imageName) {
		
		String path="uploads/screenshots/" + imageName;
		return ocrService.extractText(path);
	}
}
