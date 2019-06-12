package com.data.integration.service.vo;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;


/**
 * class for Report execution outcome
 * 
 * @author Aniket
 *
 */
public class GenerateReportActivityExecutionOutcomeVO extends ActivityExecutionOutcomeVO {

	private ReportingStepVO etlStepRuntime = new ReportingStepVO();

    private String reportDefinitionFile;
	

	public ReportingStepVO getEtlStepRuntime() {
		return etlStepRuntime;
	}

	public void setEtlStepRuntime(ReportingStepVO etlStepRuntime) {
		this.etlStepRuntime = etlStepRuntime;
	}

	public String getReportDefinitionFile() {
		return reportDefinitionFile;
	}

	public void setReportDefinitionFile(String reportDefinitionFile) {
		this.reportDefinitionFile = reportDefinitionFile;
	}

	@Override
	public String toString() {
		return "GenerateReportActivityExecutionOutcomeVO [etlStepRuntime="
				+ etlStepRuntime + ", reportDefinitionFile="
				+ reportDefinitionFile + "]";
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


}
