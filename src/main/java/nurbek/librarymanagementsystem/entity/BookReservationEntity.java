package nurbek.librarymanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import nurbek.librarymanagementsystem.dto.BookReservation;
import nurbek.librarymanagementsystem.dto.ReservationStatus;

import java.util.Date;

@Entity
@Data
@Table(name = "BOOK_RESERVATION")
@AllArgsConstructor
@RequiredArgsConstructor
public class BookReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "reservation_seq")
    @TableGenerator(
            name = "reservation_seq",
            table = "id_gen_table",
            pkColumnName = "gen_name",
            valueColumnName = "gen_val",
            initialValue = 1000,
            allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "RESERVATION_DATE", nullable = false)
    private Date reservationDate = new Date();
    @Temporal(TemporalType.DATE)
    @Column(name = "DUE_DATE", nullable = false)
    private Date dueDate;
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.RESERVED;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID")
    private BookEntity book;
    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private AccountEntity account;

    public BookReservation toDto() {
        return new BookReservation(
                id,
                reservationDate,
                dueDate,
                status,
                book.toDto(),
                account.toDto()
        );
    }

    public static BookReservationEntity fromDto(BookReservation reservation) {
        BookReservationEntity entity = new BookReservationEntity();
        entity.setId(reservation.getId());
        entity.setReservationDate(reservation.getReservationDate());
        entity.setDueDate(reservation.getDueDate());
        entity.setStatus(reservation.getStatus());
        entity.setBook(BookEntity.fromDto(reservation.getBook()));
        entity.setAccount(AccountEntity.fromDto(reservation.getAccount()));
        return entity;
    }
}