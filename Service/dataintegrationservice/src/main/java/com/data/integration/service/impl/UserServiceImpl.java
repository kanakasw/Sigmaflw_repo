package com.data.integration.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.integration.data.IntegrationProcess;
import com.data.integration.data.User;
import com.data.integration.repository.UserRepository;
import com.data.integration.service.IntegrationProcessService;
import com.data.integration.service.UserService;
import com.data.integration.service.enums.UserRoleEnum;
import com.data.integration.service.enums.UserStatusEnum;
import com.data.integration.service.exceptions.IntegrationProcessException;
import com.data.integration.service.exceptions.UserException;
import com.data.integration.service.exceptions.UserNotFoundException;
import com.data.integration.service.exceptions.UsernameAlreadyExistException;

/**
 * User service implementation class.
 * 
 * @author Kalyani
 *
 */
@Service
public class UserServiceImpl implements UserService {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private IntegrationProcessService integrationProcessService;

	@Override
	public User getUserById(Long userID) throws UserNotFoundException {

		User user = userRepository.findByUserId(userID);

		if (user == null) {
			throw new UserNotFoundException("User with ID : " + userID
					+ " doesn't exists.");
		}

		return user;
	}

	@Override
	public User getUserByLogin(String userName) throws UserNotFoundException, IntegrationProcessException {

		User user = userRepository.findByLogin(userName);

		if (user == null) {
			throw new UserNotFoundException("User with User Name : " + userName
					+ " doesn't exists.");
		}

		if(user.getRole().equals(UserRoleEnum.ADMIN)){

			List<IntegrationProcess> integrationProcesses = integrationProcessService.getAllIntegrationProcess();
			Set<IntegrationProcess> integrationProcessesSet = new HashSet<IntegrationProcess>(integrationProcesses);

			user.setIntegrationProcesses(integrationProcessesSet);
		}

		return user;
	}

	@Override
	public List<User> getAllUser() throws UserException {

		try {
			Iterable<User> iterableUser = userRepository.findAll();

			List<User> users = IteratorUtils.toList(iterableUser.iterator());
			return users;
		} catch (Exception exception) {
			throw new UserException("Error occured while fetching all Users.");
		}
	}

	@Override
	public User createUser(User user) throws UserException, UsernameAlreadyExistException {

		if (user.getUserId() == null) {

			User userExistingByLogin = userRepository.findByLogin(user
					.getLogin());
			if (userExistingByLogin == null) {
				user.setStatus(UserStatusEnum.ACTIVE);
				/*user.setRole(UserRoleEnum.APP_USER);*/

				User userResult = userRepository.save(user);
				return userResult;
			} else {
				throw new UsernameAlreadyExistException(
						"Error occured while saving User. User with given Login name already exists.");
			}

		} else {
			throw new UserException(
					"Error occured while saving User. User with given id already exists.");
		}
	}

	@Override
	public User updateUser(User user) throws UserException {
		User userResult = userRepository.save(user);
		return userResult;
	}

	@Override
	public User updateUserById(Long userID, User user) throws UserException,
			UserNotFoundException, UsernameAlreadyExistException {
		User userResult = null;
		User userExisting = getUserById(userID);
		if (userExisting != null) {

			User userExistingByLogin = userRepository.findByLoginNotInUserId(user.getLogin(), userID);

			if(userExistingByLogin == null){
				user.setUserId(userID);
				/*user.setRole(userExisting.getRole());*/
				user.setUserPassword(userExisting.getUserPassword());
				user.setIntegrationProcesses(userExisting.getIntegrationProcesses());

				userResult = updateUser(user);
			}else{
				throw new UsernameAlreadyExistException(
						"Error occured while saving User. User with given Login name already exists.");
			}
		} else {
			throw new UserException("Cannot update User. User with id: "
					+ userID + " does not exists.");
		}

		return userResult;
	}

	@Override
	public boolean markUserDeactivatedById(Long userId)
			throws UserNotFoundException, UserException {
		boolean result = false;
		User userResult = getUserById(userId);
		if (userResult != null) {
			userResult.setStatus(UserStatusEnum.DELETED);
			updateUser(userResult);
			result = true;
		}
		return result;
	}

}
