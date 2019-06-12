package com.data.integration.service.util;

import java.io.File;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common place for API related to zipping file.
 * 
 * @author Aniket
 *
 */
public class ZipUtil {

	private static final Logger logger = LoggerFactory.getLogger(ZipUtil.class);

	public static final String ZIP_FILE_EXTENSION = "zip";

	/**
	 * Zip the file. If don't want to set any password for zip file then just
	 * send file password as blank or null.
	 * 
	 * @param originalFile
	 *            file to zip
	 * @param deleteOriginalFile
	 *            whether to delete original file(true/false)
	 * @param filePassword
	 *            password for protecting the zip file.
	 * 
	 * @return String zip file path
	 * @throws ZipException
	 */
	public static String zipFile(File originalFile, boolean deleteOriginalFile,
			String filePassword) throws ZipException {

		StringBuilder zipfilePath = new StringBuilder(
				FilenameUtils.removeExtension(originalFile.getAbsolutePath()));
		zipfilePath.append(".");
		zipfilePath.append(ZIP_FILE_EXTENSION);

		createZip(zipfilePath.toString(), originalFile, filePassword);

		if (deleteOriginalFile) {
			originalFile.delete();
		}

		return zipfilePath.toString();

	}

	/**
	 * Unzip file
	 * 
	 * @param zippedFile
	 *            zip file
	 * @param deleteZipFile
	 *            whether to delete zip file(true/false)
	 * @return String original file path
	 * @throws ZipException
	 */
	public static String unzipInputFile(File zippedFile, boolean deleteZipFile,String password)
			throws ZipException {
		
		String fileExtension = FilenameUtils.getExtension(zippedFile.getName());
		
		if (!ZIP_FILE_EXTENSION.equals(fileExtension)) {
			logger.info("Can't unzip the file; Given file extension is not matching with zip file extension"
					+ " i.e. 'zip'. Current File extension : " + fileExtension);
			
			return zippedFile.getAbsolutePath();
		}

		StringBuilder extractLocation = new StringBuilder(
				FilenameUtils.getFullPath(zippedFile.getAbsolutePath()));

		String originalFileName = unzipFile(zippedFile,deleteZipFile,
				extractLocation.toString(), password);

		extractLocation.append(originalFileName);

		return extractLocation.toString();

	}

	/**
	 * Create zip file
	 * 
	 * @param zipfilePath
	 *            file path with proper extension for zip file
	 * @param fileToBeZipped
	 *            file which needs to be zipped
	 * @param password
	 *            password for zip file
	 * @throws ZipException
	 */
	private static void createZip(String zipfilePath, File fileToBeZipped,
			String password) throws ZipException {

		// Initiate ZipFile object with the path/name of the zip file.
		ZipFile zipFile = new ZipFile(zipfilePath);

		// Initiate Zip Parameters which define various properties such
		// as compression method, etc.
		ZipParameters parameters = new ZipParameters();

		// Set compression method to store compression
		parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

		// Set the compression level
		parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

		if (StringUtils.isNotBlank(password)) {

			// Set the encryption flag to true
			parameters.setEncryptFiles(true);

			// Set the encryption method to AES Zip Encryption
			parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);

			// set password
			parameters.setPassword(password);
		}

		// Add folder to the zip file
		zipFile.addFile(fileToBeZipped, parameters);

	}

	/**
	 * Unzip file
	 * 
	 * @param zippedFile
	 *            file which needs to be zipped
	 * @param deleteZipFile
	 *            delete zip file flag           
	 * @param whereToExtract
	 *            location where zip needs to be extracted
	 * @param password
	 *            password of zip file
	 * @throws ZipException
	 */
	private static String unzipFile(File zippedFile,boolean deleteZipFile,String whereToExtract,
			String password) throws ZipException {

		// Initiate ZipFile object with the path/name of the zip file.
		ZipFile zipFile = new ZipFile(zippedFile);

		// Check to see if the zip file is password protected
		if (zipFile.isEncrypted()) {
			// if yes, then set the password for the zip file
			zipFile.setPassword(password);
		}

		// Get the list of file headers from the zip file
		@SuppressWarnings("rawtypes")
		List fileHeaderList = zipFile.getFileHeaders();
		FileHeader fileHeader = (FileHeader) fileHeaderList.get(0);

		zipFile.extractAll(whereToExtract);


		if (deleteZipFile) {
			zippedFile.delete();
		}
		
		return fileHeader.getFileName();
	}

}
