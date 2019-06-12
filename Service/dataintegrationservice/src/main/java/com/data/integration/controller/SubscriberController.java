package com.data.integration.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.data.integration.data.Subscriber;
import com.data.integration.service.SubscriberService;
import com.data.integration.service.exceptions.SubscriberNotFoundException;

/**
 * Subscriber controller class defines all subscriber related REST API's
 * 
 * @author Chetan
 *
 */
@RestController
public class SubscriberController {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(SubscriberController.class);

	@Autowired
	private SubscriberService subscriberService;

	@ApiOperation(value = "Get subscriber details using user name")
	@RequestMapping(method = RequestMethod.GET, path = "/Integration/subscriber/username/{userName}", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public Subscriber getSubscriber(@PathVariable("userName") String userName)
			throws SubscriberNotFoundException {

		Subscriber subscriber = subscriberService
				.getSubscriberByLogin(userName);
		subscriber.setPassword(null);
		
		return subscriber;
	}

}
