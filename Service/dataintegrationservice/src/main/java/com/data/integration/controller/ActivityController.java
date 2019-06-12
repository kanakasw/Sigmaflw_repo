package com.data.integration.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.io.IOException;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.data.integration.data.Activity;
import com.data.integration.scheduler.ActivitySchedulerService;
import com.data.integration.service.ActivityService;
import com.data.integration.service.exceptions.ActivityConfigurationException;
import com.data.integration.service.exceptions.ActivityExecutionException;
import com.data.integration.service.exceptions.ActivityNotFoundException;
import com.data.integration.service.exceptions.ActivityReExecutionException;
import com.data.integration.service.exceptions.ActivitySchedulerException;
import com.data.integration.service.exceptions.InvalidActivityTypeException;
import com.data.integration.service.vo.IntegrationProcessResultVO;
import com.data.integration.service.vo.ScheduledSetupVO;

/**
 * Activity controller class defines all activity related REST API's
 * 
 * @author Aniket
 *
 */
@RestController
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivitySchedulerService activitySchedulerService;

    @ApiOperation(value = "Get all activities details for a integration process")
    @RequestMapping(method = RequestMethod.GET, path = "/Integration/Activity/integrationprocess/{integrationProcessID}", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure") })
    @ResponseBody
    public List<Activity> getIntegrationProcesses(
            @PathVariable("integrationProcessID") Long integrationProcessID)
            throws Exception {

        return activityService.getByIntegrationProcessID(integrationProcessID);
    }

    @ApiOperation(value = "update Activity scheduled expression")
    @RequestMapping(method = RequestMethod.PUT, path = "/Integration/Activity/updateCron/{activityID}", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure") })
    @ResponseBody
    public IntegrationProcessResultVO updateActivityScheduledExpression(
            @PathVariable("activityID") Long activityID,
            @RequestBody ScheduledSetupVO scheduledSetupVO)
            throws ActivityNotFoundException, IOException,
            ActivitySchedulerException {

    	System.out.println("Activation Id : "+ activityID);
    	System.out.println("scheduledSetupVO Expression Id : "+ scheduledSetupVO.getCronExpression());
    	
        IntegrationProcessResultVO integrationProcessResultVO = activityService
                .updateActivityCronExpression(activityID, scheduledSetupVO);

        return integrationProcessResultVO;
    }
    
/*    @ApiOperation(value = "update Activity scheduled expression")
    @RequestMapping(method = RequestMethod.PUT, path = "/Integration/Activity/updateCron/{activityID}", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure") })
    @ResponseBody
    public void updateActivityScheduledExpression1(
            @PathVariable("activityID") Long activityID,
            @RequestBody String scheduledSetupVO)
            throws ActivityNotFoundException, IOException,
            ActivitySchedulerException {

     	System.out.println("Activation Id : "+ activityID);
    	System.out.println("String for testing : "+ scheduledSetupVO);
    	
    }*/

    @ApiOperation(value = "update Input Parametrs")
    @RequestMapping(method = RequestMethod.PUT, path = "/Integration/Activity/{activityID}/updateInputParameters", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure") })
    @ResponseBody
    public IntegrationProcessResultVO updateActivityInputParameters(
            @PathVariable("activityID") Long activityID,
            @RequestBody JSONObject inputParameters)
            throws ActivityNotFoundException, ActivityConfigurationException {

        activityService.mergeInputParameters(activityID, inputParameters);

        IntegrationProcessResultVO integrationProcessResultVO=new IntegrationProcessResultVO();
        integrationProcessResultVO.setStatus(200);
        integrationProcessResultVO.setMessage("Input parameters updated successfully.");
        return integrationProcessResultVO;

    }
    
    @ApiOperation(value = "update processing specification")
    @RequestMapping(method = RequestMethod.PUT, path = "/Integration/Activity/{activityID}/updateProcessingSpecification", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure") })
    @ResponseBody
    public IntegrationProcessResultVO updateActivityProcessingSpecification(
            @PathVariable("activityID") Long activityID,
            @RequestBody JSONObject processingSpecification)
            throws ActivityNotFoundException, ActivityConfigurationException {

        activityService.updateProcessingSpecification(activityID, processingSpecification);

        IntegrationProcessResultVO integrationProcessResultVO=new IntegrationProcessResultVO();
        integrationProcessResultVO.setStatus(200);
        integrationProcessResultVO.setMessage("Processing Specification updated successfully.");
        return integrationProcessResultVO;
    }
    

    @ApiOperation(value = "Get Activity by ID")
    @RequestMapping(method = RequestMethod.GET, path = "/Integration/Activity/{activityID}", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure") })
    @ResponseBody
    public Activity getActivityByID(@PathVariable("activityID") Long activityID)
            throws ActivityNotFoundException {
        Activity activity = activityService.getActivityById(activityID);
        return activity;
    }

    @ApiOperation(value = "ReExecute Activity by activityExecutionID")
    @RequestMapping(method = RequestMethod.GET, path = "/Integration/Activity/ActivityExecution/{activityExecutionID}/ReExecute", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure") })
    @ResponseBody
    public IntegrationProcessResultVO ReExecuteActivity(
            @PathVariable("activityExecutionID") Long activityExecutionID)
            throws ActivityExecutionException, ActivityReExecutionException {
        IntegrationProcessResultVO integrationProcessResultVO = activityService
                .reExecuteActivity(activityExecutionID);
        return integrationProcessResultVO;
    }

    @ApiOperation(value = "Get InputParametrs By ActivityID")
    @RequestMapping(method = RequestMethod.GET, path = "/Integration/Activity/{activityID}/getInputParameters", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure") })
    @ResponseBody
    public JSONObject getInputParameters(
            @PathVariable("activityID") Long activityID)
            throws ActivityNotFoundException, ParseException,
            InvalidActivityTypeException {

        return activityService.getInputParametersByActivityID(activityID);
    }

    @ApiOperation(value = "Get Constant variables of AppConstant Class")
    @RequestMapping(method = RequestMethod.GET, path = "/Integration/getAllFields", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure") })
    @ResponseBody
    public String[] getConstantVariables() throws IllegalArgumentException,
            IllegalAccessException {
        return activityService.getConstantVariable();
    }

    @ApiOperation(value = "Reschedule All Activities of Subscriber")
    @RequestMapping(value = "/Integration/Integration/Subscriber/{subscriberUniqueKey}/rescheduleAllActivities", method = RequestMethod.GET, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure") })
    @ResponseBody
    public IntegrationProcessResultVO rescheduleActivities(
            @PathVariable("subscriberUniqueKey") String subscriberUniqueKey)
            throws ActivitySchedulerException {
        IntegrationProcessResultVO integrationProcessResultVO = activitySchedulerService
                .rescheduleAllActivity(subscriberUniqueKey);
        return integrationProcessResultVO;
    }

    @ApiOperation(value = "Stop scheduled activity execution")
    @RequestMapping(value = "/Integration/Integration/ActivityID/{activityID}/stopScheduledExecution", method = RequestMethod.GET, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure") })
    @ResponseBody
    public IntegrationProcessResultVO stopScheduledExecution(
            @PathVariable("activityID") Long activityID)
            throws ActivitySchedulerException {
        IntegrationProcessResultVO integrationProcessResultVO = activitySchedulerService
                .stopScheduledActivityExecution(activityID);
        return integrationProcessResultVO;
    }

    @ApiOperation(value = "Create scheduled activity execution")
    @RequestMapping(value = "/Integration/Integration/ActivityID/{activityID}/createScheduledExecution", method = RequestMethod.GET, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = IntegrationProcessResultVO.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure") })
    @ResponseBody
    public IntegrationProcessResultVO createScheduledExecution(
            @PathVariable("activityID") Long activityID)
            throws ActivitySchedulerException {
        IntegrationProcessResultVO integrationProcessResultVO = activitySchedulerService
                .createScheduledActivityExecution(activityID);
        return integrationProcessResultVO;
    }

    @ApiOperation(value = "Get Keywords from Applicationvariable table")
    @RequestMapping(method = RequestMethod.GET, path = "/Integration/subscriber/{subscriberID}/getUniqueKeywords", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure") })
    @ResponseBody
    public List<String> getUniqueKeywords(
            @PathVariable("subscriberID") Long subscriberID)
            throws IllegalArgumentException, IllegalAccessException {
        return activityService.getUniqueKeywords(subscriberID);
    }
    
    
}
