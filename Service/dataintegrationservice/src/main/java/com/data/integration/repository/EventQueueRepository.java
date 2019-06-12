package com.data.integration.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.data.integration.data.EventQueue;
import com.data.integration.service.enums.EventQueueStatus;

@Repository
public interface EventQueueRepository extends CrudRepository<EventQueue, Long> {

	public List<EventQueue> findByStatus(EventQueueStatus status);

	public EventQueue findOneByIntegrationProcessExecutionID(String jobId);

	public List<EventQueue> findDistinctIntegrationProcessExecutionIDByStatusNot(EventQueueStatus completed);

	public List<EventQueue> findByStatusNot(EventQueueStatus completed);

	public EventQueue findTopByStatusNotAndIntegrationProcessExecutionIDOrderByCreatedDateAsc(EventQueueStatus completed,
			Long integrationProcessExecutionID);
}
