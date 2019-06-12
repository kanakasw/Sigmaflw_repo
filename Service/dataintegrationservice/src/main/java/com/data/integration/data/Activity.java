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

import com.data.integration.service.enums.ActivityTypeEnum;
import com.data.integration.service.enums.TriggerTypeEnum;

@Entity
@Table(name = "Activity")
public class Activity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ActivityID", unique = true, nullable = false)
	private Long activityID;

	@Column(name = "IntegrationProcessID", nullable = false)
	private Long integrationProcessID;

	@Column(name = "EventGroupName")
	private String eventGroupName;

	@Column(name = "EventGroupOrderIndex")
	private long eventGroupOrderIndex;

	@Column(name = "ActivityName")
	private String activityName;

	@Column(name = "ActivityOrderIndex")
	private long activityOrderIndex;

	@Enumerated(EnumType.STRING)
	@Column(name = "ActivityType")
	private ActivityTypeEnum activityType;

	@Column(name = "ProcessingSpecification",columnDefinition="TEXT")
	private String processingSpecification;

	@Column(name = "ActivityKey")
	private String activityKey;

	@Enumerated(EnumType.STRING)
	@Column(name = "TriggerType")
	private TriggerTypeEnum triggerType;

	@Column(name = "ScheduleSetup")
	private String scheduleSetup;

	@Column(name = "CausesNewProcessExecution")
	private boolean causesNewProcessExecution;
	
	@Column(name = "CreatedDate")
	private Date createdDate;
	
	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	public Activity() {
		//no-arg constructor as Required by JPA Entity specification
	}
	public Activity(Long activityID, Long integrationProcessID,
			String eventGroupName, long eventGroupOrderIndex,
			String activityName, long activityOrderIndex, ActivityTypeEnum activityType,
			String processingSpecification, String activityKey,
			TriggerTypeEnum triggerType, String scheduleSetup,
			boolean causesNewProcessExecution, Date createdDate,
			Date modifiedDate) {
		super();
		this.activityID = activityID;
		this.integrationProcessID = integrationProcessID;
		this.eventGroupName = eventGroupName;
		this.eventGroupOrderIndex = eventGroupOrderIndex;
		this.activityName = activityName;
		this.activityOrderIndex = activityOrderIndex;
		this.activityType = activityType;
		this.processingSpecification = processingSpecification;
		this.activityKey = activityKey;
		this.triggerType = triggerType;
		this.scheduleSetup = scheduleSetup;
		this.causesNewProcessExecution = causesNewProcessExecution;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}

	public Long getActivityID() {
		return activityID;
	}

	public void setActivityID(Long activityID) {
		this.activityID = activityID;
	}

	public Long getIntegrationProcessID() {
		return integrationProcessID;
	}

	public void setIntegrationProcessID(Long integrationProcessID) {
		this.integrationProcessID = integrationProcessID;
	}

	public String getEventGroupName() {
		return eventGroupName;
	}

	public void setEventGroupName(String eventGroupName) {
		this.eventGroupName = eventGroupName;
	}

	public long getEventGroupOrderIndex() {
		return eventGroupOrderIndex;
	}

	public void setEventGroupOrderIndex(long eventGroupOrderIndex) {
		this.eventGroupOrderIndex = eventGroupOrderIndex;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public long getActivityOrderIndex() {
		return activityOrderIndex;
	}

	public void setActivityOrderIndex(long activityOrderIndex) {
		this.activityOrderIndex = activityOrderIndex;
	}

	public ActivityTypeEnum getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityTypeEnum activityType) {
		this.activityType = activityType;
	}

	public String getProcessingSpecification() {
		return processingSpecification;
	}

	public void setProcessingSpecification(String processingSpecification) {
		this.processingSpecification = processingSpecification;
	}

	public String getActivityKey() {
		return activityKey;
	}

	public void setActivityKey(String activityKey) {
		this.activityKey = activityKey;
	}

	public TriggerTypeEnum getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(TriggerTypeEnum triggerType) {
		this.triggerType = triggerType;
	}

	public String getScheduleSetup() {
		return scheduleSetup;
	}

	public void setScheduleSetup(String scheduleSetup) {
		this.scheduleSetup = scheduleSetup;
	}

	public boolean isCausesNewProcessExecution() {
		return causesNewProcessExecution;
	}

	public void setCausesNewProcessExecution(boolean causesNewProcessExecution) {
		this.causesNewProcessExecution = causesNewProcessExecution;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	
}
