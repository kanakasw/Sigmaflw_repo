package com.data.integration.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.data.integration.service.exceptions.ActivityConfigurationException;
import com.data.integration.service.exceptions.ActivityExecutionException;
import com.data.integration.service.exceptions.ActivityNotFoundException;
import com.data.integration.service.exceptions.ActivityReExecutionException;
import com.data.integration.service.exceptions.ActivitySchedulerException;
import com.data.integration.service.exceptions.IntegrationProcessExecutionNotFoundException;
import com.data.integration.service.exceptions.IntegrationProcessNotFoundException;
import com.data.integration.service.exceptions.SubscriberNotFoundException;
import com.data.integration.service.exceptions.UsernameAlreadyExistException;
import com.data.integration.service.vo.IntegrationProcessResultVO;

/**
 * @author KALYANI
 *
 */
@ControllerAdvice
public class AppErrorController extends ResponseEntityExceptionHandler {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(AppErrorController.class);

	@ExceptionHandler(MultipartException.class)
	@ResponseBody
	private ResponseEntity<IntegrationProcessResultVO> handleFileException(
			HttpServletRequest request, Throwable ex) {
		LOGGER.error("Error occured ", ex);
		IntegrationProcessResultVO integrationProcessResultVO = new IntegrationProcessResultVO();
		integrationProcessResultVO.setMessage(ex.getMessage());
		integrationProcessResultVO.setStatus(HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.badRequest().body(integrationProcessResultVO);
	}

	@ExceptionHandler(UsernameAlreadyExistException.class)
	@ResponseBody
	public final ResponseEntity<IntegrationProcessResultVO> handleUsernameAlreadyExistException(
			final HttpServletResponse httpServletResponse, Throwable ex) throws IOException {
		LOGGER.error("Error occured ", ex);
		IntegrationProcessResultVO integrationProcessResultVO = new IntegrationProcessResultVO();
		integrationProcessResultVO.setMessage(ex.getMessage());
		integrationProcessResultVO.setStatus(HttpStatus.CONFLICT.value());
		return ResponseEntity.badRequest().body(integrationProcessResultVO);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
			MissingServletRequestParameterException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		LOGGER.error("Error occured ", ex);
		IntegrationProcessResultVO integrationProcessResultVO = new IntegrationProcessResultVO();
		integrationProcessResultVO.setMessage(ex.getMessage());
		integrationProcessResultVO.setStatus(HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.badRequest().body(integrationProcessResultVO);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
			HttpMessageNotReadableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		LOGGER.error("Error occured ", ex);
		IntegrationProcessResultVO integrationProcessResultVO = new IntegrationProcessResultVO();
		integrationProcessResultVO.setMessage(ex.getMessage());
		integrationProcessResultVO.setStatus(HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.badRequest().body(integrationProcessResultVO);
	}

	@ExceptionHandler({ IntegrationProcessNotFoundException.class,
			ActivityConfigurationException.class,
			SubscriberNotFoundException.class,
			ActivityExecutionException.class,
			IntegrationProcessExecutionNotFoundException.class,
			ActivitySchedulerException.class,
			ActivityNotFoundException.class,
			ActivityReExecutionException.class})
	@ResponseBody
	private ResponseEntity<IntegrationProcessResultVO> handleApplicationError(
			HttpServletRequest request, Throwable ex) {
		LOGGER.error("Error occured ", ex);
		IntegrationProcessResultVO integrationProcessResultVO = new IntegrationProcessResultVO();
		integrationProcessResultVO.setMessage(ex.getMessage());
		integrationProcessResultVO.setStatus(HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.badRequest().body(integrationProcessResultVO);
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	private ResponseEntity<IntegrationProcessResultVO> handleAllError(
			HttpServletRequest request, Throwable ex) {
		LOGGER.error("Error occured ", ex);
		IntegrationProcessResultVO integrationProcessResultVO = new IntegrationProcessResultVO();
		integrationProcessResultVO.setMessage(ex.getMessage());
		integrationProcessResultVO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR
				.value());
		return ResponseEntity.badRequest().body(integrationProcessResultVO);
	}

}