package nurbek.librarymanagementsystem.applicationEvent.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nurbek.librarymanagementsystem.applicationEvent.events.ReservationEvent;
import nurbek.librarymanagementsystem.service.NotificationService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationListener {
    private final NotificationService notificationService;

    @Async
    @EventListener
    public void onNotificationSendEvent(ReservationEvent event) {
        // Send email
        notificationService.sendNotification(event.getAccount().getEmail(), event.getSubject(), event.getContent());
        log.info("Notification sent to {}", event.getAccount().getEmail());
        log.info("Subject: {}", event.getSubject());
    }
}