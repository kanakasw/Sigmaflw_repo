package com.data.integration;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Starter class for Data integration Application.
 * 
 * @author Aniket
 *
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class DataIntegrationApplication {

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(DataIntegrationApplication.class, args);
    }

    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatFactory() {
        return new TomcatEmbeddedServletContainerFactory() {

            @Override
            protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(
                    Tomcat tomcat) {
                tomcat.enableNaming();
                TomcatEmbeddedServletContainer container = super
                        .getTomcatEmbeddedServletContainer(tomcat);
                for (Container child : container.getTomcat().getHost()
                        .findChildren()) {
                    if (child instanceof Context) {
                        ClassLoader contextClassLoader = ((Context) child)
                                .getLoader().getClassLoader();
                        Thread.currentThread().setContextClassLoader(
                                contextClassLoader);
                        break;
                    }
                }
                return container;
            }

            // Following code create JNDI Resource in embedded tomcat
            @Override
            protected void postProcessContext(Context context) {
                if (environment.getRequiredProperty(
                        "jndi.DataIntegrationRuntimeJNDI.configure",
                        Boolean.class)) {
                    ContextResource resource = new ContextResource();
                    createRuntimeDBJNDIResource(resource);
                    context.getNamingResources().addResource(resource);
                }
                if (environment.getRequiredProperty(
                        "jndi.sidelDataSource.configure", Boolean.class)) {
                    ContextResource mySQLResource = new ContextResource();
                    createRuntimeDBMySQLJNDIResource(mySQLResource);
                    context.getNamingResources().addResource(mySQLResource);
                }

                if (environment.getRequiredProperty(
                        "jndi.DataIntegrationServiceJNDI.configure",
                        Boolean.class)) {
                    ContextResource integrationResource = new ContextResource();
                    createServiceDBJNDIResource(integrationResource);
                    context.getNamingResources().addResource(
                            integrationResource);
                }
                if (environment.getRequiredProperty(
                        "jndi.SourceDatabase.configure",
                        Boolean.class)) {
                    ContextResource integrationResource = new ContextResource();
                    createServiceDBJNDISourceResource(integrationResource);
                    context.getNamingResources().addResource(
                            integrationResource);
                }
                if (environment.getRequiredProperty(
                        "jndi.DestinationDatabase.configure",
                        Boolean.class)) {
                    ContextResource integrationResource = new ContextResource();
                    createServiceDBJNDIDestinationResource(integrationResource);
                    context.getNamingResources().addResource(
                            integrationResource);
                }
            }

            private void createServiceDBJNDIResource(
                    final ContextResource integrationResource) {
                String integrationResourceName = "jdbc/"
                        + environment.getProperty("service.jndiname",
                                "DataIntegrationServiceJNDI");
                integrationResource.setName(integrationResourceName);
                integrationResource.setAuth("Container");
                integrationResource.setType(DataSource.class.getName());
                integrationResource
                        .setProperty(
                                "driverClassName",
                                environment
                                        .getProperty("spring.dataIntegrationService.datasource.driverClassName"));
                integrationResource
                        .setProperty(
                                "factory",
                                environment
                                        .getProperty("spring.dataIntegrationService.datasource.factory"));
                integrationResource
                        .setProperty(
                                "url",
                                environment
                                        .getProperty("spring.dataIntegrationService.datasource.url"));
                integrationResource
                        .setProperty(
                                "username",
                                environment
                                        .getProperty("spring.dataIntegrationService.datasource.username"));
                integrationResource
                        .setProperty(
                                "password",
                                environment
                                        .getProperty("spring.dataIntegrationService.datasource.password"));
                integrationResource
                        .setProperty(
                                "initialSize",
                                environment
                                        .getProperty("spring.dataIntegrationService.datasource.initial-size"));
                integrationResource
                        .setProperty(
                                "maxActive",
                                environment
                                        .getProperty("spring.dataIntegrationService.datasource.max-Active"));

                integrationResource.setProperty("testOnBorrow", "true");
                integrationResource.setProperty("testWhileIdle", "true");
                integrationResource.setProperty("jmxEnabled", "true");
                integrationResource.setProperty("maxAge", "1700000");

                integrationResource
                        .setProperty(
                                "maxIdle",
                                environment
                                        .getProperty("spring.dataIntegrationService.datasource.max-idle"));
                integrationResource
                        .setProperty(
                                "minIdle",
                                environment
                                        .getProperty("spring.dataIntegrationService.datasource.min-idle"));
                integrationResource
                        .setProperty(
                                "validationQuery",
                                environment
                                        .getProperty("spring.dataIntegrationService.datasource.validationQuery"));
            }

            private void createServiceDBJNDISourceResource(
                    final ContextResource integrationResource) {
                String integrationResourceName = "jdbc/"
                        + environment.getProperty("source.jndiname",
                                "SourceDatabase");
                integrationResource.setName(integrationResourceName);
                integrationResource.setAuth("Container");
                integrationResource.setType(DataSource.class.getName());
                integrationResource
                        .setProperty(
                                "driverClassName",
                                environment
                                        .getProperty("spring.dataIntegrationSource.datasource.driverClassName"));
                integrationResource
                        .setProperty(
                                "factory",
                                environment
                                        .getProperty("spring.dataIntegrationSource.datasource.factory"));
                integrationResource
                        .setProperty(
                                "url",
                                environment
                                        .getProperty("spring.dataIntegrationSource.datasource.url"));
                integrationResource
                        .setProperty(
                                "username",
                                environment
                                        .getProperty("spring.dataIntegrationSource.datasource.username"));
                integrationResource
                        .setProperty(
                                "password",
                                environment
                                        .getProperty("spring.dataIntegrationSource.datasource.password"));
                integrationResource
                        .setProperty(
                                "initialSize",
                                environment
                                        .getProperty("spring.dataIntegrationSource.datasource.initial-size"));
                integrationResource
                        .setProperty(
                                "maxActive",
                                environment
                                        .getProperty("spring.dataIntegrationSource.datasource.max-Active"));

                integrationResource.setProperty("testOnBorrow", "true");
                integrationResource.setProperty("testWhileIdle", "true");
                integrationResource.setProperty("jmxEnabled", "true");
                integrationResource.setProperty("maxAge", "1700000");

                integrationResource
                        .setProperty(
                                "maxIdle",
                                environment
                                        .getProperty("spring.dataIntegrationSource.datasource.max-idle"));
                integrationResource
                        .setProperty(
                                "minIdle",
                                environment
                                        .getProperty("spring.dataIntegrationSource.datasource.min-idle"));
                integrationResource
                        .setProperty(
                                "validationQuery",
                                environment
                                        .getProperty("spring.dataIntegrationSource.datasource.validationQuery"));
            }

            private void createServiceDBJNDIDestinationResource(
                    final ContextResource integrationResource) {
                String integrationResourceName = "jdbc/"
                        + environment.getProperty("destination.jndiname",
                                "DestinationDatabase");
                integrationResource.setName(integrationResourceName);
                integrationResource.setAuth("Container");
                integrationResource.setType(DataSource.class.getName());
                integrationResource
                        .setProperty(
                                "driverClassName",
                                environment
                                        .getProperty("spring.dataIntegrationDestination.datasource.driverClassName"));
                integrationResource
                        .setProperty(
                                "factory",
                                environment
                                        .getProperty("spring.dataIntegrationDestination.datasource.factory"));
                integrationResource
                        .setProperty(
                                "url",
                                environment
                                        .getProperty("spring.dataIntegrationDestination.datasource.url"));
                integrationResource
                        .setProperty(
                                "username",
                                environment
                                        .getProperty("spring.dataIntegrationDestination.datasource.username"));
                integrationResource
                        .setProperty(
                                "password",
                                environment
                                        .getProperty("spring.dataIntegrationDestination.datasource.password"));
                integrationResource
                        .setProperty(
                                "initialSize",
                                environment
                                        .getProperty("spring.dataIntegrationDestination.datasource.initial-size"));
                integrationResource
                        .setProperty(
                                "maxActive",
                                environment
                                        .getProperty("spring.dataIntegrationDestination.datasource.max-Active"));

                integrationResource.setProperty("testOnBorrow", "true");
                integrationResource.setProperty("testWhileIdle", "true");
                integrationResource.setProperty("jmxEnabled", "true");
                integrationResource.setProperty("maxAge", "1700000");

                integrationResource
                        .setProperty(
                                "maxIdle",
                                environment
                                        .getProperty("spring.dataIntegrationDestination.datasource.max-idle"));
                integrationResource
                        .setProperty(
                                "minIdle",
                                environment
                                        .getProperty("spring.dataIntegrationDestination.datasource.min-idle"));
                integrationResource
                        .setProperty(
                                "validationQuery",
                                environment
                                        .getProperty("spring.dataIntegrationDestination.datasource.validationQuery"));
            }

            private void createRuntimeDBJNDIResource(ContextResource resource) {
                String resourceName = "jdbc/"
                        + environment.getProperty("runtime.jndiname",
                                "DataIntegrationRuntimeJNDI");
                resource.setName(resourceName);
                resource.setAuth("Container");
                resource.setType(DataSource.class.getName());
                resource.setProperty(
                        "driverClassName",
                        environment
                                .getProperty("spring.dataIntegrationRuntime.datasource.driverClassName"));
                resource.setProperty(
                        "factory",
                        environment
                                .getProperty("spring.dataIntegrationRuntime.datasource.factory"));
                resource.setProperty(
                        "url",
                        environment
                                .getProperty("spring.dataIntegrationRuntime.datasource.url"));
                resource.setProperty(
                        "username",
                        environment
                                .getProperty("spring.dataIntegrationRuntime.datasource.username"));
                resource.setProperty(
                        "password",
                        environment
                                .getProperty("spring.dataIntegrationRuntime.datasource.password"));
                resource.setProperty(
                        "initialSize",
                        environment
                                .getProperty("spring.dataIntegrationRuntime.datasource.initial-size"));
                resource.setProperty(
                        "maxActive",
                        environment
                                .getProperty("spring.dataIntegrationRuntime.datasource.max-Active"));

                resource.setProperty("testOnBorrow", "true");
                resource.setProperty("testWhileIdle", "true");
                resource.setProperty("jmxEnabled", "true");
                resource.setProperty("maxAge", "1700000");

                resource.setProperty(
                        "maxIdle",
                        environment
                                .getProperty("spring.dataIntegrationRuntime.datasource.max-idle"));
                resource.setProperty(
                        "minIdle",
                        environment
                                .getProperty("spring.dataIntegrationRuntime.datasource.min-idle"));
                resource.setProperty(
                        "validationQuery",
                        environment
                                .getProperty("spring.dataIntegrationRuntime.datasource.validationQuery"));
            }

            private void createRuntimeDBMySQLJNDIResource(
                    ContextResource resource) {
                String resourceName = "jdbc/"
                        + environment.getProperty("runtime_2.jndiname",
                                "sidelDataSource");
                resource.setName(resourceName);
                resource.setAuth("Container");
                resource.setType(DataSource.class.getName());
                resource.setProperty(
                        "driverClassName",
                        environment
                                .getProperty("spring.dataIntegrationRuntime_2.datasource.driverClassName"));
                resource.setProperty(
                        "factory",
                        environment
                                .getProperty("spring.dataIntegrationRuntime_2.datasource.factory"));
                resource.setProperty(
                        "url",
                        environment
                                .getProperty("spring.dataIntegrationRuntime_2.datasource.url"));
                resource.setProperty(
                        "username",
                        environment
                                .getProperty("spring.dataIntegrationRuntime_2.datasource.username"));
                resource.setProperty(
                        "password",
                        environment
                                .getProperty("spring.dataIntegrationRuntime_2.datasource.password"));
                resource.setProperty(
                        "initialSize",
                        environment
                                .getProperty("spring.dataIntegrationRuntime_2.datasource.initial-size"));
                resource.setProperty(
                        "maxActive",
                        environment
                                .getProperty("spring.dataIntegrationRuntime_2.datasource.max-Active"));

                resource.setProperty("testOnBorrow", "true");
                resource.setProperty("testWhileIdle", "true");
                resource.setProperty("jmxEnabled", "true");
                resource.setProperty("maxAge", "1700000");

                resource.setProperty(
                        "maxIdle",
                        environment
                                .getProperty("spring.dataIntegrationRuntime_2.datasource.max-idle"));
                resource.setProperty(
                        "minIdle",
                        environment
                                .getProperty("spring.dataIntegrationRuntime_2.datasource.min-idle"));
                resource.setProperty(
                        "validationQuery",
                        environment
                                .getProperty("spring.dataIntegrationRuntime_2.datasource.validationQuery"));
            }
        };
    }

    @Bean
    EmbeddedServletContainerCustomizer containerCustomizer() throws Exception {
        return (ConfigurableEmbeddedServletContainer container) -> {
            if (container instanceof TomcatEmbeddedServletContainerFactory) {
                TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
                tomcat.addConnectorCustomizers((connector) -> {
                    connector.setMaxPostSize(Integer.parseInt(environment
                            .getProperty("embeded.tomcat.maxpostsize")));

                });
            }
        };
    }

    // Spring Datasource bean
    @Bean(destroyMethod = "")
    public DataSource jndiDataSource() throws IllegalArgumentException,
            NamingException {
        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        String jndiName = "java:comp/env/jdbc/"
                + environment.getProperty("service.jndiname",
                        "DataIntegrationServiceJNDI");
        bean.setJndiName(jndiName);
        bean.setProxyInterface(DataSource.class);
        bean.setLookupOnStartup(false);
        bean.afterPropertiesSet();
        bean.setResourceRef(true);
        return (DataSource) bean.getObject();
    }

}
