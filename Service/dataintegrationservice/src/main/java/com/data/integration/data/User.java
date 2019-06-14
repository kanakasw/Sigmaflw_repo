package com.data.integration.data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.data.integration.service.enums.UserRoleEnum;
import com.data.integration.service.enums.UserStatusEnum;

@Entity
@Table(name = "User")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "UserId", unique = true, nullable = false)
	private Long userId;

	@Column(name = "FirstName")
	private String firstName;

	@Column(name = "LastName")
	private String lastName;

	@Column(name = "Email")
	private String email;

	@Column(name = "UserName", unique = true, nullable = false)
	private String login;

	@Column(name = "Password", nullable = false)
	private String userPassword;

	@Enumerated(EnumType.STRING)
	@Column(name = "Role")
	private UserRoleEnum role;

	@Enumerated(EnumType.STRING)
	@Column(name = "Status")
	private UserStatusEnum status;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "UserId", referencedColumnName = "UserId", nullable = true)
	private Set<IntegrationProcess> integrationProcesses = new HashSet<IntegrationProcess>();

	@Column(name = "CreatedDate")
	private Date createdDate;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	public User() {
		super();
	}

	public User(Long userID, String firstName, String lastName, String email,
			String userName, String password,
			UserRoleEnum role, UserStatusEnum status,
			Set<IntegrationProcess> integrationProcesses, Date createdDate,
			Date modifiedDate) {
		super();
		this.userId = userID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.login = userName;
		this.userPassword = password;
		this.role = role;
		this.status = status;
		this.integrationProcesses = integrationProcesses;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userID) {
		this.userId = userID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserRoleEnum getRole() {
		return role;
	}

	public void setRole(UserRoleEnum role) {
		this.role = role;
	}

	public UserStatusEnum getStatus() {
		return status;
	}

	public void setStatus(UserStatusEnum status) {
		this.status = status;
	}

	public Set<IntegrationProcess> getIntegrationProcesses() {
		return integrationProcesses;
	}

	public void setIntegrationProcesses(
			Set<IntegrationProcess> integrationProcesses) {
		this.integrationProcesses = integrationProcesses;
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

	public User(User user) {
		super();
		this.userId = user.userId;
		this.firstName = user.firstName;
		this.lastName = user.lastName;
		this.email = user.email;
		this.login = user.login;
		this.userPassword = user.userPassword;
		this.role = user.role;
		this.status = user.status;
		this.integrationProcesses = user.integrationProcesses;
		this.createdDate = user.createdDate;
		this.modifiedDate = user.modifiedDate;
	}

}
