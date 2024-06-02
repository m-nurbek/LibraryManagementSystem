package nurbek.librarymanagementsystem.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableScheduling
public class DeleteBookJob {


    @Scheduled(cron = "*/3 * * * * *")
    public void deleteBook() {
        log.info("Deleting book...");
    }
}