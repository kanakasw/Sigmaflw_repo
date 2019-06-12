package com.data.integration.service.impl;

import java.io.IOException;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.integration.data.Activity;
import com.data.integration.data.ActivityExecution;
import com.data.integration.data.EventQueue;
import com.data.integration.data.IntegrationProcess;
import com.data.integration.data.IntegrationProcessExecution;
import com.data.integration.data.Subscriber;
import com.data.integration.repository.ActivityExecutionRepository;
import com.data.integration.repository.ActivityRepository;
import com.data.integration.repository.EventQueueRepository;
import com.data.integration.repository.IntegrationProcessExecutionRepository;
import com.data.integration.repository.IntegrationProcessRepository;
import com.data.integration.repository.SubscriberRepository;
import com.data.integration.service.ActivityExecutor;
import com.data.integration.service.IntegrationProcessExecutor;
import com.data.integration.service.enums.ActivityExecutionStatusEnum;
import com.data.integration.service.enums.EventQueueSpecEnum;
import com.data.integration.service.enums.EventQueueStatus;
import com.data.integration.service.enums.IntegrationProcessExecutionStatusEnum;
import com.data.integration.service.enums.WorkflowKeysEnum;
import com.data.integration.service.exceptions.ActivityConfigurationException;
import com.data.integration.service.exceptions.ActivityExecutionException;
import com.data.integration.service.exceptions.InvalidActivityTypeException;
import com.data.integration.service.exceptions.ProcessingException;
import com.data.integration.service.executor.factory.ActivityExecutorFactory;
import com.data.integration.service.vo.ActivityExecutionOutcomeVO;
import com.data.integration.service.vo.ExecuteIntegrationProcessVO;
import com.data.integration.service.vo.IntegrationProcessResultVO;


