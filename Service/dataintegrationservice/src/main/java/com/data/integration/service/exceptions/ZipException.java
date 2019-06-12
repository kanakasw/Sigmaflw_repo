package com.data.integration.service.exceptions;

/**
 * This exception can be caused due to error while executing Transformation/Job
 * 
 * @author Aniket
 *
 */
public class ZipException extends RuntimeException {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;

	public ZipException() {
		super();
	}

	public ZipException(String message) {
		super(message);
	}

	public ZipException(Throwable cause) {
		super(cause);
	}

	public ZipException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZipException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
