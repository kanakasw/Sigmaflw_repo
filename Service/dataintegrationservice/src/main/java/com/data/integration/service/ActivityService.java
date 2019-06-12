package com.data.integration.service;

import java.io.IOException;
import java.util.List;

import org.json.simple.JSONObject;

import com.data.integration.data.Activity;
import com.data.integration.data.IntegrationProcess;
import com.data.integration.service.exceptions.ActivityConfigurationException;
import com.data.integration.service.exceptions.ActivityExecutionException;
import com.data.integration.service.exceptions.ActivityNotFoundException;
import com.data.integration.service.exceptions.ActivityReExecutionException;
import com.data.integration.service.exceptions.ActivitySchedulerException;
import com.data.integration.service.exceptions.InvalidActivityTypeException;
import com.data.integration.service.exceptions.SubscriberNotFoundException;
import com.data.integration.service.vo.IntegrationProcessResultVO;
import com.data.integration.service.vo.ScheduledSetupVO;

/**
 * ActivityService interface
 * 
 * @author Chetan
 *
 */
public interface ActivityService {

	/**
	 * update Activity.
	 * 
	 * @param subscriberId
	 * @return Subscriber
	 * @throws IOException
	 * @throws ActivitySchedulerException
	 * @throws SubscriberNotFoundException
	 */
	public IntegrationProcessResultVO updateActivityCronExpression(
			Long activityID, ScheduledSetupVO scheduledSetupVO)
			throws ActivityNotFoundException, IOException,
			ActivitySchedulerException;

	/**
	 * get Activity by ID.
	 * 
	 * @param subscriberId
	 * @return Subscriber
	 * @throws SubscriberNotFoundException
	 */
	public Activity getActivityById(Long activityID)
			throws ActivityNotFoundException;
	
	
	/**
     * get Input Parameters of Integration Type Activity.
     * 
     * @param subscriberId
     * @return Subscriber
	 * @throws InvalidActivityTypeException 
     * @throws SubscriberNotFoundException
     */
    public JSONObject getInputParametersByActivityID(Long activityID)
            throws ActivityNotFoundException,org.json.simple.parser.ParseException, InvalidActivityTypeException;

	/**
	 * Get all activities by IntegrationProcessID
	 * 
	 * @param integrationProcessID
	 * @return List<Activity>
	 * @throws ActivityNotFoundException
	 */
	public List<Activity> getByIntegrationProcessID(Long integrationProcessID)
			throws ActivityNotFoundException;
	
	
	/**
     * Get all activities by IntegrationProcessID
     * 
     * @param integrationProcessID
     * @return List<Activity>
	 * @throws ActivityExecutionException 
	 * @throws ActivityReExecutionException 
     * @throws ActivityNotFoundException
     */
    public IntegrationProcessResultVO reExecuteActivity(Long activityExecutionID) throws ActivityExecutionException, ActivityReExecutionException;
    
    
    /**
     * Get all constant variables of AppConstant class
     * @return Field[] 
     * @throws IllegalArgumentException
     * @throws IllegalAccessException*/
    public String[] getConstantVariable() throws IllegalArgumentException, IllegalAccessException;
	
    
    /**
     * Get all constant variables of AppConstant class
     * @return Field[] 
     * @throws IllegalArgumentException
     * @throws IllegalAccessException*/
    public List<String> getUniqueKeywords(Long subscriberID) throws IllegalArgumentException, IllegalAccessException;
    
    
    /**
     * Merge Input parameters in activity specification
     * @param activityID
     * @param inputData
     * @throws ActivityConfigurationException 
     * @throws ActivityNotFoundException 
     */
    public void mergeInputParameters(Long activityID,JSONObject inputData) throws ActivityConfigurationException, ActivityNotFoundException;

    /**
     * Merge Input parameters in activity specification
     * @param activityID
     * @param inputData
     * @throws ActivityConfigurationException 
     * @throws ActivityNotFoundException 
     */
    public void updateProcessingSpecification(Long activityID,JSONObject processingSpecification) throws ActivityConfigurationException, ActivityNotFoundException;

    /**
     * Get Scheduled Activities By Integration ProcessID
     * 
     * @param integrationProcessID Long
     * @return List<Activity>
     * @throws ActivityNotFoundException
     * @author Akshay
     */
    public Iterable<Activity> getScheduledByIntegrationProcessID(Long integrationProcessID)throws ActivityNotFoundException;
    
    /**
     * <b>Update Activity Scheduling Configuration.</b>
     * <p>
     *  <i> If Process is disabled then scheduled activites will be unscheduled.
     *  If process is enabled then activites will be scheduled.</i>
     * </p>
     * 
     * @param integrationProcessSetup
     * @author Akshay
     */
    public void updateActivitySchedulingConfig(IntegrationProcess integrationProcessSetup);
}
