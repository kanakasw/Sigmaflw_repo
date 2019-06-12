package com.data.integration.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.integration.data.Subscriber;
import com.data.integration.repository.SubscriberRepository;
import com.data.integration.service.SubscriberService;
import com.data.integration.service.exceptions.SubscriberNotFoundException;

/**
 * Subscriber service implementation class.
 * 
 * @author Chetan
 *
 */
@Service
public class SubscriberServiceImpl implements SubscriberService {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(SubscriberServiceImpl.class);

	@Autowired
	private SubscriberRepository subscriberRepository;

	@Override
	public Subscriber getSubscriber(Long subscriberID)
			throws SubscriberNotFoundException {

		Subscriber subscriber = subscriberRepository
				.findBySubscriberID(subscriberID);

		if (subscriber == null) {
			throw new SubscriberNotFoundException("Subscriber with ID : "
					+ subscriberID + " doesn't exists.");
		}

		return subscriber;
	}

	@Override
	public Subscriber getSubscriberByLogin(String loginName)
			throws SubscriberNotFoundException {

		Subscriber subscriber = subscriberRepository.findByLogin(loginName);

		if (subscriber == null) {
			throw new SubscriberNotFoundException(
					"Subscriber with login name : " + loginName
							+ " doesn't exists.");
		}

		return subscriber;
	}

}
