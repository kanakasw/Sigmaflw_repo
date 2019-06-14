package com.data.integration.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.data.integration.data.IntegrationProcess;
import com.data.integration.service.ActivityExecutionService;
import com.data.integration.service.ActivityService;
import com.data.integration.service.FileUploadService;
import com.data.integration.service.IntegrationProcessService;
import com.data.integration.service.exceptions.ActivityConfigurationException;
import com.data.integration.service.exceptions.IntegrationProcessException;
import com.data.integration.service.exceptions.IntegrationProcessNotFoundException;
import com.data.integration.service.exceptions.SubscriberNotFoundException;
import com.data.integration.service.vo.BatchProcessVO;
import com.data.integration.service.vo.IntegrationProcessResultVO;

/**
 * IntegrationProcess controller class defines all IntegrationProcess related
 * REST API's
 * 
 * @author Chetan
 *
 */
@RestController
public class IntegrationProcessController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(IntegrationProcessController.class);

	@Autowired
	private IntegrationProcessService integrationProcessService;

	@Autowired
	private ActivityExecutionService activityExecutionService;

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private ActivityService activityService;

	@ApiOperation(value = "Get all processes details using user name")
	@RequestMapping(method = RequestMethod.GET, path = "/Integration/process/subscriber/username/{userName}", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public List<IntegrationProcess> getIntegrationProcesses(
			@PathVariable("userName") String userName) throws Exception {

		LOGGER.info("Fetching all subscriber related processes with UserName : "
				+ userName);

		return integrationProcessService.getBySubscriberUserName(userName);
	}

	@ApiOperation(value = "Execute Integration Process")
	@RequestMapping(method = RequestMethod.GET, path = "/Integration/subscriber/process/{IntegrationProcessID}/execute", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public IntegrationProcessResultVO executeIntegrationProcesses(
			@PathVariable("IntegrationProcessID") Long integrationProcessID)
			throws Exception {

		LOGGER.info("Fetching IntegrationProcess with IntegrationProcessID : "
				+ integrationProcessID);

		return integrationProcessService
				.executeIntegrationProcess(integrationProcessID);
	}

	@ApiOperation(value = "Get IntegrationProcess By ID")
	@RequestMapping(method = RequestMethod.GET, path = "/Integration/subscriber/process/{IntegrationProcessID}", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public IntegrationProcess getIntegrationProcessWithID(
			@PathVariable("IntegrationProcessID") Long integrationProcessID)
			throws IntegrationProcessNotFoundException {

		return integrationProcessService
				.getIntegrationProcessByID(integrationProcessID);
	}

	@ApiOperation(value = "Upload File against Integration Process")
	@RequestMapping(value = "/Integration/subscriber/process/{subscriberUniqueKey}/{processUniqueKey}/{fileUniqueKey}/{fileName}/{ChunkNumber}/{TotalChunks}/upload", method = RequestMethod.POST, produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 202, message = "Accepted"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public String uploadFileAndConfigureBatchExecution(
			@PathVariable("subscriberUniqueKey") String subscriberUniqueKey,
			@PathVariable("processUniqueKey") String processUniqueKey,
			@PathVariable("fileUniqueKey") String fileUniqueKey,
			@PathVariable("fileName") String fileName,
			@PathVariable("ChunkNumber") String chunkNumber,
			@PathVariable("TotalChunks") String totalChunks,
			@RequestPart(required = true) @RequestParam("file") MultipartFile file)
			throws ActivityConfigurationException,
			IntegrationProcessNotFoundException, SubscriberNotFoundException,
			IOException {
		IntegrationProcessResultVO integrationProcessResultVO = null;

		BatchProcessVO batchProcessVO = new BatchProcessVO();
		batchProcessVO.setSubscriberUniqueKey(subscriberUniqueKey);
		batchProcessVO.setProcessUniqueKey(processUniqueKey);
		batchProcessVO.setFileUniqueKey(fileUniqueKey);
		batchProcessVO.setFileName(fileName);
		batchProcessVO.setChunkNumber(Integer.parseInt(chunkNumber));
		batchProcessVO.setTotalChunks(Integer.parseInt(totalChunks));
		batchProcessVO.setFile(file);

		integrationProcessResultVO = fileUploadService.saveFile(batchProcessVO);
		return new StringBuilder().append("{\"message\":")
				.append(integrationProcessResultVO.getMessage()).append("}")
				.toString();

	}

	@ApiOperation(value = "Update Integration Process Setup")
	@RequestMapping(method = RequestMethod.PUT, path = "/Integration/subscriber/process/{IntegrationProcessID}/update", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public IntegrationProcessResultVO updateIntegrationProcessSetup(
			@PathVariable("IntegrationProcessID") Long integrationProcessID,
			@RequestBody IntegrationProcess integrationProcessSetup)
			throws IntegrationProcessNotFoundException {

		IntegrationProcessResultVO integrationProcessResultVO = new IntegrationProcessResultVO();

		integrationProcessService.updateIntegrationProcessSetup(
				integrationProcessID, integrationProcessSetup);

		// update in process may change in its activity scheduling configuration
		// i.e. process enabled/disabled by user will schedule/unschedule
		// process activities
		activityService.updateActivitySchedulingConfig(integrationProcessSetup);

		integrationProcessResultVO.setStatus(200);
		integrationProcessResultVO
				.setMessage("Updated integration process setup details successfully");
		return integrationProcessResultVO;
	}

	@ApiOperation(value = "Create Integration Process Setup")
	@RequestMapping(method = RequestMethod.POST, path = "/Integration/subscriber/process/create", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public IntegrationProcessResultVO createIntegrationProcessSetup(
			@RequestBody IntegrationProcess integrationProcessSetup)
			throws IntegrationProcessException {

		IntegrationProcessResultVO integrationProcessResultVO = new IntegrationProcessResultVO();

		integrationProcessService
				.createIntegrationProcessSetup(integrationProcessSetup);

		// update in process may change in its activity scheduling configuration
		// i.e. process enabled/disabled by user will schedule/unschedule
		// process activities
		// activityService.updateActivitySchedulingConfig(integrationProcessSetupResult);

		integrationProcessResultVO.setStatus(200);
		integrationProcessResultVO
				.setMessage("Created integration process setup details successfully");
		return integrationProcessResultVO;
	}

	@ApiOperation(value = "Make Integration Process Disabled")
	@RequestMapping(method = RequestMethod.PUT, path = "/Integration/subscriber/process/{id}/disable")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public Boolean makeIntegrationProcessDisabledById(
			@PathVariable("id") Long integrationProcessID)
			throws IntegrationProcessException,
			IntegrationProcessNotFoundException {
		return integrationProcessService
				.makeIntegrationProcessDisabledById(integrationProcessID);
	}
}
