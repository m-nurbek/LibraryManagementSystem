package nurbek.librarymanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "BOOK")
@AllArgsConstructor
@NoArgsConstructor
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "ISBN", unique = true, nullable = false)
    private String ISBN;
    @Column(name = "TITLE", nullable = false)
    private String title;
    @Column(name = "LANGUAGE", nullable = false)
    private String language;
    @Column(name = "NUMBER_OF_PAGES", nullable = false)
    private String numberOfPages;
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private BookStatus status = BookStatus.ACTIVE;
    @Column(name = "PUBLISH_DATE")
    private Date publishDate = new Date();
    @Column(name = "ARCHIVE_DATE")
    private Date archiveDate;
    @Column(name = "NUMBER_OF_COPIES", nullable = false)
    private int numberOfCopies;

    @ManyToOne
    private AuthorEntity author;

    public enum BookStatus {
        ARCHIVED, DELETED, ACTIVE
    }
}