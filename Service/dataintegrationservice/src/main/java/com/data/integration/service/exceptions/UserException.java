package com.data.integration.service.exceptions;

/**
 * This exception can be caused due to error while executing Transformation/Job
 * 
 * @author Aniket
 *
 */
public class UserException extends Exception {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;

	public UserException() {
		super();
	}

	public UserException(String message) {
		super(message);
	}

	public UserException(Throwable cause) {
		super(cause);
	}

	public UserException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
