package com.data.integration.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.data.integration.data.Subscriber;

@Repository
public interface SubscriberRepository  extends CrudRepository<Subscriber,Long>{

	Subscriber findByClientID(String clientID);
	
	Subscriber findByLogin(String clientID);

	Subscriber findBySubscriberUniqueKey(String subscriberId);
	
	Subscriber findBySubscriberID(Long subscriberId);
	
	@Query("Select subscriber from Subscriber subscriber join subscriber.integrationProcesses as subips join subips.activities as subipsactivities where subscriber.subscriberID=subips.subscriberID and subips.integrationProcessID=subipsactivities.integrationProcessID and subipsactivities.activityID=:activityID ")
	Subscriber findByActivityID(@Param("activityID") Long activityID);
}
 