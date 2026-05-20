package com.harsha.autoapply.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.harsha.autoapply.service.OCRService;
import com.harsha.autoapply.utils.TextParserUtil;
import com.harsha.autoapply.dto.ParsedJobResponse;

@RestController
@RequestMapping("api/ocr")
@CrossOrigin("*")
public class OCRController {
	
	@Autowired
	private OCRService ocrService;
	
	@GetMapping("/extract")
	public ParsedJobResponse extractText(@RequestParam String imageName) {
		
		String path="uploads/screenshots/" + imageName;
		
		String extractedText = ocrService.extractText(path);
		String email = TextParserUtil.extractEmail(extractedText);

		String company = TextParserUtil.extractCompany(extractedText);

	    String role = TextParserUtil.extractRole(extractedText);
	    
	    String subject=TextParserUtil.extarctSubject(extractedText);
	    
	    return new ParsedJobResponse(company, role, email,subject);
	}
}
