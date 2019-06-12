package com.data.integration.service.vo;

import java.util.HashMap;
import java.util.Map;

public class EtlStepVO {

	private Map<String, Object> inputParameters = new HashMap<String, Object>();

	private String ktrFilePath;
	
	public EtlStepVO() {
	}
	public Map<String, Object> getInputParameters() {
		return inputParameters;
	}

	public void addInputParameters(String key, String value) {
		inputParameters.put(key, value);
	}

	public String getKtrFilePath() {
		return ktrFilePath;
	}

	public void setKtrFilePath(String ktrFilePath) {
		this.ktrFilePath = ktrFilePath;
	}

	public void setInputParameters(Map<String, Object> inputParameters) {
		this.inputParameters = inputParameters;
	}

}
