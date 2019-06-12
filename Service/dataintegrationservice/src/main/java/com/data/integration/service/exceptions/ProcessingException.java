package com.data.integration.service.exceptions;

/**
 * This exception can be caused due to error while executing Transformation/Job
 * 
 * @author Aniket
 *
 */
public class ProcessingException extends Exception {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;

	public ProcessingException() {
		super();
	}

	public ProcessingException(String message) {
		super(message);
	}

	public ProcessingException(Throwable cause) {
		super(cause);
	}

	public ProcessingException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProcessingException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
