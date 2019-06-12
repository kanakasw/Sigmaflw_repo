package com.data.integration;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.data.integration.DataIntegrationApplication;
import com.data.integration.data.Activity;
import com.data.integration.data.IntegrationProcess;
import com.data.integration.data.Subscriber;
import com.data.integration.repository.ActivityRepository;
import com.data.integration.repository.IntegrationProcessRepository;
import com.data.integration.repository.SubscriberRepository;
import com.data.integration.service.enums.ActivityTypeEnum;
import com.data.integration.service.enums.TriggerTypeEnum;
import com.data.integration.service.enums.WorkflowKeysEnum;
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DataIntegrationApplication.class)
@WebAppConfiguration
public class BaseTest {
	
	@Autowired
	private static Environment environment;
	
	static boolean  isSetupDone=false;

	@Autowired
	private IntegrationProcessRepository integrationProcessRepository;
	@Autowired
	private ActivityRepository activityRepository;
	@Autowired
	private SubscriberRepository subscriberRepository;

	
	@BeforeClass
    public static void setupJndi() throws Exception {
		
		final SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
		 BasicDataSource dataSource = new BasicDataSource();
	     dataSource.setUsername("sa");
	     dataSource.setPassword("iQuantifi1024");
	     dataSource.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
	     dataSource.setUrl("jdbc:jtds:sqlserver://win-9vnllmg216b;instance=SQLEXPRESS;DatabaseName=iQuantifiDB");
	     dataSource.setValidationQuery("SELECT 1");
	     builder.bind("java:comp/env/jdbc/iQuantifiDataSource", dataSource);
	     builder.activate();
   }
	@Before
	public void setUp() {
		
	   /*if(!isSetupDone) {
		Subscriber subscriber = mockSubscriber();

		IntegrationProcess singleRequestIntegrationProcess = mockSingleRequestIntegrationProcess(subscriber);

		IntegrationProcess batchRequestIntegrationProcess = mockBatchRequestIntegrationProcess(subscriber);

		setUpSingleRequestActivity(singleRequestIntegrationProcess.getIntegrationProcessID());
		
		setUpBatchRequestActivity(batchRequestIntegrationProcess.getIntegrationProcessID());
		
		isSetupDone=true;
		
	   } */
	}

	@Test
	public void test(){
		
	}
	private IntegrationProcess mockSingleRequestIntegrationProcess(Subscriber subscriber) {

		IntegrationProcess singleRequestIntegrationProcess = new IntegrationProcess();

		singleRequestIntegrationProcess.setEnabled(true);
		singleRequestIntegrationProcess.setFileEncryptionKey("IQuantifiKey");
		singleRequestIntegrationProcess.setIntegrationProcessName("Refresh User profile");
		singleRequestIntegrationProcess.setIntegrationProcessUniqueReference("7bea686e-4cde-11e6-beb8-9e71128cae777");
		singleRequestIntegrationProcess.setSubscriberID(subscriber.getSubscriberID());
		integrationProcessRepository.save(singleRequestIntegrationProcess);
		return singleRequestIntegrationProcess;

	}

	private IntegrationProcess mockBatchRequestIntegrationProcess(Subscriber subscriber) {
		IntegrationProcess batchIntegrationProcess = new IntegrationProcess();
		batchIntegrationProcess.setEnabled(true);
		batchIntegrationProcess.setFileEncryptionKey("IQuantifiKey");
		batchIntegrationProcess.setIntegrationProcessName("Batch Refresh User profile");
		batchIntegrationProcess.setIntegrationProcessUniqueReference("928d6166-4815-11e6-beb8-9e71128cae77");
		batchIntegrationProcess.setSubscriberID(subscriber.getSubscriberID());
		integrationProcessRepository.save(batchIntegrationProcess);
		return batchIntegrationProcess;

	}

	private Subscriber mockSubscriber() {
		Subscriber subscriber = new Subscriber();
		subscriber.setClientID("172d4260-4802-11e6-beb8-9e71128cae77");
		subscriber.setSubscriberUniqueKey("9c7065e4-4cc9-11e6-beb8-9e71128cae77");
		subscriber.setClientSecret("e4feeb68-4801-11e6-beb8-9e71128cae77");
		subscriber.setLogin("iquantifi");
		subscriber.setPassword("12345");
		subscriberRepository.save(subscriber);
		return subscriber;
	}

