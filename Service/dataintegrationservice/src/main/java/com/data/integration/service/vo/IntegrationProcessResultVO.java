package com.data.integration.service.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class IntegrationProcessResultVO {

	private Long integrationProcessExecutionId;

	private String subscriberUniqueReference;

	private String integrationProcessUniqueReference;

	private String message;
	
	private int status;

	private String integrationProcessExecutionStatus;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getIntegrationProcessExecutionId() {
		return integrationProcessExecutionId;
	}

	public void setIntegrationProcessExecutionId(Long integrationProcessExecutionId) {
		this.integrationProcessExecutionId = integrationProcessExecutionId;
	}

	public String getSubscriberUniqueReference() {
		return subscriberUniqueReference;
	}

	public void setSubscriberUniqueReference(String subscriberUniqueReference) {
		this.subscriberUniqueReference = subscriberUniqueReference;
	}

	public String getIntegrationProcessUniqueReference() {
		return integrationProcessUniqueReference;
	}

	public void setIntegrationProcessUniqueReference(String integrationProcessUniqueReference) {
		this.integrationProcessUniqueReference = integrationProcessUniqueReference;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String uploadprocessMessage) {
		this.message = uploadprocessMessage;
	}

	public String getIntegrationProcessExecutionStatus() {
		return integrationProcessExecutionStatus;
	}

	public void setIntegrationProcessExecutionStatus(String integrationProcessExecutionStatus) {
		this.integrationProcessExecutionStatus = integrationProcessExecutionStatus;
	}

}
