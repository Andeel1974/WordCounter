package com.companyname.projectname.wordcounter.exception;

import java.util.Date;
import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ExceptionResponse {
	private Date timestamp;
	private String message;
	private String messageCode;
	private String uri;
	private HttpStatus httpStatus;
	private int httpStatusCode;
	private Map<String, String> errors;
}
