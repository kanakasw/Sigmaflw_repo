package com.data.integration.service.exceptions;

/**
 *  InvalidStepTypeException
 * @author Aniket
 *
 */
public class InvalidActivityTypeException extends Exception {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;

	public InvalidActivityTypeException() {
		super();
	}

	public InvalidActivityTypeException(String message) {
		super(message);
	}

	public InvalidActivityTypeException(Throwable cause) {
		super(cause);
	}

	public InvalidActivityTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidActivityTypeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