	private void setUpSingleRequestActivity(Long integrationProcessID) {

		Activity activity3 = new Activity();
		activity3.setActivityKey("66daed12-4cd0-11e6-beb8-9e71128cae77");
		activity3.setActivityName("Read and Update User profileJSON");
		activity3.setActivityOrderIndex(1);
		activity3.setActivityType(ActivityTypeEnum.INTEGRATION);
		activity3.setCausesNewProcessExecution(true);
		activity3.setEventGroupName("REFRESH_USERPROFILE_JSON");
		activity3.setEventGroupOrderIndex(1);
		activity3.setIntegrationProcessID(integrationProcessID);
		JSONObject requestJson3 = new JSONObject();
		requestJson3.put(WorkflowKeysEnum.ACTIVITY_EXECUTION_ID.getKey(), "${ActivityExecutionID}");
		requestJson3.put(WorkflowKeysEnum.WORKFLOW_TYPE.getKey(), "Transformation");
		requestJson3.put(WorkflowKeysEnum.WORKFLOW_FILE_PATH.getKey(),
				"iquantifi-files\\Sub_${SubscriberID}\\Proc_${IntegrationProcessID}\\PentahoFiles\\refresh-user-profile-JSON.ktr");

		JSONObject inputPrameters3 = new JSONObject();
		inputPrameters3.put("inputData", "${inputData}");
		inputPrameters3.put("IntegrationProcessExecutionID", "${IntegrationProcessExecutionID}");
		inputPrameters3.put("IntegrationProcessID", "${IntegrationProcessID}");
		inputPrameters3.put("SubscriberID", "${SubscriberID}");
		inputPrameters3.put("ActivityExecutionID", "${ActivityExecutionID}");
		requestJson3.put(WorkflowKeysEnum.INPUT_PARAMETERS.getKey(), inputPrameters3);
		requestJson3.put(WorkflowKeysEnum.OUTPUT_PARAMETERS.getKey(), new JSONArray());
		requestJson3.put(WorkflowKeysEnum.STEP_TO_TRIGGER.getKey(), 2);
		activity3.setProcessingSpecification(requestJson3.toJSONString());
		activity3.setScheduleSetup("");
		activity3.setTriggerType(TriggerTypeEnum.MANUAL);
		activityRepository.save(activity3);

		Activity activity4 = new Activity();
		activity4.setActivityKey("771d242e-4cd0-11e6-beb8-9e71128cae77");
		activity4.setActivityName("End execution");
		activity4.setActivityOrderIndex(1);
		activity4.setActivityType(ActivityTypeEnum.END);
		activity4.setCausesNewProcessExecution(false);
		activity4.setEventGroupName("END_EXECUTION");
		activity4.setEventGroupOrderIndex(2);
		activity4.setIntegrationProcessID(integrationProcessID);
		JSONObject requestJsonEnd4 = new JSONObject();
		activity4.setProcessingSpecification(requestJsonEnd4.toJSONString());
		activity4.setScheduleSetup("");
		activity4.setTriggerType(TriggerTypeEnum.MANUAL);
		activityRepository.save(activity4);
	}

