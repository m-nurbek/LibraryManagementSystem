package nurbek.librarymanagementsystem.applicationEvent.listeners;

import lombok.RequiredArgsConstructor;
import nurbek.librarymanagementsystem.applicationEvent.events.NotificationSendEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    @Async
    @EventListener
    public void onNotificationSendEvent(NotificationSendEvent event) {
    }
}