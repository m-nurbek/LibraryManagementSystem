package nurbek.librarymanagementsystem.service.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nurbek.librarymanagementsystem.service.GreetingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Getter
@NoArgsConstructor
@Service
@Slf4j
public class GreetingServiceImpl implements GreetingService {
    @Value("${greeting.message}")
    private String greeting;

    @Override
    public void logGreeting() {
        log.info(greeting);
    }
}