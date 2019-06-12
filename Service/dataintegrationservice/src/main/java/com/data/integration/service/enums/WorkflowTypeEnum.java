package com.data.integration.service.enums;

import org.apache.commons.lang.StringUtils;

import com.data.integration.service.exceptions.InvalidWorkflowTypeException;

/**
 * Enum represent kettle file type supported by system.<br/>
 * <br/>
 * Enum Syntax : <br/>
 * ENUM_NAME_IN_CAPS("transformation type", "File Extension");
 * 
 * @author Aniket
 *
 */
public enum WorkflowTypeEnum {

	JOB("Job", "kjb"), TRANSFORMATION("Transformation", "ktr");

	private String key;
	private String fileExtension;

	private WorkflowTypeEnum(String key, String fileExtension) {
		this.key = key;
		this.fileExtension = fileExtension;
	}

	public String getKey() {
		return key;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	/**
	 * Return enum on the basis of file type passed as argument. If not found
	 * then throws InvalidKettleFileTypeException.
	 * 
	 * @param fileType
	 * @return KettleFileTypeEnum
	 * @throws InvalidWorkflowTypeException
	 */
	public static WorkflowTypeEnum getByKey(String fileType)
			throws InvalidWorkflowTypeException {

		if (StringUtils.isNotBlank(fileType)) {

			for (WorkflowTypeEnum fileTypeEnum : WorkflowTypeEnum.values()) {

				if (fileType.equalsIgnoreCase(fileTypeEnum.getKey())) {
					return fileTypeEnum;
				}
			}
		}

		throw new InvalidWorkflowTypeException(
				String.format("Invalid workflow file type : %s ",fileType));
	}

	/**
	 * Return enum on the basis of file extension passed as argument. If not
	 * found then throws InvalidKettleFileTypeException.
	 * 
	 * @param fileExtension
	 * @return KettleFileTypeEnum
	 * @throws InvalidWorkflowTypeException
	 */
	public static WorkflowTypeEnum getByFileExtension(String fileExtension)
			throws InvalidWorkflowTypeException {

		if (StringUtils.isNotBlank(fileExtension)) {

			for (WorkflowTypeEnum fileTypeEnum : WorkflowTypeEnum.values()) {

				if (fileExtension.equalsIgnoreCase(fileTypeEnum
						.getFileExtension())) {
					return fileTypeEnum;
				}
			}
		}

		throw new InvalidWorkflowTypeException(String.format("Invalid workflow file type with extension : %s ",fileExtension));
				
	}

}
