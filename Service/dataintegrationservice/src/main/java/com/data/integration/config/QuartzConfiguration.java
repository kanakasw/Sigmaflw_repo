package com.data.integration.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@ConditionalOnProperty(name="quartz.enabled")
public class QuartzConfiguration {

    public static final String CONTEXT_KEY = "applicationContext";

    @Autowired
    private DataSource dataSource;
    
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setApplicationContextSchedulerContextKey("applicationContext");
        scheduler.setConfigLocation(new ClassPathResource("quartz.properties"));
        scheduler.setDataSource(dataSource);
        // scheduler.setAutoStartup(false);  // to not automatically start after startup
        scheduler.setWaitForJobsToCompleteOnShutdown(true);
        return scheduler;
    }

}
