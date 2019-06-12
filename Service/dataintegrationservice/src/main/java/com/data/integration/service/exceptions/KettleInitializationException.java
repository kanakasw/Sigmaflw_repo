package com.data.integration.service.exceptions;

/**
 * This exception can be caused due to error while initializating Kettle
 * 
 * @author Aniket
 *
 */
public class KettleInitializationException extends Exception {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;

	public KettleInitializationException() {
		super();
	}

	public KettleInitializationException(String message) {
		super(message);
	}

	public KettleInitializationException(Throwable cause) {
		super(cause);
	}

	public KettleInitializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public KettleInitializationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
