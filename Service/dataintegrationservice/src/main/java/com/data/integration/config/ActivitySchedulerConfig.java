package com.data.integration.config;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import com.data.integration.data.Activity;
import com.data.integration.repository.ActivityRepository;
import com.data.integration.scheduler.ActivitySchedulerBean;
import com.data.integration.service.vo.ExecuteIntegrationProcessVO;
import com.data.integration.service.vo.ScheduledSetupVO;

/**
 * This is configuration class for Spring boot <br>
 * if we set spring.scheduler.enabled=true in application.properties, then this
 * configuration is used for Activity scheduling.
 * 
 * @author Aniket
 *
 */
@Configuration
@ConditionalOnProperty(name = "spring.scheduler.enabled")
public class ActivitySchedulerConfig implements SchedulingConfigurer {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ActivitySchedulerConfig.class);
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ActivityRepository activityRepository;

    @Value("${scheduled.process.execution}")
    private boolean scheduleActivity;

    @Override
    public void configureTasks(final ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler());

        if (scheduleActivity) {
            final Iterable<Activity> activitys = activityRepository
                    .findScheduledActivitiesForActiveProcess();

            ObjectMapper objectMapper = new ObjectMapper();
            LOGGER.info("Scheduling activities ");
            for (Activity activity : activitys) {
                try {

                    ScheduledSetupVO scheduledSetupVO = objectMapper
                            .readValue(activity.getScheduleSetup(),
                                    ScheduledSetupVO.class);
                    ExecuteIntegrationProcessVO executeIntegrationProcessVO = new ExecuteIntegrationProcessVO();
                    executeIntegrationProcessVO.setActivityID(activity
                            .getActivityID());
                    executeIntegrationProcessVO
                            .setCausesNewIntegrationProcessExecution(activity
                                    .isCausesNewProcessExecution());

                    ActivitySchedulerBean activitySchedulerBean = applicationContext
                            .getBean(ActivitySchedulerBean.class);
                    activitySchedulerBean
                            .setExecuteIntegrationProcessVO(executeIntegrationProcessVO);
                    taskRegistrar.addTriggerTask(
                            activitySchedulerBean,
                            new CronTrigger(scheduledSetupVO
                                    .getCronExpression()));
                    LOGGER.info(
                            "Scheduled ActivityID ={} ,Cron Expression = {}",
                            activity.getActivityID(),
                            scheduledSetupVO.getCronExpression());

                } catch (IOException e) {
                    LOGGER.error("Error occured while parsing ScheduledSetup",
                            e);
                } catch (Exception e) {
                    LOGGER.error("Error occured while Scheduling activities", e);
                }

            }
        }

    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskScheduler() {
        return Executors.newScheduledThreadPool(5);
    }

}
