package com.data.integration.service.enums;

/**
 * This Enum holds all types of keys used across this application module.
 * 
 * @author Aniket
 *
 */
public enum KeysEnum {

	STATUS("status"),
	STATUS_CODE("statusCode"),
	MESSAGE("message"),
	SUCCESS("success"),
	ERROR("error"),
	NOTE("note"),
	INPUT_PARAM_LIST("InputParamList"),
	OTUPUT_PARAM_LIST("OutputParamList"),
	PARAMETER_NAME("ParameterName"),
	PARAMETER_DESC("ParameterDesc"),
	SOURCE_FILE_PATH("sourceFilePath");
   

	private String key;

	private KeysEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

}
