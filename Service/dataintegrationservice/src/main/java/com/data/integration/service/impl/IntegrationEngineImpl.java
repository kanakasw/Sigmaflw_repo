package com.data.integration.service.impl;

import java.io.File;

import javax.annotation.PostConstruct;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.util.DatabaseUtil;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.data.integration.service.IntegrationEngine;
import com.data.integration.service.enums.KeysEnum;
import com.data.integration.service.enums.WorkflowKeysEnum;
import com.data.integration.service.exceptions.IntegrationActivityExecutionException;
import com.data.integration.service.exceptions.InvalidFilePathException;
import com.data.integration.service.exceptions.InvalidWorkflowTypeException;
import com.data.integration.service.exceptions.KettleInitializationException;
import com.data.integration.service.exceptions.ProcessingException;
import com.data.integration.service.executor.ETLexecutor;
import com.data.integration.service.executor.factory.ExecutorFactory;
import com.data.integration.service.util.ValidationUtil;

/**
 * Purpose of this Class is to provide a way to execute the kettle
 * Transformation/Jobs files used across this application.
 * 
 * @author Aniket
 *
 */
@Service
public class IntegrationEngineImpl implements IntegrationEngine {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(IntegrationEngineImpl.class);

    @Autowired
    private ExecutorFactory executorFactory;

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject executeWorkflow(JSONObject requestJson)
            throws IntegrationActivityExecutionException {

        JSONObject responseJson = null;
        JSONObject inputParams = null;
        JSONArray outputParams = null;
        Long stepExecutionID = null;

        try {

            // Get the identifier
            String strStepExecutionID = (String) requestJson
                    .get(WorkflowKeysEnum.ACTIVITY_EXECUTION_ID.getKey());

            if (strStepExecutionID != null) {
                // convert to long
                stepExecutionID = Long.valueOf(strStepExecutionID);
            }

            // Get the file path
            String workflowType = (String) requestJson
                    .get(WorkflowKeysEnum.WORKFLOW_TYPE.getKey());

            // Get the file path
            String workflowFilePath = (String) requestJson
                    .get(WorkflowKeysEnum.WORKFLOW_FILE_PATH.getKey());

            // get Absolute path of pentaho file
            requestJson.put(WorkflowKeysEnum.WORKFLOW_FILE_PATH.getKey(),
                    new File(workflowFilePath).getAbsolutePath());

            // Check if valid kettle file path passed
            ValidationUtil.isValidKettleFilePath(workflowFilePath);

            inputParams = (JSONObject) requestJson
                    .get(WorkflowKeysEnum.INPUT_PARAMETERS.getKey());

            outputParams = (JSONArray) requestJson
                    .get(WorkflowKeysEnum.OUTPUT_PARAMETERS.getKey());

            // Get the appropriate executor
            ETLexecutor executor = executorFactory.getExecutor(workflowType);

            // Execute workflow and get result
            responseJson = executor.execute(workflowFilePath, inputParams,
                    outputParams);

            return responseJson;
        } catch (InvalidFilePathException e) {

            throw new IntegrationActivityExecutionException(String.format(
                    "invalid pentaho file path for activityExecution = %d",
                    stepExecutionID), e);
        } catch (InvalidWorkflowTypeException e) {
            throw new IntegrationActivityExecutionException(String.format(
                    "invalid work flow Type for activityExecution = %d",
                    stepExecutionID), e);
        } catch (ProcessingException e) {
            throw new IntegrationActivityExecutionException(e);
        } catch (KettleException e) {
            throw new IntegrationActivityExecutionException(e);
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject getWorkflowInfo(String workflowType,
            String workflowFilePath) {

        JSONObject responseJson = null;

        try {

            // Check if valid kettle file path passed
            ValidationUtil.isValidKettleFilePath(workflowFilePath);

            // Get the appropriate executor
            ETLexecutor executor = executorFactory.getExecutor(workflowType);

            // Get workflow information
            responseJson = executor.getWorkflowInfo(workflowFilePath);

            responseJson.put(KeysEnum.STATUS_CODE.getKey(), 200);

            responseJson.put(KeysEnum.STATUS.getKey(),
                    KeysEnum.SUCCESS.getKey());

        } catch (InvalidFilePathException | InvalidWorkflowTypeException e) {

            LOGGER.error(
                    "An error occurred fetching input param list for workflow : "
                            + workflowFilePath, e);

        } catch (Exception e) {

            LOGGER.error(
                    "An error occurred fetching input param list for workflow : "
                            + workflowFilePath, e);

        }

        return responseJson;
    }

    /**
     * Check if KettleEnvironment is initialized. If not then it will try to
     * initialize it else throws KettleInitializationException. It also Boot
     * pentaho Reporting Engine
     * 
     * @throws KettleInitializationException
     */

}
