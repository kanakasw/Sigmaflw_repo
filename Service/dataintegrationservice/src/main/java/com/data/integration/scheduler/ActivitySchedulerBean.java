package com.data.integration.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.data.integration.service.IntegrationProcessExecutor;
import com.data.integration.service.exceptions.ActivityConfigurationException;
import com.data.integration.service.exceptions.ActivityExecutionException;
import com.data.integration.service.exceptions.ProcessingException;
import com.data.integration.service.vo.ExecuteIntegrationProcessVO;

@Service
@Scope(value="prototype",proxyMode=ScopedProxyMode.NO)
@ConditionalOnProperty(name="spring.scheduler.enabled")
public class ActivitySchedulerBean implements Runnable {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ActivitySchedulerBean.class);

	private ExecuteIntegrationProcessVO executeIntegrationProcessVO;

	@Autowired
	private IntegrationProcessExecutor integrationProcessExecutor;

	@Override
	public void run() {
		Long activityID = executeIntegrationProcessVO.getActivityID();
		Boolean causesNewExecution=executeIntegrationProcessVO.getCausesNewIntegrationProcessExecution();
		LOGGER.info("Executing scheduled Activity with ID {}",executeIntegrationProcessVO);
		ExecuteIntegrationProcessVO executeIntegrationProcessVO=new ExecuteIntegrationProcessVO();
		executeIntegrationProcessVO.setActivityID(activityID);
		executeIntegrationProcessVO.setCausesNewIntegrationProcessExecution(causesNewExecution);
		try {
			integrationProcessExecutor.executeIntegrationProcess(executeIntegrationProcessVO);
		} catch (ProcessingException e) {
			LOGGER.error("Error occured while executing IntegrationProcess", e);
		} catch (ActivityConfigurationException e) {
			LOGGER.error("Error occured while parsing Activity Specification", e);
		} catch (ActivityExecutionException e) {
			LOGGER.error("Error occured while executing Activity ", e);
		}
	}

	public ExecuteIntegrationProcessVO getExecuteIntegrationProcessVO() {
		return executeIntegrationProcessVO;
	}

	public void setExecuteIntegrationProcessVO(
			ExecuteIntegrationProcessVO executeIntegrationProcessVO) {
		this.executeIntegrationProcessVO = executeIntegrationProcessVO;
	}

	public IntegrationProcessExecutor getIntegrationProcessExecutor() {
		return integrationProcessExecutor;
	}

	public void setIntegrationProcessExecutor(
			IntegrationProcessExecutor integrationProcessExecutor) {
		this.integrationProcessExecutor = integrationProcessExecutor;
	}

	@Override
	public String toString() {
		return "ActivitySchedulerBean [executeIntegrationProcessVO="
				+ executeIntegrationProcessVO.toString() + "]";
	}

}
