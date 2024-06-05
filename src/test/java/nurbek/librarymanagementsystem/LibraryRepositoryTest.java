package nurbek.librarymanagementsystem;

import lombok.extern.slf4j.Slf4j;
import nurbek.librarymanagementsystem.dto.BookStatus;
import nurbek.librarymanagementsystem.entity.BookEntity;
import nurbek.librarymanagementsystem.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext
public class LibraryRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    private final PageRequest pageRequest = PageRequest.of(0, 10);

    @Test
    public void shouldFindBooksByTitle() {
        String keyword = "Dea";

        Page<BookEntity> searchResults = bookRepository.searchByKeyword(keyword, pageRequest);

        assertThat(searchResults.getContent()).isNotEmpty();
        assertThat(searchResults.getContent().size()).isGreaterThanOrEqualTo(2);

        checkFoundByTitleOrIsbn(true, false, keyword, searchResults);
    }

    @Test
    public void shouldFindExactBookByTitle() {
        String keyword = "Dead Space: Aftermath";

        Page<BookEntity> searchResults = bookRepository.searchByKeyword(keyword, pageRequest);

        assertThat(searchResults.getContent()).isNotEmpty();
        assertThat(searchResults.getContent().size()).isEqualTo(1);

        checkFoundByTitleOrIsbn(true, false, keyword, searchResults);
    }

    @Test
    public void shouldFindBooksByISBN() {
        String keyword = "-4";

        Page<BookEntity> searchResults = bookRepository.searchByKeyword(keyword, pageRequest);

        assertThat(searchResults.getContent()).isNotEmpty();
        assertThat(searchResults.getContent().size()).isGreaterThanOrEqualTo(2);

        checkFoundByTitleOrIsbn(false, true, keyword, searchResults);
    }

    @Test
    public void shouldFindExactBookByISBN() {
        String keyword = "108357909-6";

        Page<BookEntity> searchResults = bookRepository.searchByKeyword(keyword, pageRequest);

        assertThat(searchResults.getContent()).isNotEmpty();
        assertThat(searchResults.getContent().size()).isEqualTo(1);

        checkFoundByTitleOrIsbn(false, true, keyword, searchResults);
    }

    private void checkFoundByTitleOrIsbn(boolean shouldFindByTitle, boolean shouldFindByIsbn, String keyword, Page<BookEntity> searchResults) {
        boolean foundByTitle = false;
        boolean foundByIsbn = false;
        for (BookEntity book : searchResults) {
            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                foundByTitle = true;
            } else if (book.getISBN().contains(keyword)) {
                foundByIsbn = true;
            }
        }
        if (shouldFindByTitle) {
            assertThat(foundByTitle).isTrue();
        }
        if (shouldFindByIsbn) {
            assertThat(foundByIsbn).isTrue();
        }
    }

    @Test
    public void searchShouldReturnEmptyOnNonExistentKeyword() {
        String keyword = "non-existent";

        Page<BookEntity> searchResults = bookRepository.searchByKeyword(keyword, pageRequest);

        assertThat(searchResults.getContent()).isEmpty();
    }

    @Test
    public void shouldFindBooksByStatus() {
        BookEntity book1 = bookRepository.findById(1L).orElse(null);
        BookEntity book2 = bookRepository.findById(2L).orElse(null);
        assert book1 != null;
        assert book2 != null;

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        // Set the book status to ARCHIVED and save it
        book1.setStatus(BookStatus.ARCHIVED);
        book2.setStatus(BookStatus.ARCHIVED);
        book1.setArchiveDate(date);
        book2.setArchiveDate(date);

        bookRepository.save(book1);
        bookRepository.save(book2);

        Page<BookEntity> searchResults = bookRepository.getBookEntitiesByStatusIgnoreCase(BookStatus.ARCHIVED.name(), pageRequest);

        log.info("searchResults: {}", searchResults.getContent());
        assertThat(searchResults.getContent()).isNotEmpty();
        assertThat(searchResults.getContent().size()).isEqualTo(2);

        checkFoundByStatus(BookStatus.ARCHIVED, searchResults.getContent());
    }

    @Test
    public void shouldFindBooksByStatusAndArchiveDateBefore() {
        BookEntity book1 = bookRepository.findById(1L).orElse(null);
        BookEntity book2 = bookRepository.findById(2L).orElse(null);
        assert book1 != null;
        assert book2 != null;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date date = calendar.getTime();

        // Set the book status to ARCHIVED and save it
        book1.setStatus(BookStatus.ARCHIVED);
        book2.setStatus(BookStatus.ARCHIVED);
        book1.setArchiveDate(date);
        book2.setArchiveDate(date);

        bookRepository.save(book1);
        bookRepository.save(book2);

        Date currentDate = new Date();

        List<BookEntity> searchResults = bookRepository.getBooksByStatusAndArchiveDateBefore(BookStatus.ARCHIVED.name(), currentDate);

        log.info("searchResults: {}", searchResults);
        assertThat(searchResults).isNotEmpty();
        assertThat(searchResults.size()).isEqualTo(2);

        checkFoundByStatus(BookStatus.ARCHIVED, searchResults);
    }

    private void checkFoundByStatus(BookStatus status, Iterable<BookEntity> searchResults) {
        boolean foundByStatus = false;
        for (BookEntity book : searchResults) {
            if (book.getStatus().equals(status)) {
                foundByStatus = true;
            }
        }
        assertThat(foundByStatus).isTrue();
    }
}