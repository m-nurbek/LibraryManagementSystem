package nurbek.librarymanagementsystem;

import nurbek.librarymanagementsystem.entity.BookEntity;
import nurbek.librarymanagementsystem.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class LibraryRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    // Sample book entities for testing
    private final List<BookEntity> bookList = Arrays.asList(
            new BookEntity(1L, "0001", "Book 1", "RU", 50, BookEntity.BookStatus.ACTIVE, new Date(), new Date(), 23, null),
            new BookEntity(2L, "0002", "Book 2", "KZ", 100, BookEntity.BookStatus.ACTIVE, new Date(2004, Calendar.JANUARY, 1), new Date(), 23, null),
            new BookEntity(3L, "0003", "Book 3", "EN", 200, BookEntity.BookStatus.ACTIVE, new Date(2004, Calendar.MARCH, 2), new Date(), 23, null),
            new BookEntity(4L, "0004", "Book 4", "EN", 300, BookEntity.BookStatus.ACTIVE, new Date(2004, Calendar.JULY, 3), new Date(), 23, null)
    );

    private final PageRequest pageRequest = PageRequest.of(0, 10);

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        bookRepository.saveAll(bookList);
    }

    @Test
    public void shouldFindBooksByTitle() {
        String keyword = "book";

        Page<BookEntity> searchResults = bookRepository.searchByKeyword(keyword, pageRequest);

        assertThat(searchResults.getContent()).isNotEmpty();
        assertThat(searchResults.getContent().size()).isGreaterThanOrEqualTo(2);

        checkFoundByTitleOrIsbn(true, false, keyword, searchResults);
    }

    @Test
    public void shouldFindExactBookByTitle() {
        String keyword = "book 3";

        Page<BookEntity> searchResults = bookRepository.searchByKeyword(keyword, pageRequest);

        assertThat(searchResults.getContent()).isNotEmpty();
        assertThat(searchResults.getContent().size()).isEqualTo(1);

        checkFoundByTitleOrIsbn(true, false, keyword, searchResults);
    }

    @Test
    public void shouldFindBooksByISBN() {
        String keyword = "000";

        Page<BookEntity> searchResults = bookRepository.searchByKeyword(keyword, pageRequest);

        assertThat(searchResults.getContent()).isNotEmpty();
        assertThat(searchResults.getContent().size()).isGreaterThanOrEqualTo(2);

        checkFoundByTitleOrIsbn(false, true, keyword, searchResults);
    }

    @Test
    public void shouldFindExactBookByISBN() {
        String keyword = "0004";

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
}