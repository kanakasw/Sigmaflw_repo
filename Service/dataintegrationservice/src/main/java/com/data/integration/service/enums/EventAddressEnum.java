package com.data.integration.service.enums;

/**
 * Represent addresses for all application events
 * 
 * @author Aniket
 *
 */
public enum EventAddressEnum {
	
	EXECUTE_WORKFLOW("execute.workflow"),
	GET_WORKFLOW_INFO("get.workflow.info");
	
	private String key;

	private EventAddressEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
