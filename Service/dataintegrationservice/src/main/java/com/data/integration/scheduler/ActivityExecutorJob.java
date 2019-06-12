package com.data.integration.scheduler;

import java.io.IOException;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.data.integration.config.QuartzJobConfiguration.JobKeysEnum;
import com.data.integration.service.IntegrationProcessExecutor;
import com.data.integration.service.exceptions.ActivityConfigurationException;
import com.data.integration.service.exceptions.ActivityExecutionException;
import com.data.integration.service.exceptions.ProcessingException;
import com.data.integration.service.vo.ExecuteIntegrationProcessVO;


@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ActivityExecutorJob extends QuartzJobBean{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ActivityExecutorJob.class);

	private ApplicationContext applicationContext;
	
	private IntegrationProcessExecutor integrationProcessExecutor;

    public void setApplicationContext(ApplicationContext applicationContext) {
	        this.applicationContext = applicationContext;
	    }

  
	private void execute(ExecuteIntegrationProcessVO executeIntegrationProcessVO) {
		try {
			integrationProcessExecutor=applicationContext.getBean(IntegrationProcessExecutor.class);
			integrationProcessExecutor.executeIntegrationProcess(executeIntegrationProcessVO);
		} catch (ProcessingException e) {
			LOGGER.error("Error occured while executing IntegrationProcess", e);
		} catch (ActivityConfigurationException e) {
			LOGGER.error("Error occured while parsing Activity Specification", e);
		} catch (ActivityExecutionException e) {
			LOGGER.error("Error occured while executing Activity ", e);
		}
	}


	public IntegrationProcessExecutor getIntegrationProcessExecutor() {
		return integrationProcessExecutor;
	}

	public void setIntegrationProcessExecutor(
			IntegrationProcessExecutor integrationProcessExecutor) {
		this.integrationProcessExecutor = integrationProcessExecutor;
	}


	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		String executeIntegrationProcess = context.getJobDetail()
				.getJobDataMap().getString(JobKeysEnum.JOBDETAIL.name());
		ExecuteIntegrationProcessVO executeIntegrationProcessVO = new ExecuteIntegrationProcessVO();
		try {
			executeIntegrationProcessVO = executeIntegrationProcessVO
					.toObject(executeIntegrationProcess);
			execute(executeIntegrationProcessVO);
		} catch (IOException e) {
			LOGGER.error("Error occured while deserialization of Job Detail",e);
		}

		
	}

}
