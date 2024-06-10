package nurbek.librarymanagementsystem;

import lombok.extern.slf4j.Slf4j;
import nurbek.librarymanagementsystem.service.common.GreetingServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@Slf4j
public class LibraryManagementSystemApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(LibraryManagementSystemApplication.class, args);
        var greetingService = context.getBean(GreetingServiceImpl.class);
        greetingService.logGreeting();
    }
}