package com.data.integration.service.executor.impl;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.logging.LoggingBuffer;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.setvariables.JobEntrySetVariables;
import org.pentaho.di.job.entries.trans.JobEntryTrans;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.data.integration.service.enums.KeysEnum;
import com.data.integration.service.enums.WorkflowKeysEnum;
import com.data.integration.service.enums.WorkflowTypeEnum;
import com.data.integration.service.exceptions.ProcessingException;
import com.data.integration.service.executor.ETLexecutor;

/**
 * Executes operations related to Kettle JOB.
 * 
 * @author Aniket
 *
 */
@Service
public class JobExecutor implements ETLexecutor {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(JobExecutor.class);

	@Value("${pentaho.integration.jobLogLevelCode}")
	private String jobLogLevelCode;

	@SuppressWarnings("unchecked")
	public JSONObject execute(String workflowFilePath, JSONObject inputParams,
			JSONArray outputParams) throws ProcessingException, KettleException {

		Job job = null;
		Result result = null;
		JSONObject outputParamsDetails = new JSONObject();

		try {

			// Get the instance of job
			job = getJob(workflowFilePath);

			// Set the input parameters
			Set<String> paramKeys = inputParams.keySet();
			for (String paramKey : paramKeys) {
				job.setParameterValue(paramKey, inputParams.get(paramKey)
						.toString());
			}

			// add Base path
			String basePath = FilenameUtils.getFullPath(workflowFilePath);
			basePath = new File(basePath).getAbsolutePath();
			LOGGER.debug("Base path :{}", basePath);

			job.setVariable("Internal.Job.Filename.Directory", basePath);
			job.setVariable("Internal.Entry.Current.Directory", basePath);
			// Execute the job
			job.activateParameters();
			// Check the result values
			result = job.execute(0, null);
			job.waitUntilFinished();

			if (job.getErrors() > 0) {
				LoggingBuffer appender = KettleLogStore.getAppender();
				appender.removeGeneralMessages();
				String logText = appender.getBuffer(job.getLogChannelId(),
						false).toString();
				throw new ProcessingException(logText);
			}

			// Retrieve final values of output parameters
			for (Object param : outputParams) {
				outputParamsDetails.put(param.toString(),
						job.getVariable(param.toString()));
			}

		} catch (KettleException e) {
			throw new ProcessingException("Error occured processing Job.", e);
		} finally {
			if (result != null && result.getLogChannelId()!=null) {
				KettleLogStore.discardLines(result.getLogChannelId(), true);
				result = null;
			}
			if (job != null ) {
				KettleLogStore.discardLines(job.getLogChannelId(), true);
				job = null;
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

		// Get the instance of job
		Job job = getJob(workflowFilePath);

		// 1. Listing out all input parameters
		for (String param : job.listParameters()) {

			JSONObject paramInfoJson = new JSONObject();

			// Get the parameter description
			String description = job.getParameterDescription(param);

			paramInfoJson.put(KeysEnum.PARAMETER_NAME.getKey(), param);

			paramInfoJson.put(KeysEnum.PARAMETER_DESC.getKey(), StringUtils
					.isNotBlank(description) ? description
					: "Paramater description not available.");

			inputParamArray.add(paramInfoJson);
		}

		// 2. Listing out all output parameters
		Set<String> outputParams = getOutputParams(job.getJobMeta()
				.getJobCopies());
		for (String outputParam : outputParams) {
			outputParamArray.add(outputParam);
		}

		// 3. Collect all info in single json
		workflowInfo.put(KeysEnum.INPUT_PARAM_LIST.getKey(), inputParamArray);
		workflowInfo.put(KeysEnum.OTUPUT_PARAM_LIST.getKey(), outputParamArray);
		workflowInfo.put(WorkflowKeysEnum.WORKFLOW_TYPE.getKey(),
				WorkflowTypeEnum.JOB.getKey());
		workflowInfo.put(WorkflowKeysEnum.WORKFLOW_FILE_NAME.getKey(),
				FilenameUtils.getName(workflowFilePath));

		return workflowInfo;
	}

	@SuppressWarnings("unchecked")
	public JSONArray getOutputParams(String workflowFilePath)
			throws KettleException {

		JSONArray outputParamArray = new JSONArray();

		// 1. Get the instance of job
		Job job = getJob(workflowFilePath);

		// 2. Listing out all output parameters
		Set<String> outputParams = getOutputParams(job.getJobMeta()
				.getJobCopies());
		for (String outputParam : outputParams) {
			outputParamArray.add(outputParam);
		}

		return outputParamArray;
	}

	/**
	 * List out all output parameters defined in the job as well as in
	 * subsequent transformation added in the job.
	 * 
	 * @param stepList
	 * @return Set<String>
	 */
	private Set<String> getOutputParams(List<JobEntryCopy> stepList) {

		Set<String> outputParams = new HashSet<String>();

		for (JobEntryCopy step : stepList) {

			if (step.getEntry() instanceof JobEntrySetVariables) {

				// Get the o/p params listed in job
				JobEntrySetVariables setVarStep = (JobEntrySetVariables) step
						.getEntry();

				outputParams.addAll(Arrays.asList(setVarStep.variableName));

				setVarStep = null;
			} else if (step.isTransformation()) {

				// Get the o/p params listed in transformation
				JobEntryTrans jobEntryTrans = (JobEntryTrans) step.getEntry();

				outputParams.addAll(getTransOutputParams(jobEntryTrans
						.getFilename()));

				jobEntryTrans = null;
			}
		}

		return outputParams;
	}

	/**
	 * Get the list of all output parameters for the transformation.
	 * 
	 * @param workflowFilePath
	 *            transformation file path
	 * @return List<String> list of all output parameters
	 */
	@SuppressWarnings("unchecked")
	private List<String> getTransOutputParams(String workflowFilePath) {

		JSONArray outputParams = new JSONArray();

		TransformationExecutor transExecutor = new TransformationExecutor();

		try {

			outputParams
					.addAll(transExecutor.getOutputParams(workflowFilePath));

		} catch (KettleException e) {
			LOGGER.error(
					"Error has occured fetching transformation information : ",
					e);
		} finally {
			// Nullifying Objects
			transExecutor = null;
		}

		return outputParams;
	}

	/**
	 * Instantiate Job and return the reference
	 * 
	 * @param kettleFilePath
	 * @return
	 * @throws KettleException
	 */
	private Job getJob(String kettleFilePath) throws KettleException {

		// set the job file name
		JobMeta jobMetaData = new JobMeta(kettleFilePath, null);

		Job job = new Job(null, jobMetaData);
		job.initializeVariablesFrom(null);
		job.getJobMeta().setInternalKettleVariables(job);
		job.copyParametersFrom(job.getJobMeta());
		job.setLogLevel(LogLevel.getLogLevelForCode(jobLogLevelCode));
		return job;
	}

}
