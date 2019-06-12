package com.data.integration.service.enums;

/**
 * Represent keys required by Reporting engine
 * 
 * @author Aniket
 *
 */
public enum GenerateReportKeysEnum {

	REPORT_DEFINITION_FILEPATH("ReportDefinitionFilePath"),
	OUTPUT_FILE_PATH("OutputFilePath"),
	OUTPUT_TYPE("OutputType"),
	STEP_TO_TRIGGER("StepToTrigger"),
	INPUT_PARAMETERS("InputParameters");
	
	private String key;

	private GenerateReportKeysEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
