package com.data.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.data.integration.data.Activity;
import com.data.integration.data.ActivityExecution;

@Repository
public interface ActivityExecutionRepository  extends CrudRepository<ActivityExecution,Long>{

	ActivityExecution findByActivityExecutionID(Long activityExecutionID);
	
	ActivityExecution findOneByActivity(Activity activity);
	
	@Query("SELECT activityExecution FROM ActivityExecution activityExecution WHERE activityExecution.integrationProcessExecution.integrationProcessExecutionID =  :integrationProcessExecutionID")
	List<ActivityExecution> findByIntegrationProcessExecutionID(@Param(value="integrationProcessExecutionID")Long integrationProcessExecutionID);
}
