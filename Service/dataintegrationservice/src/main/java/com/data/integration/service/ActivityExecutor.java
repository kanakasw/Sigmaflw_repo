package com.data.integration.service;

import com.data.integration.service.exceptions.ActivityExecutionException;
import com.data.integration.service.vo.ExecuteIntegrationProcessVO;

/**
 * ActivityExecutor interface
 * 
 * @author Aniket
 *
 */
public interface ActivityExecutor {

	/**
	 * Execute the step.
	 * 
	 * @param activityExecutionID
	 * @param integrationProcessExecutionID
	 */
	void executeActivity(Long activityExecutionID, Long integrationProcessExecutionID,
			ExecuteIntegrationProcessVO executeIntegrationProcessVO) throws ActivityExecutionException;
}
