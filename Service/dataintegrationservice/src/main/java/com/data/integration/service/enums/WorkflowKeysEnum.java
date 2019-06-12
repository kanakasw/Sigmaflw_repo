package com.data.integration.service.enums;

/**
 * Represent keys required by kettle environment
 * 
 * @author Aniket
 *
 */
public enum WorkflowKeysEnum {

	WORKFLOW_TYPE("workflowType"),
	WORKFLOW_FILE_PATH("workflowFilePath"),
	WORKFLOW_FILE_NAME("workflowFileName"),
	INPUT_PARAMETERS("inputParameters"),
	OUTPUT_PARAMETERS("outputParameters"),
	ACTIVITY_EXECUTION_ID("ActivityExecutionID"),
	ON_ERROR_EVENT_ADDRESS("OnErrorEventAddress"),
	ON_SUCCESS_EVENT_ADDRESS("OnSuccessEventAddress"),
    STEP_TO_TRIGGER("StepToTrigger"),
    EVENT_TO_TRIGGER("EventToTrigger"),
    SOURCE_FILE_PATH("sourceFilePath"),
    UN_ZIP("unzip");
	
	private String key;

	private WorkflowKeysEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
