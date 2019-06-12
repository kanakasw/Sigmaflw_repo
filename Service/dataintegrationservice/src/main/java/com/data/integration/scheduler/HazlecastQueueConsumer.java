package com.data.integration.scheduler;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.data.integration.config.HazelcastConfiguration;
import com.data.integration.service.IntegrationProcessExecutor;
import com.data.integration.service.exceptions.ActivityConfigurationException;
import com.data.integration.service.exceptions.ActivityExecutionException;
import com.data.integration.service.exceptions.ProcessingException;
import com.data.integration.service.vo.ExecuteIntegrationProcessVO;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;

@Component
public class HazlecastQueueConsumer {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(HazlecastQueueConsumer.class);

	@Autowired
	private ApplicationContext context;

	private ExecutorService executorService;
	
	private ExecutorService newWorkStealingPool;

	@PostConstruct
	public void init() {

		BasicThreadFactory factory = new BasicThreadFactory.Builder()
				.namingPattern("HazlecastQueueConsumer-Thread-%d").build();
		newWorkStealingPool = newWorkStealingPool();
		executorService = Executors.newSingleThreadExecutor(factory);
		executorService.execute(new Runnable() {

			@Override
			public void run() {

					IntegrationProcessExecutor integrationProcessExecutor = context
							.getBean(IntegrationProcessExecutor.class);
					HazelcastInstance hazelcastInstance = context
							.getBean(HazelcastInstance.class);
					IQueue<Object> consumerQueue = hazelcastInstance
							.getQueue(HazelcastConfiguration.EXECUTEINTEGRATIONPROCESSQUEUE);
					while (true) {
						LOGGER.debug("HazelcastConfiguration.EXECUTEINTEGRATIONPROCESSQUEUE size ={}"
								, consumerQueue.size());
						String item = null;
						try {
							item = (String) consumerQueue.take();
						} catch (InterruptedException e1) {
							LOGGER.error("Error occured while taking item from Hazalcast Queue",e1);
						}
						
						if (item != null) {
							final String finalitem=item;
							newWorkStealingPool.execute(new Runnable() {
								
								@Override
								public void run() {
									try {
										ExecuteIntegrationProcessVO executeIntegrationProcessVO = new ExecuteIntegrationProcessVO();
										executeIntegrationProcessVO = executeIntegrationProcessVO
												.toObject((String) finalitem);
										integrationProcessExecutor
												.executeIntegrationProcess(executeIntegrationProcessVO);

									} catch (ProcessingException | IOException e) {
										LOGGER.error(
												"Error occured while accesing Hazalcast IQueue",
												e);
									} catch (ActivityConfigurationException e) {
										LOGGER.error(
												"Error occured while parsing Activity Specification",
												e);
									} catch (ActivityExecutionException e) {
										LOGGER.error(
												"Error occured while executing Activity",
												e);
									}
								}
							});	
						
						}
					
					}
					
			}
		});

		executorService.shutdown();

	}

	@PreDestroy
	public void beandestroy() {
		if (executorService != null) {
			executorService.shutdownNow();
		}
		if(newWorkStealingPool!=null){
			newWorkStealingPool.shutdownNow();
		}
	}
	
	 public static ExecutorService newWorkStealingPool() {
	        return new ForkJoinPool
	            (Runtime.getRuntime().availableProcessors(),
	             ForkJoinPool.defaultForkJoinWorkerThreadFactory,
	             new UncaughtExceptionHandler() {
					@Override
					public void uncaughtException(Thread t, Throwable e) {
						LOGGER.error("Error occured while executing Thread ", e);
					}
				}, true);
	    }

}
