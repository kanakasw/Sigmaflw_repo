package com.data.integration.service;

import java.io.IOException;

import com.data.integration.service.exceptions.IntegrationProcessNotFoundException;
import com.data.integration.service.exceptions.SubscriberNotFoundException;
import com.data.integration.service.vo.BatchProcessVO;
import com.data.integration.service.vo.IntegrationProcessResultVO;


/**
 * FileUploadService interface
 * 
 * @author Aniket
 *
 */
public interface FileUploadService {

	public IntegrationProcessResultVO saveFile(BatchProcessVO batchProcessVO) throws SubscriberNotFoundException, IOException, IntegrationProcessNotFoundException;

	
}
