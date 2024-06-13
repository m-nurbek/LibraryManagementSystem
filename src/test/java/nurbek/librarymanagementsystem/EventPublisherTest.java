package nurbek.librarymanagementsystem;

import nurbek.librarymanagementsystem.service.NotificationService;
import nurbek.librarymanagementsystem.service.serviceImpl.ReservationServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EventPublisherTest {
    @Autowired
    private ReservationServiceImpl reservationService;
    @SpyBean
    private NotificationService notificationService;

    private static final int WAIT_TIME = 5000;

    @Test
    void shouldNotGetNotification() {
        // given
        long bookId = 12L;
        long accountId = 4L;

        // when
        reservationService.addReservation(bookId, accountId);

        // then
        then(notificationService).shouldHaveNoInteractions();
    }

    @Test
    void shouldGetNotification() {
        // given
        long bookId = 1L;
        long accountId = 4L;

        // when
        reservationService.addReservation(bookId, accountId);

        // then
        verify(notificationService, timeout(WAIT_TIME).times(1)).sendNotification(anyString(), anyString(), anyString());
    }
}