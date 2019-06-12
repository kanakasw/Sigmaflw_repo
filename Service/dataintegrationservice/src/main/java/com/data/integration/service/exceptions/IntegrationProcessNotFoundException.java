package com.data.integration.service.exceptions;

/**
 * This exception can be caused due to IntegrationProcessNotFoundException
 * 
 * @author Aniket
 *
 */
public class IntegrationProcessNotFoundException extends Exception {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;

	public IntegrationProcessNotFoundException() {
		super();
	}

	public IntegrationProcessNotFoundException(String message) {
		super(message);
	}

	public IntegrationProcessNotFoundException(Throwable cause) {
		super(cause);
	}

	public IntegrationProcessNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public IntegrationProcessNotFoundException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
