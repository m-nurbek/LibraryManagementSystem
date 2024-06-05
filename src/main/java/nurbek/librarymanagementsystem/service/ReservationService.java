package nurbek.librarymanagementsystem.service;

import lombok.AllArgsConstructor;
import nurbek.librarymanagementsystem.dto.Account;
import nurbek.librarymanagementsystem.dto.Book;
import nurbek.librarymanagementsystem.dto.BookReservation;
import nurbek.librarymanagementsystem.dto.ReservationStatus;
import nurbek.librarymanagementsystem.entity.AccountEntity;
import nurbek.librarymanagementsystem.entity.BookEntity;
import nurbek.librarymanagementsystem.entity.BookReservationEntity;
import nurbek.librarymanagementsystem.property.ConfigurationProperty;
import nurbek.librarymanagementsystem.repository.BookReservationRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationService {
    private ConfigurationProperty props;
    private BookReservationRepository reservationRepository;
    private UserService userService;
    private LibraryService libraryService;

    // TODO: Test this method
    public List<BookReservation> getReservationsByAccountId(long accountId) {
        return reservationRepository.findByAccountId(accountId).stream()
                .map(BookReservationEntity::toDto).toList();
    }

    // TODO: Test this method
    public void deleteReservationByAccountId(long accountId) {
        reservationRepository.deleteByAccountId(accountId);
    }

    // TODO: Test this method
    public void deleteReservationByBookIdAndAccountId(long bookId, long accountId) {
        reservationRepository.deleteByBookIdAndAccountId(bookId, accountId);
    }

    // TODO: Test this method
    public void reserveBook(long bookId, long accountId) {
        BookReservationEntity reservation = new BookReservationEntity();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        reservation.setStatus(ReservationStatus.RESERVED);
        reservation.setReservationDate(calendar.getTime());

        // Add 7 days to the current date
        calendar.add(Calendar.DATE, props.getMaxDaysToReturnBook());
        reservation.setDueDate(calendar.getTime());

        Book book = libraryService.getBookById(bookId).orElse(null);

        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }

        Account account = userService.getAccountById(accountId).orElse(null);

        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }

        reservation.setBook(BookEntity.fromDto(book));
        reservation.setAccount(AccountEntity.fromDto(account));

        reservationRepository.save(reservation);
    }

    // TODO: Test this method
    public void returnBook(long bookId, long accountId) {
        BookReservationEntity reservation = reservationRepository.findByBookIdAndAccountId(bookId, accountId).orElse(null);

        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found");
        }

        reservationRepository.deleteByBookIdAndAccountId(bookId, accountId);
    }

    // TODO: Test this method
    public Optional<BookReservation> updateReservationStatus(long bookId, long accountId, ReservationStatus status) {
        BookReservationEntity reservation = reservationRepository.findByBookIdAndAccountId(bookId, accountId).orElse(null);

        if (reservation == null) {
            return Optional.empty();
        }

        reservation.setStatus(status);
        reservationRepository.save(reservation);

        return Optional.of(reservation.toDto());
    }

    // TODO: Test this method
    public Optional<BookReservation> updateReservationStatus(long reservationId, ReservationStatus status) {
        BookReservationEntity reservation = reservationRepository.findById(reservationId).orElse(null);

        if (reservation == null) {
            return Optional.empty();
        }

        reservation.setStatus(status);
        reservationRepository.save(reservation);

        return Optional.of(reservation.toDto());
    }

    // TODO: Test this method
    public List<BookReservation> getOverdueReservations() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        return reservationRepository.getOverdueBookReservations(calendar.getTime())
                .stream().map(BookReservationEntity::toDto).toList();
    }
}