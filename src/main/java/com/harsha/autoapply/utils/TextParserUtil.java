package com.harsha.autoapply.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextParserUtil {

	public static String extractEmail(String text) {
		
		String regex = "[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(text);
        
        if (matcher.find()) {
            return matcher.group();
        }
        
        return "Email Not Found";
	}
	
	// for now fixed name later we can use ai to extarct company and role
	public static String extractCompany(String text) {
		
		if (text.contains("Gravitix Tech Solutions")) {
            return "Gravitix Tech Solutions";
        }
		return "Company Not Found";
	}
	
	public static String extractRole(String text) {

        if (text.contains("Software Development Engineer")) {
            return "Software Development Engineer";
        }

        return "Role Not Found";
    }
	
	public static String extarctSubject(String text) {
		
		String keyword="Subject:";
		
		int startIndex=text.indexOf(keyword);
		
		if(startIndex ==-1) {
			return "Subject not found ";
		}
		
		int endIndex=text.indexOf("\n",startIndex);
		
		if(endIndex==-1) {
			endIndex=text.length();
		}
		
		return text.substring(startIndex+keyword.length(),endIndex).trim();
	}
	
}
