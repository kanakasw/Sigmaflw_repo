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

import com.data.integration.service.enums.EventQueueStatus;

@Entity
@Table(name = "EventQueue")
public class EventQueue {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "EventQueueID", unique = true, nullable = false)
	private long eventQueueID;

	@Column(name = "EventSpecification")
	private String eventSpecification;

	@Enumerated(EnumType.STRING)
	@Column(name = "Status")
	private EventQueueStatus status;

	@Column(name = "SubscriberID")
	private Long subscriberID;

	@Column(name = "IntegrationProcessID")
	private Long integrationProcessID;

	@Column(name = "CreatedDate")
	private Date createdDate;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@Column(name = "IntegrationProcessExecutionID")
	private Long integrationProcessExecutionID;

	public EventQueue() {
		// no-arg constructor as Required by JPA Entity specification
	}

	public EventQueue(long eventQueueID, String eventSpecification, EventQueueStatus status, Long subscriberID,
			Long integrationProcessID, Date createdDate, Date modifiedDate) {
		super();
		this.eventQueueID = eventQueueID;
		this.eventSpecification = eventSpecification;
		this.status = status;
		this.subscriberID = subscriberID;
		this.integrationProcessID = integrationProcessID;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}

	public long getEventQueueID() {
		return eventQueueID;
	}

	public void setEventQueueID(long eventQueueID) {
		this.eventQueueID = eventQueueID;
	}

	public String getEventSpecification() {
		return eventSpecification;
	}

	public void setEventSpecification(String eventSpecification) {
		this.eventSpecification = eventSpecification;
	}

	public void setStatus(EventQueueStatus status) {
		this.status = status;
	}

	public Long getSubscriberID() {
		return subscriberID;
	}

	public void setSubscriberID(Long subscriberID) {
		this.subscriberID = subscriberID;
	}

	public Long getIntegrationProcessID() {
		return integrationProcessID;
	}

	public void setIntegrationProcessID(Long integrationProcessID) {
		this.integrationProcessID = integrationProcessID;
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

	public void setIntegrationProcessExecutionID(Long integrationProcessExecutionID) {
		this.integrationProcessExecutionID = integrationProcessExecutionID;

	}

	public EventQueueStatus getStatus() {
		return status;
	}

	public Long getIntegrationProcessExecutionID() {
		return integrationProcessExecutionID;
	}
	

}
