package com.data.integration.service.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.data.integration.config.QuartzJobConfiguration;
import com.data.integration.data.Activity;
import com.data.integration.data.ActivityExecution;
import com.data.integration.data.IntegrationProcess;
import com.data.integration.data.IntegrationProcessExecution;
import com.data.integration.data.Subscriber;
import com.data.integration.repository.ActivityExecutionRepository;
import com.data.integration.repository.ActivityRepository;
import com.data.integration.repository.ApplicationVariableRepository;
import com.data.integration.repository.IntegrationProcessExecutionRepository;
import com.data.integration.repository.SubscriberRepository;
import com.data.integration.scheduler.ActivitySchedulerService;
import com.data.integration.service.ActivityExecutor;
import com.data.integration.service.ActivityService;
import com.data.integration.service.IntegrationEngine;
import com.data.integration.service.constant.AppConstant;
import com.data.integration.service.enums.ActivityExecutionStatusEnum;
import com.data.integration.service.enums.ActivityTypeEnum;
import com.data.integration.service.enums.IntegrationProcessExecutionStatusEnum;
import com.data.integration.service.enums.TriggerTypeEnum;
import com.data.integration.service.enums.WorkflowKeysEnum;
import com.data.integration.service.exceptions.ActivityConfigurationException;
import com.data.integration.service.exceptions.ActivityExecutionException;
import com.data.integration.service.exceptions.ActivityNotFoundException;
import com.data.integration.service.exceptions.ActivityReExecutionException;
import com.data.integration.service.exceptions.ActivitySchedulerException;
import com.data.integration.service.exceptions.InvalidActivityTypeException;
import com.data.integration.service.executor.factory.ActivityExecutorFactory;
import com.data.integration.service.util.ActivityUtil;
import com.data.integration.service.vo.ActivityExecutionOutcomeVO;
import com.data.integration.service.vo.ExecuteIntegrationProcessVO;
import com.data.integration.service.vo.IntegrationProcessResultVO;
import com.data.integration.service.vo.ScheduledSetupVO;
import com.google.common.collect.Iterables;

