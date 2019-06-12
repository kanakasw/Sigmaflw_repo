package com.data.integration.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.data.integration.data.IntegrationProcessExecution;
import com.data.integration.service.IntegrationProcessService;
import com.data.integration.service.enums.IntegrationProcessExecutionStatusEnum;

/**
 * IntegrationProcessExecution controller class defines all
 * IntegrationProcessExecution related REST API's
 * 
 * @author Chetan
 *
 */
@RestController
public class IntegrationProcessExecutionController {

    private static final String AVERAGE_EXECUTION_TIME = "AverageExecutionTime";

    @Autowired
    private IntegrationProcessService integrationProcessService;

    @ApiOperation(value = "Get Current IntegrationProcessExecution using IntegrationProcessID")
    @RequestMapping(method = RequestMethod.GET, path = "/Integration/subscriber/process/{IntegrationProcessID}/IntegrationProcessExecution/current", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure") })
    @ResponseBody
    public IntegrationProcessExecution getcurrentIntegrationProcessExecution(
            @PathVariable("IntegrationProcessID") Long integrationProcessID)
            throws Exception {

        IntegrationProcessExecution integrationProcessExecution = integrationProcessService
                .getCurrentIntegrationProcessExecution(integrationProcessID);
        if (integrationProcessExecution != null) {
            if (!integrationProcessExecution.getStatus().equals(
                    IntegrationProcessExecutionStatusEnum.COMPLETED)) {
                return integrationProcessExecution;
            }
        }

        return null;

    }

    @ApiOperation(value = "Get Last successful IntegrationProcessExecution using IntegrationProcessID")
    @RequestMapping(method = RequestMethod.GET, path = "/Integration/IntegrationProcess/{IntegrationProcessID}/IntegrationProcessExecution/lastSuccessful", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure") })
    @ResponseBody
    public IntegrationProcessExecution getLastSuccessfullIntegrationProcessExecution(
            @PathVariable("IntegrationProcessID") Long integrationProcessID)
            throws Exception {

        IntegrationProcessExecution integrationProcessExecution = integrationProcessService
                .getLastSuccessIntegrationProcessExecution(integrationProcessID);
        if (integrationProcessExecution != null) {
            return integrationProcessExecution;
        }
        return null;

    }
    @ApiOperation(value = "Get All Previous IntegrationProcessExecution using IntegrationProcessID")
    @RequestMapping(method = RequestMethod.GET, path = "/Integration/IntegrationProcess/{IntegrationProcessID}/IntegrationProcessExecutions", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure") })
    @ResponseBody
    public List<IntegrationProcessExecution> getAllIntegrationProcessExecution(
            @PathVariable("IntegrationProcessID") Long integrationProcessID)
            throws Exception {

        List<IntegrationProcessExecution> integrationProcessExecutionlist = integrationProcessService
                .getPreviousIntegrationProcessExecution(integrationProcessID);
        return integrationProcessExecutionlist;
    }

    @ApiOperation(value = "Get Average Integration Process Execution time ")
    @RequestMapping(method = RequestMethod.GET, path = "/Integration/IntegrationProcess/{IntegrationProcessID}/AverageExecutionTime", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure") })
    @ResponseBody
    public String getAverageExecutionTime(
            @PathVariable("IntegrationProcessID") Long integrationProcessID)
            throws Exception {
        return new JSONObject().put(AVERAGE_EXECUTION_TIME, integrationProcessService.getAverageExecutionTime(integrationProcessID)).toString();
    }
}
