package com.data.integration.service.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

public class ReportGenerator extends AbstractReportGenerator{

	private Map<String, Object> reportParameters=new HashMap<String, Object>();
	private String reportDefinitionFile;
	@Override
	public MasterReport getReportDefinition() {
		try {
			URL reportDefinitionURL = null;
			try {
				reportDefinitionURL = new URL(new StringBuilder().append("file:///").append(reportDefinitionFile).toString());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			final ResourceManager resourceManager = new ResourceManager();
			resourceManager.registerDefaults();
			final Resource directly = resourceManager.createDirectly(
					reportDefinitionURL, MasterReport.class);
			return (MasterReport) directly.getResource();
		}

		catch (ResourceException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public DataFactory getDataFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getReportParameters() {
		return reportParameters;
	}

	public void setReportDefinitionFile(String reportDefinitionFile) {
		this.reportDefinitionFile = reportDefinitionFile;
	}

	public void setReportParameters(Map<String, Object> reportParameters) {
		//this.reportParameters=reportParameters;
		for (String key : reportParameters.keySet()) {
			String value = (String) reportParameters.get(key);
			if (NumberUtils.isParsable(value)) {
				this.reportParameters.put(key, Integer.parseInt(value));
			} else {
				this.reportParameters.put(key,value);
			}
		}
	}

}
