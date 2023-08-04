package com.companyname.projectname.wordcounter.exception;

/**
 * Exception class for an empty or null list of words in a request.
 */
public class EmptyWordListException extends RuntimeException {

	private static final long serialVersionUID = -378195800212592619L;

	public EmptyWordListException() {
	}

	public EmptyWordListException(String message) {
		super(message);
	}

	public EmptyWordListException(Throwable cause) {
		super(cause);
	}

	public EmptyWordListException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmptyWordListException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
