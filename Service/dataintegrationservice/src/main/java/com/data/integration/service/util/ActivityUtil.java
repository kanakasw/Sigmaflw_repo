package com.data.integration.service.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.data.integration.config.HazelcastConfiguration;
import com.data.integration.data.Activity;
import com.data.integration.data.ActivityExecution;
import com.data.integration.data.ApplicationVariable;
import com.data.integration.data.EventQueue;
import com.data.integration.data.IntegrationProcess;
import com.data.integration.data.IntegrationProcessExecution;
import com.data.integration.data.Subscriber;
import com.data.integration.repository.ActivityRepository;
import com.data.integration.repository.ApplicationVariableRepository;
import com.data.integration.repository.EventQueueRepository;
import com.data.integration.repository.IntegrationProcessExecutionRepository;
import com.data.integration.repository.IntegrationProcessRepository;
import com.data.integration.repository.SubscriberRepository;
import com.data.integration.service.constant.AppConstant;
import com.data.integration.service.enums.ApplicationVariableTypeEnum;
import com.data.integration.service.enums.EventQueueSpecEnum;
import com.data.integration.service.enums.EventQueueStatus;
import com.data.integration.service.enums.WorkflowKeysEnum;
import com.data.integration.service.exceptions.TagValueReplacerException;
import com.data.integration.service.vo.ActivityExecutionContextVO;
import com.data.integration.service.vo.ExecuteIntegrationProcessVO;
import com.hazelcast.core.HazelcastInstance;

/**
 * This class contains Utility methods for PDI Integration Engine java API
 * 
 * @author Aniket
 *
 */
