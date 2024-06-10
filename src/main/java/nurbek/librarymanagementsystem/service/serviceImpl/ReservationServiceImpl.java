package nurbek.librarymanagementsystem.service.serviceImpl;

import lombok.AllArgsConstructor;
import nurbek.librarymanagementsystem.dto.Account;
import nurbek.librarymanagementsystem.dto.Book;
import nurbek.librarymanagementsystem.dto.BookReservation;
import nurbek.librarymanagementsystem.dto.ReservationStatus;
import nurbek.librarymanagementsystem.entity.AccountEntity;
import nurbek.librarymanagementsystem.entity.BookEntity;
import nurbek.librarymanagementsystem.entity.BookReservationEntity;
import nurbek.librarymanagementsystem.property.ConfigurationProperty;
import nurbek.librarymanagementsystem.repository.BookRepository;
import nurbek.librarymanagementsystem.repository.BookReservationRepository;
import nurbek.librarymanagementsystem.service.LibraryService;
import nurbek.librarymanagementsystem.service.ReservationService;
import nurbek.librarymanagementsystem.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final BookRepository bookRepository;
    private ConfigurationProperty props;
    private BookReservationRepository reservationRepository;
    private UserService userService;
    private LibraryService libraryService;

    @Override
    public List<BookReservation> getReservationsByAccountId(long accountId) {
        return reservationRepository.findByAccountId(accountId).stream()
                .map(BookReservationEntity::toDto).toList();
    }

    @Override
    @Transactional
    public void deleteReservationsByAccountId(long accountId) {
        List<BookReservation> reservations = getReservationsByAccountId(accountId);
        reservations.forEach(
                reservation -> libraryService.returnBook(reservation.getBook().getId())
        );
        reservationRepository.deleteByAccountId(accountId);
    }

    @Override
    @Transactional
    public void deleteReservationByBookIdAndAccountId(long bookId, long accountId) {
        libraryService.returnBook(bookId);
        reservationRepository.deleteByBookIdAndAccountId(bookId, accountId);
    }

    @Override
    @Transactional
    public Optional<BookReservation> deleteReservationById(long reservationId) {
        Optional<BookReservationEntity> reservation = reservationRepository.findById(reservationId);

        if (reservation.isPresent()) {
            libraryService.returnBook(reservation.get().getBook().getId());
            reservationRepository.deleteById(reservationId);
            return Optional.of(reservation.get().toDto());
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public boolean addReservation(long bookId, long accountId) {
        if (reservationRepository.existsByAccount_IdAndBook_Id(accountId, bookId)) {
            // Reservation already exists
            return false;
        }

        Account account = userService.getAccountById(accountId).orElse(null);
        Book book = libraryService.reserveBook(bookId).orElse(null);

        if (account == null || book == null) {
            return false;
        }

        BookReservationEntity reservation = new BookReservationEntity();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        reservation.setStatus(ReservationStatus.RESERVED);
        reservation.setReservationDate(calendar.getTime());

        // Add 7 days to the current date
        calendar.add(Calendar.DATE, props.getMaxDaysToReturnBook());
        reservation.setDueDate(calendar.getTime());

        reservation.setBook(BookEntity.fromDto(book));
        reservation.setAccount(AccountEntity.fromDto(account));

        reservationRepository.save(reservation);

        return true;
    }

    @Override
    @Transactional
    public void returnBook(long bookId, long accountId) {
        BookReservationEntity reservation = reservationRepository.findByBookIdAndAccountId(bookId, accountId).orElse(null);

        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found");
        }

        libraryService.returnBook(bookId);

        reservationRepository.deleteByBookIdAndAccountId(bookId, accountId);
    }

    @Override
    public Optional<BookReservation> updateReservationStatus(long bookId, long accountId, ReservationStatus status) {
        BookReservationEntity reservation = reservationRepository.findByBookIdAndAccountId(bookId, accountId).orElse(null);

        if (reservation == null) {
            return Optional.empty();
        }

        reservation.setStatus(status);
        reservationRepository.save(reservation);

        return Optional.of(reservation.toDto());
    }

    @Override
    public Optional<BookReservation> updateReservationStatus(long reservationId, ReservationStatus status) {
        BookReservationEntity reservation = reservationRepository.findById(reservationId).orElse(null);

        if (reservation == null) {
            return Optional.empty();
        }

        reservation.setStatus(status);
        reservationRepository.save(reservation);

        return Optional.of(reservation.toDto());
    }

    @Override
    public List<BookReservation> getOverdueReservations() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        return reservationRepository.getOverdueBookReservations(calendar.getTime())
                .stream().map(BookReservationEntity::toDto).toList();
    }

    /**
     * Renew a book reservation.
     * The due date is extended by the number of days specified in the configuration property.
     * If the reservation is not found, an empty optional is returned.
     *
     * @param reservationId the reservation ID
     * @return an optional of the renewed reservation, otherwise an empty optional
     */
    @Override
    public Optional<BookReservation> renewReservation(long reservationId) {
        BookReservationEntity reservation = reservationRepository.findById(reservationId).orElse(null);

        if (reservation == null || reservation.getStatus().equals(ReservationStatus.RESERVED)) {
            return Optional.empty();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reservation.getDueDate());
        calendar.add(Calendar.DATE, props.getMaxDaysToReturnBook());
        reservation.setDueDate(calendar.getTime());

        reservationRepository.save(reservation);

        return Optional.of(reservation.toDto());
    }
}