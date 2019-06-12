package com.data.integration.service.exceptions;

/**
 * This exception can be caused due to error occurred while replacing Tag values
 * 
 * @author Aniket
 *
 */
public class TagValueReplacerException extends RuntimeException {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;

	public TagValueReplacerException() {
		super();
	}

	public TagValueReplacerException(String message) {
		super(message);
	}

	public TagValueReplacerException(Throwable cause) {
		super(cause);
	}

	public TagValueReplacerException(String message, Throwable cause) {
		super(message, cause);
	}

	public TagValueReplacerException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
