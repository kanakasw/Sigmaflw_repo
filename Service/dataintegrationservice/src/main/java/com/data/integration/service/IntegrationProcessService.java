package com.data.integration.service;

import java.util.List;



import com.data.integration.data.IntegrationProcess;
import com.data.integration.data.IntegrationProcessExecution;
import com.data.integration.service.exceptions.ActivityConfigurationException;
import com.data.integration.service.exceptions.ActivityExecutionException;
import com.data.integration.service.exceptions.IntegrationProcessExecutionException;
import com.data.integration.service.exceptions.IntegrationProcessNotFoundException;
import com.data.integration.service.exceptions.ProcessingException;
import com.data.integration.service.vo.IntegrationProcessResultVO;

/**
 * IntegrationProcessService interface
 * 
 * @author Chetan
 *
 */
public interface IntegrationProcessService {

	/**
	 * Get all processes for by subscriber username
	 * 
	 * @param userName
	 * @return List<IntegrationProcess>
	 * @throws IntegrationProcessNotFoundException
	 */
	public List<IntegrationProcess> getBySubscriberUserName(String userName)
			throws IntegrationProcessNotFoundException;

	
	/**
	 * Get all processes for by subscriber username
	 * 
	 * @param integrationProcessID
	 * @return List<IntegrationProcess>
	 * @throws IntegrationProcessNotFoundException
	 * @throws ActivityExecutionException 
	 * @throws ActivityConfigurationException 
	 * @throws ProcessingException 
	 * @throws IntegrationProcessExecutionException 
	 */
	public IntegrationProcessResultVO executeIntegrationProcess(Long integrationProcessID)
			throws IntegrationProcessNotFoundException, ProcessingException, ActivityConfigurationException, ActivityExecutionException, IntegrationProcessExecutionException;
	
	/**
	 * Get integration Process By ID
	 * @return
	 * @throws IntegrationProcessNotFoundException 
	 */
	public IntegrationProcess getIntegrationProcessByID(Long integrationProcessID) throws IntegrationProcessNotFoundException;
	
	
	
	/**
	 * Get current IntegrationProcessExecution By integrationProcessID
	 * @return
	 * @throws IntegrationProcessNotFoundException 
	 */
	public IntegrationProcessExecution getCurrentIntegrationProcessExecution(Long integrationProcessID) throws IntegrationProcessNotFoundException;
	
	/**
     * Get Last Successful IntegrationProcessExecution By integrationProcessID
     * @return
     * @throws IntegrationProcessNotFoundException 
     */
    public IntegrationProcessExecution getLastSuccessIntegrationProcessExecution(Long integrationProcessID) throws IntegrationProcessNotFoundException;
	
	
	/**
	 * Get All previous IntegrationProcessExecution By integrationProcessID
	 * @return
	 * @throws IntegrationProcessNotFoundException 
	 */
	public List<IntegrationProcessExecution> getPreviousIntegrationProcessExecution(Long integrationProcessID) throws IntegrationProcessNotFoundException;
	

    /**
     * Get  Average Execution time for IntegrationProcessExecution
     * @return
     * @throws IntegrationProcessNotFoundException 
     */
    public String getAverageExecutionTime(Long integrationProcessID);
    
    /**
     * Update Integration Procees Setup
     * @param integrationProcessID
     * @param integrationProcessSetup
     * @return IntegrationProcessResultVO
     * @throws IntegrationProcessNotFoundException
     * */
    public void updateIntegrationProcessSetup(Long integrationProcessID, IntegrationProcess integrationProcessSetup)throws IntegrationProcessNotFoundException;
}
