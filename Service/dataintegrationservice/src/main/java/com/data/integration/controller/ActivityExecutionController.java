package com.data.integration.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.data.integration.data.ActivityExecution;
import com.data.integration.service.ActivityExecutionService;

/**
 * ActivityExecution controller class defines all ActivityExecution related API
 * 
 * @author Aniket
 *
 */
@RestController
public class ActivityExecutionController {

	@Autowired
	private ActivityExecutionService activityExecutionService;

	@ApiOperation(value = "Get All ActivityExecutions using IntegrationProcessExecutionID ")
	@RequestMapping(method = RequestMethod.GET, path = "/Integration/IntegrationProcessExecution/{IntegrationProcessExecutionID}/ActivityExecutions", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public List<ActivityExecution> getActivityExecutions(
			@PathVariable("IntegrationProcessExecutionID") Long IntegrationProcessExecutionID)
			throws Exception {

		List<ActivityExecution> activityExecutionList = activityExecutionService
				.getActivityExecutionByProcessExecutionID(IntegrationProcessExecutionID);
		return activityExecutionList;
	}

}
