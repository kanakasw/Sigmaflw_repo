package com.data.integration.scheduler;

import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.data.integration.data.Activity;
import com.data.integration.data.EventQueue;
import com.data.integration.repository.ActivityRepository;
import com.data.integration.repository.EventQueueRepository;
import com.data.integration.service.IntegrationProcessExecutor;
import com.data.integration.service.enums.EventQueueSpecEnum;
import com.data.integration.service.enums.EventQueueStatus;
import com.data.integration.service.enums.WorkflowKeysEnum;
import com.data.integration.service.exceptions.ActivityConfigurationException;
import com.data.integration.service.exceptions.ActivityExecutionException;
import com.data.integration.service.exceptions.ProcessingException;
import com.data.integration.service.vo.ExecuteIntegrationProcessVO;

@Component
public class EventProcessorJob {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EventProcessorJob.class);

	@Autowired
	EventQueueRepository eventQueueRepository;

	@Autowired
	private IntegrationProcessExecutor integrationProcessExecutor;

	@Autowired
	private ActivityRepository activityRepository;

	@Scheduled(fixedRateString = "${event.processor.job.fixedRate}")
	public void processEvent() {
		List<EventQueue> eventListWithDistinctExecutionId = null;

		eventListWithDistinctExecutionId = eventQueueRepository
				.findDistinctIntegrationProcessExecutionIDByStatusNot(EventQueueStatus.COMPLETED);

		for (EventQueue eventQueue : eventListWithDistinctExecutionId) {
			EventQueue innerEventQueue = eventQueueRepository
					.findTopByStatusNotAndIntegrationProcessExecutionIDOrderByCreatedDateAsc(
							EventQueueStatus.COMPLETED,
							eventQueue.getIntegrationProcessExecutionID());
			if (innerEventQueue.getStatus().equals(EventQueueStatus.PROCESSING)) {
				continue;
			}
			String eventSpecification = innerEventQueue.getEventSpecification();
			JSONObject eventSpecificationJson;
			try {
				eventSpecificationJson = (JSONObject) new JSONParser()
						.parse(eventSpecification);
				String eventToTrigger = eventSpecificationJson.get(
						WorkflowKeysEnum.EVENT_TO_TRIGGER.getKey()).toString();
				Activity initActivity = activityRepository
						.findOneByIntegrationProcessIDAndEventGroupNameOrderByActivityOrderIndex(
								innerEventQueue.getIntegrationProcessID(),
								eventToTrigger);

				Object sourceFile = eventSpecificationJson
						.get(WorkflowKeysEnum.SOURCE_FILE_PATH.getKey());
				String sourceFilePath = sourceFile != null ? sourceFile
						.toString() : null;

				Object inputData = eventSpecificationJson
						.get(EventQueueSpecEnum.EVENT_INPUT_DATA.getKey());
				String inputDataToPass = inputData != null ? inputData
						.toString() : null;
						
				Object unzip = eventSpecificationJson
								.get(EventQueueSpecEnum.UN_ZIP.getKey());
				Boolean unzipFlag = unzip != null ? (boolean) unzip
								 : false;		

				if (initActivity != null) {
					ExecuteIntegrationProcessVO executeIntegrationProcessVO = new ExecuteIntegrationProcessVO();
					executeIntegrationProcessVO.setActivityID(initActivity
							.getActivityID());
					executeIntegrationProcessVO
							.setIntegrationProcessExcutionID(innerEventQueue
									.getIntegrationProcessExecutionID());
					// CausesNewIntegrationProcessExecution need to set false,
					// Because without execution event can't exists
					executeIntegrationProcessVO
							.setCausesNewIntegrationProcessExecution(false);
					executeIntegrationProcessVO
							.setSourcefilePath(sourceFilePath);
					executeIntegrationProcessVO.setInputData(inputDataToPass);
					executeIntegrationProcessVO.setUnzip(unzipFlag);
					
					innerEventQueue.setStatus(EventQueueStatus.PROCESSING);
					eventQueueRepository.save(innerEventQueue);
					executeNextActivity(executeIntegrationProcessVO,eventQueueRepository,innerEventQueue);

				} else {
					LOGGER.error("Their is no Activity,with EventGroup ={}"
							, eventToTrigger);
				}

			} catch (ParseException e) {
				LOGGER.error(
						"Error occured while parsing EventSpecification JSON string",
						e);
			} catch (ProcessingException e) {
				LOGGER.error("Error occured while processing event", e);
			} catch (Exception e) {
				LOGGER.error("Error occured while processing event", e);
			}

		}
	}

	@Async
	private void executeNextActivity(
			ExecuteIntegrationProcessVO executeIntegrationProcessVO, EventQueueRepository eventQueueRepository2, EventQueue innerEventQueue)
			throws ProcessingException, ActivityConfigurationException,
			ActivityExecutionException {
		integrationProcessExecutor
				.executeIntegrationProcess(executeIntegrationProcessVO);
		innerEventQueue.setStatus(EventQueueStatus.COMPLETED);
		eventQueueRepository.save(innerEventQueue);
	}

}
