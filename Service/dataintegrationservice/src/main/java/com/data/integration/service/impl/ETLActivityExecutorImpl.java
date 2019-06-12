package com.data.integration.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import net.lingala.zip4j.exception.ZipException;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.data.integration.data.Activity;
import com.data.integration.data.ActivityExecution;
import com.data.integration.data.IntegrationProcess;
import com.data.integration.data.IntegrationProcessExecution;
import com.data.integration.repository.ActivityExecutionRepository;
import com.data.integration.repository.IntegrationProcessExecutionRepository;
import com.data.integration.repository.IntegrationProcessRepository;
import com.data.integration.service.ActivityExecutor;
import com.data.integration.service.IntegrationEngine;
import com.data.integration.service.IntegrationProcessExecutor;
import com.data.integration.service.enums.ActivityExecutionStatusEnum;
import com.data.integration.service.enums.IntegrationProcessExecutionStatusEnum;
import com.data.integration.service.enums.WorkflowKeysEnum;
import com.data.integration.service.exceptions.ActivityExecutionException;
import com.data.integration.service.exceptions.IntegrationActivityExecutionException;
import com.data.integration.service.exceptions.TagValueReplacerException;
import com.data.integration.service.util.ActivityUtil;
import com.data.integration.service.util.ZipUtil;
import com.data.integration.service.vo.ETLActivityExecutionOutcomeVO;
import com.data.integration.service.vo.EtlStepVO;
import com.data.integration.service.vo.ExecuteIntegrationProcessVO;
import com.hazelcast.core.HazelcastInstance;

@Service
public class ETLActivityExecutorImpl implements ActivityExecutor {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ETLActivityExecutorImpl.class);

    @Autowired
    private IntegrationProcessRepository integrationProcessRepository;

    @Autowired
    private IntegrationEngine integrationEngine;

    @Autowired
    private ActivityExecutionRepository activityExecutionRepository;

    @Autowired
    private IntegrationProcessExecutor integrationProcessExecutor;

    @Autowired
    private IntegrationProcessExecutionRepository integrationProcessExecutionRepository;

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Value("${integration.files}")
    private String basePath;

    @Autowired
    private ActivityUtil activityUtil;

    @SuppressWarnings("unchecked")
    @Override
    public void executeActivity(Long activityExecutionID,
            Long integrationProcessExecutionID,
            ExecuteIntegrationProcessVO executeIntegrationProcessVO)
            throws ActivityExecutionException {

        LOGGER.info("INTEGRATION Step Executor,activityID = {}",
                executeIntegrationProcessVO.getActivityID());
        boolean isError = false;
        ActivityExecution activityExecution = activityExecutionRepository
                .findByActivityExecutionID(activityExecutionID);

        Activity activity = activityExecution.getActivity();

        String processingSpecification = activity.getProcessingSpecification();

        JSONObject requestJson = null;

        ETLActivityExecutionOutcomeVO etlActivityExecutionOutcomeVO = new ETLActivityExecutionOutcomeVO();
        EtlStepVO etlStepVO = etlActivityExecutionOutcomeVO.getEtlStepRuntime();
        etlActivityExecutionOutcomeVO.addLogEntries(String.format(
                "Executing %s", activity.getActivityName()));
        etlActivityExecutionOutcomeVO
                .setExecuteIntegrationProcessVO(executeIntegrationProcessVO);
        try {
            if (executeIntegrationProcessVO.isUnzip()) {
                unzipSourceFile(executeIntegrationProcessVO, activityExecution);
            }
            requestJson = activityUtil.getInputJson(processingSpecification,
                    activityExecution, executeIntegrationProcessVO,
                    integrationProcessRepository, basePath);
            etlStepVO.setInputParameters((Map<String, Object>) requestJson
                    .get(WorkflowKeysEnum.INPUT_PARAMETERS.getKey()));
            etlStepVO.setKtrFilePath(requestJson.get(
                    WorkflowKeysEnum.WORKFLOW_FILE_PATH.getKey()).toString());

            activityExecution.setStatus(ActivityExecutionStatusEnum.PROCESSING);
            activityExecutionRepository.save(activityExecution);

            JSONObject outputJson = integrationEngine
                    .executeWorkflow(requestJson);

            etlActivityExecutionOutcomeVO.setOutputParameters(outputJson);

            activityExecution.setExecutionFinishTime(new Date());

            activityUtil.callNextActivityOrEvent(integrationProcessExecutionID,
                    executeIntegrationProcessVO, requestJson, activity.getIntegrationProcessID());

        } catch (TagValueReplacerException e) {
            isError = true;
            LOGGER.error("Error Occured while parsing input json", e);
            etlActivityExecutionOutcomeVO.addLogEntries(e.getMessage());
        } catch (IntegrationActivityExecutionException e) {
            isError = true;
            etlActivityExecutionOutcomeVO.addLogEntries(e.getMessage());
            throw new ActivityExecutionException(
                    "Error Occured while executing Integration Activity", e);
        } catch (ZipException e) {
            isError = true;
            etlActivityExecutionOutcomeVO.addLogEntries(e.getMessage());
            throw new ActivityExecutionException(
                    "Error Occured while executing Integration Activity", e);
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
                        "Error occured while logging etlActivityExecutionOutcomeVO ",
                        etlActivityExecutionOutcomeVO.toString(), e);
            } finally {
                activityExecutionRepository.save(activityExecution);
            }
        }

    }


    private void unzipSourceFile(
            ExecuteIntegrationProcessVO executeIntegrationProcessVO,
            ActivityExecution activityExecution) throws ZipException {
        IntegrationProcess integrationProcess = integrationProcessRepository
                .findByIntegrationProcessID(activityExecution
                        .getIntegrationProcessExecution()
                        .getIntegrationProcessID());
        String unzippedFile = ZipUtil.unzipInputFile(new File(
                executeIntegrationProcessVO.getSourcefilePath()), true,
                integrationProcess.getFileEncryptionKey());
        executeIntegrationProcessVO.setSourcefilePath(unzippedFile);
    }


}
