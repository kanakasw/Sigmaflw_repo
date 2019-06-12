package com.data.integration.service;

import org.json.simple.JSONObject;

import com.data.integration.service.exceptions.IntegrationActivityExecutionException;


public interface IntegrationEngine {

	/**
	 * Executes the process workflow(PDI workflows).<br/>
	 * Input will be Json object which contains all sort of configuration and
	 * input parameters required for the execution. Following is the syntax of
	 * Json Object must required as input to this method:<br/>
	 * <br/>
	 * {<br/>
	 * "Identifier": "Identifier", <br/>
	 * "WorkflowType": "WorkflowType", <br/>
	 * "WorkflowFilePath": "WorkflowFilePath", <br/>
	 * "InputParams": {"StepID": 1, "InputFilePath": "InputFilePath",
	 * "OutputFilePath": "OutputFilePath"}, <br/>
	 * "OutputParams": ["BatchID", "runStatus", "project"] <br/>
	 * }<br/>
	 * <br/>
	 * Where,<br/>
	 * <b>Identifier</b> Caller will make use of it for identification purpose.<br/>
	 * <b>WorkflowType</b> indicate workflow type, like job or transformation.<br/>
	 * <b>WorkflowFilePath</b> indicate path of transformation file and must be
	 * needed for execution.<br/>
	 * <b>InputParams</b> is a JsonObject, holding zero or many key value pair
	 * which act as input parameters for the kettle file. <br/>
	 * <b>OutputParams</b> is JsonArray indicating a list of parameters whose
	 * value will be returned as response after completion of process.
	 * 
	 * @param message
	 * @return TODO
	 */
	 JSONObject executeWorkflow(JSONObject requestJson) throws IntegrationActivityExecutionException;

	/**
	 * Returns workflow related information like input and output parameters,
	 * etc. Following is the Input Json format :<br/>
	 * <br/>
	 * {<br/>
	 * "WorkflowType": "WorkflowType", <br/>
	 * "WorkflowFilePath": "WorkflowFilePath" <br/>
	 * }<br/>
	 * <br/>
	 * Where,<br/>
	 * <b>WorkflowType</b> indicate workflow type, like job or transformation.<br/>
	 * <b>WorkflowFilePath</b> indicate path of transformation file.<br/>
	 * 
	 * @param message
	 */
	 JSONObject getWorkflowInfo(String workflowType ,String workflowFilePath);

}