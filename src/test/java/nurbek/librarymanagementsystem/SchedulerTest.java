package nurbek.librarymanagementsystem;

import lombok.extern.slf4j.Slf4j;
import nurbek.librarymanagementsystem.dto.BookStatus;
import nurbek.librarymanagementsystem.entity.BookEntity;
import nurbek.librarymanagementsystem.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SchedulerTest {
    @Autowired
    private BookRepository bookRepository;

    private static final int WAIT_TIME = 5000;

    @Test
    @DisplayName("Should delete book after time passed. Books that were archived 30 days ago should be deleted, others should not be deleted")
    public void shouldDeleteBookAfterTimePassed() throws InterruptedException {
        BookEntity book1 = bookRepository.findById(1L).orElse(null);
        BookEntity book2 = bookRepository.findById(2L).orElse(null);
        assert book1 != null;
        assert book2 != null;

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        calendar.add(Calendar.DATE, -31);
        Date thirtyDaysAgo = calendar.getTime();

        // Set the book status to ARCHIVED and save it
        book1.setStatus(BookStatus.ARCHIVED);
        book2.setStatus(BookStatus.ARCHIVED);

        // Set the archive date to 30 days ago for book1 and to the current date for book2
        // book 1 should be deleted, book 2 should not be deleted
        book1.setArchiveDate(thirtyDaysAgo);
        book2.setArchiveDate(date);
        bookRepository.save(book1);
        bookRepository.save(book2);

        Thread.sleep(WAIT_TIME);

        assertThat(bookRepository.findById(1L).orElse(null)).isNull();
        assertThat(bookRepository.findById(2L).orElse(null)).isNotNull();
    }

    @Test
    @DisplayName("Should delete multiple books after time passed. Books that were archived 30 days ago should be deleted, others should not be deleted")
    public void shouldDeleteMultipleBooksAfterTimePassed() throws InterruptedException {
        BookEntity book1 = bookRepository.findById(1L).orElse(null);
        BookEntity book2 = bookRepository.findById(2L).orElse(null);
        BookEntity book3 = bookRepository.findById(3L).orElse(null);
        BookEntity book4 = bookRepository.findById(4L).orElse(null);
        BookEntity book8 = bookRepository.findById(8L).orElse(null);
        BookEntity book9 = bookRepository.findById(9L).orElse(null);
        assert book1 != null;
        assert book2 != null;
        assert book3 != null;
        assert book4 != null;
        assert book8 != null;
        assert book9 != null;

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        calendar.add(Calendar.DATE, -31);
        Date thirtyDaysAgo = calendar.getTime();

        // Set the book status to ARCHIVED and save it
        book1.setStatus(BookStatus.ARCHIVED);
        book2.setStatus(BookStatus.ARCHIVED);
        book3.setStatus(BookStatus.ARCHIVED);
        book4.setStatus(BookStatus.ARCHIVED);
        book8.setStatus(BookStatus.ARCHIVED);
        book9.setStatus(BookStatus.ARCHIVED);

        // Set the archive date to 30 days ago for book1, book2, book3, and book4
        book1.setArchiveDate(thirtyDaysAgo);
        book2.setArchiveDate(thirtyDaysAgo);
        book3.setArchiveDate(thirtyDaysAgo);
        book4.setArchiveDate(thirtyDaysAgo);
        // book 8 and 9 should not be deleted
        book8.setArchiveDate(date);
        book9.setArchiveDate(date);

        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
        bookRepository.save(book4);
        bookRepository.save(book8);
        bookRepository.save(book9);

        Thread.sleep(WAIT_TIME);

        // Check whether all books are deleted
        assertThat(bookRepository.findById(1L).orElse(null)).isNull();
        assertThat(bookRepository.findById(2L).orElse(null)).isNull();
        assertThat(bookRepository.findById(3L).orElse(null)).isNull();
        assertThat(bookRepository.findById(4L).orElse(null)).isNull();
        assertThat(bookRepository.findById(8L).orElse(null)).isNotNull();
        assertThat(bookRepository.findById(9L).orElse(null)).isNotNull();
    }

    @Test
    @DisplayName("Should not delete archived books before the scheduled time")
    public void shouldNotDeleteArchivedBooksBeforeScheduledTime() throws InterruptedException {
        BookEntity book1 = bookRepository.findById(1L).orElse(null);
        BookEntity book2 = bookRepository.findById(2L).orElse(null);
        assert book1 != null;
        assert book2 != null;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -31);
        Date thirtyDaysAgo = calendar.getTime();

        // Set the book status to ARCHIVED and save it
        book1.setStatus(BookStatus.ARCHIVED);
        book2.setStatus(BookStatus.ARCHIVED);

        // These books should be deleted but will not be deleted before the scheduled time
        book1.setArchiveDate(thirtyDaysAgo);
        book2.setArchiveDate(thirtyDaysAgo);
        bookRepository.save(book1);
        bookRepository.save(book2);

        assertThat(bookRepository.findById(1L).orElse(null)).isNotNull();
        assertThat(bookRepository.findById(2L).orElse(null)).isNotNull();
    }
}