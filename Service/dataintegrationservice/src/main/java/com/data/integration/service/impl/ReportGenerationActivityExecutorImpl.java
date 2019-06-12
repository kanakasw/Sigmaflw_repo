package com.data.integration.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.data.integration.data.Activity;
import com.data.integration.data.ActivityExecution;
import com.data.integration.data.IntegrationProcessExecution;
import com.data.integration.repository.ActivityExecutionRepository;
import com.data.integration.repository.EventQueueRepository;
import com.data.integration.repository.IntegrationProcessExecutionRepository;
import com.data.integration.repository.IntegrationProcessRepository;
import com.data.integration.service.ActivityExecutor;
import com.data.integration.service.IntegrationEngine;
import com.data.integration.service.IntegrationProcessExecutor;
import com.data.integration.service.enums.ActivityExecutionStatusEnum;
import com.data.integration.service.enums.GenerateReportKeysEnum;
import com.data.integration.service.enums.IntegrationProcessExecutionStatusEnum;
import com.data.integration.service.exceptions.ActivityExecutionException;
import com.data.integration.service.impl.AbstractReportGenerator.OutputType;
import com.data.integration.service.util.ActivityUtil;
import com.data.integration.service.vo.ExecuteIntegrationProcessVO;
import com.data.integration.service.vo.GenerateReportActivityExecutionOutcomeVO;
import com.data.integration.service.vo.ReportingStepVO;
import com.hazelcast.core.HazelcastInstance;

@Service
public class ReportGenerationActivityExecutorImpl implements ActivityExecutor {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ReportGenerationActivityExecutorImpl.class);

	@Autowired
	private IntegrationProcessRepository integrationProcessRepository;

	@Autowired
	private IntegrationEngine integrationEngine;

	@Autowired
	private ActivityExecutionRepository activityExecutionRepository;

	@Autowired
	private IntegrationProcessExecutor integrationProcessExecutor;

	@Autowired
	private EventQueueRepository eventQueueRepository;

	@Autowired
	private IntegrationProcessExecutionRepository integrationProcessExecutionRepository;

	@Autowired
	private HazelcastInstance hazelcastInstance;

	@Value("${integration.files}")
	private String basePath;


	@Autowired
	private ActivityUtil activityUtil;

	/**
	 * Sample specification for Report Generation activity.
	 * {
    "ActivityExecutionID": "${ActivityExecutionID}",
    "ReportDefinitionFilePath": "${BaseFolderPath}\\Sub_${SubscriberID}\\ReportFiles\\Execution Report.prpt",
    "OutputFilePath": "${BaseFolderPath}\\Sub_${SubscriberID}\\Proc_${IntegrationProcessID}\\Exec_${IntegrationProcessExecutionID}\\ReportOutput\\Sub_${SubscriberID}_Proc_${IntegrationProcessID}_ExecutionReport_${IntegrationProcessExecutionID}.xls",
    "OutputType": "xls",
    "StepToTrigger": 24,
    "InputParameters": {
        "ActivityExecutionID": "${ActivityExecutionID}",
        "SubscriberID": "${SubscriberID}",
        "IntegrationProcessExecutionID": "${IntegrationProcessExecutionID}",
        "IntegrationProcessID": "${IntegrationProcessID}"
        }
    }
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void executeActivity(Long activityExecutionID,
			Long integrationProcessExecutionID,
			ExecuteIntegrationProcessVO executeIntegrationProcessVO)
			throws ActivityExecutionException {

		LOGGER.info("Report Generation Activity Executor,activityID = {}"
				,executeIntegrationProcessVO.getActivityID());
		boolean isError = false;
		ActivityExecution activityExecution = activityExecutionRepository
				.findByActivityExecutionID(activityExecutionID);

		Activity activity = activityExecution.getActivity();

		String processingSpecification = activity.getProcessingSpecification();

		JSONObject requestJson = null;

		GenerateReportActivityExecutionOutcomeVO etlActivityExecutionOutcomeVO = new GenerateReportActivityExecutionOutcomeVO();
		ReportingStepVO etlStepVO = etlActivityExecutionOutcomeVO.getEtlStepRuntime();
		etlActivityExecutionOutcomeVO.addLogEntries(String.format("Executing %s", activity.getActivityName()));
		try {
		
			requestJson = activityUtil.getInputJson(processingSpecification,
                    activityExecution, executeIntegrationProcessVO,
                    integrationProcessRepository, basePath);
			

			activityExecution.setStatus(ActivityExecutionStatusEnum.PROCESSING);
			activityExecutionRepository.save(activityExecution);
			
			String reportDefinitionFile=(String) requestJson.get(GenerateReportKeysEnum.REPORT_DEFINITION_FILEPATH.getKey());
			String outPutFilePath=(String) requestJson.get(GenerateReportKeysEnum.OUTPUT_FILE_PATH.getKey());
			
			String outPutType=(String) requestJson.get(GenerateReportKeysEnum.OUTPUT_TYPE.getKey());
			Map<String,Object> inputParameters=(Map<String, Object>) requestJson.get(GenerateReportKeysEnum.INPUT_PARAMETERS.getKey());
			
			etlStepVO.setInputParameters(inputParameters);
			etlStepVO.setOutputFilePath(outPutFilePath);
			etlActivityExecutionOutcomeVO.setReportDefinitionFile(requestJson.get(
					GenerateReportKeysEnum.REPORT_DEFINITION_FILEPATH.getKey()).toString());
			
			createOutputFilePath(outPutFilePath);
			ReportGenerator reportGenerator=new ReportGenerator();
			reportGenerator.setReportDefinitionFile(reportDefinitionFile);
			reportGenerator.setReportParameters(inputParameters);
			reportGenerator.generateReport(getOutputType(outPutType),new File(outPutFilePath));
			activityExecution.setExecutionFinishTime(new Date());

			activityUtil.callNextActivityOrEvent(integrationProcessExecutionID,
                    executeIntegrationProcessVO, requestJson, null);
			
		} catch (IllegalArgumentException | IOException | ReportProcessingException e) {
			isError = true;
			LOGGER.error("Error Occured ", e);
		etlActivityExecutionOutcomeVO
					.addLogEntries(e.getMessage());
		} finally {

			try {
				activityExecution
						.setExecutionStepOutcome(etlActivityExecutionOutcomeVO
								.toJsonString());
				if (isError) {
					activityExecution
							.setStatus(ActivityExecutionStatusEnum.ERROR);
					IntegrationProcessExecution execution = integrationProcessExecutionRepository
							.findOne(integrationProcessExecutionID);
					execution
							.setStatus(IntegrationProcessExecutionStatusEnum.ERROR);
					integrationProcessExecutionRepository.save(execution);
				} else {
					activityExecution
							.setStatus(ActivityExecutionStatusEnum.COMPLETED);
				}
			} catch (IOException e) {
				LOGGER.error(
						"Error occured while logging reportActivityExecutionOutcomeVO ",
						etlActivityExecutionOutcomeVO.toString(), e);
			}finally{
			activityExecutionRepository.save(activityExecution);
			}
		}

	}

	private OutputType getOutputType(String type){
		switch(type){
		case "xls":
			return OutputType.EXCEL;
		case "pdf":
			return OutputType.PDF;
		case "html":
			return OutputType.HTML;
		default: 
			return null;
		}
	}
	
	private void createOutputFilePath(String outputFilePath){
		String parentDir=FilenameUtils.getFullPathNoEndSeparator(outputFilePath);
		File parentDirObject=new File(parentDir);
		parentDirObject.mkdirs();
	}

}
