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
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.data.integration.data.Activity;
import com.data.integration.data.EventQueue;
import com.data.integration.data.FileChunk;
import com.data.integration.data.IntegrationProcess;
import com.data.integration.data.IntegrationProcessExecution;
import com.data.integration.data.Subscriber;
import com.data.integration.repository.EventQueueRepository;
import com.data.integration.repository.FileChunkRepository;
import com.data.integration.repository.IntegrationProcessExecutionRepository;
import com.data.integration.repository.IntegrationProcessRepository;
import com.data.integration.repository.SubscriberRepository;
import com.data.integration.service.IntegrationProcessExecutor;
import com.data.integration.service.ProcessInitiator;
import com.data.integration.service.enums.EventQueueSpecEnum;
import com.data.integration.service.enums.EventQueueStatus;
import com.data.integration.service.enums.IntegrationProcessExecutionStatusEnum;
import com.data.integration.service.enums.WorkflowKeysEnum;
import com.data.integration.service.exceptions.ActivityConfigurationException;
import com.data.integration.service.exceptions.ActivityExecutionException;
import com.data.integration.service.exceptions.IntegrationProcessExecutionNotFoundException;
import com.data.integration.service.exceptions.IntegrationProcessNotFoundException;
import com.data.integration.service.exceptions.ProcessingException;
import com.data.integration.service.exceptions.SubscriberNotFoundException;
import com.data.integration.service.util.DataIntegrationFileSystemUtil;
import com.data.integration.service.vo.BatchProcessVO;
import com.data.integration.service.vo.ExecuteIntegrationProcessVO;
import com.data.integration.service.vo.IntegrationProcessResultVO;

@Service
public class ProcessInitiatorImpl implements ProcessInitiator {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(ProcessInitiatorImpl.class);

	@Autowired
	private IntegrationProcessRepository integrationProcessRepository;

	@Autowired
	private SubscriberRepository subscriberRepository;

	@Autowired
	private IntegrationProcessExecutor integrationProcessExecutor;

	@Autowired
	IntegrationProcessExecutionRepository integrationProcessExecutionRepository;

	@Autowired
	private DataIntegrationFileSystemUtil iQuantifiFileSystemUtil;

	@Autowired
	private EventQueueRepository eventQueueRepository;

	@Autowired
	private FileChunkRepository fileChunkRepository;

	@Value("${integration.filechunk.temp.location}")
	private String tempChunkLocation;

