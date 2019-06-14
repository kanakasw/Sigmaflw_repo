package com.data.integration.service.exceptions;

/**
 * This exception can be caused due to error while executing Transformation/Job
 * 
 * @author Aniket
 *
 */
public class IntegrationProcessException extends Exception {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;

	public IntegrationProcessException() {
		super();
	}

	public IntegrationProcessException(String message) {
		super(message);
	}

	public IntegrationProcessException(Throwable cause) {
		super(cause);
	}

	public IntegrationProcessException(String message, Throwable cause) {
		super(message, cause);
	}

	public IntegrationProcessException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
