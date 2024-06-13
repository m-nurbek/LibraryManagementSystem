package nurbek.librarymanagementsystem;

import nurbek.librarymanagementsystem.dto.Author;
import nurbek.librarymanagementsystem.dto.Book;
import nurbek.librarymanagementsystem.dto.BookStatus;
import nurbek.librarymanagementsystem.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = LibraryManagementSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LibraryIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LibraryService libraryService;

    @ParameterizedTest
    @MethodSource("validBooks")
    @WithUserDetails("ltownes5@google.pl")
    void shouldUpdateBook(String isbn, String title, int numberOfCopies, int numberOfPages, String language, Date publishDate, Author author) throws Exception {
        var book = Book.builder()
                .ISBN(isbn)
                .title(title)
                .numberOfCopies(numberOfCopies)
                .numberOfPages(numberOfPages)
                .language(language)
                .publishDate(publishDate)
                .author(author)
                .build();

        mockMvc.perform(patch("/library/books/1")
                        .flashAttr("updatedBook", book))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/library/books/1"));

        // Verify the book is updated
        Book updatedBook = libraryService.getBookById(1L).orElse(null);

        assertThat(updatedBook).isNotNull();
        assertThat(updatedBook.getISBN()).isEqualTo(isbn);
        assertThat(updatedBook.getTitle()).isEqualTo(title);
        assertThat(updatedBook.getNumberOfCopies()).isEqualTo(numberOfCopies);
        assertThat(updatedBook.getNumberOfPages()).isEqualTo(numberOfPages);
        assertThat(updatedBook.getLanguage()).isEqualTo(language);
        assertThat(updatedBook.getStatus()).isEqualTo(BookStatus.ACTIVE);
        assertThat(updatedBook.getPublishDate()).isNotNull();
        assertThat(updatedBook.getArchiveDate()).isNull();
        assertThat(updatedBook.getAuthor().getId()).isEqualTo(author.getId());
        assertThat(updatedBook.getAuthor().getName()).isEqualTo(author.getName());
    }

    private static Stream<Arguments> validBooks() {
        return Stream.of(
                Arguments.of("13212143121", "Updated Book 1", 1, 1, "PH", new Date(2023, 2, 16), new Author(1L, "Kali Shiels")),
                Arguments.of("8838848288", "Updated Book 2", 2, 2, "EN", new Date(2023, 2, 16), new Author(2L, "Oby Teanby")),
                Arguments.of("108357909-6", "Updated Book 3", 3, 3, "RU", new Date(2023, 2, 16), new Author(3L, "Liva Rown")),
                Arguments.of("108357909-6", "Updated Book 4", 4, 4, "KZ", new Date(2023, 2, 16), new Author(4L, "Ola Dunstan")),
                Arguments.of("108357909-6", "Updated Book 5", 5, 5, "EN", new Date(2023, 2, 16), new Author(5L, "Elysee Stammer")),
                Arguments.of("108357909-6", "Updated Book 6", 6, 6, "PR", new Date(2023, 2, 16), new Author(6L, "Steffane Dewerson"))
        );
    }

    @Test
    @WithUserDetails("ltownes5@google.pl")
    void shouldNotUpdateBookThatDoesNotExists() throws Exception {
        // given
        var book = Book.builder()
                .ISBN("13212143121")
                .title("Updated Book 1")
                .numberOfCopies(1)
                .numberOfPages(20)
                .language("RU")
                .publishDate(new Date())
                .author(null)
                .build();

        // when
        // Book id 100 does not exist
        mockMvc.perform(patch("/library/books/100")
                        .flashAttr("updatedBook", book))
                .andExpect(view().name("error"));

        List<Book> libraryBook = libraryService.getBookList();

        // Verify the number of books is still 10
        assertThat(libraryBook.size()).isEqualTo(10);
    }

    @ParameterizedTest
    @MethodSource("invalidBooks")
    @WithUserDetails("ltownes5@google.pl")
    void shouldNotUpdateInvalidBooks(String isbn, String title, int numberOfCopies, int numberOfPages, String language, Date publishDate, Author author) throws Exception {
        var book = Book.builder()
                .ISBN(isbn)
                .title(title)
                .numberOfCopies(numberOfCopies)
                .numberOfPages(numberOfPages)
                .language(language)
                .publishDate(publishDate)
                .author(author)
                .build();

        mockMvc.perform(patch("/library/books/1")
                        .flashAttr("updatedBook", book))
                .andExpect(status().isOk())
                .andExpect(view().name("book"));

        // Verify the book is not updated
        Book updatedBook = libraryService.getBookById(1L).orElse(null);

        assertThat(updatedBook).isNotNull();
        assertThat(updatedBook.getISBN()).isEqualTo("108357909-6");
        assertThat(updatedBook.getTitle()).isEqualTo("Champagne for Caesar");
        assertThat(updatedBook.getNumberOfCopies()).isEqualTo(1);
        assertThat(updatedBook.getNumberOfPages()).isEqualTo(1);
        assertThat(updatedBook.getLanguage()).isEqualTo("PH");
        assertThat(updatedBook.getStatus()).isEqualTo(BookStatus.ACTIVE);
        assertThat(updatedBook.getPublishDate()).isNotNull();
        assertThat(updatedBook.getArchiveDate()).isNull();
        assertThat(updatedBook.getAuthor().getId()).isEqualTo(1);
        assertThat(updatedBook.getAuthor().getName()).isEqualTo("Kali Shiels");
    }

    private static Stream<Arguments> invalidBooks() {
        return Stream.of(
                Arguments.of("", "Updated Book 1", 1, 1, "PH", new Date(2023, 2, 16), new Author(2L, "Oby Teanby")),
                Arguments.of("8838848288", "", 1, 2, "EN", new Date(2023, 2, 16), new Author(2L, "Oby Teanby")),
                Arguments.of("108357909-6", "Updated Book 3", 0, 3, "RU", new Date(2023, 2, 16), new Author(3L, "Liva Rown")),
                Arguments.of("108357909-6", "Updated Book 4", 1000, 4, "KZ", new Date(2023, 2, 16), new Author(4L, "Ola Dunstan")),
                Arguments.of("108357909-68911111111111111111111111", "Updated Book 5", 5, 5, "EN", new Date(2023, 2, 16), new Author(5L, "Elysee Stammer"))
        );
    }

    @Test
    @WithUserDetails("ltownes5@google.pl")
    void shouldAddBook() throws Exception {
        var book = Book.builder()
                .ISBN("13212143121")
                .title("New Book")
                .numberOfCopies(1)
                .numberOfPages(20)
                .language("RU")
                .status(BookStatus.ACTIVE)
                .publishDate(new Date())
                .author(new Author(2L, "Oby Teanby"))
                .build();

        mockMvc.perform(post("/library/books/add")
                        .flashAttr("book", book))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/library/books"));

        // Verify the book is not updated
        // Books are added starting from id 1001
        Book updatedBook = libraryService.getBookById(1001L).orElse(null);

        assertThat(updatedBook).isNotNull();
        assertThat(updatedBook.getISBN()).isEqualTo("13212143121");
        assertThat(updatedBook.getTitle()).isEqualTo("New Book");
        assertThat(updatedBook.getNumberOfCopies()).isEqualTo(1);
        assertThat(updatedBook.getNumberOfPages()).isEqualTo(20);
        assertThat(updatedBook.getLanguage()).isEqualTo("RU");
        assertThat(updatedBook.getStatus()).isEqualTo(BookStatus.ACTIVE);
        assertThat(updatedBook.getPublishDate()).isNotNull();
        assertThat(updatedBook.getArchiveDate()).isNull();
        assertThat(updatedBook.getAuthor().getId()).isEqualTo(2);
        assertThat(updatedBook.getAuthor().getName()).isEqualTo("Oby Teanby");
    }

    @Test
    @WithUserDetails("ltownes5@google.pl")
    void shouldNotAddBookThatAlreadyExists() throws Exception {
        var book = Book.builder()
                .ISBN("108357909-6") // a book with this ISBN already exists
                .title("New Book")
                .numberOfCopies(1)
                .numberOfPages(20)
                .language("RU")
                .status(BookStatus.ACTIVE)
                .publishDate(new Date())
                .author(new Author(2L, "Oby Teanby"))
                .build();

        mockMvc.perform(post("/library/books/add")
                        .flashAttr("book", book))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/library/books"));

        // Verify the book is not updated
        // Books are added starting from id 1001
        Book updatedBook = libraryService.getBookById(1001L).orElse(null);

        Book existingBook = libraryService.getBookById(1L).orElse(null);

        assertThat(updatedBook).isNull();
        assertThat(existingBook).isNotNull();
        assertThat(existingBook.getISBN()).isEqualTo("108357909-6");
        assertThat(existingBook.getTitle()).isEqualTo("Champagne for Caesar");
        assertThat(existingBook.getNumberOfCopies()).isEqualTo(2); // 1 copy was added to the existing book
    }

    @ParameterizedTest
    @MethodSource("invalidBooks")
    @WithUserDetails("ltownes5@google.pl")
    void shouldNotAddInvalidBook(String isbn, String title, int numberOfCopies, int numberOfPages, String language, Date publishDate, Author author) throws Exception {
        var book = Book.builder()
                .ISBN(isbn)
                .title(title)
                .numberOfCopies(numberOfCopies)
                .numberOfPages(numberOfPages)
                .language(language)
                .publishDate(publishDate)
                .author(author)
                .status(BookStatus.ACTIVE)
                .build();

        mockMvc.perform(post("/library/books/add")
                        .flashAttr("book", book))
                .andExpect(status().isOk())
                .andExpect(view().name("bookAdd"));

        // Verify the book is not updated
        // Books are added starting from id 1001
        Book updatedBook = libraryService.getBookById(1001L).orElse(null);

        assertThat(updatedBook).isNull();
    }

    @Test
    @WithUserDetails("ltownes5@google.pl")
    void shouldRestoreBook() throws Exception {
        // given
        Book book = libraryService.getBookById(1L).orElse(null);
        assert book != null;
        book.setStatus(BookStatus.ARCHIVED);
        book.setArchiveDate(new Date());

        // when
        mockMvc.perform(put("/library/books/restore/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/library/books/1"));

        // then
        Book restoredBook = libraryService.getBookById(1L).orElse(null);
        assertThat(restoredBook).isNotNull();
        assertThat(restoredBook.getStatus()).isEqualTo(BookStatus.ACTIVE);
        assertThat(restoredBook.getArchiveDate()).isNull();
    }

    @Test
    @WithUserDetails("ltownes5@google.pl")
    void shouldNotRestoreBookThatDoesNotExists() throws Exception {
        // when
        mockMvc.perform(put("/library/books/restore/100"))
                .andExpect(view().name("error"));

        // then
        Book restoredBook = libraryService.getBookById(100L).orElse(null);
        assertThat(restoredBook).isNull();
    }

    @Test
    @WithUserDetails("ltownes5@google.pl")
    void shouldArchiveBook() throws Exception {
        // given
        Book book = libraryService.getBookById(1L).orElse(null);
        assert book != null;

        // when
        mockMvc.perform(delete("/library/books/archive/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/library/books/1"));

        // then
        Book archivedBook = libraryService.getBookById(1L).orElse(null);
        assertThat(archivedBook).isNotNull();
        assertThat(archivedBook.getStatus()).isEqualTo(BookStatus.ARCHIVED);
        assertThat(archivedBook.getArchiveDate()).isNotNull();
    }

    @Test
    @WithUserDetails("ltownes5@google.pl")
    void shouldNotArchiveBookThatDoesNotExists() throws Exception {
        // when
        mockMvc.perform(delete("/library/books/archive/100"))
                .andExpect(view().name("error"));

        // then
        Book archivedBook = libraryService.getBookById(100L).orElse(null);
        assertThat(archivedBook).isNull();
    }
}