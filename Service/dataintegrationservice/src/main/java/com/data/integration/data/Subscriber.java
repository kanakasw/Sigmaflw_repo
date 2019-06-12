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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "Subscriber")
public class Subscriber {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "SubscriberID", unique = true, nullable = false)
	private Long subscriberID;

	@Column(name = "SubscriberName", nullable = false)
	private String subscriberName;

	@Column(name = "SubscriberUniqueKey", unique = true, nullable = false)
	private String subscriberUniqueKey;

	@Column(name = "ClientID", unique = true, nullable = false)
	private String clientID;

	@Column(name = "ClientSecret", unique = true, nullable = false)
	private String clientSecret;

	@Column(name = "Login")
	private String login;

	@JsonIgnore
	@Column(name = "Password")
	private String password;

	@Column(name = "CreatedDate")
	private Date createdDate;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "SubscriberID", referencedColumnName = "SubscriberID", nullable = true)
	private Set<IntegrationProcess> integrationProcesses = new HashSet<IntegrationProcess>();

	public Subscriber() {
		super();
	}

	public Subscriber(Long subscriberID, String subscriberName,
			String subscriberUniqueKey, String clientID, String clientSecret,
			String login, String password, Date createdDate, Date modifiedDate,
			Set<IntegrationProcess> integrationProcesses) {
		super();
		this.subscriberID = subscriberID;
		this.subscriberName = subscriberName;
		this.subscriberUniqueKey = subscriberUniqueKey;
		this.clientID = clientID;
		this.clientSecret = clientSecret;
		this.login = login;
		this.password = password;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.integrationProcesses = integrationProcesses;
	}

	public Long getSubscriberID() {
		return subscriberID;
	}

	public void setSubscriberID(Long subscriberID) {
		this.subscriberID = subscriberID;
	}

	public String getSubscriberUniqueKey() {
		return subscriberUniqueKey;
	}

	public void setSubscriberUniqueKey(String subscriberUniqueKey) {
		this.subscriberUniqueKey = subscriberUniqueKey;
	}

	@JsonIgnore
	public String getClientID() {
		return clientID;
	}

	@JsonProperty
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	@JsonIgnore
	public String getClientSecret() {
		return clientSecret;
	}

	@JsonProperty
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
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

	public Set<IntegrationProcess> getIntegrationProcesses() {
		return integrationProcesses;
	}

	public void setIntegrationProcesses(
			Set<IntegrationProcess> integrationProcesses) {
		this.integrationProcesses = integrationProcesses;
	}

	public String getSubscriberName() {
		return subscriberName;
	}

	public void setSubscriberName(String subscriberName) {
		this.subscriberName = subscriberName;
	}

	public Subscriber(Subscriber subscriber) {
		super();
		this.subscriberID = subscriber.getSubscriberID();
		this.subscriberName = subscriber.getSubscriberName();
		this.subscriberUniqueKey = subscriber.getSubscriberUniqueKey();
		this.clientID = subscriber.getClientID();
		this.clientSecret = subscriber.getClientSecret();
		this.login = subscriber.getLogin();
		this.password = subscriber.getPassword();
		this.createdDate = subscriber.getCreatedDate();
		this.modifiedDate = subscriber.getModifiedDate();
		this.integrationProcesses = subscriber.getIntegrationProcesses();
	}

}
