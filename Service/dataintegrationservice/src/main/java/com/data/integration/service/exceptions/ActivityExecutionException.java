package com.data.integration.service.exceptions;

/**
 * This exception can be caused due to Invalid values in activity configuration
 * 
 * @author Aniket
 *
 */
public class ActivityExecutionException extends Exception {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;

	public ActivityExecutionException() {
		super();
	}

	public ActivityExecutionException(String message) {
		super(message);
	}

	public ActivityExecutionException(Throwable cause) {
		super(cause);
	}

	public ActivityExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActivityExecutionException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
