package nurbek.librarymanagementsystem.service.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Getter
@NoArgsConstructor
@Service
@Slf4j
public class GreetingService {
    @Value("${greeting.message}")
    private String greeting;

    public void logGreeting() {
        log.info(greeting);
    }
}