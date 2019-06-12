package com.data.integration;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.data.integration.DataIntegrationApplication;
import com.data.integration.controller.AppController;
import com.data.integration.scheduler.EventProcessorJob;
import com.data.integration.service.exceptions.ActivityConfigurationException;
import com.data.integration.service.exceptions.ActivityExecutionException;
import com.data.integration.service.exceptions.IntegrationProcessNotFoundException;
import com.data.integration.service.exceptions.ProcessingException;
import com.data.integration.service.exceptions.SubscriberNotFoundException;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DataIntegrationApplication.class)
@WebAppConfiguration
public class AppControllerTest extends BaseTest {

	public static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

	@Autowired
	private AppController appController;
	
	@Autowired
	EventProcessorJob eventProcessorJob;

	private String subscriberId1;	
	private String processId1;
	private String processId2;

	@Before
	public void setup() {
		subscriberId1 = "172d4260-4802-11e6-beb8-9e71128cae77";
		processId1 = "7bea686e-4cde-11e6-beb8-9e71128cae777";	
		processId2 = "928d6166-4815-11e6-beb8-9e71128cae77";
	}

	
	@Test
	@Ignore
	public void testUpdateSingleConsumer() throws ProcessingException, ActivityConfigurationException, IntegrationProcessNotFoundException, ActivityExecutionException, SubscriberNotFoundException {
		LOGGER.info("testUpdateSingleConsumer test started");
		String json="{\"People\":{\"ColumnNames\":[\"FirstName\",\"LastName\",\"FullName\",\"State\",\"Dob\",\"Relationship\",\"CustomerUniqueReference\"],\"Values\":[[\"Elmer\",\"Fudd\",\"Elmer Fudd\",\"NY\",\"1960-03-09\",\"primary\",\"582C8CD0-2127-48E5-A5AF-47BA2B8F3A9D\"],[\"Wilma\",\"Fudd\",\"Wilma Fudd\",\"NY\",\"1961-04-12\",\"spouse\",\"634-21-5299\"]]},\"Accounts\":{\"ColumnNames\":[\"AccountId\",\"Name\",\"Is_active\",\"Type\",\"Balance\",\"CustomerUniqueReference\"],\"Values\":[[\"005703713536\",\"Checking 1\",true,\"Checking\",1234,\"582C8CD0-2127-48E5-A5AF-47BA2B8F3A9D\"],[\"005703713537\",\"Saving 1\",true,\"Saving\",1234,\"582C8CD0-2127-48E5-A5AF-47BA2B8F3A9D\"]]},\"Loans\":{\"ColumnNames\":[\"AccountId\",\"Name\",\"Is_active\",\"Type\",\"OutstandingDueAmount\",\"CustomerUniqueReference\",\"Term\",\"InterestRate\",\"MonthlyPayment\",\"OriginationDate\"],\"Values\":[[\"005703713845\",\"Mortgage 1\",true,\"Mortgage\",350000,\"582C8CD0-2127-48E5-A5AF-47BA2B8F3A9D\",30,\"3.45\",\"2000\",\"2005-07-21\"],[\"005703713956\",\"AutoLoan 1\",true,\"Autoloan\",25000,\"582C8CD0-2127-48E5-A5AF-47BA2B8F3A9D\",5,\"6.5\",\"1200\",\"2008-04-15\"]]},\"CreditCards\":{\"ColumnNames\":[\"CardNumber\",\"Name\",\"Expiry\",\"Issuer\",\"limit\",\"OutstandingDueAmount\",\"CustomerUniqueReference\"],\"Values\":[[\"5312234500022345\",\"Credit card 1\",\"042020\",\"Visa\",25000.00,8000.00,\"582C8CD0-2127-48E5-A5AF-47BA2B8F3A9D\"],[\"5612234530022378\",\"Credit card 1\",\"042020\",\"Master Card\",12000.00,1000.00,\"582C8CD0-2127-48E5-A5AF-47BA2B8F3A9D\"]]}}";
		try {
			appController.updateSingleConsumer("9c7065e4-4cc9-11e6-beb8-9e71128cae77", "7bea686e-4cde-11e6-beb8-9e71128cae777",(JSONObject) new JSONParser().parse(json) );
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Test
	public void uploadFileAndConfigureBatchExecution() throws ActivityConfigurationException, IntegrationProcessNotFoundException, ProcessingException, ActivityExecutionException, SubscriberNotFoundException {
		LOGGER.info("uploadFileAndConfigureBatchExecution test started");
		/*CommonsMultipartFile [] files = new CommonsMultipartFile[1];
		appController.uploadFileAndConfigureBatchExecution("9c7065e4-4cc9-11e6-beb8-9e71128cae77", "928d6166-4815-11e6-beb8-9e71128cae77", null);
		eventProcessorJob.processEvent();
		appController.uploadFileAndConfigureBatchExecution("9c7065e4-4cc9-11e6-beb8-9e71128cae77", "928d6166-4815-11e6-beb8-9e71128cae77", null);
		eventProcessorJob.processEvent();*/
	}

	@After
	public void wrapUp() {
		LOGGER.info("AppControllerTest Initialized");
	}

}
