package com.data.integration.service;

import com.data.integration.service.exceptions.ActivityConfigurationException;
import com.data.integration.service.exceptions.ActivityExecutionException;
import com.data.integration.service.exceptions.ProcessingException;
import com.data.integration.service.vo.ExecuteIntegrationProcessVO;
import com.data.integration.service.vo.IntegrationProcessResultVO;

/**
 * This interface defines method to Kick start IntegrationProcess execution.
 * 
 * @author Aniket
 *
 */
public interface IntegrationProcessExecutor {

	/**
	 * <b>This method has been called from following places :</b><br>
	 * 1. EventProcessorJob .processEvent()<br>
	 *       CausesNewIntegrationProcessExecution need to set FALSE,Because without IntegrationProcessExecution event can't exists
	 * <br>      
	 * 2. ActivitySchedulerBean  run() <br>
	 *       CausesNewIntegrationProcessExecution for Scheduled activity is  either TRUE/FALSE
	 * <br>      
	 * 3. ProcessInitiatorImpl  <br>
	 *      initiateBatchProcess() :<br>
	 *      We get All activity's for IntegrationProcess and choose starter activity for execution using  CausesNewIntegrationProcessExecution equals TRUE flag
	 *      <br>
	 *      <br>
	 *      updateSingleCustomerProfile() :<br>
	 *      We get All activity's for IntegrationProcess and choose starter activity for execution using  CausesNewIntegrationProcessExecution equals TRUE flag
	 *      and In addition,we check for Async flag ,if it is found TRUE,it will be set to executeIntegrationProcessVO,
	 *      in executeIntegrationProcess() method IntegrationProcessExecution and event is created,it will not execute any transformation/job
	 *      When EventProcessorJob process this Event it will set CausesNewIntegrationProcessExecution FLAG AS FALSE  
	 *      (This scenario is handled because framework should support executing starter Activity asynchronous.) 
	 * <br>
	 * 4.HazalcastQueueConsumer<br>
	 *     itemAdded()<br>
	 *       This will be called when we add executeIntegrationProcessVO in Hazalecast queue     
	 * @param executeIntegrationProcessVO
	 * @return
	 * @throws ProcessingException
	 * @throws ActivityConfigurationException
	 * @throws ActivityExecutionException 
	 */
	IntegrationProcessResultVO executeIntegrationProcess(
			ExecuteIntegrationProcessVO executeIntegrationProcessVO)
			throws ProcessingException, ActivityConfigurationException, ActivityExecutionException;

}
