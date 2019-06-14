package com.data.integration.config;

import java.io.File;

import javax.annotation.PostConstruct;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.util.DatabaseUtil;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.plugins.PluginFolder;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.data.integration.service.exceptions.KettleInitializationException;

/**
 * 
 * This Class will initilize pentaho Reporting engine,Kettle and it does lookup
 * for JNDI resources in embeded Tomcat. to enable or disable this two component
 * we need to configure following properties. <br>
 * <b>pentaho.reportingEngine.enabled</b> and <b>pentaho.kettle.enabled</b>
 * 
 * @author Aniket
 *
 */
@Component
public class DataIntegrationServiceInitilizerHelper {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DataIntegrationServiceInitilizerHelper.class);

    @Value("${runtime.jndiname}")
    private String dataIntegrationRuntimeJNDI;

    @Value("${runtime_2.jndiname}")
    private String dataIntegrationRuntime_2JNDI;
    
    @Value("${service.jndiname}")
    private String dataIntegrationServiceJNDI;

    @Value("${source.jndiname}")
    private String dataIntegrationSource;

    @Value("${destination.jndiname}")
    private String dataIntegrationDestination;

    @Value("${pentaho.reportingEngine.enabled}")
    private Boolean initilizeReportingEngine;

    @Value("${pentaho.kettle.enabled}")
    private Boolean initilizeKettle;

    @Value("${jndi.DataIntegrationRuntimeJNDI.configure}")
    private Boolean jndiDataIntegrationRuntimeJNDIconfigure;

    @Value("${jndi.sidelDataSource.configure}")
    private Boolean jndisidelDataSourceconfigure;

    @Value("${jndi.DataIntegrationServiceJNDI.configure}")
    private Boolean jndiDataIntegrationServiceJNDIconfigure;

    @Value("${jndi.SourceDatabase.configure}")
    private Boolean jndiSourceDatabaseconfigure;

    @Value("${jndi.DestinationDatabase.configure}")
    private Boolean jndiDestinationDatabaseconfigure;
    
    @Value("${pentaho.plugin}")
    private String pentahoPluginPath;
    
 
    @PostConstruct
    private void initializeKettleAndPentahoReportingEngine()
            throws KettleInitializationException {

        if (initilizeKettle) {
            LOGGER.info("Initilizing Kettle Enviorment");
            try {
				File directory = new File(pentahoPluginPath);
				StepPluginType.getInstance().getPluginFolders().add(new PluginFolder(directory.getAbsolutePath(), false, true));
                if (!KettleEnvironment.isInitialized()) {
                    // Initialize the kettle environment
                    KettleEnvironment.init(false);
                    // Lets lookup JNDI datasource at kettle initilization
                    // DatabaseUtil internally cache the Datasource
                    DatabaseUtil databaseUtil = new DatabaseUtil();
                    if(jndiDataIntegrationRuntimeJNDIconfigure){
                    databaseUtil.getNamedDataSource(dataIntegrationRuntimeJNDI);
                    LOGGER.info("{} Lookup successfull",
                            dataIntegrationRuntimeJNDI);
                    }
                    if(jndisidelDataSourceconfigure){
                    databaseUtil.getNamedDataSource(dataIntegrationRuntime_2JNDI);
                    LOGGER.info("{} Lookup successfull",
                            dataIntegrationRuntime_2JNDI);
                    }
                    if(jndiDataIntegrationServiceJNDIconfigure){
                    databaseUtil.getNamedDataSource(dataIntegrationServiceJNDI);
                    LOGGER.info("{} Lookup successfull",
                            dataIntegrationServiceJNDI);
                    }

					if (jndiSourceDatabaseconfigure) {
						databaseUtil.getNamedDataSource(dataIntegrationSource);
						LOGGER.info("{} Lookup successfull",
								dataIntegrationSource);
					}
					if (jndiDestinationDatabaseconfigure) {
						databaseUtil
								.getNamedDataSource(dataIntegrationDestination);
						LOGGER.info("{} Lookup successfull",
								dataIntegrationDestination);
					}
                }
            } catch (KettleException e) {
                throw new KettleInitializationException(e);
            }
        }
        if (initilizeReportingEngine) {
            LOGGER.info("Initilizing Pentaho Reporting ClassicEngine");
            // Initialize the pentaho reporting Engine
            ClassicEngineBoot.getInstance().start();
        }
    }

}
