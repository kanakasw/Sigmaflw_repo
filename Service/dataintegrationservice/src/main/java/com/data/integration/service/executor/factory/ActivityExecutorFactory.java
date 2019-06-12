package com.data.integration.service.executor.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.data.integration.service.ActivityExecutor;
import com.data.integration.service.enums.ActivityTypeEnum;
import com.data.integration.service.exceptions.InvalidActivityTypeException;
import com.data.integration.service.impl.ENDActivityExecutorImpl;
import com.data.integration.service.impl.ETLActivityExecutorImpl;
import com.data.integration.service.impl.ReportGenerationActivityExecutorImpl;


/**
 * Factory to generate object of appropriate Step Executor based on processing
 * step type.
 * 
 * @author Aniket
 *
 */
@Component
public class ActivityExecutorFactory {
	
	@Autowired
	ApplicationContext applicationContext;

	/**
	 * Provides the object of appropriate step executor.
	 * 
	 * @param method
	 * @return
	 */
	public ActivityExecutor getStepExecutor(ActivityTypeEnum workflowTypeEnum)
			throws InvalidActivityTypeException {

		// Return the appropriate Step Executor instance
		switch (workflowTypeEnum) {
		
		case INTEGRATION:
			return (ActivityExecutor) applicationContext.getBean(ETLActivityExecutorImpl.class);

		case END:
			return (ActivityExecutor) applicationContext.getBean(ENDActivityExecutorImpl.class);

		case GENERATE_REPORT:
			return (ActivityExecutor) applicationContext.getBean(ReportGenerationActivityExecutorImpl.class);

		default:
			throw new InvalidActivityTypeException(
					"Invalid processing step type specified.");

		}
	}

}
