package com.data.integration.data;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.data.integration.service.enums.ActivityExecutionStatusEnum;

/**
 * Represent ActivityExecution table.
 * 
 * @author Aniket
 *
 */
@Entity
@Table(name = "ActivityExecution")
public class ActivityExecution {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ActivityExecutionID", unique = true, nullable = false)
	private Long activityExecutionID;

	@Column(name = "ExecutionStartTime")
	private Date executionStartTime;

	@Column(name = "ExecutionFinishTime")
	private Date executionFinishTime;

	@Enumerated(EnumType.STRING)
	@Column(name = "Status")
	private ActivityExecutionStatusEnum status;

	@Column(name = "ExecutionStepOutcome",columnDefinition="TEXT")
	private String executionStepOutcome;
	
	@OneToOne
	@JoinColumn(name="ActivityID", referencedColumnName  = "ActivityID")
	private Activity activity;
	
	@OneToOne
	@JoinColumn(name="IntegrationProcessExecutionID", referencedColumnName  = "IntegrationProcessExecutionID")
	private IntegrationProcessExecution integrationProcessExecution;

	public ActivityExecution() {
		//no-arg constructor as Required by JPA specification
	}

	public ActivityExecution(Long activityExecutionID, Date executionStartTime,
			Date executionFinishTime, ActivityExecutionStatusEnum status,
			String executionStepOutcome, Activity activity,
			IntegrationProcessExecution integrationProcessExecution) {
		super();
		this.activityExecutionID = activityExecutionID;
		this.executionStartTime = executionStartTime;
		this.executionFinishTime = executionFinishTime;
		this.status = status;
		this.executionStepOutcome = executionStepOutcome;
		this.activity = activity;
		this.integrationProcessExecution = integrationProcessExecution;
	}

	public Long getActivityExecutionID() {
		return activityExecutionID;
	}

	public void setActivityExecutionID(Long activityExecutionID) {
		this.activityExecutionID = activityExecutionID;
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

	public ActivityExecutionStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ActivityExecutionStatusEnum status) {
		this.status = status;
	}

	public String getExecutionStepOutcome() {
		return executionStepOutcome;
	}

	public void setExecutionStepOutcome(String executionStepOutcome) {
		this.executionStepOutcome = executionStepOutcome;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public IntegrationProcessExecution getIntegrationProcessExecution() {
		return integrationProcessExecution;
	}

	public void setIntegrationProcessExecution(
			IntegrationProcessExecution integrationProcessExecution) {
		this.integrationProcessExecution = integrationProcessExecution;
	}

	@Override
	public String toString() {
		return "ActivityExecution [activityExecutionID=" + activityExecutionID
				+ ", executionStartTime=" + executionStartTime
				+ ", executionFinishTime=" + executionFinishTime + ", status="
				+ status + ", executionStepOutcome=" + executionStepOutcome
				+ ", activity=" + activity + ", integrationProcessExecution="
				+ integrationProcessExecution + "]";
	}
	
}
