package com.data.integration.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.data.integration.data.FileChunk;
import com.data.integration.data.IntegrationProcess;
import com.data.integration.data.Subscriber;
import com.data.integration.repository.FileChunkRepository;
import com.data.integration.repository.IntegrationProcessRepository;
import com.data.integration.repository.SubscriberRepository;
import com.data.integration.service.FileUploadService;
import com.data.integration.service.exceptions.IntegrationProcessNotFoundException;
import com.data.integration.service.exceptions.SubscriberNotFoundException;
import com.data.integration.service.util.DataIntegrationFileSystemUtil;
import com.data.integration.service.vo.BatchProcessVO;
import com.data.integration.service.vo.IntegrationProcessResultVO;

/**
 * FileUploadService implementation class.
 * 
 * @author Aniket
 *
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(FileUploadServiceImpl.class);

	@Value("${integration.filechunk.temp.location}")
	private String tempChunkLocation;

	@Autowired
	private FileChunkRepository fileChunkRepository;

	@Autowired
	private IntegrationProcessRepository integrationProcessRepository;

	@Autowired
	private SubscriberRepository subscriberRepository;

	@Autowired
	private DataIntegrationFileSystemUtil iQuantifiFileSystemUtil;

	@Override
	public IntegrationProcessResultVO saveFile(BatchProcessVO batchProcessVO)
			throws SubscriberNotFoundException, IOException,
			IntegrationProcessNotFoundException {
		String message = null;
		IntegrationProcessResultVO integrationProcessResultVO = new IntegrationProcessResultVO();
		String mergedFileName = null;
		String mergedFilePath = null;

		String ext = FilenameUtils.getExtension(batchProcessVO.getFileName());
		String fileName = UUID.randomUUID().toString() + "." + ext;
		String newFileName = copyFile(tempChunkLocation,
				batchProcessVO.getFile(), fileName);

		saveChunk(batchProcessVO, newFileName);

		// if all chunk Received
		if (fileChunkRepository.countByKey(batchProcessVO.getFileUniqueKey()).equals(batchProcessVO
				.getTotalChunks())) {
			mergedFileName = UUID.randomUUID().toString() + "." + ext;
			mergedFilePath = doMerging(batchProcessVO, mergedFileName);
		} else {
			integrationProcessResultVO
					.setMessage("File chunk Uploaded successfully");
			integrationProcessResultVO.setStatus(200);
			return integrationProcessResultVO;
		}
		Subscriber subscriber = subscriberRepository
				.findBySubscriberUniqueKey(batchProcessVO
						.getSubscriberUniqueKey());

		if (subscriber == null) {
			throw new SubscriberNotFoundException("Subscriber with unique ID :"
					+ batchProcessVO.getSubscriberUniqueKey()
					+ " doesn't exists.");
		}
		IntegrationProcess integrationProcess = integrationProcessRepository
				.findBySubscriberIDAndIntegrationProcessUniqueReference(
						subscriber.getSubscriberID(),
						batchProcessVO.getProcessUniqueKey());

		if (integrationProcess != null) {

			moveUploadedfileToInputFolder(mergedFilePath, subscriber,
					integrationProcess);

			integrationProcessResultVO.setMessage("'"
					+ batchProcessVO.getFileName()
					+ "' file uploded successfully.");
			integrationProcessResultVO
					.setIntegrationProcessUniqueReference(integrationProcess
							.getIntegrationProcessUniqueReference());
			integrationProcessResultVO.setSubscriberUniqueReference(subscriber
					.getSubscriberUniqueKey());

		} else {
			message = "IntegrationProcess with uniqueID "
					+ batchProcessVO.getProcessUniqueKey() + " doesn't Exist.";
			throw new IntegrationProcessNotFoundException(message);
		}
		return integrationProcessResultVO;
	}

	private String copyFile(String filePath, MultipartFile file, String fileName) {
		Path path = Paths.get(filePath);
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				LOGGER.error("Error occured while creating Path", e);
			}
		}
		try {
			Path target = Paths.get(filePath, fileName);
			Files.copy(file.getInputStream(), target);
			return target.toAbsolutePath().toString();
		} catch (IOException | RuntimeException e) {

			LOGGER.error("Error while copying file", e);
		}

		return fileName;
	}

	private void saveChunk(BatchProcessVO batchProcessVO, String newFileName) {
		FileChunk fileChunk = new FileChunk();
		fileChunk.setChunkNumber(batchProcessVO.getChunkNumber());
		fileChunk.setTotalChunks(batchProcessVO.getTotalChunks());
		fileChunk.setKey(batchProcessVO.getFileUniqueKey());
		fileChunk.setInputFileChunkPath(newFileName);
		fileChunkRepository.save(fileChunk);
	}

	private String doMerging(BatchProcessVO batchProcessVO, String mergedFile) {
		String mergedFilePath = null;
		List<FileChunk> listFileChunk = fileChunkRepository
				.findByKeyOrderByChunkNumber(batchProcessVO.getFileUniqueKey());
		try {
			mergedFilePath = mergeFileChunks(listFileChunk, mergedFile);
		} catch (IOException e) {
			LOGGER.error("Error occured while merging file chunk", e);
		} finally {
			deleteFileChunks(listFileChunk);
		}
		return mergedFilePath;
	}

	private String mergeFileChunks(List<FileChunk> listFileChunk,
			String mergedFileName) throws IOException {

		String mergedFilePath = tempChunkLocation + "/" + mergedFileName;
		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(mergedFilePath));

		for (FileChunk fileChunk : listFileChunk) {
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(fileChunk.getInputFileChunkPath()));
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
		}
		out.close();
		return mergedFilePath;

	}

	private void deleteFileChunks(List<FileChunk> listFileChunk) {
		for (FileChunk fileChunk : listFileChunk) {
			new File(fileChunk.getInputFileChunkPath()).delete();
			fileChunkRepository.delete(fileChunk);
		}
	}

	private String moveUploadedfileToInputFolder(String mergedFilePath,
			Subscriber subscriber, IntegrationProcess integrationProcess)
			throws IOException {
		String sourceFile;
		String filePath = iQuantifiFileSystemUtil.createFilePathForSubscriber(
				subscriber, integrationProcess);
		File toMerge = new File(mergedFilePath);
		sourceFile = createFolderStructureAndwriteFileToDisk(filePath, toMerge,
				new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()),
				FilenameUtils.getExtension(mergedFilePath));
		toMerge.delete();
		return sourceFile;
	}

	private String createFolderStructureAndwriteFileToDisk(String filePath,
			File file, String fileName, String ext) throws IOException {
		Path path = Paths.get(filePath);
		if (!Files.exists(path)) {
			Files.createDirectories(path);
		}
		Path target = Paths.get(filePath, fileName + "." + ext);

		try {
			FileUtils.moveFile(file, target.toFile());
		} catch (IOException | RuntimeException e) {
			LOGGER.error("Error occured while moving file", e);
		}
		return target.toAbsolutePath().toString();
	}

}
