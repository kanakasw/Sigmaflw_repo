package com.data.integration.service.executor;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.pentaho.di.core.exception.KettleException;

import com.data.integration.service.exceptions.ProcessingException;

/**
 * Defines the operations performed by kettle job or transformation
 * 
 * @author Aniket
 *
 */
public interface ETLexecutor {

	/**
	 * Executes workflow file.
	 * 
	 * @param workflowFilePath
	 *            path of kettle transformation file.
	 * @param inputParams
	 *            parameter required for processing transformation
	 * @param outputParams
	 *            parameter value required after processing transformation
	 * @return JsonObject with the values for outputParams
	 * @throws ProcessingException
	 * @throws KettleException
	 */
	JSONObject execute(String workflowFilePath, JSONObject inputParams,
			JSONArray outputParams) throws ProcessingException, KettleException;

	/**
	 * Returns workflow related information.<br/>
	 * Information is like what input parameters it accept, what are the output
	 * or result parameters, workflow type, workflow file name, etc.
	 * 
	 * @param workflowFilePath
	 *            path of kettle transformation file.
	 * @return JsonObject with all workflow related information
	 * @throws KettleException
	 */
    JSONObject getWorkflowInfo(String workflowFilePath)
			throws KettleException;

	/**
	 * Get the json array of all output parameters a job/transformation can
	 * return.
	 * 
	 * @param workflowFilePath
	 * @return JsonArray
	 * @throws KettleException
	 */
     JSONArray getOutputParams(String workflowFilePath)
			throws KettleException;
}
