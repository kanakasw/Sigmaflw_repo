package com.data.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.data.integration.data.Activity;
import com.data.integration.service.enums.TriggerTypeEnum;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {

	Activity findByActivityID(Long activityID);

	Activity findOneByIntegrationProcessIDAndEventGroupNameOrderByActivityOrderIndex(Long integrationProcessID,
			String eventGroupName);
	
	Iterable<Activity> findByTriggerType(TriggerTypeEnum triggerType);
	
	List<Activity> findByIntegrationProcessID(Long integrationProcessID);
	
	Iterable<Activity> findByTriggerTypeAndIntegrationProcessID(TriggerTypeEnum triggerType,Long integrationProcessID);
	
	Iterable<Activity> findByTriggerTypeAndIntegrationProcessIDIn(TriggerTypeEnum triggerType,List<Long> integrationProcessIDList);
	
	Activity findOneByIntegrationProcessIDAndActivityOrderIndex(Long integrationProcessID,
            Long activityOrderIndex);
	
	@Query("SELECT act from IntegrationProcess ip JOIN ip.activities act where act.integrationProcessID=ip.integrationProcessID AND ip.enabled=true AND act.triggerType='SCHEDULED'")
	Iterable<Activity> findScheduledActivitiesForActiveProcess();
}
