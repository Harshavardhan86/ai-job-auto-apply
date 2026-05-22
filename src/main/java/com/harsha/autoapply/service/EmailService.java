package com.harsha.autoapply.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String fromEmail;
	
	public void sendApplicationEmail(
	        String toEmail,
	        String candidateName,
	        String jobRole,
	        String companyName,
	        String resumePath,
	        String emailBody) throws MessagingException {   
		
		//create an empty mail container
	    MimeMessage message = mailSender.createMimeMessage(); 
	    
	    // (message)send text, (true) attachments together
	    MimeMessageHelper helper = new MimeMessageHelper(message, true);

	    helper.setFrom(fromEmail);
	    helper.setTo(toEmail);
	    helper.setSubject("Application for " + jobRole + " – " + candidateName);
	    helper.setText(emailBody);

	    File resumeFile = new File(resumePath);
	    if (resumeFile.exists()) {
	        helper.addAttachment(resumeFile.getName(), resumeFile);
	    }

	    mailSender.send(message);
	}
	
	
	
}
