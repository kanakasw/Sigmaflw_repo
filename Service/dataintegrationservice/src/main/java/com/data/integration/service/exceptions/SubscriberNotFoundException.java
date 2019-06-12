package com.data.integration.service.exceptions;

/**
 * SubscriberNotFoundException
 * 
 * @author Aniket
 *
 */
public class SubscriberNotFoundException extends Exception {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;

	public SubscriberNotFoundException() {
		super();
	}

	public SubscriberNotFoundException(String message) {
		super(message);
	}

	public SubscriberNotFoundException(Throwable cause) {
		super(cause);
	}

	public SubscriberNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public SubscriberNotFoundException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