@Component
public class ActivityUtil {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ActivityUtil.class);
    @Autowired
    private ApplicationVariableRepository applicationVariableRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;
    
    @Autowired
    private HazelcastInstance hazelcastInstance;
    
    @Autowired
    private IntegrationProcessExecutionRepository integrationProcessExecutionRepository;
    
    @Autowired
    private IntegrationProcessRepository integrationProcessRepository;
    
    @Autowired
    private EventQueueRepository eventQueueRepository;

    @Autowired
    private ActivityRepository activityRepository;
    
    @Value("${integration.files}")
    private String basePath;
    
    public JSONObject getInputJson(String specification,
            ActivityExecution activityExecution,
            ExecuteIntegrationProcessVO executeIntegrationProcessVO,
            IntegrationProcessRepository integrationProcessRepository,
            String basePath) throws TagValueReplacerException {

        Map<String, Object> tagValues = new HashMap<String, Object>();

        IntegrationProcessExecution integrationProcessExecution = activityExecution
                .getIntegrationProcessExecution();
        Activity activity = activityExecution.getActivity();

        Long integrationProcessID = integrationProcessExecution
                .getIntegrationProcessID();

        IntegrationProcess integrationProcess = integrationProcessRepository
                .findByIntegrationProcessID(integrationProcessID);

        Subscriber subscriber = subscriberRepository
                .findBySubscriberID(integrationProcess.getSubscriberID());

        ActivityExecutionContextVO activityExecutionContextVO = new ActivityExecutionContextVO(
                subscriber, integrationProcess, integrationProcessExecution,
                activityExecution, activity, executeIntegrationProcessVO);
        try {
            tagValues = replaceApplicationVariables(activityExecutionContextVO);
            String replacedSpecification = replaceString(specification,
                    tagValues);
            JSONParser jsonParser = new JSONParser();
            return (JSONObject) jsonParser.parse(replacedSpecification);
        } catch (NoSuchFieldException | SecurityException
                | IllegalArgumentException | IllegalAccessException
                | ParseException | ClassNotFoundException e) {
            throw new TagValueReplacerException(
                    "Error occured while Replacing Tag values", e);
        }

    }

    public static String replaceString(String specification,
            Map<String, Object> tagValues) {
        StrSubstitutor tagReplacer = new StrSubstitutor(tagValues);
        String replacedSpecification = tagReplacer.replace(specification);
        return replacedSpecification;
    }

    public Map<String, Object> replaceApplicationVariables(
            final ActivityExecutionContextVO activityExecutionContextVO)
            throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException, ClassNotFoundException {

        Map<String, Object> tagValues = new HashMap<String, Object>();
        List<ApplicationVariable> applicationVariables = null;

        applicationVariables = applicationVariableRepository
                .findBySubscriberIDIsNull();

        // get Application Level variables
        tagValues = resolveApplicationVariables(activityExecutionContextVO,
                tagValues, applicationVariables);

        tagValues.put(AppConstant.BASE_FOLDER_PATH, basePath);
        
        applicationVariables = applicationVariableRepository
                .findBySubscriberID(activityExecutionContextVO.getSubscriber()
                        .getSubscriberID());

        // get Subscriber Level variables
        tagValues = resolveApplicationVariables(activityExecutionContextVO,
                tagValues, applicationVariables);

        return tagValues;
    }

    public Map<String, Object> resolveApplicationVariables(
            ActivityExecutionContextVO activityExecutionContextVO,
            final Map<String, Object> tagValues,
            final List<ApplicationVariable> applicationVariables)
            throws NoSuchFieldException, IllegalAccessException, SecurityException, IllegalArgumentException, ClassNotFoundException {
        for (ApplicationVariable applicationVariable : applicationVariables) {

            if (applicationVariable.getType().equals(
                    ApplicationVariableTypeEnum.CONSTANT)) {
                tagValues.put(applicationVariable.getKeyword(),
                        applicationVariable.getValue());
            } else {
                Object resolveValue = replaceApplicationVariable(
                        activityExecutionContextVO,
                        applicationVariable.getValue());
                tagValues.put(applicationVariable.getKeyword(), resolveValue);
            }
        }

        return tagValues;
    }

    /*public Object replaceApplicationVariable(
            ActivityExecutionContextVO activityExecutionContextVO,
            final String value) throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException {

        String parts[] = StringUtils.split(value, ".");
        Field field = null;
        switch (parts[0]) {

        case "Subscriber":
            field = Subscriber.class.getDeclaredField(parts[1]);
            field.setAccessible(true);
            return field.get(activityExecutionContextVO.getSubscriber());

        case "IntegrationProcess":
            field = IntegrationProcess.class.getDeclaredField(parts[1]);
            field.setAccessible(true);
            return field
                    .get(activityExecutionContextVO.getIntegrationProcess());

        case "Activity":
            field = Activity.class.getDeclaredField(parts[1]);
            field.setAccessible(true);
            return field.get(activityExecutionContextVO.getActivity());

        case "ActivityExecution":
            field = ActivityExecution.class.getDeclaredField(parts[1]);
            field.setAccessible(true);
            return field.get(activityExecutionContextVO.getActivityExecution());

        case "IntegrationProcessExecution":
            field = IntegrationProcessExecution.class
                    .getDeclaredField(parts[1]);
            field.setAccessible(true);
            return field.get(activityExecutionContextVO
                    .getIntegrationProcessExecution());

        case "ExecuteIntegrationProcessVO":
            field = ExecuteIntegrationProcessVO.class
                    .getDeclaredField(parts[1]);
            field.setAccessible(true);
            return field.get(activityExecutionContextVO
                    .getExecuteIntegrationProcessVO());
        default:
            return null;
        }
    }*/
    
    
   public Object replaceApplicationVariable(
            ActivityExecutionContextVO activityExecutionContextVO,
            final String value) throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException, ClassNotFoundException {

        String parts[] = StringUtils.split(value, ".");
        Field[] contextFields = ActivityExecutionContextVO.class.getDeclaredFields();
        Object foundValue=null;
        Class clazz=null;
        for (Field contextField : contextFields) {
            
            try {
                clazz = Class.forName("com.data.integration.data."+parts[0]);
            } catch(ClassNotFoundException e){
                clazz = Class.forName("com.data.integration.service.vo."+parts[0]); 
            }
            if(contextField.getType().equals(clazz)){
            contextField.setAccessible(true);
            Object executionContextInnerObject = contextField
                    .get(activityExecutionContextVO);
            Field field = executionContextInnerObject.getClass().getDeclaredField(
                    parts[1]);
            field.setAccessible(true);
            foundValue=field.get(executionContextInnerObject);
            break;
            }
        }
           
        return foundValue;
    }
   
   
   public void callNextActivityOrEvent(Long integrationProcessExecutionID,
           ExecuteIntegrationProcessVO executeIntegrationProcessVO,
           JSONObject requestJson, Long integrationProcessID) {
       Long stepToTrigger = (Long) requestJson
               .get(WorkflowKeysEnum.STEP_TO_TRIGGER.getKey());
       String eventToTrigger = (String) requestJson
               .get(WorkflowKeysEnum.EVENT_TO_TRIGGER.getKey());

       if (stepToTrigger != null) {
           LOGGER.info("Next Step to trigger found : {}", stepToTrigger);
         Activity  activity=activityRepository.findOneByIntegrationProcessIDAndActivityOrderIndex(integrationProcessID, stepToTrigger);
           ExecuteIntegrationProcessVO executeIntegrationProcessVO2 = new ExecuteIntegrationProcessVO();
           executeIntegrationProcessVO2.setActivityID(activity.getActivityID());
           executeIntegrationProcessVO2
                   .setIntegrationProcessExcutionID(integrationProcessExecutionID);
           executeIntegrationProcessVO2
                   .setCausesNewIntegrationProcessExecution(false);
           try {
               hazelcastInstance
                       .getQueue(
                               HazelcastConfiguration.EXECUTEINTEGRATIONPROCESSQUEUE)
                       .add(executeIntegrationProcessVO2.toJsonString());
           } catch (IOException e) {
               LOGGER.error("Error occured while adding item to Queue :",
                       e);
           }

       } else if (eventToTrigger != null) {
           LOGGER.info("Next event to trigger found : {}", eventToTrigger);

           createEventForNextActivityExecution(eventToTrigger,
                   integrationProcessExecutionID,
                   executeIntegrationProcessVO.getSourcefilePath());

       } else {
           LOGGER.info("Either API call or Scheduler will trigger next Activity");
       }
   }
   
   @SuppressWarnings("unchecked")
   private void createEventForNextActivityExecution(String eventToTrigger,
           Long integrationProcessExecutionID, String sourceFilePath) {
       IntegrationProcessExecution execution = integrationProcessExecutionRepository
               .findOne(integrationProcessExecutionID);
       IntegrationProcess integrationProcess = integrationProcessRepository
               .findOne(execution.getIntegrationProcessID());
       EventQueue eventQueue = new EventQueue();
       eventQueue.setSubscriberID(integrationProcess.getSubscriberID());
       eventQueue.setIntegrationProcessID(integrationProcess
               .getIntegrationProcessID());
       eventQueue.setStatus(EventQueueStatus.READY);
       eventQueue.setCreatedDate(new Date());
       eventQueue.setModifiedDate(new Date());
       eventQueue
               .setIntegrationProcessExecutionID(integrationProcessExecutionID);
       JSONObject eventQueueSpec = new JSONObject();
       eventQueueSpec.put(WorkflowKeysEnum.EVENT_TO_TRIGGER.getKey(),
               eventToTrigger);
       eventQueueSpec.put(WorkflowKeysEnum.SOURCE_FILE_PATH.getKey(),
               sourceFilePath);
       String ext = FilenameUtils.getExtension(sourceFilePath);
       if (ZipUtil.ZIP_FILE_EXTENSION.equalsIgnoreCase(ext)) {
           eventQueueSpec.put(EventQueueSpecEnum.UN_ZIP.getKey(), true);
       }
       eventQueue.setEventSpecification(eventQueueSpec.toJSONString());
       eventQueueRepository.save(eventQueue);
   }
}
