package com.data.integration.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.integration.data.Activity;
import com.data.integration.data.IntegrationProcess;
import com.data.integration.data.IntegrationProcessExecution;
import com.data.integration.data.User;
import com.data.integration.repository.IntegrationProcessExecutionRepository;
import com.data.integration.repository.IntegrationProcessRepository;
import com.data.integration.service.IntegrationProcessExecutor;
import com.data.integration.service.IntegrationProcessService;
import com.data.integration.service.enums.IntegrationProcessExecutionStatusEnum;
import com.data.integration.service.exceptions.ActivityConfigurationException;
import com.data.integration.service.exceptions.ActivityExecutionException;
import com.data.integration.service.exceptions.IntegrationProcessException;
import com.data.integration.service.exceptions.IntegrationProcessExecutionException;
import com.data.integration.service.exceptions.IntegrationProcessNotFoundException;
import com.data.integration.service.exceptions.ProcessingException;
import com.data.integration.service.exceptions.UserException;
import com.data.integration.service.util.RandomStringGenerator;
import com.data.integration.service.vo.ExecuteIntegrationProcessVO;
import com.data.integration.service.vo.IntegrationProcessResultVO;

/**
 * IntegrationProcessService implementation class
 * 
 * @author Chetan
 *
 */
@Service
public class IntegrationProcessServiceImpl implements IntegrationProcessService {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(IntegrationProcessServiceImpl.class);

	@Autowired
	private IntegrationProcessRepository integrationProcessRepository;

	@Autowired
	private IntegrationProcessExecutor integrationProcessExecutor;

	@Autowired
	private IntegrationProcessExecutionRepository integrationProcessExecutionRepository;

	@Override
	public List<IntegrationProcess> getBySubscriberUserName(String userName)
			throws IntegrationProcessNotFoundException {

		List<IntegrationProcess> processes = integrationProcessRepository
				.findBySubscriberUserName(userName);

		if (processes == null || processes.isEmpty()) {
			throw new IntegrationProcessNotFoundException(
					"Processes doesn't exist for subscriber : " + userName);
		}

		return processes;
	}

	@Override
	public IntegrationProcessResultVO executeIntegrationProcess(
			Long integrationProcessID)
			throws IntegrationProcessNotFoundException, ProcessingException,
			ActivityConfigurationException, ActivityExecutionException,
			IntegrationProcessExecutionException {

		IntegrationProcessResultVO integrationProcessResultVO = new IntegrationProcessResultVO();
		IntegrationProcess integrationProcess = integrationProcessRepository
				.findByIntegrationProcessID(integrationProcessID);
		if (integrationProcess != null) {

			if (!integrationProcess.getEnabled()) {
				throw new IntegrationProcessExecutionException(
						String.format(
								"Process with ID: %d can not be executed,because it is disabled.",
								integrationProcess.getIntegrationProcessID()));
			}
			Activity initActivity = integrationProcess.getActivities().stream()
					.filter(x -> true == x.isCausesNewProcessExecution())
					.sorted(Comparator.comparingLong(Activity::getActivityOrderIndex))
					.findFirst().orElse(null);
			if (initActivity != null) {

				ExecuteIntegrationProcessVO executeIntegrationProcessVO = new ExecuteIntegrationProcessVO();
				executeIntegrationProcessVO.setActivityID(initActivity
						.getActivityID());
				executeIntegrationProcessVO
						.setCausesNewIntegrationProcessExecution(initActivity
								.isCausesNewProcessExecution());
				integrationProcessExecutor
						.executeIntegrationProcess(executeIntegrationProcessVO);
				integrationProcessResultVO.setStatus(200);
				integrationProcessResultVO
						.setMessage("Integration Process execution started");

			}
		} else {
			throw new IntegrationProcessNotFoundException(String.format(
					"Integration process with ID %d doesn't exists.",
					integrationProcessID));
		}
		return integrationProcessResultVO;
	}

	@Override
	public IntegrationProcess getIntegrationProcessByID(
			Long integrationProcessID)
			throws IntegrationProcessNotFoundException {
		IntegrationProcess integrationProcess = integrationProcessRepository
				.findByIntegrationProcessID(integrationProcessID);
		if (integrationProcess == null) {
			throw new IntegrationProcessNotFoundException(String.format(
					"IntegrationProcess with ID %d doesn't exists.",
					integrationProcessID));
		} else {
			return integrationProcess;
		}
	}

	@Override
	public List<IntegrationProcess> getAllIntegrationProcess()
			throws IntegrationProcessException {

		List<IntegrationProcess> processes = null;
		try {

			Iterable<IntegrationProcess> iterableIntegrationProcess = integrationProcessRepository
					.findAll();
			processes = IteratorUtils.toList(iterableIntegrationProcess
					.iterator());

		} catch (Exception exception) {
			throw new IntegrationProcessException(
					"Error occured while fetching all Integration Processes.");
		}

		return processes;
	}

