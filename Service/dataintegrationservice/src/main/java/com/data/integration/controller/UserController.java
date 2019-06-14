package com.data.integration.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.data.integration.data.User;
import com.data.integration.service.UserService;
import com.data.integration.service.exceptions.IntegrationProcessException;
import com.data.integration.service.exceptions.UserException;
import com.data.integration.service.exceptions.UserNotFoundException;
import com.data.integration.service.exceptions.UsernameAlreadyExistException;

/**
 * User controller class defines all user related REST API's
 * 
 * @author Kalyani
 *
 */
@RestController
public class UserController {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@ApiOperation(value = "Get user details using id")
	@RequestMapping(method = RequestMethod.GET, path = "/Integration/user/id/{id}", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public User getUserByID(@PathVariable("id") Long userID)
			throws UserNotFoundException {

		User user = userService.getUserById(userID);

		return user;
	}

	@ApiOperation(value = "Get user details using user name")
	@RequestMapping(method = RequestMethod.GET, path = "/Integration/user/username/{name}", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public User getUserByLogin(@PathVariable("name") String userName)
			throws UserNotFoundException, IntegrationProcessException {

		User user = userService.getUserByLogin(userName);

		return user;
	}

	@ApiOperation(value = "Get all user details")
	@RequestMapping(method = RequestMethod.GET, path = "/Integration/users", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public List<User> getAllUser() throws UserNotFoundException, UserException {

		List<User> users = userService.getAllUser();
		return users;
	}

	@ApiOperation(value = "Create new User")
	@RequestMapping(method = RequestMethod.POST, path = "/Integration/user", produces = "application/json", consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public User createUser(@RequestBody final User user) throws UserException, UsernameAlreadyExistException {
		User userResult = userService.createUser(user);
		return userResult;
	}

	@ApiOperation(value = "Update new User")
	@RequestMapping(method = RequestMethod.PUT, path = "/Integration/user/{id}/update", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public User updateUser(@PathVariable("id") Long userID,
			@Valid @RequestBody final User user) throws UserException,
			UserNotFoundException, UsernameAlreadyExistException {
		User userResult = userService.updateUserById(userID, user);
		return userResult;
	}

	@ApiOperation(value = "Deactivate User")
	@RequestMapping(method = RequestMethod.PUT, path = "/Integration/user/{id}/deactivate")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public Boolean makeUserDeactivate(@PathVariable("id") Long userID)
			throws UserException, UserNotFoundException {
		return userService.markUserDeactivatedById(userID);
	}
}