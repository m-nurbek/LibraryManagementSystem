package nurbek.librarymanagementsystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "BOOK_RESERVATION")
public class BookReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "RESERVATION_DATE", nullable = false)
    private Date reservationDate = new Date();
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

    public enum ReservationStatus {
        RESERVED, OVERDUE, NULL
    }
}