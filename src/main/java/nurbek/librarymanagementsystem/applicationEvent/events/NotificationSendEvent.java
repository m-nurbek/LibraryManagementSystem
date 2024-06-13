package nurbek.librarymanagementsystem.applicationEvent.events;

import lombok.Data;
import nurbek.librarymanagementsystem.dto.Account;

// ApplicationEventPublisher publisher
// publisher.publishEvent(new NotificationSendEvent(account));

@Data
public class NotificationSendEvent {

    private final Account account;
}