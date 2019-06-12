package com.data.integration.service.executor.impl;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.logging.LoggingBuffer;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.setvariable.SetVariableMeta;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.data.integration.service.enums.KeysEnum;
import com.data.integration.service.enums.WorkflowKeysEnum;
import com.data.integration.service.enums.WorkflowTypeEnum;
import com.data.integration.service.exceptions.ProcessingException;
import com.data.integration.service.executor.ETLexecutor;

/**
 * Executes operations related to Kettle Transformation.
 * 
 * @author Aniket
 *
 */
@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class TransformationExecutor implements ETLexecutor {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TransformationExecutor.class);
	
	@Value("${pentaho.integration.transLogLevelCode}")
    private String transLogLevelCode; 

	@SuppressWarnings("unchecked")
	public JSONObject execute(String workflowFilePath, JSONObject inputParams,
			JSONArray outputParams) throws ProcessingException, KettleException {

		Trans trans = null;
		JSONObject outputParamsDetails = new JSONObject();

		try {

			// Get the instance of Trans
			trans = getTrans(workflowFilePath);

			// Set the input parameters
			Set<String> paramKeys = inputParams.keySet();
			for (String paramKey : paramKeys) {
				trans.setParameterValue(paramKey,
						inputParams.get(paramKey).toString());
			}

			// Execute the transformation
			trans.execute(null);
			trans.waitUntilFinished();

			if (trans.getErrors() > 0) {
				LoggingBuffer appender = KettleLogStore.getAppender();
				appender.removeGeneralMessages();
				String logText = appender.getBuffer(trans.getLogChannelId(),
						false).toString();
				throw new ProcessingException(logText);
			}
			// Retrieve final values of output parameters
			for (Object param : outputParams) {
				outputParamsDetails.put(param.toString(),
						trans.getVariable(param.toString()));
			}

			if (LOGGER.isInfoEnabled()) {
				// Get the log details from KettleLogStore
				LoggingBuffer appender = KettleLogStore.getAppender();
				String logText = appender.getBuffer(trans.getLogChannelId(),
						false).toString();
				LOGGER.info(logText);
			}

		} catch (KettleException e) {
			throw new ProcessingException("Error occured processing transformaion. : ",e);
		} finally {
			if (trans != null) {
				KettleLogStore.discardLines(trans.getLogChannelId(), true);
				KettleLogStore.discardLines(trans.getTransMeta().getLogChannelId(),
						true);
				trans = null;
			}
		}
		return outputParamsDetails;

	}

	@SuppressWarnings("unchecked")
	public JSONObject getWorkflowInfo(String workflowFilePath)
			throws KettleException {

		JSONObject workflowInfo = new JSONObject();
		JSONArray inputParamArray = new JSONArray();
		JSONArray outputParamArray = new JSONArray();

		// Get the instance of Trans
		Trans trans = getTrans(workflowFilePath);

		// 1. Listing out all input parameter
		for (String param : trans.listParameters()) {

			JSONObject paramInfoJson = new JSONObject();

			// Get the parameter description
			String description = trans.getParameterDescription(param);

			paramInfoJson.put(KeysEnum.PARAMETER_NAME.getKey(), param);

			paramInfoJson.put(KeysEnum.PARAMETER_DESC.getKey(), StringUtils
					.isNotBlank(description) ? description
					: "Paramater description not available.");

			inputParamArray.add(paramInfoJson);
		}

		// 2. Listing out all output parameters
		List<String> outputParams = getOutputParams(trans.getTransMeta()
				.getSteps());
		for (String outputParam : outputParams) {
			outputParamArray.add(outputParam);
		}

		// 3. Collect all info in single json
		workflowInfo.put(KeysEnum.INPUT_PARAM_LIST.getKey(), inputParamArray);
		workflowInfo.put(KeysEnum.OTUPUT_PARAM_LIST.getKey(), outputParamArray);
		workflowInfo.put(WorkflowKeysEnum.WORKFLOW_TYPE.getKey(),
				WorkflowTypeEnum.TRANSFORMATION.getKey());
		workflowInfo.put(WorkflowKeysEnum.WORKFLOW_FILE_NAME.getKey(),
				FilenameUtils.getName(workflowFilePath));

		return workflowInfo;
	}

	@SuppressWarnings("unchecked")
	public JSONArray getOutputParams(String workflowFilePath)
			throws KettleException {

		JSONArray outputParamArray = new JSONArray();

		// 1. Get the instance of Trans
		Trans trans = getTrans(workflowFilePath);

		// 2. Listing out all output parameters
		List<String> outputParams = getOutputParams(trans.getTransMeta()
				.getSteps());
		for (String outputParam : outputParams) {
			outputParamArray.add(outputParam);
		}

		return outputParamArray;
	}

	/**
	 * List out all output parameters defined in the transformation.
	 * 
	 * @param stepMetaList
	 * @return List<String>
	 */
	private List<String> getOutputParams(List<StepMeta> stepMetaList) {

		List<String> outputParams = new ArrayList<String>();

		for (StepMeta stepMeta : stepMetaList) {

			// Check if step type is SetVariable
			if (stepMeta.getStepID().equals("SetVariable")) {

				SetVariableMeta setVariableMeta = (SetVariableMeta) stepMeta
						.getStepMetaInterface();

				outputParams.addAll(Arrays.asList(setVariableMeta
						.getVariableName()));

				setVariableMeta = null;
			}
		}

		return outputParams;
	}

	/**
	 * Instantiate Trans and return the reference
	 * 
	 * @param kettleFilePath
	 * @return
	 * @throws KettleException
	 */
	private Trans getTrans(String kettleFilePath) throws KettleException {

		// set the transformation file name
		TransMeta metaData = new TransMeta(kettleFilePath);

		Trans trans = new Trans(metaData);

		trans.setLogLevel(LogLevel.getLogLevelForCode(transLogLevelCode));
		return trans;
	}

}
