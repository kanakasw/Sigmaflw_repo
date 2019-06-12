package com.data.integration.config;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import javax.annotation.PostConstruct;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.impl.JobDetailImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.data.integration.data.Activity;
import com.data.integration.repository.ActivityRepository;
import com.data.integration.scheduler.ActivityExecutorJob;
import com.data.integration.scheduler.ActivitySchedulerService;
import com.data.integration.service.vo.ScheduledSetupVO;

@Configuration
@ConditionalOnProperty(name = "quartz.enabled")
public class QuartzJobConfiguration {

	public enum JobKeysEnum {
		JOBDETAIL;
	}

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	@Autowired
	private ActivityRepository activityRepository;
	
	@Autowired
	private ActivitySchedulerService activitySchedulerService;

	@PostConstruct
	private void initialize() throws Exception {
		final Iterable<Activity> activities = activityRepository.findScheduledActivitiesForActiveProcess();
		activitySchedulerService.scheduleActivites(activities);
	}

	public static JobDetail jobDetail(String jobdetail, Activity activity) {
		JobDetailImpl jobDetail = new JobDetailImpl();
		jobDetail.setKey(new JobKey(activity.getIntegrationProcessID()
				.toString(), activity.getActivityID().toString()));
		jobDetail.setJobClass(ActivityExecutorJob.class);
		jobDetail.setDurability(true);
		JobDataMap map = new JobDataMap();
		map.put(JobKeysEnum.JOBDETAIL.name(), jobdetail);
		jobDetail.setJobDataMap(map);
		return jobDetail;
	}

	public static Trigger cronTrigger(Activity activity,
			ScheduledSetupVO scheduledSetupVO, JobDetail jobDetail) {
		return newTrigger()
				.forJob(jobDetail)
				.withIdentity(activity.getIntegrationProcessID().toString(),
						activity.getActivityID().toString())
				.withPriority(100)
				.withSchedule(
						cronSchedule(scheduledSetupVO.getCronExpression()).withMisfireHandlingInstructionDoNothing())
				.build();
	}

}