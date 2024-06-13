package nurbek.librarymanagementsystem.applicationEvent.events;

import lombok.Data;
import nurbek.librarymanagementsystem.dto.Account;

// ApplicationEventPublisher publisher
// publisher.publishEvent(new NotificationSendEvent(account));

@Data
public class NotificationEvent {
    private final Account account;
    private final String subject;
    private final String content;
}