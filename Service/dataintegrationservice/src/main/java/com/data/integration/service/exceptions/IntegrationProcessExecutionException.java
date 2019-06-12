package com.data.integration.service.exceptions;

/**
 * This exception is raised when IntegrationProcess is disabled.
 * 
 * @author Aniket
 *
 */
public class IntegrationProcessExecutionException extends Exception {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;

	public IntegrationProcessExecutionException() {
		super();
	}

	public IntegrationProcessExecutionException(String message) {
		super(message);
	}

	public IntegrationProcessExecutionException(Throwable cause) {
		super(cause);
	}

	public IntegrationProcessExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public IntegrationProcessExecutionException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
