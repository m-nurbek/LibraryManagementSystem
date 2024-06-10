package nurbek.librarymanagementsystem.service;

import nurbek.librarymanagementsystem.dto.BookReservation;
import nurbek.librarymanagementsystem.dto.ReservationStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ReservationService {
    // TODO: Test this method
    List<BookReservation> getReservationsByAccountId(long accountId);

    // TODO: Test this method
    @Transactional
    void deleteReservationsByAccountId(long accountId);

    // TODO: Test this method
    @Transactional
    void deleteReservationByBookIdAndAccountId(long bookId, long accountId);

    @Transactional
    Optional<BookReservation> deleteReservationById(long reservationId);

    // TODO: Test this method
    @Transactional
    boolean addReservation(long bookId, long accountId);

    // TODO: Test this method
    @Transactional
    void returnBook(long bookId, long accountId);

    // TODO: Test this method
    Optional<BookReservation> updateReservationStatus(long bookId, long accountId, ReservationStatus status);

    // TODO: Test this method
    Optional<BookReservation> updateReservationStatus(long reservationId, ReservationStatus status);

    // TODO: Test this method
    List<BookReservation> getOverdueReservations();

    // TODO: Test this method
    /**
     * Renew a book reservation.
     * The due date is extended by the number of days specified in the configuration property.
     * If the reservation is not found, an empty optional is returned.
     *
     * @param reservationId the reservation ID
     * @return an optional of the renewed reservation, otherwise an empty optional
     */
    Optional<BookReservation> renewReservation(long reservationId);
}