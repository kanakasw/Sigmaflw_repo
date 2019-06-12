package com.data.integration.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.data.integration.config.HazelcastConfiguration;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.ItemEvent;
import com.hazelcast.core.ItemListener;

@Service
public class QueueItemListner implements ItemListener<Object> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(QueueItemListner.class);

	@Autowired
	private ApplicationContext context;

	@Override
	public void itemAdded(ItemEvent<Object> item) {

		LOGGER.info("Added item in queue {}" ,item.getItem());
	}

	@Override
	public void itemRemoved(ItemEvent<Object> item) {
		HazelcastInstance hazelcastInstance = context
				.getBean(HazelcastInstance.class);
		IQueue<Object> consumerQueue = hazelcastInstance
				.getQueue(HazelcastConfiguration.EXECUTEINTEGRATIONPROCESSQUEUE);
		LOGGER.debug("HazelcastConfiguration.EXECUTEINTEGRATIONPROCESSQUEUE size = {}"
				, consumerQueue.size());
		LOGGER.info("Removed item from queue {}",item.getItem());
	}

}
