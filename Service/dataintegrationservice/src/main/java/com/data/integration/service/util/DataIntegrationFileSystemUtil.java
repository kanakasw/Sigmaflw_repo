package com.data.integration.service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.data.integration.data.IntegrationProcess;
import com.data.integration.data.Subscriber;

@Component
public class DataIntegrationFileSystemUtil {

	@Value("${subscriber.prefix}")
	private String subscriberPrefix;

	@Value("${process.prefix}")
	private String processPrefix;

	@Value("${execution.prefix}")
	private String executionPrefix;

	@Value("${input.prefix}")
	private String inputPrefix;
	
	@Value("${integration.files}")
	private String rootFolder;

	public String createFilePathForSubscriber(Subscriber subscriber, IntegrationProcess integrationProcess) {
		StringBuilder sb = new StringBuilder();
		sb.append(rootFolder);
		sb.append("/");
		sb.append(subscriberPrefix);
		sb.append(subscriber.getSubscriberID());
		sb.append("/");
		sb.append(processPrefix);
		sb.append(integrationProcess.getIntegrationProcessID());
		sb.append("/");
		sb.append(inputPrefix);
		return sb.toString();
	}

}
