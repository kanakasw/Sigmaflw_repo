package com.data.integration.data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "IntegrationProcess")
public class IntegrationProcess {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IntegrationProcessID", unique = true, nullable = false)
	private Long integrationProcessID;

	@Column(name = "SubscriberID", nullable = false)
	private long subscriberID;

	@Column(name = "UserId")
	private long userId;

	@Column(name = "IntegrationProcessUniqueReference", nullable = false)
	private String integrationProcessUniqueReference;

	@Column(name = "IntegrationProcessName", nullable = false)
	private String integrationProcessName;

	@Column(name = "FileEncryptionKey")
	private String fileEncryptionKey;

	@Column(name = "Enabled", nullable = false)
	private Boolean enabled;

	@Column(name = "CreatedDate")
	private Date createdDate;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "IntegrationProcessID", referencedColumnName = "IntegrationProcessID", nullable = true)
	private Set<Activity> activities = new HashSet<Activity>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "IntegrationProcessID", referencedColumnName = "IntegrationProcessID", nullable = true)
	private Set<IntegrationProcessExecution> integrationProcessExecutions = new HashSet<IntegrationProcessExecution>();

	public IntegrationProcess() {
		// no-arg constructor as Required by JPA Entity specification
	}

	public IntegrationProcess(Long integrationProcessID, long subscriberID,
			long userID, String integrationProcessUniqueReference,
			String integrationProcessName, String fileEncryptionKey,
			Boolean enabled, Date createdDate, Date modifiedDate,
			Set<Activity> activities,
			Set<IntegrationProcessExecution> integrationProcessExecutions) {
		super();
		this.integrationProcessID = integrationProcessID;
		this.subscriberID = subscriberID;
		this.userId = userID;
		this.integrationProcessUniqueReference = integrationProcessUniqueReference;
		this.integrationProcessName = integrationProcessName;
		this.fileEncryptionKey = fileEncryptionKey;
		this.enabled = enabled;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.activities = activities;
		this.integrationProcessExecutions = integrationProcessExecutions;
	}

	public Long getIntegrationProcessID() {
		return integrationProcessID;
	}

	public void setIntegrationProcessID(Long integrationProcessID) {
		this.integrationProcessID = integrationProcessID;
	}

	public long getSubscriberID() {
		return subscriberID;
	}

	public void setSubscriberID(long subscriberID) {
		this.subscriberID = subscriberID;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userID) {
		this.userId = userID;
	}

	public String getIntegrationProcessUniqueReference() {
		return integrationProcessUniqueReference;
	}

	public void setIntegrationProcessUniqueReference(
			String integrationProcessUniqueReference) {
		this.integrationProcessUniqueReference = integrationProcessUniqueReference;
	}

	public String getIntegrationProcessName() {
		return integrationProcessName;
	}

	public void setIntegrationProcessName(String integrationProcessName) {
		this.integrationProcessName = integrationProcessName;
	}

	public String getFileEncryptionKey() {
		return fileEncryptionKey;
	}

	public void setFileEncryptionKey(String fileEncryptionKey) {
		this.fileEncryptionKey = fileEncryptionKey;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
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

	public Set<IntegrationProcessExecution> getIntegrationProcessExecutions() {
		return integrationProcessExecutions;
	}

	public void setIntegrationProcessExecutions(
			Set<IntegrationProcessExecution> integrationProcessExecutions) {
		this.integrationProcessExecutions = integrationProcessExecutions;
	}

	public Set<Activity> getActivities() {
		return activities;
	}

	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}

	@Override
	public String toString() {
		return "IntegrationProcess [integrationProcessID="
				+ integrationProcessID + ", subscriberID=" + subscriberID
				+ ", userId=" + userId
				+ ", integrationProcessUniqueReference="
				+ integrationProcessUniqueReference
				+ ", integrationProcessName=" + integrationProcessName
				+ ", fileEncryptionKey=" + fileEncryptionKey + ", enabled="
				+ enabled + ", createdDate=" + createdDate + ", modifiedDate="
				+ modifiedDate + ", activities=" + activities
				+ ", integrationProcessExecutions="
				+ integrationProcessExecutions + "]";
	}

}
