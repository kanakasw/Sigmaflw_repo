package com.data.integration.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.data.integration.service.IntegrationEngine;
import com.data.integration.service.ProcessInitiator;
import com.data.integration.service.exceptions.ActivityConfigurationException;
import com.data.integration.service.exceptions.ActivityExecutionException;
import com.data.integration.service.exceptions.IntegrationProcessExecutionNotFoundException;
import com.data.integration.service.exceptions.IntegrationProcessNotFoundException;
import com.data.integration.service.exceptions.ProcessingException;
import com.data.integration.service.exceptions.SubscriberNotFoundException;
import com.data.integration.service.executor.impl.JobExecutor;
import com.data.integration.service.vo.BatchProcessVO;
import com.data.integration.service.vo.IntegrationProcessResultVO;

@RestController
public class AppController {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(AppController.class);

	@Value("${server.port}")
	private String serverPort;

	@Value("${host.name}")
	private String host;

	@Autowired
	private IntegrationEngine integrationEngineService;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private ProcessInitiator processInitiator;
	
	@Autowired
	private JobExecutor jobExecutor;
	 
    @Autowired
	private TokenStore tokenStore;
	
	@ApiOperation(value = "Update single customer profile")
	@RequestMapping(method = RequestMethod.POST, path = "/Integration/{subscriberUniqueKey}/{processUniqueKey}/Execute", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public String updateSingleConsumer(
			@PathVariable("subscriberUniqueKey") String subscriberUniqueKey,
			@PathVariable("processUniqueKey") String processUniqueKey,
			@RequestBody JSONObject inputData)
			throws ActivityConfigurationException, ProcessingException,
			IntegrationProcessNotFoundException, ActivityExecutionException,
			SubscriberNotFoundException {

		IntegrationProcessResultVO integrationProcessResultVO = processInitiator
				.updateSingleCustomerProfile(subscriberUniqueKey,
						processUniqueKey, inputData.toJSONString());
		return getSingleCustomerResultJsonString(integrationProcessResultVO);
	}

	@ApiOperation(value = "Submit customer profile file")
	@RequestMapping(value = "/Integration/{subscriberUniqueKey}/{processUniqueKey}/{fileUniqueKey}/{fileName}/{ChunkNumber}/{TotalChunks}/Jobs", 
	method = RequestMethod.POST, produces = "application/json")
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
			IntegrationProcessNotFoundException, ProcessingException,
			ActivityExecutionException, SubscriberNotFoundException {
		IntegrationProcessResultVO integrationProcessResultVO = null;

		BatchProcessVO batchProcessVO = new BatchProcessVO();
		batchProcessVO.setSubscriberUniqueKey(subscriberUniqueKey);
		batchProcessVO.setProcessUniqueKey(processUniqueKey);
		batchProcessVO.setFileUniqueKey(fileUniqueKey);
		batchProcessVO.setFileName(fileName);
		batchProcessVO.setChunkNumber(Integer.parseInt(chunkNumber));
        batchProcessVO.setTotalChunks(Integer.parseInt(totalChunks));
		batchProcessVO.setFile(file);

		integrationProcessResultVO = processInitiator
				.initiateBatchProcess(batchProcessVO);
		if (integrationProcessResultVO.getIntegrationProcessExecutionId() != null) {
			return getResultJsonString(integrationProcessResultVO);
		} else {
			return new StringBuilder().append("{\"message\":").append(integrationProcessResultVO.getMessage())
					.append("}").toString();
		}
	}

	@ApiOperation(value = "Start Integration job")
	@RequestMapping(value = "/Integration/{subscriberUniqueKey}/{processUniqueKey}/{jobId}/Start", method = RequestMethod.GET, produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public IntegrationProcessResultVO startAJob(
			@PathVariable("subscriberUniqueKey") String subscriberUniqueKey,
			@PathVariable("processUniqueKey") String processUniqueKey,
			@PathVariable("jobId") String jobId) throws IntegrationProcessExecutionNotFoundException {

		IntegrationProcessResultVO integrationProcessResultVO = processInitiator
				.startJobExecution(subscriberUniqueKey, processUniqueKey, jobId);

		return integrationProcessResultVO;
	}

	@ApiOperation(value = "Get Integration job status")
	@RequestMapping(value = "/Integration/{subscriberUniqueKey}/{processUniqueKey}/{jobId}/Status", method = RequestMethod.GET, produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public IntegrationProcessResultVO getStatusOfJob(
			@PathVariable("subscriberUniqueKey") String subscriberUniqueKey,
			@PathVariable("processUniqueKey") String processUniqueKey,
			@PathVariable("jobId") String jobId) {
		IntegrationProcessResultVO integrationProcessResultVO = processInitiator
				.getJobExecutionStatus(subscriberUniqueKey, processUniqueKey,
						jobId);
		return integrationProcessResultVO;
	}

	private String getResultJsonString(
			IntegrationProcessResultVO integrationProcessResultVO) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("Message", integrationProcessResultVO.getMessage());
		String jobStartAPI = generateJobStartUrl(integrationProcessResultVO);
		String jobStatusAPI = generateJobStatusUrl(integrationProcessResultVO);
		result.put("JobStartAPI", jobStartAPI);
		result.put("JobStatusAPI", jobStatusAPI);
		return JSONObject.toJSONString(result);
	}

	private String generateJobStatusUrl(
			IntegrationProcessResultVO integrationProcessResultVO) {
		StringBuilder sb = getURL(integrationProcessResultVO);
		sb.append("/Status");
		return sb.toString();
	}

	private String generateJobStartUrl(
			IntegrationProcessResultVO integrationProcessResultVO) {
		StringBuilder sb = getURL(integrationProcessResultVO);
		sb.append("/Start");
		return sb.toString();
	}

	private String getSingleCustomerResultJsonString(
			IntegrationProcessResultVO integrationProcessResultVO) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("Message", integrationProcessResultVO.getMessage());
		String jobStatusAPI = generateJobStatusUrl(integrationProcessResultVO);
		result.put("JobStatusAPI", jobStatusAPI);
		return JSONObject.toJSONString(result);
	}

	private StringBuilder getURL(
			IntegrationProcessResultVO integrationProcessResultVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("http://").append(host).append(":");
		sb.append(serverPort).append("/Integration/");
		sb.append(integrationProcessResultVO.getSubscriberUniqueReference());
		sb.append("/");
		sb.append(integrationProcessResultVO
				.getIntegrationProcessUniqueReference());
		sb.append("/");
		sb.append(integrationProcessResultVO.getIntegrationProcessExecutionId());
		return sb;
	}
	

	@ApiOperation(value = "invalidate token")
	@RequestMapping(value = "/revoketoken/{accessToken}", method = RequestMethod.GET, produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = IntegrationProcessResultVO.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure") })
	@ResponseBody
	public IntegrationProcessResultVO logout(@PathVariable("accessToken") String accessTokenValue) {

		OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenValue);
		IntegrationProcessResultVO integrationProcessResultVO=new IntegrationProcessResultVO();
        if (accessToken == null) {
        	integrationProcessResultVO.setStatus(400);
        	integrationProcessResultVO.setMessage("invalid access token.");
        	return integrationProcessResultVO;
        }
        if (accessToken.getRefreshToken() != null) {
            tokenStore.removeRefreshToken(accessToken.getRefreshToken());
        }
        tokenStore.removeAccessToken(accessToken);
        integrationProcessResultVO.setStatus(200);
    	integrationProcessResultVO.setMessage("Revoked Token Succesfully.");
    	return integrationProcessResultVO;
	}
}