	@Override
	public IntegrationProcessExecution getCurrentIntegrationProcessExecution(
			Long integrationProcessID)
			throws IntegrationProcessNotFoundException {
		IntegrationProcessExecution integrationProcessExecution = integrationProcessExecutionRepository
				.findTopByIntegrationProcessIDOrderByIntegrationProcessExecutionIDDesc(integrationProcessID);
		return integrationProcessExecution;
	}

	@Override
	public List<IntegrationProcessExecution> getPreviousIntegrationProcessExecution(
			Long integrationProcessID)
			throws IntegrationProcessNotFoundException {
		List<IntegrationProcessExecution> integrationProcessExecutionList = integrationProcessExecutionRepository
				.findByIntegrationProcessIDOrderByIntegrationProcessExecutionIDDesc(integrationProcessID);
		return integrationProcessExecutionList;
	}

	@Override
	public String getAverageExecutionTime(Long integrationProcessID) {
		Long avgExecutionTimeInMills = integrationProcessExecutionRepository
				.getAvarageExecutionTime(integrationProcessID);
		String avgExecution = "Not Available";
		if (avgExecutionTimeInMills != null && avgExecutionTimeInMills != 0) {
			avgExecution = DurationFormatUtils.formatDurationWords(
					avgExecutionTimeInMills, true, true);
		}
		return avgExecution;
	}

	@Override
	public IntegrationProcessExecution getLastSuccessIntegrationProcessExecution(
			Long integrationProcessID)
			throws IntegrationProcessNotFoundException {
		IntegrationProcessExecution integrationProcessExecution = integrationProcessExecutionRepository
				.findTopByIntegrationProcessIDAndStatusOrderByIntegrationProcessExecutionIDDesc(
						integrationProcessID,
						IntegrationProcessExecutionStatusEnum.COMPLETED);
		return integrationProcessExecution;
	}

	@Override
	public void updateIntegrationProcessSetup(Long integrationProcessID,
			IntegrationProcess integrationProcessSetup)
			throws IntegrationProcessNotFoundException {
		// TODO Auto-generated method stub
		IntegrationProcess integrationProcess = integrationProcessRepository
				.findByIntegrationProcessID(integrationProcessID);
		if (integrationProcess == null) {
			throw new IntegrationProcessNotFoundException(String.format(
					"Integration process with id %d doesn't exists",
					integrationProcessID));
		}
		integrationProcess.setFileEncryptionKey(integrationProcessSetup
				.getFileEncryptionKey());
		integrationProcess.setEnabled(integrationProcessSetup.getEnabled());
		integrationProcess.setUserId(integrationProcessSetup.getUserId());
		integrationProcessRepository.save(integrationProcess);
	}

	@Override
	public IntegrationProcess createIntegrationProcessSetup(
			IntegrationProcess integrationProcessSetup)
			throws IntegrationProcessException {
		IntegrationProcess integrationProcess = integrationProcessRepository
				.findByIntegrationProcessID(integrationProcessSetup
						.getIntegrationProcessID());
		if (integrationProcess == null) {
			integrationProcessSetup.setEnabled(true);
			integrationProcessSetup.setIntegrationProcessUniqueReference(RandomStringGenerator.generateString());

			Set<Activity> activities = integrationProcessSetup.getActivities();
			if (activities != null && !activities.isEmpty()) {
				for (Activity activityInLoop : activities) {
					activityInLoop.setActivityKey(RandomStringGenerator.generateString());
				}
			}

			IntegrationProcess integrationProcessResult =  integrationProcessRepository.save(integrationProcessSetup);
			integrationProcessResult = populateForeignKeysInChildEntities(integrationProcessResult);
			return integrationProcessResult;
		} else {
			throw new IntegrationProcessException(
					"Integration process with id %d already exists");
		}
	}

	private IntegrationProcess populateForeignKeysInChildEntities(final IntegrationProcess integrationProcess) {

		Long integrationProcessId = integrationProcess.getIntegrationProcessID();

		Set<Activity> activities = integrationProcess.getActivities();
		if (activities != null && !activities.isEmpty()) {
			for (Activity activityInLoop : activities) {

				activityInLoop.setIntegrationProcessID(integrationProcessId);
			}
		}

		Set<IntegrationProcessExecution> executions = integrationProcess.getIntegrationProcessExecutions();
		if (executions != null && !executions.isEmpty()) {
			for (IntegrationProcessExecution executionInLoop : executions) {
				executionInLoop.setIntegrationProcessID(integrationProcessId);
			}
		}

		return integrationProcess;
	}

	@Override
	public boolean makeIntegrationProcessDisabledById(Long integrationProcessID)
			throws IntegrationProcessNotFoundException,
			IntegrationProcessException {
		boolean result = false;
		IntegrationProcess integrationProcess = getIntegrationProcessByID(integrationProcessID);
		if (integrationProcess != null) {
			integrationProcess.setEnabled(false);
			updateIntegrationProcessSetup(integrationProcessID, integrationProcess);
			result = true;
		}
		return result;
	}

}
