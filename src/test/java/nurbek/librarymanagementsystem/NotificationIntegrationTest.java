package nurbek.librarymanagementsystem;

import lombok.extern.slf4j.Slf4j;
import nurbek.librarymanagementsystem.dto.Notification;
import nurbek.librarymanagementsystem.repository.NotificationRepository;
import nurbek.librarymanagementsystem.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = LibraryManagementSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class NotificationIntegrationTest {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    @WithUserDetails("ltownes5@google.pl")
    void shouldSendNotification() {
        // given
        String accountEmail = "jcumbes8@tamu.edu";
        String subject = "Subject";
        String content = "Content...";

        // when
        notificationService.sendNotification(accountEmail, subject, content);

        // then
        // verify that the notification is sent
        List<Notification> notificationList = notificationService.getNotifications(accountEmail);
        assertThat(notificationList).isNotEmpty();
        assertThat(notificationList.size()).isEqualTo(1);
        assertThat(notificationList.getFirst().getSubject()).isEqualTo(subject);
        assertThat(notificationList.getFirst().getContent()).isEqualTo(content);
        assertThat(notificationList.getFirst().getPublishDate()).isNotNull();
        assertThat(notificationList.getFirst().getAccount().getEmail()).isEqualTo(accountEmail);
    }

    @Test
    @WithUserDetails("ltownes5@google.pl")
    void shouldGetNotifications() {
        // given
        String accountEmail1 = "jcumbes8@tamu.edu";
        String accountEmail2 = "smallard6@paypal.com";
        String subject = "Subject";
        String content = "Content...";

        // when
        notificationService.sendNotification(accountEmail1, subject, content);
        notificationService.sendNotification(accountEmail1, subject, content);
        notificationService.sendNotification(accountEmail1, subject, content);
        notificationService.sendNotification(accountEmail1, subject, content);
        notificationService.sendNotification(accountEmail1, subject, content);

        notificationService.sendNotification(accountEmail2, subject, content);
        notificationService.sendNotification(accountEmail2, subject, content);
        notificationService.sendNotification(accountEmail2, subject, content);

        // then
        // verify that the notification is sent
        List<Notification> notificationList1 = notificationService.getNotifications(accountEmail1);
        assertThat(notificationList1).isNotEmpty();
        assertThat(notificationList1.size()).isEqualTo(5);

        List<Notification> notificationList2 = notificationService.getNotifications(accountEmail2);
        assertThat(notificationList2).isNotEmpty();
        assertThat(notificationList2.size()).isEqualTo(3);
    }

    @Test
    @WithUserDetails("ltownes5@google.pl")
    void anotherAccountShouldNotGetNotification() {
        // given
        String accountEmail = "jcumbes8@tamu.edu";
        String subject = "Subject";
        String content = "Content...";

        // when
        notificationService.sendNotification(accountEmail, subject, content);

        // then
        // verify that the notification is sent
        List<Notification> notificationList = notificationService.getNotifications("smallard6@paypal.com");
        assertThat(notificationList).isEmpty();
    }
}