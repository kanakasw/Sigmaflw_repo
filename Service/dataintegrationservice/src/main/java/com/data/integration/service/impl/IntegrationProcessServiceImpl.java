package com.data.integration.service.impl;

import java.util.List;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.integration.data.Activity;
import com.data.integration.data.IntegrationProcess;
import com.data.integration.data.IntegrationProcessExecution;
import com.data.integration.repository.IntegrationProcessExecutionRepository;
import com.data.integration.repository.IntegrationProcessRepository;
import com.data.integration.service.IntegrationProcessExecutor;
import com.data.integration.service.IntegrationProcessService;
import com.data.integration.service.enums.IntegrationProcessExecutionStatusEnum;
import com.data.integration.service.exceptions.ActivityConfigurationException;
import com.data.integration.service.exceptions.ActivityExecutionException;
import com.data.integration.service.exceptions.IntegrationProcessExecutionException;
import com.data.integration.service.exceptions.IntegrationProcessNotFoundException;
import com.data.integration.service.exceptions.ProcessingException;
import com.data.integration.service.vo.ExecuteIntegrationProcessVO;
import com.data.integration.service.vo.IntegrationProcessResultVO;

/**
 * IntegrationProcessService implementation class
 * 
 * @author Chetan
 *
 */
@Service
public class IntegrationProcessServiceImpl implements IntegrationProcessService {

    public static final Logger LOGGER = LoggerFactory
            .getLogger(IntegrationProcessServiceImpl.class);

    @Autowired
    private IntegrationProcessRepository integrationProcessRepository;

    @Autowired
    private IntegrationProcessExecutor integrationProcessExecutor;

    @Autowired
    private IntegrationProcessExecutionRepository integrationProcessExecutionRepository;

    @Override
    public List<IntegrationProcess> getBySubscriberUserName(String userName)
            throws IntegrationProcessNotFoundException {

        List<IntegrationProcess> processes = integrationProcessRepository
                .findBySubscriberUserName(userName);

        if (processes == null || processes.isEmpty()) {
            throw new IntegrationProcessNotFoundException(
                    "Processes doesn't exist for subscriber : " + userName);
        }

        return processes;
    }

    @Override
    public IntegrationProcessResultVO executeIntegrationProcess(
            Long integrationProcessID)
            throws IntegrationProcessNotFoundException, ProcessingException,
            ActivityConfigurationException, ActivityExecutionException, IntegrationProcessExecutionException {

        IntegrationProcessResultVO integrationProcessResultVO = new IntegrationProcessResultVO();
        IntegrationProcess integrationProcess = integrationProcessRepository
                .findByIntegrationProcessID(integrationProcessID); 
        if (integrationProcess != null) {
            
            if(!integrationProcess.getEnabled()){
                throw new IntegrationProcessExecutionException(String.format("Process with ID: %d can not be executed,because it is disabled.", integrationProcess.getIntegrationProcessID()));
            }
            Activity initActivity = integrationProcess.getActivities().stream()
                    .filter(x -> true == x.isCausesNewProcessExecution())
                    .findAny().orElse(null);
            if (initActivity != null) {

                ExecuteIntegrationProcessVO executeIntegrationProcessVO = new ExecuteIntegrationProcessVO();
                executeIntegrationProcessVO.setActivityID(initActivity
                        .getActivityID());
                executeIntegrationProcessVO
                        .setCausesNewIntegrationProcessExecution(initActivity
                                .isCausesNewProcessExecution());
                integrationProcessExecutor
                        .executeIntegrationProcess(executeIntegrationProcessVO);
                integrationProcessResultVO.setStatus(200);
                integrationProcessResultVO
                        .setMessage("Integration Process execution started");

            }
        } else {
            throw new IntegrationProcessNotFoundException(String.format(
                    "Integration process with ID %d doesn't exists.",
                    integrationProcessID));
        }
        return integrationProcessResultVO;
    }

    @Override
    public IntegrationProcess getIntegrationProcessByID(
            Long integrationProcessID)
            throws IntegrationProcessNotFoundException {
        IntegrationProcess integrationProcess = integrationProcessRepository
                .findByIntegrationProcessID(integrationProcessID);
        if (integrationProcess == null) {
            throw new IntegrationProcessNotFoundException(String.format(
                    "IntegrationProcess with ID %d doesn't exists.",
                    integrationProcessID));
        } else {
            return integrationProcess;
        }
    }

    @Override
    public IntegrationProcessExecution getCurrentIntegrationProcessExecution(
            Long integrationProcessID)
            throws IntegrationProcessNotFoundException {
        IntegrationProcessExecution integrationProcessExecution = integrationProcessExecutionRepository
                .findTopByIntegrationProcessIDOrderByIntegrationProcessExecutionIDDesc(integrationProcessID);
        return integrationProcessExecution;
    }

    @Override
    public List<IntegrationProcessExecution> getPreviousIntegrationProcessExecution(
            Long integrationProcessID)
            throws IntegrationProcessNotFoundException {
        List<IntegrationProcessExecution> integrationProcessExecutionList = integrationProcessExecutionRepository
                .findByIntegrationProcessIDOrderByIntegrationProcessExecutionIDDesc(integrationProcessID);
        return integrationProcessExecutionList;
    }

    @Override
    public String getAverageExecutionTime(Long integrationProcessID) {
        Long avgExecutionTimeInMills = integrationProcessExecutionRepository
                .getAvarageExecutionTime(integrationProcessID);
        String avgExecution = "Not Available";
        if (avgExecutionTimeInMills!=null && avgExecutionTimeInMills != 0) {
            avgExecution = DurationFormatUtils.formatDurationWords(
                    avgExecutionTimeInMills, true, true);
        }
        return avgExecution;
    }

    @Override
    public IntegrationProcessExecution getLastSuccessIntegrationProcessExecution(
            Long integrationProcessID)
            throws IntegrationProcessNotFoundException {
        IntegrationProcessExecution integrationProcessExecution=
        integrationProcessExecutionRepository
                .findTopByIntegrationProcessIDAndStatusOrderByIntegrationProcessExecutionIDDesc(
                        integrationProcessID,
                        IntegrationProcessExecutionStatusEnum.COMPLETED);
        return integrationProcessExecution;
    }

	@Override
	public void updateIntegrationProcessSetup(
			Long integrationProcessID, IntegrationProcess integrationProcessSetup)throws IntegrationProcessNotFoundException {
		// TODO Auto-generated method stub
		IntegrationProcess integrationProcess = integrationProcessRepository.findByIntegrationProcessID(integrationProcessID);
		if(integrationProcess==null){
			throw new IntegrationProcessNotFoundException(String.format("Integration process with id %d doesn't exists", integrationProcessID));
		}
		integrationProcess.setFileEncryptionKey(integrationProcessSetup.getFileEncryptionKey());
		integrationProcess.setEnabled(integrationProcessSetup.getEnabled());
		integrationProcessRepository.save(integrationProcess);
	}

}
