package com.data.integration.service.exceptions;

/**
 * This exception can be caused due to error while executing Transformation/Job
 * 
 * @author Aniket
 *
 */
public class IntegrationActivityExecutionException extends Exception {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;

	public IntegrationActivityExecutionException() {
		super();
	}

	public IntegrationActivityExecutionException(String message) {
		super(message);
	}

	public IntegrationActivityExecutionException(Throwable cause) {
		super(cause);
	}

	public IntegrationActivityExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public IntegrationActivityExecutionException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
