package com.data.integration.service;

import com.data.integration.data.Subscriber;
import com.data.integration.service.exceptions.SubscriberNotFoundException;

/**
 * SubscriberService interface
 * 
 * @author Chetan
 *
 */
public interface SubscriberService {

	/**
	 * Get the subscriber details.
	 * 
	 * @param subscriberId
	 * @return Subscriber
	 * @throws SubscriberNotFoundException
	 */
	public Subscriber getSubscriber(Long subscriberId)
			throws SubscriberNotFoundException;

	/**
	 * Get the subscriber details by using login name.
	 * 
	 * @param loginName
	 * @return Subscriber
	 * @throws SubscriberNotFoundException
	 */
	public Subscriber getSubscriberByLogin(String loginName)
			throws SubscriberNotFoundException;
}
