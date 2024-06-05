package nurbek.librarymanagementsystem.job;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nurbek.librarymanagementsystem.dto.BookReservation;
import nurbek.librarymanagementsystem.dto.ReservationStatus;
import nurbek.librarymanagementsystem.service.ReservationService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
@PersistJobDataAfterExecution
public class OverdueReservationJob implements Job {
    private final ReservationService reservationService;

    // TODO: Test this job
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<BookReservation> overdueReservations = reservationService.getOverdueReservations();

        overdueReservations.forEach(reservation -> {
            log.info("Reservation is overdue: {}", reservation.getId());
            reservationService.updateReservationStatus(reservation.getId(), ReservationStatus.OVERDUE);
        });
    }
}