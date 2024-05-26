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
    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne
    private BookEntity book;
    @ManyToOne AccountEntity account;

    public enum ReservationStatus {
        RESERVED, OVERDUE, NULL
    }
}