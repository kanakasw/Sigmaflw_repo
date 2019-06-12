package com.data.integration.service.exceptions;

/**
 * SubscriberNotFoundException
 * 
 * @author Aniket
 *
 */
public class IntegrationProcessExecutionNotFoundException extends Exception {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;

	public IntegrationProcessExecutionNotFoundException() {
		super();
	}

	public IntegrationProcessExecutionNotFoundException(String message) {
		super(message);
	}

	public IntegrationProcessExecutionNotFoundException(Throwable cause) {
		super(cause);
	}

	public IntegrationProcessExecutionNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public IntegrationProcessExecutionNotFoundException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