@Service
public class IntegrationProcessExecutorImpl implements
		IntegrationProcessExecutor {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(IntegrationProcessExecutorImpl.class);

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private IntegrationProcessExecutionRepository integrationProcessExecutionRepository;

	@Autowired
	private ActivityExecutionRepository activityExecutionRepository;

	@Autowired
	private ActivityExecutorFactory stepExecutorFactory;

	@Autowired
	private IntegrationProcessRepository integrationProcessRepository;

	@Autowired
	private SubscriberRepository subscriberRepository;

	@Autowired
	private EventQueueRepository eventQueueRepository;

	@Override
	//@Transactional
	public IntegrationProcessResultVO executeIntegrationProcess(
			ExecuteIntegrationProcessVO executeIntegrationProcessVO)
			throws ProcessingException, ActivityConfigurationException, ActivityExecutionException {

		IntegrationProcessResultVO integrationProcessResultVO=null;

		IntegrationProcessExecution integrationProcessExecution = null;

		// Get Activity By ID
		Activity activity = activityRepository
				.findByActivityID(executeIntegrationProcessVO.getActivityID());

		integrationProcessExecution = createIntegrationProcessExecution(
				executeIntegrationProcessVO, activity);

		IntegrationProcess integrationProcess = integrationProcessRepository
				.findByIntegrationProcessID(activity.getIntegrationProcessID());

		integrationProcessResultVO=createEventIfNeeded(executeIntegrationProcessVO, integrationProcessExecution,
				activity, integrationProcess);
		
		if(integrationProcessResultVO != null){
			//Event created
			return integrationProcessResultVO;
		}else {
			//Event not created
			integrationProcessResultVO= new IntegrationProcessResultVO();
		}
		
		ActivityExecution activityExecution = createActivityExecution(
				integrationProcessExecution, activity);
		ActivityExecutionOutcomeVO activityExecutionOutcomeVO=new ActivityExecutionOutcomeVO();
		activityExecutionOutcomeVO.setExecuteIntegrationProcessVO(executeIntegrationProcessVO);
		if (integrationProcessExecution != null) {
			integrationProcessResultVO
					.setIntegrationProcessExecutionId(integrationProcessExecution
							.getIntegrationProcessExecutionID());

			if (integrationProcessExecution.getStatus().equals(
					IntegrationProcessExecutionStatusEnum.ERROR)) {
				String errorMessage = "IntegrationProcessExecution is already in ERROR status, So IntegrationProcess Execution is aborted.";
				LOGGER.error(errorMessage);
				integrationProcessResultVO.setMessage(errorMessage);
				activityExecutionOutcomeVO.addLogEntries(errorMessage);
				 logActivityOutcome(activityExecution,activityExecutionOutcomeVO);
				 return integrationProcessResultVO;

			} else if (integrationProcessExecution.getStatus().equals(
					IntegrationProcessExecutionStatusEnum.COMPLETED)) {
				String message = "IntegrationProcessExecution can not continue,Dependent activity execution is not completed.";
				LOGGER.error(message);
				activityExecutionOutcomeVO.addLogEntries(message);
				 logActivityOutcome(activityExecution,activityExecutionOutcomeVO);
				 return integrationProcessResultVO;
			}
			try {
				ActivityExecutor activityExecutor = stepExecutorFactory
						.getStepExecutor(activity.getActivityType());

				activityExecutor.executeActivity(activityExecution
						.getActivityExecutionID(), integrationProcessExecution
						.getIntegrationProcessExecutionID(),
						executeIntegrationProcessVO);
			} catch (InvalidActivityTypeException e) {
				throw new ActivityExecutionException("Invalid Activity Type ",e);
			} 
		} else {

			String message = "Could not find IntegrationProcessExecution.";
			LOGGER.error(message);
			activityExecutionOutcomeVO.addLogEntries(message);
			 logActivityOutcome(activityExecution,activityExecutionOutcomeVO);
			 return integrationProcessResultVO;
		}
		return integrationProcessResultVO;
	}

	/**
	 * This methods creates event if "this activity" needs to be execute in asynchronous way.
	 * @param executeIntegrationProcessVO
	 * @param integrationProcessExecution
	 * @param activity
	 * @param integrationProcess
	 * @return
	 * @throws ActivityConfigurationException
	 */
	private IntegrationProcessResultVO createEventIfNeeded(
			ExecuteIntegrationProcessVO executeIntegrationProcessVO,
			IntegrationProcessExecution integrationProcessExecution,
			Activity activity, IntegrationProcess integrationProcess)
			throws ActivityConfigurationException {
		IntegrationProcessResultVO integrationProcessResultVO=null;
		if (integrationProcessExecution != null) {

				// If Activity specification contains Async flag then create event and
				// Return
				boolean eventCreated = createEventIfAsyncFlagFound(
						integrationProcessExecution
								.getIntegrationProcessExecutionID(),
						integrationProcess, executeIntegrationProcessVO,
						activity);
				if (eventCreated) {
					// TODO: Refactor the code below
					integrationProcessResultVO=new IntegrationProcessResultVO();
					Subscriber subscriber = subscriberRepository
							.findBySubscriberID(integrationProcess
									.getSubscriberID());
					integrationProcessResultVO
							.setIntegrationProcessExecutionId(integrationProcessExecution
									.getIntegrationProcessExecutionID());
					integrationProcessResultVO
							.setIntegrationProcessExecutionStatus(integrationProcessExecution
									.getStatus().name());
					integrationProcessResultVO
							.setIntegrationProcessUniqueReference(integrationProcess
									.getIntegrationProcessUniqueReference());
					integrationProcessResultVO
							.setSubscriberUniqueReference(subscriber
									.getSubscriberUniqueKey());
					integrationProcessResultVO.setStatus(200);
				}
			
		}
		return integrationProcessResultVO;
	}

	private void logActivityOutcome(
			ActivityExecution activityExecution,ActivityExecutionOutcomeVO activityExecutionOutcomeVO) {
		activityExecutionOutcomeVO
				.addLogEntries("Activity execution completed with ERROR Because,file is not available");

		activityExecution
				.setStatus(ActivityExecutionStatusEnum.COMPLETED_WITH_ERROR);
		activityExecution.setExecutionFinishTime(new Date());
		try {
			activityExecution
					.setExecutionStepOutcome(activityExecutionOutcomeVO
							.toJsonString());
		} catch (IOException e) {
			LOGGER.error("Error occured while logging execution Outcome {}"
					, activityExecutionOutcomeVO.toString());
		}
		activityExecutionRepository.save(activityExecution);
	}

	private ActivityExecution createActivityExecution(
			IntegrationProcessExecution integrationProcessExecution,
			Activity activity) {
		// create new ActivityExceution
		ActivityExecution activityExecution = new ActivityExecution();
		activityExecution.setActivity(activity);
		activityExecution.setExecutionStartTime(new Date());
		activityExecution
				.setIntegrationProcessExecution(integrationProcessExecution);
		activityExecution.setStatus(ActivityExecutionStatusEnum.PROCESSING);
		activityExecutionRepository.save(activityExecution);
		return activityExecution;
	}

	private IntegrationProcessExecution createIntegrationProcessExecution(
			ExecuteIntegrationProcessVO executeIntegrationProcessVO,
			Activity activity) {
		IntegrationProcessExecution integrationProcessExecution;
		// if Activity has causesNewExecution then create new
		// IntegrationProcessExecution

		if (activity.isCausesNewProcessExecution()
				&& executeIntegrationProcessVO
						.getCausesNewIntegrationProcessExecution()) {
			integrationProcessExecution = new IntegrationProcessExecution();
			integrationProcessExecution.setIntegrationProcessID(activity
					.getIntegrationProcessID());
			integrationProcessExecution.setExecutionStartTime(new Date());
			integrationProcessExecution
					.setStatus(IntegrationProcessExecutionStatusEnum.PROCESSING);
			integrationProcessExecutionRepository
					.save(integrationProcessExecution);
		} else {

			/*
			 * in case of collision of integration Process Exceution check if
			 * ExecuteIntegrationProcessVO contains integrationProcessExcetionID
			 * so we can pick correct execution
			 */
			if (executeIntegrationProcessVO.getIntegrationProcessExcutionID() != null) {
				integrationProcessExecution = integrationProcessExecutionRepository
						.findByIntegrationProcessExecutionID(executeIntegrationProcessVO
								.getIntegrationProcessExcutionID());
			} else {
				integrationProcessExecution = integrationProcessExecutionRepository
						.findTopByIntegrationProcessIDOrderByIntegrationProcessExecutionIDDesc(activity
								.getIntegrationProcessID());
			}

		}
		return integrationProcessExecution;
	}

	@SuppressWarnings("unchecked")
	private boolean createEventIfAsyncFlagFound(Long processExecutionID,
			IntegrationProcess integrationProcess,
			ExecuteIntegrationProcessVO executeIntegrationProcessVO,
			Activity activity) throws ActivityConfigurationException {
		try {
			JSONObject activitySpec = (JSONObject) new JSONParser()
					.parse(activity.getProcessingSpecification().toString());
			boolean isAsync = (boolean) activitySpec.getOrDefault("Async",
					false);
			if (executeIntegrationProcessVO.getAsync() && isAsync) {
				createEvent(processExecutionID,
						integrationProcess, activity.getEventGroupName(),
						executeIntegrationProcessVO);
				return true;
			}
		} catch (ParseException e) {
			throw new ActivityConfigurationException(
					String.format("Error occured while parsing activity specification for activityID= %d ,%s",activity.getActivityID()
							,activity.getProcessingSpecification().toString()),
					e);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private EventQueue createEvent(Long processExecutionID,
			IntegrationProcess integrationProcesses, String eventToTrigger,
			ExecuteIntegrationProcessVO executeIntegrationProcessVO) {

		EventQueue eventQueue = new EventQueue();
		eventQueue.setCreatedDate(new Date());
		eventQueue.setModifiedDate(new Date());
		eventQueue.setStatus(EventQueueStatus.READY);
		eventQueue.setIntegrationProcessExecutionID(processExecutionID);
		eventQueue.setIntegrationProcessID(integrationProcesses
				.getIntegrationProcessID());
		eventQueue.setSubscriberID(integrationProcesses.getSubscriberID());
		JSONObject eventQueueSpec = new JSONObject();
		eventQueueSpec.put(WorkflowKeysEnum.EVENT_TO_TRIGGER.getKey(),
				eventToTrigger);
		eventQueueSpec.put(EventQueueSpecEnum.EVENT_INPUT_DATA.getKey(),
				executeIntegrationProcessVO.getInputData());
		eventQueue.setEventSpecification(eventQueueSpec.toJSONString());
		eventQueueRepository.save(eventQueue);
		return eventQueue;
	}

}
