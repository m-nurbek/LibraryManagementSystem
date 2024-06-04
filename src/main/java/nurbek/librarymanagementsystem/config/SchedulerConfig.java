package nurbek.librarymanagementsystem.config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nurbek.librarymanagementsystem.job.DeleteBookJob;
import nurbek.librarymanagementsystem.property.SchedulerProperty;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {
    @NonNull
    private SchedulerProperty schedulerProperties;

    @Bean
    public JobDetail deleteBookJobDetail() {
        return JobBuilder
                .newJob(DeleteBookJob.class)
                .withIdentity("deleteBookJob")
                .storeDurably()
                .requestRecovery(true)
                .build();
    }

    @Bean
    public Trigger deleteBookJobTrigger() {
        return TriggerBuilder
                .newTrigger()
                .forJob(deleteBookJobDetail())
                .withIdentity("deleteBookJobTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(schedulerProperties.getShowTimeJobCron()))
                .build();
    }

    @Bean
    public Scheduler scheduler(List<Trigger> triggers, List<JobDetail> jobDetails, SchedulerFactoryBean schedulerFactory) throws SchedulerException {
        schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
        Scheduler scheduler = schedulerFactory.getScheduler();
        rescheduleTriggers(triggers, scheduler);
        revalidateJobs(jobDetails, scheduler);
        scheduler.start();
        return scheduler;
    }

    private void rescheduleTriggers(List<Trigger> triggers, Scheduler scheduler) {
        triggers.forEach(trigger -> {
            try {
                if (scheduler.checkExists(trigger.getKey())) {
                    scheduler.rescheduleJob(trigger.getKey(), trigger);
                } else {
                    scheduler.scheduleJob(trigger);
                }
            } catch (SchedulerException e) {
                log.error("Error rescheduling trigger", e);
            }
        });
    }

    private void revalidateJobs(List<JobDetail> jobDetails, Scheduler scheduler) {
        List<JobKey> jobKeys = jobDetails.stream().map(JobDetail::getKey).toList();

        try {
            scheduler.getJobKeys(GroupMatcher.jobGroupEquals(schedulerProperties.getPermanentJobsGroupName())).forEach(
                    key -> {
                        // Delete job if it is not in the list of jobDetails
                        if (!jobKeys.contains(key)) {
                            try {
                                scheduler.deleteJob(key);
                            } catch (SchedulerException e) {
                                log.error("Error deleting job", e);
                            }
                        }
                    }
            );
        } catch (SchedulerException e) {
            log.error("Error revalidating jobs", e);
        }
    }


}