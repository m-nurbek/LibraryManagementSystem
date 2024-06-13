package nurbek.librarymanagementsystem.service.serviceImpl;

import lombok.AllArgsConstructor;
import nurbek.librarymanagementsystem.dto.Notification;
import nurbek.librarymanagementsystem.entity.AccountEntity;
import nurbek.librarymanagementsystem.entity.NotificationEntity;
import nurbek.librarymanagementsystem.repository.NotificationRepository;
import nurbek.librarymanagementsystem.service.NotificationService;
import nurbek.librarymanagementsystem.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserService userService;

    @Override
    public void sendNotification(String accountEmail, String subject, String content) {
        var notification = new NotificationEntity(
                null,
                subject,
                content,
                new Date(),
                AccountEntity.fromDto(userService.getAccountByEmail(accountEmail).orElse(null))
        );

        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getNotifications(String accountEmail) {
        var account = userService.getAccountByEmail(accountEmail).orElse(null);

        if (account != null) {
            return notificationRepository
                    .findByAccount_Email(account.getEmail())
                    .stream().map(NotificationEntity::toDto).toList();
        }

        return List.of();
    }
}