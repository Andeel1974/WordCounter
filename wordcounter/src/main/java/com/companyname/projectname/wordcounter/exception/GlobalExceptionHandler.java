package com.companyname.projectname.wordcounter.exception;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	public static final String INTERNAL_SERVER_ERROR_MESSAGE_CODE = "INTERNAL_SERVER_ERROR";
	public static final String EMPTY_WORD_LIST_MESSAGE_CODE = "EMPTY_WORD_LIST_ERROR";
	
	@ExceptionHandler(EmptyWordListException.class)
	public ResponseEntity<ExceptionResponse> handleInvalidInputException(Exception ex, WebRequest request) {
		// BAD_REQUEST (400) chosen ahead of NO_CONTENT (204) because it allows for the response to have a 
		// message body, i.e in this case the ExceptionResponse instance..
		ExceptionResponse exceptionResponse = getExceptionResponseObject(ex, request, HttpStatus.BAD_REQUEST,
				EMPTY_WORD_LIST_MESSAGE_CODE, WordCounterService.EMPTYWORDLIST_MESSAGE);
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
		logger.error("Exception" ,ex);
		ExceptionResponse exceptionResponse = getExceptionResponseObject(ex, request, HttpStatus.INTERNAL_SERVER_ERROR,
				INTERNAL_SERVER_ERROR_MESSAGE_CODE, ex.getMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ExceptionResponse getExceptionResponseObject(Exception ex, WebRequest request, HttpStatus httpStatus,
			   String messageCode, String message) {
		ExceptionResponse exceptionResponse = ExceptionResponse.builder().timestamp(new Date()).message(message)
		.messageCode(messageCode).uri(request.getDescription(false)).httpStatus(httpStatus)
		.httpStatusCode(httpStatus.value()).build();
		return exceptionResponse;
	}
}