/**
 * Activity Service implementation class.
 * 
 * @author Aniket
 *
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    public static final Logger LOGGER = LoggerFactory
            .getLogger(ActivityServiceImpl.class);

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private ActivityExecutionRepository activityExecutionRepository;

    @Autowired
    private ActivityExecutorFactory stepExecutorFactory;

    @Autowired
    private IntegrationProcessExecutionRepository integrationProcessExecutionRepository;

    @Autowired
    private IntegrationEngine integrationEngine;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private ApplicationVariableRepository applicationVariableRepository;
    
    @Autowired
    private ActivitySchedulerService activitySchedulerService;
    
    @Value("${integration.files}")
    private String basePath;

    @Override
    public IntegrationProcessResultVO updateActivityCronExpression(
            Long activityID, ScheduledSetupVO scheduledSetupVO)
            throws ActivityNotFoundException, IOException,
            ActivitySchedulerException {

        ObjectMapper mapper = new ObjectMapper();
        Activity activity = activityRepository.findByActivityID(activityID);
        activity.setScheduleSetup(mapper.writeValueAsString(scheduledSetupVO));
        activityRepository.save(activity);

        IntegrationProcessResultVO integrationProcessResultVO = new IntegrationProcessResultVO();
        TriggerKey triggerKey = new TriggerKey(activity
                .getIntegrationProcessID().toString(), activity.getActivityID()
                .toString());
        try {

            ExecuteIntegrationProcessVO executeIntegrationProcessVO = new ExecuteIntegrationProcessVO();
            executeIntegrationProcessVO.setActivityID(activity.getActivityID());
            executeIntegrationProcessVO
                    .setCausesNewIntegrationProcessExecution(activity
                            .isCausesNewProcessExecution());
            JobDetail jobDetail = QuartzJobConfiguration.jobDetail(
                    executeIntegrationProcessVO.toJsonString(), activity);
            schedulerFactoryBean.getScheduler().addJob(jobDetail, true, true);
            // if Trigger exists then reschedule job,it will update Triggers
            if (schedulerFactoryBean.getScheduler().checkExists(triggerKey)) {
                StringBuilder message = new StringBuilder();
                rescheduleActivity(mapper, message, activity);
                integrationProcessResultVO.setMessage(message.toString());
            } else {
                // otherwise create new Trigger and schedule it
                schedulerFactoryBean.getScheduler().scheduleJob(
                        QuartzJobConfiguration.cronTrigger(activity,
                                scheduledSetupVO, jobDetail));
                integrationProcessResultVO
                        .setMessage("Scheduled Execution of Activity with ID ="
                                + activity.getActivityID()
                                + " with cron expression = "
                                + scheduledSetupVO.getCronExpression()
                                + " has been created.");
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

    @Override
    public Activity getActivityById(Long activityID)
            throws ActivityNotFoundException {
        Activity activity = activityRepository.findOne(activityID);
        if (activity == null) {
            throw new ActivityNotFoundException(String.format(
                    "Activity with ID = %d doesn't exists.", activityID));
        }

        return activity;
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

    @Override
    public List<Activity> getByIntegrationProcessID(Long integrationProcessID)
            throws ActivityNotFoundException {

        List<Activity> activities = activityRepository
                .findByIntegrationProcessID(integrationProcessID);

        if (activities == null || activities.isEmpty()) {
            throw new ActivityNotFoundException(
                    "Activities doesn't exist for IntegrationProcessID : "
                            + integrationProcessID);
        }

        return activities;
    }

    @Override
    public IntegrationProcessResultVO reExecuteActivity(Long activityExecutionID)
            throws ActivityExecutionException, ActivityReExecutionException {
        IntegrationProcessResultVO integrationProcessResultVO = new IntegrationProcessResultVO();
        ActivityExecution activityExecution = activityExecutionRepository
                .findByActivityExecutionID(activityExecutionID);

        if (!(activityExecution.getStatus().equals(
                ActivityExecutionStatusEnum.ERROR) || activityExecution
                .getStatus().equals(
                        ActivityExecutionStatusEnum.COMPLETED_WITH_ERROR))) {

            integrationProcessResultVO.setStatus(400);
            integrationProcessResultVO
                    .setMessage("Activity can not be Re-Executed.");
            return integrationProcessResultVO;
        }
        Activity activity = activityExecution.getActivity();

        IntegrationProcessExecution integrationProcessExecution = activityExecution
                .getIntegrationProcessExecution();
        
        //Reset integrationProcessExecution  Status as Processing
        integrationProcessExecution
                .setStatus(IntegrationProcessExecutionStatusEnum.PROCESSING);
        integrationProcessExecutionRepository.save(integrationProcessExecution);
        
        //Reset activityExecution StartTime
        activityExecution.setExecutionStartTime(new Date());
        activityExecutionRepository.save(activityExecution);
        
        Long integrationProcessExecutionID = activityExecution
                .getIntegrationProcessExecution()
                .getIntegrationProcessExecutionID();

        ActivityExecutionOutcomeVO activityExecutionOutcomeVO = null;

        activityExecutionOutcomeVO = getPreviousExecutionOutcome(
                activityExecution, activityExecutionOutcomeVO);
       
        executeActivityAsynchronously(activityExecution, activity,
                integrationProcessExecutionID, activityExecutionOutcomeVO);

        integrationProcessResultVO.setStatus(200);
        integrationProcessResultVO
                .setMessage("Activity submitted for ReExecution");
        return integrationProcessResultVO;
    }

    private ActivityExecutionOutcomeVO getPreviousExecutionOutcome(
            ActivityExecution activityExecution,
            ActivityExecutionOutcomeVO activityExecutionOutcomeVO)
            throws ActivityReExecutionException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            activityExecutionOutcomeVO = mapper.readValue(
                    activityExecution.getExecutionStepOutcome(),
                    ActivityExecutionOutcomeVO.class);
        } catch (IOException e) {
            throw new ActivityReExecutionException(
                    "Error occured while getting previous Activity execution details",
                    e);
        }
        return activityExecutionOutcomeVO;
    }

    private void executeActivityAsynchronously(
            ActivityExecution activityExecution, Activity activity,
            Long integrationProcessExecutionID,
            ActivityExecutionOutcomeVO activityExecutionOutcomeVO)
            throws ActivityReExecutionException {
        final ExecuteIntegrationProcessVO executeIntegrationProcessVO = activityExecutionOutcomeVO
                .getExecuteIntegrationProcessVO();
        try {

            BasicThreadFactory factory = new BasicThreadFactory.Builder()
                    .namingPattern("ActivityReExecutor-Thread-%d").build();
            ExecutorService executorService = Executors
                    .newSingleThreadExecutor(factory);
            ActivityExecutor activityExecutor = stepExecutorFactory
                    .getStepExecutor(activity.getActivityType());

            executorService.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        activityExecutor.executeActivity(
                                activityExecution.getActivityExecutionID(),
                                integrationProcessExecutionID,
                                executeIntegrationProcessVO);
                    } catch (ActivityExecutionException e) {
                        LOGGER.error(
                                "Error occured while Re-Executing activity", e);
                    }
                }
            });

        } catch (InvalidActivityTypeException e) {
            throw new ActivityReExecutionException("Invalid Activity Type ", e);
        }
    }

    @Override
    public JSONObject getInputParametersByActivityID(Long activityID)
            throws ActivityNotFoundException,
            org.json.simple.parser.ParseException, InvalidActivityTypeException {
        Activity activity = activityRepository.findByActivityID(activityID);
        if(!activity.getActivityType().equals(ActivityTypeEnum.INTEGRATION)){
            throw new InvalidActivityTypeException("Invalid Activity Type: currently input parameters API is available to call on Activity with type INTEGRATION.");
        }
        String processingSpecification = activity.getProcessingSpecification();
        Subscriber subscriber = subscriberRepository
                .findByActivityID(activityID);
        Map<String, Object> tagValues = new HashMap<String, Object>();
        tagValues.put(AppConstant.SUBSCRIBER_ID, subscriber.getSubscriberID());
        tagValues.put(AppConstant.INTEGRATION_PROCESS_ID,
                activity.getIntegrationProcessID());
        tagValues.put(AppConstant.BASE_FOLDER_PATH, basePath);
        String replacedSpecification = ActivityUtil.replaceString(
                processingSpecification, tagValues);
        JSONParser jsonParser = new JSONParser();
        JSONObject specficationObject = (JSONObject) jsonParser
                .parse(replacedSpecification);
        return integrationEngine.getWorkflowInfo(
                specficationObject.get(WorkflowKeysEnum.WORKFLOW_TYPE.getKey())
                        .toString(),
                specficationObject.get(
                        WorkflowKeysEnum.WORKFLOW_FILE_PATH.getKey())
                        .toString());
    }

    @Override
    public String[] getConstantVariable() throws IllegalArgumentException,
            IllegalAccessException {
        AppConstant appConstant = new AppConstant();
        Field[] fields = AppConstant.class.getDeclaredFields();
        String[] values = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            values[i] = "${" + fields[i].get(appConstant) + "}";
        }
        return values;
    }

	@Override
	public List<String> getUniqueKeywords(Long subscriberID)
			throws IllegalArgumentException, IllegalAccessException {
		List<String> keywords = null;
		if (subscriberID != null) {
			keywords = applicationVariableRepository
					.getKeywordsBySubscriberID(subscriberID);
		} else {
			keywords = applicationVariableRepository.getAllKeywords();
		}
    	for(int i = 0; i < keywords.size(); i++){
			String value = keywords.get(i);
			value = "${"+value+"}";
			keywords.set(i,value);
		}
        return keywords;
	}

    @SuppressWarnings("unchecked")
    @Override
    public void mergeInputParameters(Long activityID, JSONObject inputData) throws ActivityConfigurationException, ActivityNotFoundException {
      
        Activity activity = activityRepository.findOne(activityID);
        if(activity==null) {
            throw new ActivityNotFoundException(String.format("Activity with id %d doesn't exists", activityID));
        }
        String processingSpecification = activity.getProcessingSpecification();
        JSONObject jsonObject = null;
        JSONParser jsonParser = new JSONParser();
        try {
            jsonObject = (JSONObject) jsonParser.parse(processingSpecification);
            jsonObject.put(WorkflowKeysEnum.INPUT_PARAMETERS.getKey(),
                    inputData);
        } catch (org.json.simple.parser.ParseException e) {
           throw new ActivityConfigurationException("Error occured while Updating Input parameters",e);
        }
        activity.setProcessingSpecification(jsonObject.toJSONString());
        activityRepository.save(activity);
    }

	@SuppressWarnings("unchecked")
	@Override
	public void updateProcessingSpecification(Long activityID,
			JSONObject newProcessingSpecification)
			throws ActivityConfigurationException, ActivityNotFoundException {
		Activity activity = activityRepository.findOne(activityID);
		String inputData;
		if(activity==null) {
            throw new ActivityNotFoundException(String.format("Activity with id %d doesn't exists", activityID));
        }
        String existingprocessingSpecification = activity.getProcessingSpecification();
        JSONObject existingProcessingSpecObject = null;
        JSONParser jsonParser = new JSONParser();
        try {
            existingProcessingSpecObject = (JSONObject) jsonParser.parse(existingprocessingSpecification);
            inputData = (String) existingProcessingSpecObject.get(WorkflowKeysEnum.INPUT_PARAMETERS);
        } catch (org.json.simple.parser.ParseException e) {
           throw new ActivityConfigurationException("Error occured while Updating Processing Specification",e);
        }
        newProcessingSpecification.put(WorkflowKeysEnum.INPUT_PARAMETERS.getKey(),inputData==null||inputData.equalsIgnoreCase("null")?new JSONObject():inputData);
        activity.setProcessingSpecification(newProcessingSpecification.toJSONString());
        activityRepository.save(activity);
	}

	@Override
    public void updateActivitySchedulingConfig(IntegrationProcess integrationProcessSetup){
    	
		Iterable<Activity> activities = null;

		try {
			activities = getScheduledByIntegrationProcessID(integrationProcessSetup
					.getIntegrationProcessID());
			if (integrationProcessSetup.getEnabled()) {
				activitySchedulerService.scheduleActivites(activities);
			} else {
				activitySchedulerService.unscheduleActivites(activities);
			}
		} catch (ActivityNotFoundException activityNotFoundException) {
			LOGGER.error("While updating Activity Scheduling configuration, ",
					activityNotFoundException);
		} catch (SchedulerException schedulerException) {
			LOGGER.error("While updating Activity Scheduling configuration, ",
					schedulerException);
		} catch (JsonParseException | JsonMappingException jsonExecption) {
			LOGGER.error("While updating Activity Scheduling configuration, ",
					jsonExecption);
		} catch (IOException ioException) {
			LOGGER.error("While updating Activity Scheduling configuration, ",
					ioException);
		}
    }
    
	@Override
	public Iterable<Activity> getScheduledByIntegrationProcessID(
			Long integrationProcessID) throws ActivityNotFoundException {

		Iterable<Activity> activities = activityRepository
				.findByTriggerTypeAndIntegrationProcessID(
						TriggerTypeEnum.SCHEDULED, integrationProcessID);

		if (activities == null || Iterables.size(activities) == 0) {
			throw new ActivityNotFoundException(
					"Activities doesn't exist for IntegrationProcessID : "
							+ integrationProcessID);
		}

		return activities;
	}
}
