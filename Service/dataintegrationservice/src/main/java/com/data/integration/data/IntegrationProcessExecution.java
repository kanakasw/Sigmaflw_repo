package com.data.integration.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.data.integration.service.enums.IntegrationProcessExecutionStatusEnum;

/**
 * Represent IntegrationProcessExecution table.
 * 
 * @author Aniket
 *
 */
@Entity
@Table(name = "IntegrationProcessExecution")
public class IntegrationProcessExecution {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "IntegrationProcessExecutionID", unique = true, nullable = false)
	private Long integrationProcessExecutionID;

	@Column(name = "IntegrationProcessID")
	private Long integrationProcessID;

	@Column(name = "ExecutionStartTime")
	private Date executionStartTime;

	@Column(name = "ExecutionFinishTime")
	private Date executionFinishTime;

	@Enumerated(EnumType.STRING)
	@Column(name = "Status")
	private IntegrationProcessExecutionStatusEnum status;

	public IntegrationProcessExecution() {
		//no-arg constructor as Required by JPA Entity specification
	}

	public IntegrationProcessExecution(Long integrationProcessExecutionID,
			Long integrationProcessID, Date executionStartTime,
			Date executionFinishTime,
			IntegrationProcessExecutionStatusEnum status) {
		super();
		this.integrationProcessExecutionID = integrationProcessExecutionID;
		this.integrationProcessID = integrationProcessID;
		this.executionStartTime = executionStartTime;
		this.executionFinishTime = executionFinishTime;
		this.status = status;
	}

	public Long getIntegrationProcessExecutionID() {
		return integrationProcessExecutionID;
	}

	public void setIntegrationProcessExecutionID(Long integrationProcessExecutionID) {
		this.integrationProcessExecutionID = integrationProcessExecutionID;
	}

	public Long getIntegrationProcessID() {
		return integrationProcessID;
	}

	public void setIntegrationProcessID(Long integrationProcessID) {
		this.integrationProcessID = integrationProcessID;
	}

	public Date getExecutionStartTime() {
		return executionStartTime;
	}

	public void setExecutionStartTime(Date executionStartTime) {
		this.executionStartTime = executionStartTime;
	}

	public Date getExecutionFinishTime() {
		return executionFinishTime;
	}

	public void setExecutionFinishTime(Date executionFinishTime) {
		this.executionFinishTime = executionFinishTime;
	}

	public IntegrationProcessExecutionStatusEnum getStatus() {
		return status;
	}

	public void setStatus(IntegrationProcessExecutionStatusEnum status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "IntegrationProcessExecution [integrationProcessExecutionID="
				+ integrationProcessExecutionID + ", integrationProcessID="
				+ integrationProcessID + ", executionStartTime="
				+ executionStartTime + ", executionFinishTime="
				+ executionFinishTime + ", status=" + status + "]";
	}
	

	
}
