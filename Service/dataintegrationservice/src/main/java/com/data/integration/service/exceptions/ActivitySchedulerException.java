package com.data.integration.service.exceptions;

/**
 * This exception can be caused due to error while Rescheduling Activities
 * 
 * @author Aniket
 *
 */
public class ActivitySchedulerException extends Exception {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;

	public ActivitySchedulerException() {
		super();
	}

	public ActivitySchedulerException(String message) {
		super(message);
	}

	public ActivitySchedulerException(Throwable cause) {
		super(cause);
	}

	public ActivitySchedulerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActivitySchedulerException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
