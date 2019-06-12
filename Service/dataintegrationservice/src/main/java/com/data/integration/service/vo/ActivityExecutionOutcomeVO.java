package com.data.integration.service.vo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;

import com.data.integration.service.util.DateUtil;

/**
 * Base class for execution outcome
 * 
 * @author Aniket
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityExecutionOutcomeVO {

	private Map<String,String> logEntries=new HashMap<String, String>();
	
	private ExecuteIntegrationProcessVO executeIntegrationProcessVO;
	
	public Map<String, String> getLogEntries() {
		return logEntries;
	}

	public void setLogEntries(Map<String, String> logEntries) {
		this.logEntries = logEntries;
	}
	
	public void addLogEntries(String logEntry) {
		logEntries.put(DateUtil.now().toString(), logEntry);
	}
    
	public String toJsonString() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (IOException e) {
			throw e;
		}
	}

	public ExecuteIntegrationProcessVO getExecuteIntegrationProcessVO() {
        return executeIntegrationProcessVO;
    }

    public void setExecuteIntegrationProcessVO(
            ExecuteIntegrationProcessVO executeIntegrationProcessVO) {
        this.executeIntegrationProcessVO = executeIntegrationProcessVO;
    }

    @Override
    public String toString() {
        return "ActivityExecutionOutcomeVO [logEntries=" + logEntries
                + ", executeIntegrationProcessVO="
                + executeIntegrationProcessVO + "]";
    }

  
}
