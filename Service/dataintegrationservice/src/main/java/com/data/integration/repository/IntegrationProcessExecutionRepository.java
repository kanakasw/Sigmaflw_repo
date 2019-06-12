package com.data.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.data.integration.data.IntegrationProcessExecution;
import com.data.integration.service.enums.IntegrationProcessExecutionStatusEnum;

@Repository
public interface IntegrationProcessExecutionRepository extends
		CrudRepository<IntegrationProcessExecution, Long> {

	IntegrationProcessExecution findTopByIntegrationProcessIDOrderByIntegrationProcessExecutionIDDesc(
			Long integrationProcessID);

	IntegrationProcessExecution findTopByIntegrationProcessIDAndStatusOrderByIntegrationProcessExecutionIDDesc(
			Long integrationProcessID,
			IntegrationProcessExecutionStatusEnum status);

	IntegrationProcessExecution findByIntegrationProcessExecutionID(
			Long integrationProcessExecutionID);

	@Query("SELECT ipe FROM IntegrationProcessExecution ipe where ipe.integrationProcessID=:integrationProcessID AND ipe.integrationProcessExecutionID!=(SELECT COALESCE(MAX(inpe.integrationProcessExecutionID),0) from IntegrationProcessExecution inpe where inpe.status='PROCESSING')order by ipe.integrationProcessExecutionID desc")
	List<IntegrationProcessExecution> findByIntegrationProcessIDOrderByIntegrationProcessExecutionIDDesc(
			@Param("integrationProcessID")Long integrationProcessID);

	// TODO:this query is postgresql Dependent need to write Database
	// independent query
	@Query(value = "Select CAST(EXTRACT(epoch from avg(ipe.ExecutionFinishTime-ipe.ExecutionStartTime)) as Bigint)*1000 as millseconds from IntegrationProcessExecution ipe where ipe.integrationProcessID=:integrationProcessID and ipe.status='COMPLETED'", nativeQuery = true)
	Long getAvarageExecutionTime(
			@Param("integrationProcessID") Long integrationProcessID);

}
