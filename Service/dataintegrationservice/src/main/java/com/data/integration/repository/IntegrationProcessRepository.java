package com.data.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.data.integration.data.IntegrationProcess;

@Repository
public interface IntegrationProcessRepository extends CrudRepository<IntegrationProcess, Long> {

	IntegrationProcess findByIntegrationProcessID(Long integrationProcessID);

	IntegrationProcess findBySubscriberIDAndIntegrationProcessUniqueReference(Long subscriberID,
			String integrationProcessUniqueReference);
	
	IntegrationProcess findByIntegrationProcessUniqueReference(String integrationProcessUniqueReference);
	
	@Query("select ip.integrationProcessID from Subscriber su  inner join su.integrationProcesses ip WHERE su.subscriberID=ip.subscriberID AND su.subscriberUniqueKey = :subscriberUniqueKey")
	List<Long> findBySubscriberUnique(@Param(value="subscriberUniqueKey") String subscriberUniqueKey);
	
	@Query("SELECT process FROM Subscriber sub INNER JOIN sub.integrationProcesses process WHERE sub.subscriberID = process.subscriberID AND sub.login = :userName")
	List<IntegrationProcess> findBySubscriberUserName(@Param(value="userName") String userName);
}
