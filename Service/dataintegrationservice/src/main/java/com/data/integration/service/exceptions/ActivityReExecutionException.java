package com.data.integration.service.exceptions;

/**
 * 
 * This exception can be caused due to error occurred while Re-executing Activity
 * @author Aniket
 *
 */
public class ActivityReExecutionException extends Exception {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;

	public ActivityReExecutionException() {
		super();
	}

	public ActivityReExecutionException(String message) {
		super(message);
	}

	public ActivityReExecutionException(Throwable cause) {
		super(cause);
	}

	public ActivityReExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActivityReExecutionException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
