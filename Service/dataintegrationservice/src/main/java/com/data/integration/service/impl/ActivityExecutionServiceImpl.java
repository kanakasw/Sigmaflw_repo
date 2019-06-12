package com.data.integration.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.integration.data.ActivityExecution;
import com.data.integration.repository.ActivityExecutionRepository;
import com.data.integration.service.ActivityExecutionService;

/**
 * ActivityExecution Service implementation class.
 * 
 * @author Aniket
 *
 */
@Service
public class ActivityExecutionServiceImpl implements ActivityExecutionService {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(ActivityExecutionServiceImpl.class);

	@Autowired
	private ActivityExecutionRepository activityExecutionRepository;

	@Override
	public List<ActivityExecution> getActivityExecutionByProcessExecutionID(
			Long processExecutionID) {
		return activityExecutionRepository
				.findByIntegrationProcessExecutionID(processExecutionID);

	}

}
