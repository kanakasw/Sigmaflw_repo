package com.data.integration.service.exceptions;

/**
 * InvalidFilePathException
 * 
 * @author Aniket
 *
 */
public class InvalidFilePathException extends Exception {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;

	public InvalidFilePathException() {
		super();
	}

	public InvalidFilePathException(String message) {
		super(message);
	}

	public InvalidFilePathException(Throwable cause) {
		super(cause);
	}

	public InvalidFilePathException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidFilePathException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