	@Override
	public IntegrationProcessResultVO initiateBatchProcess(
			BatchProcessVO batchProcessVO)
			throws ActivityConfigurationException,
			IntegrationProcessNotFoundException, ProcessingException,
			ActivityExecutionException, SubscriberNotFoundException {
		String message = null;
		IntegrationProcessResultVO integrationProcessResultVO = null;
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
			integrationProcessResultVO = new IntegrationProcessResultVO();
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
			Activity initActivity = integrationProcess.getActivities().stream()
					.filter(x -> true == x.isCausesNewProcessExecution())
					.findAny().orElse(null);
			if (initActivity != null) {
				try {
					String sourceFile = null;
					ExecuteIntegrationProcessVO executeIntegrationProcessVO = new ExecuteIntegrationProcessVO();
					executeIntegrationProcessVO.setActivityID(initActivity
							.getActivityID());
					executeIntegrationProcessVO
							.setCausesNewIntegrationProcessExecution(initActivity
									.isCausesNewProcessExecution());

					sourceFile = moveUploadedfileToInputFolder(mergedFilePath,
							subscriber, integrationProcess);

					executeIntegrationProcessVO.setSourcefilePath(sourceFile);

					integrationProcessResultVO = integrationProcessExecutor
							.executeIntegrationProcess(executeIntegrationProcessVO);

				} catch (IOException e) {
					LOGGER.error("File uploading failed", e);
					message = "Error occured while uploading file";
				}
				integrationProcessResultVO.setMessage("'"
						+ batchProcessVO.getFileName()
						+ "' file uploded successfully.");
				integrationProcessResultVO
						.setIntegrationProcessUniqueReference(integrationProcess
								.getIntegrationProcessUniqueReference());
				integrationProcessResultVO
						.setSubscriberUniqueReference(subscriber
								.getSubscriberUniqueKey());

			} else {
				String errorMessage = "Starter activity not found for IntegrationProcess ="
						+ batchProcessVO.getProcessUniqueKey()
						+ ", configuration error occured.";
				throw new ActivityConfigurationException(errorMessage);
			}

		} else {
			message = "IntegrationProcess with uniqueID "
					+ batchProcessVO.getProcessUniqueKey() + " doesn't Exist.";
			throw new IntegrationProcessNotFoundException(message);
		}
		return integrationProcessResultVO;
	}

	private String moveUploadedfileToInputFolder(String mergedFilePath,
			Subscriber subscriber, IntegrationProcess integrationProcess)
			throws IOException {
		String sourceFile;
		String filePath = iQuantifiFileSystemUtil.createFilePathForSubscriber(
				subscriber, integrationProcess);
		File toMerge = new File(mergedFilePath);
		sourceFile = createFolderStructureAndwriteFileToDisk(filePath, toMerge,
				UUID.randomUUID().toString(),
				FilenameUtils.getExtension(mergedFilePath));
		toMerge.delete();
		return sourceFile;
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

	private void saveChunk(BatchProcessVO batchProcessVO, String newFileName) {
		FileChunk fileChunk = new FileChunk();
		fileChunk.setChunkNumber(batchProcessVO.getChunkNumber());
		fileChunk.setTotalChunks(batchProcessVO.getTotalChunks());
		fileChunk.setKey(batchProcessVO.getFileUniqueKey());
		fileChunk.setInputFileChunkPath(newFileName);
		fileChunkRepository.save(fileChunk);
	}

	private void deleteFileChunks(List<FileChunk> listFileChunk) {
		for (FileChunk fileChunk : listFileChunk) {
			new File(fileChunk.getInputFileChunkPath()).delete();
			fileChunkRepository.delete(fileChunk);
		}
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

	@Override
	public IntegrationProcessResultVO getJobExecutionStatus(
			String subscriberId, String processId, String jobId) {

		IntegrationProcessExecution integrationProcessExecution = integrationProcessExecutionRepository
				.findByIntegrationProcessExecutionID(Long.parseLong(jobId));
		IntegrationProcessResultVO integrationProcessResultVO = new IntegrationProcessResultVO();
		integrationProcessResultVO
				.setIntegrationProcessExecutionStatus(integrationProcessExecution
						.getStatus().name());
		integrationProcessResultVO.setStatus(200);
		return integrationProcessResultVO;
	}

	@Override
	public IntegrationProcessResultVO startJobExecution(String subscriberId,
			String processId, String jobId)
			throws IntegrationProcessExecutionNotFoundException {
		IntegrationProcessResultVO integrationProcessResultVO = new IntegrationProcessResultVO();
		Subscriber subscriber = subscriberRepository
				.findBySubscriberUniqueKey(subscriberId);

		IntegrationProcess integrationProcess = integrationProcessRepository
				.findBySubscriberIDAndIntegrationProcessUniqueReference(
						subscriber.getSubscriberID(), processId);
		IntegrationProcessExecution execution = integrationProcess
				.getIntegrationProcessExecutions()
				.stream()
				.filter(x -> x.getIntegrationProcessID() == Long.valueOf(jobId))
				.findAny().orElse(null);
		if (execution == null) {
			throw new IntegrationProcessExecutionNotFoundException(
					String.format(
							"Job Execution %s does not exists for Integration Process %s.",
							jobId, processId));
		} else if (execution.getStatus() != IntegrationProcessExecutionStatusEnum.COMPLETED) {
			this.createJobStartEvent(jobId, subscriber, integrationProcess);
			integrationProcessResultVO.setStatus(200);
			integrationProcessResultVO.setMessage("Job started successfully");
		} else {
			integrationProcessResultVO.setStatus(200);
			integrationProcessResultVO
					.setMessage("Job Execution Already completed.");
		}

		return integrationProcessResultVO;
	}

	@SuppressWarnings("unchecked")
	private void createJobStartEvent(String jobId, Subscriber subscriber,
			IntegrationProcess integrationProcesses) {
		EventQueue eventQueue = new EventQueue();
		eventQueue.setCreatedDate(new Date());
		eventQueue.setModifiedDate(new Date());
		eventQueue.setStatus(EventQueueStatus.READY);
		eventQueue.setIntegrationProcessExecutionID(Long.parseLong(jobId));
		eventQueue.setIntegrationProcessID(integrationProcesses
				.getIntegrationProcessID());
		eventQueue.setSubscriberID(subscriber.getSubscriberID());
		JSONObject eventQueueSpec = new JSONObject();
		eventQueueSpec.put(WorkflowKeysEnum.EVENT_TO_TRIGGER.getKey(),
				EventQueueSpecEnum.START.getKey());
		eventQueue.setEventSpecification(eventQueueSpec.toJSONString());

		eventQueueRepository.save(eventQueue);
	}

	@Override
	@SuppressWarnings("unchecked")
	public IntegrationProcessResultVO updateSingleCustomerProfile(
			String subscriberUniqueKey,
			String integrationProcessUniqueReference, String inputData)
			throws ActivityConfigurationException, ProcessingException,
			IntegrationProcessNotFoundException, ActivityExecutionException,
			SubscriberNotFoundException {
		IntegrationProcessResultVO integrationProcessResultVO = new IntegrationProcessResultVO();

		Subscriber subscriber = subscriberRepository
				.findBySubscriberUniqueKey(subscriberUniqueKey);

		if (subscriber == null) {
			throw new SubscriberNotFoundException("Subscriber with unique ID :"
					+ subscriberUniqueKey + " doesn't exists.");
		}

		// Get integration process by unique reference
		IntegrationProcess integrationProcess = integrationProcessRepository
				.findBySubscriberIDAndIntegrationProcessUniqueReference(
						subscriber.getSubscriberID(),
						integrationProcessUniqueReference);

		if (integrationProcess != null) {

			Activity activity = integrationProcess.getActivities().stream()
					.filter(x -> true == x.isCausesNewProcessExecution())
					.findAny().orElse(null);

			if (activity != null) {
				ExecuteIntegrationProcessVO executeIntegrationProcessVO = new ExecuteIntegrationProcessVO();
				JSONObject activitySpec;
				try {
					activitySpec = (JSONObject) new JSONParser().parse(activity
							.getProcessingSpecification().toString());
					boolean isAsync = (boolean) activitySpec.getOrDefault(
							EventQueueSpecEnum.ASYNC.getKey(), false);
					executeIntegrationProcessVO.setAsync(isAsync);
					executeIntegrationProcessVO.setActivityID(activity
							.getActivityID());
					executeIntegrationProcessVO
							.setCausesNewIntegrationProcessExecution(activity
									.isCausesNewProcessExecution());
					executeIntegrationProcessVO.setInputData(inputData);
					integrationProcessResultVO = integrationProcessExecutor
							.executeIntegrationProcess(executeIntegrationProcessVO);
					integrationProcessResultVO
							.setMessage("Updating customer profile started successfully.");
				} catch (ParseException e) {
					throw new ActivityConfigurationException(
							"Error occured while Parsing Activity specification",
							e);
				}

			} else {
				String errorMessage = "Starter activity not found for IntegrationProcess ="
						+ integrationProcessUniqueReference
						+ ", configuration error occured.";
				throw new ActivityConfigurationException(errorMessage);
			}

		} else {
			String message = "IntegrationProcess with uniqueID "
					+ integrationProcessUniqueReference + " doesn't Exist.";
			throw new IntegrationProcessNotFoundException(message);
		}
		return integrationProcessResultVO;
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

}
