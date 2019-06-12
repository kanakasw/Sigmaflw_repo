package com.data.integration;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.data.integration.DataIntegrationApplication;
import com.data.integration.scheduler.EventProcessorJob;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DataIntegrationApplication.class)
@WebAppConfiguration
public class EventProcessorJobTest {

	@Autowired
	EventProcessorJob EventProcessorJob;
	
	@Before
	public void setup(){
		
	}
	
	@Test
	public void testProcessEvent(){
		EventProcessorJob.processEvent();
	}
}
