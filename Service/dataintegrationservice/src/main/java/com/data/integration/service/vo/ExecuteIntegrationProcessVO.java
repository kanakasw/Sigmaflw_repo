package com.data.integration.service.vo;

import java.io.IOException;
import java.io.Serializable;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * This VO holds information to kick start IntegrationProcess execution
 * 
 * @author Aniket
 *
 */
public class ExecuteIntegrationProcessVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 466611177440881356L;

	private Long activityID;
	
	private String sourcefilePath;
	
	private String inputData;
	
	private Boolean causesNewIntegrationProcessExecution;

	private boolean async;
	
	private Long integrationProcessExcutionID;
	
	private boolean unzip;
	
	public ExecuteIntegrationProcessVO() {
		// TODO Auto-generated constructor stub
	}
	public ExecuteIntegrationProcessVO(Long activityID, String sourcefilePath,
			String inputData, Boolean causesNewIntegrationProcessExecution,
			boolean async) {
		super();
		this.activityID = activityID;
		this.sourcefilePath = sourcefilePath;
		this.inputData = inputData;
		this.causesNewIntegrationProcessExecution = causesNewIntegrationProcessExecution;
		this.async = async;
	}

	public String getSourcefilePath() {
		return sourcefilePath;
	}

	public void setSourcefilePath(String sourcefilePath) {
		this.sourcefilePath = sourcefilePath;
	}

	public Boolean getCausesNewIntegrationProcessExecution() {
		return causesNewIntegrationProcessExecution;
	}

	public void setCausesNewIntegrationProcessExecution(
			Boolean causesNewIntegrationProcessExecution) {
		this.causesNewIntegrationProcessExecution = causesNewIntegrationProcessExecution;
	}

	public Long getActivityID() {
		return activityID;
	}

	public void setActivityID(Long activityID) {
		this.activityID = activityID;
	}

	public String getInputData() {
		return inputData;
	}

	public void setInputData(String inputData) {
		this.inputData = inputData;
	}

	public Boolean getAsync() {
		return async;
	}

	public void setAsync(Boolean async) {
		this.async = async;
	}


	public Long getIntegrationProcessExcutionID() {
		return integrationProcessExcutionID;
	}
	public void setIntegrationProcessExcutionID(Long integrationProcessExcutionID) {
		this.integrationProcessExcutionID = integrationProcessExcutionID;
	}
	public boolean isUnzip() {
		return unzip;
	}
	public void setUnzip(boolean unzip) {
		this.unzip = unzip;
	}
	@Override
	public String toString() {
		return "ExecuteIntegrationProcessVO [activityID=" + activityID
				+ ", sourcefilePath=" + sourcefilePath + ", inputData="
				+ inputData + ", causesNewIntegrationProcessExecution="
				+ causesNewIntegrationProcessExecution + ", async=" + async
				+ "]";
	}

	
	public String toJsonString() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (IOException e) {
			throw e;
		}
	}
	
	public ExecuteIntegrationProcessVO toObject(String toJsonString) throws IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(toJsonString,ExecuteIntegrationProcessVO.class);
		} catch (IOException e) {
			throw e;
		}
	}
}
