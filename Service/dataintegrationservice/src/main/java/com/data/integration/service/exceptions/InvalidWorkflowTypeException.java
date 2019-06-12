package com.data.integration.service.exceptions;

/**
 * InvalidWorkflowTypeException
 * 
 * @author Aniket
 *
 */
public class InvalidWorkflowTypeException extends Exception {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;

	public InvalidWorkflowTypeException() {
		super();
	}

	public InvalidWorkflowTypeException(String message) {
		super(message);
	}

	public InvalidWorkflowTypeException(Throwable cause) {
		super(cause);
	}

	public InvalidWorkflowTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidWorkflowTypeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
