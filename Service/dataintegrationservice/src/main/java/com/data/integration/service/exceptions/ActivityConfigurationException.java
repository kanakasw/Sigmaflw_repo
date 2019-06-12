package com.data.integration.service.exceptions;

/**
 * 
 * This exception can be caused due to invalid/unparsable activity Configuration
 * @author Aniket
 *
 */
public class ActivityConfigurationException extends Exception {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;

	public ActivityConfigurationException() {
		super();
	}

	public ActivityConfigurationException(String message) {
		super(message);
	}

	public ActivityConfigurationException(Throwable cause) {
		super(cause);
	}

	public ActivityConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActivityConfigurationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
