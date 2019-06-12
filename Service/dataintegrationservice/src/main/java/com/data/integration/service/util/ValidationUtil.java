package com.data.integration.service.util;

import java.io.File;
import java.util.Arrays;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

import com.data.integration.service.exceptions.InvalidFilePathException;



/**
 * Validation Utility class.
 * 
 * @author Aniket
 *
 */
public class ValidationUtil {

	/**
	 * Check if kettle file path is valid or not.<br/>
	 * Return "true" if file path is valid otherwise throws an exception.
	 * 
	 * @param kettleFilePath
	 * @return true if file path is valid.
	 * @throws InvalidFilePathException
	 */
	public static boolean isValidKettleFilePath(String kettleFilePath)
			throws InvalidFilePathException {

		if (StringUtils.isBlank(kettleFilePath)) {
			throw new InvalidFilePathException(
					"Blank or empty transformation file path.");
		}

		File file = new File(kettleFilePath);

		// Check whether given path exist
		if (!file.exists()) {
			throw new InvalidFilePathException(
					"Transformation file not found on the given path : "
							+ kettleFilePath);
		}

		// Check if path represents a file
		if (!file.isFile()) {
			throw new InvalidFilePathException(
					"Transformation file not found on the given path : "
							+ kettleFilePath);
		}

		return true;
	}

	/**
	 * Check whether time zone is valid or not.
	 * 
	 * @param timezoneName
	 * @return
	 */
	public static boolean isValidTimezoneName(String timezoneName) {
		
		Boolean match = Arrays.stream(
				TimeZone.getAvailableIDs()).anyMatch(
				available -> available.equals(timezoneName));
		
		return match;
		
	}

}
