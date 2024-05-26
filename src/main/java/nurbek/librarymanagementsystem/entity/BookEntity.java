package nurbek.librarymanagementsystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;


@Data
@Entity
@Table(name = "BOOK")
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "ISBN", unique = true, nullable = false)
    private String ISBN;
    @Column(name = "TITLE", nullable = false)
    private String title;
    @Column(name = "LANGUAGE")
    private String language;
    @Column(name = "NUMBER_OF_PAGES")
    private String numberOfPages;
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private BookStatus status;
    @Column(name = "PUBLISH_DATE")
    private Date publishDate = new Date();
    @Column(name = "ARCHIVE_DATE")
    private Date archiveDate;
    @Column(name = "NUMBER_OF_COPIES")
    private int numberOfCopies;

    @ManyToOne
    private AuthorEntity author;

    public enum BookStatus {
        ARCHIVED, DELETED, ACTIVE
    }
}