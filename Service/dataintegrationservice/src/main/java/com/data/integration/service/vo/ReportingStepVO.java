package com.data.integration.service.vo;

import java.util.HashMap;
import java.util.Map;

public class ReportingStepVO {

	private Map<String, Object> inputParameters = new HashMap<String, Object>();

	private String outputFilePath;
	
	public ReportingStepVO() {
	}
	public Map<String, Object> getInputParameters() {
		return inputParameters;
	}

	public void addInputParameters(String key, String value) {
		inputParameters.put(key, value);
	}

	public String getOutputFilePath() {
		return outputFilePath;
	}
	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}
	public void setInputParameters(Map<String, Object> inputParameters) {
		this.inputParameters = inputParameters;
	}

}
