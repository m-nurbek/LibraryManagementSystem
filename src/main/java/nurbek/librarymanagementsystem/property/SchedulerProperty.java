package nurbek.librarymanagementsystem.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "scheduler")
@Component
@Data
public class SchedulerProperty {
    private String permanentJobsGroupName = "PERMANENT";
    private String showTimeJobCron = "0 0 * * * ?";
}