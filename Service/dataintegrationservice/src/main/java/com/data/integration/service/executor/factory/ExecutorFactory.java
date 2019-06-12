package com.data.integration.service.executor.factory;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.data.integration.service.enums.WorkflowTypeEnum;
import com.data.integration.service.exceptions.InvalidWorkflowTypeException;
import com.data.integration.service.executor.ETLexecutor;
import com.data.integration.service.executor.impl.JobExecutor;
import com.data.integration.service.executor.impl.TransformationExecutor;


/**
 * Factory to generate object of appropriate executor based on the kettle file
 * type.
 * 
 * @author Aniket
 *
 */
@Service
public class ExecutorFactory {

	@Autowired
	ApplicationContext applicationContext;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ExecutorFactory.class);

	/**
	 * Provides the object of appropriate workflow executor.
	 * 
	 * @param workflowType
	 * @return
	 * @throws InvalidWorkflowTypeException
	 */
	public ETLexecutor getExecutor(String workflowType)
			throws InvalidWorkflowTypeException {

		// Check for null file type
		if (StringUtils.isBlank(workflowType)) {
			
			LOGGER.error("No workflow file type specified.");
			
			throw new InvalidWorkflowTypeException(
					"No workflow file type specified.");
		}

		// Get kettle file type enum
		WorkflowTypeEnum workflowTypeEnum = WorkflowTypeEnum
				.getByKey(workflowType);

		// Return the appropriate executor type instance
		switch (workflowTypeEnum) {

		case JOB:
			return applicationContext.getBean(JobExecutor.class);

		case TRANSFORMATION:
			return applicationContext.getBean(TransformationExecutor.class);

		default:
			throw new InvalidWorkflowTypeException(
					String.format("Invalid workflow file type : %s" , workflowType));

		}
	}

}
