package com.data.integration.service;

import com.data.integration.service.exceptions.ActivityConfigurationException;
import com.data.integration.service.exceptions.ActivityExecutionException;
import com.data.integration.service.exceptions.IntegrationProcessExecutionNotFoundException;
import com.data.integration.service.exceptions.IntegrationProcessNotFoundException;
import com.data.integration.service.exceptions.ProcessingException;
import com.data.integration.service.exceptions.SubscriberNotFoundException;
import com.data.integration.service.vo.BatchProcessVO;
import com.data.integration.service.vo.IntegrationProcessResultVO;

public interface ProcessInitiator {


	IntegrationProcessResultVO initiateBatchProcess(BatchProcessVO batchProcessVO) throws ActivityConfigurationException, IntegrationProcessNotFoundException, ProcessingException, ActivityExecutionException, SubscriberNotFoundException;

	IntegrationProcessResultVO getJobExecutionStatus(String subscriberId,
			String processId, String jobId);

	IntegrationProcessResultVO startJobExecution(String subscriberId,
			String processId, String jobId) throws IntegrationProcessExecutionNotFoundException;

	IntegrationProcessResultVO updateSingleCustomerProfile(
			String subscriberUniqueKey,
			String integrationProcessUniqueReference, String inputData) throws ProcessingException, ActivityConfigurationException, IntegrationProcessNotFoundException, ActivityExecutionException, SubscriberNotFoundException;
}
