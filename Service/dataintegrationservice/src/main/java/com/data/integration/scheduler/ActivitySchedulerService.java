package com.data.integration.scheduler;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.data.integration.config.QuartzJobConfiguration;
import com.data.integration.data.Activity;
import com.data.integration.repository.ActivityRepository;
import com.data.integration.repository.IntegrationProcessRepository;
import com.data.integration.repository.SubscriberRepository;
import com.data.integration.service.enums.TriggerTypeEnum;
import com.data.integration.service.exceptions.ActivitySchedulerException;
import com.data.integration.service.vo.ExecuteIntegrationProcessVO;
import com.data.integration.service.vo.IntegrationProcessResultVO;
import com.data.integration.service.vo.ScheduledSetupVO;

/**
 * It is wrapper around Quartz Sceduling API,it is exposed as REST API,so we can
 * do scheduling related operation without restarting Server
 * 
 * @author Aniket
 *
 */
@Component
public class ActivitySchedulerService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ActivitySchedulerService.class);

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private SubscriberRepository subscriberRepository;
	
	@Autowired 
	private IntegrationProcessRepository integrationProcessRepository;

	/**
	 * This method Reschedule the execution of all Activities for
	 * subscriberUniqueKey which has TriggerType 'SCHEDULED'
	 * 
	 * @throws ActivitySchedulerException
	 */
	public IntegrationProcessResultVO rescheduleAllActivity(
			String subscriberUniqueKey) throws ActivitySchedulerException {
		List<Long> integrationProcessIDs=integrationProcessRepository.findBySubscriberUnique(subscriberUniqueKey);
		final Iterable<Activity> activities = activityRepository
				.findByTriggerTypeAndIntegrationProcessIDIn(
						TriggerTypeEnum.SCHEDULED, integrationProcessIDs);
		ObjectMapper objectMapper = new ObjectMapper();
		IntegrationProcessResultVO integrationProcessResultVO = new IntegrationProcessResultVO();
		LOGGER.info("ReScheduling activities");
		StringBuilder message = new StringBuilder("Rescheduling activities ");
		for (Activity activity : activities) {
			rescheduleActivity(objectMapper, message, activity);
		}

		integrationProcessResultVO.setMessage(message.toString());
		integrationProcessResultVO.setStatus(200);
		return integrationProcessResultVO;
	}

	private void rescheduleActivity(ObjectMapper objectMapper,
			StringBuilder message, Activity activity)
			throws ActivitySchedulerException {
		ScheduledSetupVO scheduledSetupVO = null;
		try {
			scheduledSetupVO = objectMapper.readValue(
					activity.getScheduleSetup(), ScheduledSetupVO.class);
			TriggerKey triggerKey = new TriggerKey(activity
					.getIntegrationProcessID().toString(), activity
					.getActivityID().toString());
			message.append("[ ActivityID=").append(activity.getActivityID())
					.append(",Cron Expression=")
					.append(scheduledSetupVO.getCronExpression()).append("]");
			CronTriggerImpl oldTrigger = null;
			oldTrigger = (CronTriggerImpl) schedulerFactoryBean.getScheduler()
					.getTrigger(triggerKey);
			oldTrigger.setCronExpression(scheduledSetupVO.getCronExpression());
			oldTrigger
					.setMisfireInstruction(CronTriggerImpl.MISFIRE_INSTRUCTION_DO_NOTHING);
			schedulerFactoryBean.getScheduler().rescheduleJob(triggerKey,
					oldTrigger);
		} catch (IOException e) {
			throw new ActivitySchedulerException(e);
		} catch (SchedulerException e) {
			throw new ActivitySchedulerException(e);
		} catch (ParseException e) {
			throw new ActivitySchedulerException(e);
		}
	}

	/**
	 * Stop scheduled Activity Execution.
	 * 
	 * @param activityID
	 * @return
	 * @throws ActivitySchedulerException
	 */
	public IntegrationProcessResultVO stopScheduledActivityExecution(
			Long activityID) throws ActivitySchedulerException {
		IntegrationProcessResultVO integrationProcessResultVO = new IntegrationProcessResultVO();
		Activity activity = activityRepository.findByActivityID(activityID);
		TriggerKey triggerKey = new TriggerKey(activity
				.getIntegrationProcessID().toString(), activity.getActivityID()
				.toString());
		try {
			boolean result = schedulerFactoryBean.getScheduler().unscheduleJob(
					triggerKey);
			if (result) {
				integrationProcessResultVO
						.setMessage("Scheduled Execution of Activity with ID ="
								+ activityID + " has been stopped.");
				integrationProcessResultVO.setStatus(200);
			} else {
				integrationProcessResultVO
						.setMessage("Error occured while stoping scheduled Execcution");
				integrationProcessResultVO.setStatus(500);
			}
		} catch (SchedulerException e) {
			throw new ActivitySchedulerException(e);
		}
		return integrationProcessResultVO;
	}

	/**
	 * create scheduled Activity Execution.
	 * 
	 * @param activityID
	 * @return
	 * @throws ActivitySchedulerException
	 */
	public IntegrationProcessResultVO createScheduledActivityExecution(Long activityID) throws ActivitySchedulerException {
		IntegrationProcessResultVO integrationProcessResultVO = new IntegrationProcessResultVO();
		ObjectMapper objectMapper = new ObjectMapper();
		Activity activity=activityRepository.findOne(activityID);
		TriggerKey triggerKey = new TriggerKey(activity
				.getIntegrationProcessID().toString(), activity.getActivityID()
				.toString());
		try {

			ScheduledSetupVO scheduledSetupVO = objectMapper.readValue(
					activity.getScheduleSetup(), ScheduledSetupVO.class);
			ExecuteIntegrationProcessVO executeIntegrationProcessVO = new ExecuteIntegrationProcessVO();
			executeIntegrationProcessVO.setActivityID(activity.getActivityID());
			executeIntegrationProcessVO
					.setCausesNewIntegrationProcessExecution(activity
							.isCausesNewProcessExecution());
			JobDetail jobDetail = QuartzJobConfiguration.jobDetail(
					executeIntegrationProcessVO.toJsonString(), activity);
			schedulerFactoryBean.getScheduler().addJob(jobDetail, true, true);
			//if Trigger exists then reschedule job,it will update Triggers
			if (schedulerFactoryBean.getScheduler().checkExists(triggerKey)) {
				StringBuilder message=new StringBuilder();
				rescheduleActivity(objectMapper,message, activity);
				integrationProcessResultVO.setMessage(message.toString());
			}else {
			//otherwise create new Trigger and schedule it
				schedulerFactoryBean.getScheduler().scheduleJob(
						QuartzJobConfiguration.cronTrigger(activity,
								scheduledSetupVO, jobDetail));
				integrationProcessResultVO
				.setMessage("Scheduled Execution of Activity with ID ="
						+ activity.getActivityID() + " with cron expression = "+scheduledSetupVO.getCronExpression()+" has been created.");
			}
			
			integrationProcessResultVO.setStatus(200);
		} catch (SchedulerException e) {
			throw new ActivitySchedulerException(e);
		} catch (JsonParseException e) {
			throw new ActivitySchedulerException(e);
		} catch (JsonMappingException e) {
			throw new ActivitySchedulerException(e);
		} catch (IOException e) {
			throw new ActivitySchedulerException(e);
		}
		return integrationProcessResultVO;
	}
	
	/**
     * scheduleActivites
     * 
     * @param activities
     * @throws SchedulerException
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * @author Akshay
     */
	public void scheduleActivites(Iterable<Activity> activities)
			throws SchedulerException, JsonParseException,
			JsonMappingException, IOException {
    	
    	Scheduler scheduler = schedulerFactoryBean.getScheduler();
    	ObjectMapper objectMapper = new ObjectMapper();
    	TriggerKey triggerKey = null;
    	ScheduledSetupVO scheduledSetupVO = null;
    	ExecuteIntegrationProcessVO executeIntegrationProcessVO = null;
    	JobDetail jobDetail = null;
    	
    	for(Activity activity : activities){
            
    	   //form Trigger key
		   triggerKey = new TriggerKey(activity.getIntegrationProcessID()
					.toString(), activity.getActivityID().toString());

		   //If trigger is not scheduled then add Job and schedule it
		   if (!scheduler.checkExists(triggerKey)) {
		     try {
				//get Scheduler setup
				scheduledSetupVO = objectMapper.readValue(
						activity.getScheduleSetup(), ScheduledSetupVO.class);

				executeIntegrationProcessVO = new ExecuteIntegrationProcessVO();
				executeIntegrationProcessVO.setActivityID(activity
						.getActivityID());
				executeIntegrationProcessVO
						.setCausesNewIntegrationProcessExecution(activity
								.isCausesNewProcessExecution());

				LOGGER.info("Scheduled ActivityID ={} ,Cron Expression = {}",
						activity.getActivityID(),scheduledSetupVO.getCronExpression());
              
				//create Quartz JobDetail
				jobDetail = QuartzJobConfiguration.jobDetail(
						executeIntegrationProcessVO.toJsonString(), activity);
				 //Add Job in Quartz scheduler
				scheduler.addJob(jobDetail, true, true);
				// Create trigger
				Trigger trigger = QuartzJobConfiguration.cronTrigger(activity, scheduledSetupVO, jobDetail);
				//Schedule the trigger
				scheduler.scheduleJob(trigger);
				
			}catch (IOException e) {
				LOGGER.error("Error occured while parsing ScheduledSetup", e);
			} catch (Exception e) {
				LOGGER.error("Error occured while Scheduling activities", e);
			}
				
		  }
	    }			
    }
    
    /**
     * Unschedule activities, Which are scheduled.
     * 
     * @param activity
     * @throws SchedulerException 
     * @author Akshay
     */
	public void unscheduleActivites(Iterable<Activity> activities) throws SchedulerException{
    
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = null;

		for (Activity activity : activities) {
            //form a trigger key for activity
			triggerKey = new TriggerKey(activity.getIntegrationProcessID()
					.toString(), activity.getActivityID().toString());
            
			//If Trigger exists unschedule it.
			if (scheduler.checkExists(triggerKey)){
				scheduler.unscheduleJob(triggerKey);
				LOGGER.info("Unscheduled ActivityID ={}", activity.getActivityID());
			}
		}
    }
	
}
