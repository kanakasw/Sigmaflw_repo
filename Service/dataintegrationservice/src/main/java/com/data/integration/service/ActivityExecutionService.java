package com.data.integration.service;

import java.util.List;

import com.data.integration.data.ActivityExecution;


/**
 * ActivityExecutionService interface
 * 
 * @author Aniket
 *
 */
public interface ActivityExecutionService {

	List<ActivityExecution> getActivityExecutionByProcessExecutionID(Long processExecutionID);
	
}
