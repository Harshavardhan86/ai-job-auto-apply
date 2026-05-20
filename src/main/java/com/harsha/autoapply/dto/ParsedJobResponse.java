package com.harsha.autoapply.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ParsedJobResponse {
	
	private String company;
	private String role;
	private String email;
	private String subject;
}
