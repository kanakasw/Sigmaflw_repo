package com.data.integration.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.integration.data.ActivityExecution;
import com.data.integration.data.IntegrationProcessExecution;
import com.data.integration.repository.ActivityExecutionRepository;
import com.data.integration.repository.IntegrationProcessExecutionRepository;
import com.data.integration.service.ActivityExecutor;
import com.data.integration.service.enums.ActivityExecutionStatusEnum;
import com.data.integration.service.enums.IntegrationProcessExecutionStatusEnum;
import com.data.integration.service.exceptions.ActivityExecutionException;
import com.data.integration.service.vo.ExecuteIntegrationProcessVO;

@Service
public class ENDActivityExecutorImpl implements ActivityExecutor {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ENDActivityExecutorImpl.class);

	@Autowired
	private IntegrationProcessExecutionRepository integrationProcessExecutionRepository;

	@Autowired
	private ActivityExecutionRepository activityExecutionRepository;

	@Override
	public void executeActivity(Long activityExecutionID,
			Long integrationProcessExecutionID,
			ExecuteIntegrationProcessVO executeIntegrationProcessVO)
			throws ActivityExecutionException {

		LOGGER.info("END Step Executor,activityID = {}",
				executeIntegrationProcessVO.getActivityID());

		ActivityExecution activityExecution = activityExecutionRepository
				.findByActivityExecutionID(activityExecutionID);
		activityExecution.setExecutionFinishTime(new Date());
		activityExecution.setStatus(ActivityExecutionStatusEnum.COMPLETED);
		activityExecution.setExecutionStepOutcome("{\"message\":\"Process execution completed successfully.\"}");
		activityExecutionRepository.save(activityExecution);
		IntegrationProcessExecution integrationProcessExecution = integrationProcessExecutionRepository
				.findByIntegrationProcessExecutionID(integrationProcessExecutionID);
		integrationProcessExecution.setExecutionFinishTime(new Date());
		integrationProcessExecution
				.setStatus(IntegrationProcessExecutionStatusEnum.COMPLETED);
		integrationProcessExecutionRepository.save(integrationProcessExecution);
	}

}
