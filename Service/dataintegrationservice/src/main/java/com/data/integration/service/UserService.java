package com.data.integration.service;

import java.util.List;

import com.data.integration.data.User;
import com.data.integration.service.exceptions.IntegrationProcessException;
import com.data.integration.service.exceptions.UserException;
import com.data.integration.service.exceptions.UserNotFoundException;
import com.data.integration.service.exceptions.UsernameAlreadyExistException;

/**
 * UserService interface
 * 
 * @author Kalyani
 *
 */
public interface UserService {

	/**
	 * get User details by user ID.
	 * 
	 * @param userId
	 * @return User
	 * @throws UserNotFoundException
	 */
	public User getUserById(final Long userId) throws UserNotFoundException;

	/**
	 * get User details by User Name.
	 *
	 * @param userName
	 * @return User
	 * @throws UserNotFoundException
	 */
	public User getUserByLogin(final String userName) throws UserNotFoundException, IntegrationProcessException;

	/**
	 * @return List of all Users.
	 * @throws UserException
	 */
	public List<User> getAllUser() throws UserException;

	/**
	 * Create new User.
	 * 
	 * @param user
	 *            User to be created.
	 * @return Created User.
	 * @throws UserException
	 *             In case of invalid entity passed as argument.
	 * @throws UsernameAlreadyExistException
	 * 				In case of existing username passed as argument.
	 */
	public User createUser(final User user) throws UserException, UsernameAlreadyExistException;

	/**
	 * Update new User.
	 * 
	 * @param user
	 *            User to be updated.
	 * @return Updated User.
	 * @throws UserException
	 *             In case of invalid entity passed as argument.
	 */
	public User updateUser(final User user) throws UserException;

	/**
	 * Update User by id.
	 * 
	 * @param userID
	 *            The id of the User to be updated.
	 * @param user
	 *            The User with updated fields.
	 * @return Updated User.
	 * @throws UserException
	 *             In case of null User id passed as an argument, User with
	 *             given id not found or any other exception.
	 * @throws UserNotFoundException
	 *             In case of null id passed as an argument.
	 * @throws UsernameAlreadyExistException
	 */
	public User updateUserById(final Long userID, final User user)
			throws UserException, UserNotFoundException, UsernameAlreadyExistException;

	/**
	 * Delete User by id.
	 * 
	 * @param userId
	 *            The id of the User to be deleted.
	 * @return true if deletion is successful.
	 * @throws UserNotFoundException
	 *             In case of null id passed as an argument.
	 * @throws UserException
	 *             In case of null User id passed as an argument, User with
	 *             given id not found or any other exception.
	 */
	public boolean markUserDeactivatedById(final Long userId)
			throws UserNotFoundException, UserException;

}
