package com.data.integration.service.exceptions;

/**
 * This exception can be caused due to error while creating user with the existing user name.
 * 
 * @author Kalyani
 *
 */
public class UsernameAlreadyExistException extends Exception {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;

	public UsernameAlreadyExistException() {
		super();
	}

	public UsernameAlreadyExistException(String message) {
		super(message);
	}

	public UsernameAlreadyExistException(Throwable cause) {
		super(cause);
	}

	public UsernameAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public UsernameAlreadyExistException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