	private void setUpBatchRequestActivity(Long integrationProcessID) {

		Activity activity = new Activity();
		activity.setActivityKey("afcab9ae-4815-11e6-beb8-9e71128cae78");
		activity.setActivityName("Prepare Runtime Environment");
		activity.setActivityOrderIndex(1);
		activity.setActivityType(ActivityTypeEnum.INTEGRATION);
		activity.setCausesNewProcessExecution(true);
		activity.setEventGroupName("PREPARE_RUNTIME");
		activity.setEventGroupOrderIndex(1);
		activity.setIntegrationProcessID(integrationProcessID);
		JSONObject requestJson = new JSONObject();
		requestJson.put(WorkflowKeysEnum.ACTIVITY_EXECUTION_ID.getKey(), "${ActivityExecutionID}");
		requestJson.put(WorkflowKeysEnum.WORKFLOW_TYPE.getKey(), "Job");
		requestJson.put(WorkflowKeysEnum.WORKFLOW_FILE_PATH.getKey(),
				"iquantifi-files\\Sub_${SubscriberID}\\Proc_${IntegrationProcessID}\\PentahoFiles\\prepare-runtime-enviorment.kjb");
		JSONObject inputPrameters = new JSONObject();
		inputPrameters.put("IntegrationProcessExecutionID", "${IntegrationProcessExecutionID}");
		inputPrameters.put("IntegrationProcessID", "${IntegrationProcessID}");
		inputPrameters.put("SubscriberID", "${SubscriberID}");
		inputPrameters.put("ActivityExecutionID", "${ActivityExecutionID}");
		inputPrameters.put("BaseFolderPath", "${BaseFolderPath}");
		inputPrameters.put("SourceFile", "${sourceFilePath}");
		requestJson.put(WorkflowKeysEnum.INPUT_PARAMETERS.getKey(), inputPrameters);
		requestJson.put(WorkflowKeysEnum.OUTPUT_PARAMETERS.getKey(), new JSONArray());
		requestJson.put(WorkflowKeysEnum.EVENT_TO_TRIGGER.getKey(),"BATCH_REFRESH_USERPROFILE_JSON");
		activity.setProcessingSpecification(requestJson.toJSONString());
		activity.setScheduleSetup("");
		activity.setTriggerType(TriggerTypeEnum.MANUAL);
		activityRepository.save(activity);
				
		activity = new Activity();
		activity.setActivityKey("afcab9ae-4815-11e6-beb8-9e71128cae77");
		activity.setActivityName("Batch Update User profileJSON");
		activity.setActivityOrderIndex(1);
		activity.setActivityType(ActivityTypeEnum.INTEGRATION);
		activity.setCausesNewProcessExecution(false);
		activity.setEventGroupName("BATCH_REFRESH_USERPROFILE_JSON");
		activity.setEventGroupOrderIndex(2);
		activity.setIntegrationProcessID(integrationProcessID);
		requestJson = new JSONObject();
		requestJson.put(WorkflowKeysEnum.ACTIVITY_EXECUTION_ID.getKey(), "${ActivityExecutionID}");
		requestJson.put(WorkflowKeysEnum.WORKFLOW_TYPE.getKey(), "Transformation");
		requestJson.put(WorkflowKeysEnum.WORKFLOW_FILE_PATH.getKey(),
				"iquantifi-files\\Sub_${SubscriberID}\\Proc_${IntegrationProcessID}\\PentahoFiles\\refresh-user-profile-JSON-Batch.ktr");
		inputPrameters = new JSONObject();
		inputPrameters.put("inputFileName",
				"iquantifi-files\\Sub_${SubscriberID}\\Proc_${IntegrationProcessID}\\Exec_${IntegrationProcessExecutionID}\\input\\input.${IntegrationProcessExecutionID}.csv");
		inputPrameters.put("IntegrationProcessExecutionID", "${IntegrationProcessExecutionID}");
		inputPrameters.put("IntegrationProcessID", "${IntegrationProcessID}");
		inputPrameters.put("SubscriberID", "${SubscriberID}");
		inputPrameters.put("ActivityExecutionID", "${ActivityExecutionID}");
		requestJson.put(WorkflowKeysEnum.INPUT_PARAMETERS.getKey(), inputPrameters);
		requestJson.put(WorkflowKeysEnum.OUTPUT_PARAMETERS.getKey(), new JSONArray());
		requestJson.put(WorkflowKeysEnum.STEP_TO_TRIGGER.getKey(), 5);
		activity.setProcessingSpecification(requestJson.toJSONString());
		activity.setScheduleSetup("");
		activity.setTriggerType(TriggerTypeEnum.MANUAL);
		activityRepository.save(activity);

		activity = new Activity();
		activity.setActivityKey("7779a6ba-4cce-11e6-beb8-9e71128cae77");
		activity.setActivityName("End execution");
		activity.setActivityOrderIndex(1);
		activity.setActivityType(ActivityTypeEnum.END);
		activity.setCausesNewProcessExecution(false);

		activity.setEventGroupName("END_EXECUTION");
		activity.setEventGroupOrderIndex(3);
		activity.setIntegrationProcessID(integrationProcessID);
		JSONObject requestJsonEnd = new JSONObject();
		activity.setProcessingSpecification(requestJsonEnd.toJSONString());
		activity.setScheduleSetup("");
		activity.setTriggerType(TriggerTypeEnum.MANUAL);
		activityRepository.save(activity);

	}

}
