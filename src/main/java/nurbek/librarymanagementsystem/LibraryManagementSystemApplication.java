package nurbek.librarymanagementsystem;

import lombok.extern.slf4j.Slf4j;
import nurbek.librarymanagementsystem.service.common.GreetingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
@Slf4j
public class LibraryManagementSystemApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(LibraryManagementSystemApplication.class, args);
        var greetingService = context.getBean(GreetingService.class);
        greetingService.logGreeting();
    }

//    @Bean
//    public CommandLineRunner runner(TaskScheduler taskScheduler) {
//        return args -> {
//            var task = taskScheduler.scheduleAtFixedRate(() -> log.info("Hello World!"), 1000);
//            Thread.sleep(5000);
//            task.cancel(true);
//        };
//    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ThreadPoolTaskScheduler();
    }
}