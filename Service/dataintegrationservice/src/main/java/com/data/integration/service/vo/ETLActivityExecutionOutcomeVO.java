package com.data.integration.service.vo;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;


/**
 * Base class for execution outcome
 * 
 * @author Aniket
 *
 */
public class ETLActivityExecutionOutcomeVO extends ActivityExecutionOutcomeVO {

	private EtlStepVO etlStepRuntime = new EtlStepVO();

	private Map<String, Object> outputParameters;

	public EtlStepVO getEtlStepRuntime() {
		return etlStepRuntime;
	}

	public void setEtlStepRuntime(EtlStepVO etlStepRuntime) {
		this.etlStepRuntime = etlStepRuntime;
	}

	public Map<String, Object> getOutputParameters() {
		return outputParameters;
	}

	public void setOutputParameters(Map<String, Object> outputParameters) {
		this.outputParameters = outputParameters;
	}

	public void addOutputParameter(String key, String value) {
		this.outputParameters.put(key, value);

	}
	
	@Override
	public String toJsonString() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (IOException e) {
			throw e;
		}
	}

	@Override
	public String toString() {
		return "ETLActivityExecutionOutcomeVO [etlStepRuntime="
				+ etlStepRuntime + ", outputParameters=" + outputParameters
				+ "]";
	}


}
