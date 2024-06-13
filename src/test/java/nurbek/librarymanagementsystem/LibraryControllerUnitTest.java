package nurbek.librarymanagementsystem;

import nurbek.librarymanagementsystem.dto.Author;
import nurbek.librarymanagementsystem.dto.Book;
import nurbek.librarymanagementsystem.dto.BookStatus;
import nurbek.librarymanagementsystem.service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LibraryControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LibraryService libraryService;
    private Page<Book> bookPage;

    @BeforeEach
    void setUp() {
        bookPage = new PageImpl<>(List.of(
                Book.builder().ISBN("0000-0000-0000-0001").title("Book 1").language("RU").numberOfPages(50).status(BookStatus.ACTIVE).publishDate(new Date()).numberOfCopies(23).build(),
                Book.builder().ISBN("0000-0000-0000-0002").title("Book 2").language("KZ").numberOfPages(100).status(BookStatus.ACTIVE).publishDate(new Date(2004, Calendar.JANUARY, 1)).numberOfCopies(23).build(),
                Book.builder().ISBN("0000-0000-0000-0003").title("Book 3").language("EN").numberOfPages(200).status(BookStatus.ACTIVE).publishDate(new Date(2004, Calendar.MARCH, 2)).numberOfCopies(23).build(),
                Book.builder().ISBN("0000-0000-0000-0004").title("Book 4").language("EN").numberOfPages(300).status(BookStatus.ACTIVE).publishDate(new Date(2004, Calendar.JULY, 3)).numberOfCopies(23).build()
        ));
    }

    @Test
    @WithMockUser(value = "ltownes5@google.pl", username = "ltownes5@google.pl", roles = "USER")
    void shouldReturnBooksPageForUser() throws Exception {
        when(libraryService.getBookList(any())).thenReturn(bookPage);

        mockMvc.perform(get("/library/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", bookPage))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("totalItems"))
                .andExpect(model().attributeExists("account"));
    }

    @Test
    @WithMockUser(value = "ltownes5@google.pl", username = "ltownes5@google.pl", roles = "LIBRARIAN")
    void shouldReturnBooksPageForLibrarian() throws Exception {
        when(libraryService.getBookList(any())).thenReturn(bookPage);

        mockMvc.perform(get("/library/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", bookPage))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("totalItems"))
                .andExpect(model().attributeExists("account"));
    }

    @Test
    void shouldReturnBooksPageForAnonymousUser() throws Exception {
        when(libraryService.getBookList(any())).thenReturn(bookPage);

        mockMvc.perform(get("/library/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", bookPage))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("totalItems"))
                .andExpect(model().attributeDoesNotExist("account"));
    }

    @Test
    void shouldReturnBookDetailViewForAnonymousUser() throws Exception {
        var book = Book.builder()
                .ISBN("0000-0000-0000-0001")
                .title("Book 1")
                .language("RU")
                .numberOfPages(50)
                .status(BookStatus.ACTIVE)
                .publishDate(new Date())
                .numberOfCopies(23)
                .author(new Author(1L, "John Doe"))
                .build();
        when(libraryService.getBookById(any(long.class))).thenReturn(Optional.of(book));

        mockMvc.perform(get("/library/books/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("book"))
                .andExpect(model().attributeExists("updatedBook"))
                .andExpect(model().attributeExists("libraryBook"))
                .andExpect(model().attributeExists("statusList"))
                .andExpect(model().attribute("libraryBook", book))
                .andExpect(model().attribute("libraryBook", book))
                .andExpect(model().attributeDoesNotExist("account"));
    }

    @Test
    @WithMockUser(value = "ltownes5@google.pl", username = "ltownes5@google.pl", roles = "LIBRARIAN")
    void shouldReturnBookDetailViewForLibrarian() throws Exception {
        var book = Book.builder()
                .ISBN("0000-0000-0000-0001")
                .title("Book 1")
                .language("RU")
                .numberOfPages(50)
                .status(BookStatus.ACTIVE)
                .publishDate(new Date())
                .numberOfCopies(23)
                .author(new Author(1L, "John Doe"))
                .build();
        when(libraryService.getBookById(any(long.class))).thenReturn(Optional.of(book));

        mockMvc.perform(get("/library/books/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("book"))
                .andExpect(model().attributeExists("updatedBook"))
                .andExpect(model().attributeExists("libraryBook"))
                .andExpect(model().attributeExists("statusList"))
                .andExpect(model().attribute("libraryBook", book))
                .andExpect(model().attribute("libraryBook", book))
                .andExpect(model().attributeExists("account"));
    }

    @Test
    void shouldAbleToSearchBooks() throws Exception {
        when(libraryService.searchBooksByKeyword(any(String.class), any(Pageable.class))).thenReturn(bookPage);

        mockMvc.perform(get("/library/books").param("keyword", "test"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("totalItems", 4L));
    }

    @Test
    @WithMockUser(value = "ltownes5@google.pl", username = "ltownes5@google.pl", roles = "LIBRARIAN")
    void shouldUpdateBook() throws Exception {
        var updatedBook = Book.builder()
                .ISBN("225294039-5")
                .title("Updated Book 1")
                .numberOfCopies(23)
                .numberOfPages(50)
                .language("RU")
                .status(BookStatus.ACTIVE)
                .publishDate(new Date())
                .archiveDate(null)
                .author(new Author(1L, "John Doe"))
                .build();

        when(libraryService.updateBookInfo(any(), any(Book.class))).thenReturn(Optional.of(updatedBook));

        mockMvc.perform(patch("/library/books/1")
                        .flashAttr("updatedBook", updatedBook))
                .andExpect(view().name("redirect:/library/books/1"));
    }

    @Test
    @WithMockUser(value = "ltownes5@google.pl", username = "ltownes5@google.pl", roles = "LIBRARIAN")
    void shouldNotUpdateBookBecauseDoesNotExist() throws Exception {
        var book = Book.builder()
                .ISBN("225294039-5")
                .title("Updated Book 1")
                .numberOfCopies(23)
                .numberOfPages(50)
                .language("RU")
                .status(BookStatus.ACTIVE)
                .publishDate(new Date())
                .archiveDate(null)
                .author(new Author(1L, "John Doe"))
                .build();

        when(libraryService.updateBookInfo(any(), any(Book.class))).thenReturn(Optional.empty());

        mockMvc.perform(patch("/library/books/1")
                        .flashAttr("updatedBook", book))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    void shouldNotAccessUpdateBookControllerForAnonymousUser() throws Exception {
        mockMvc.perform(patch("/library/books/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(value = "ltownes5@google.pl", username = "ltownes5@google.pl", roles = "USER")
    void shouldNotAccessUpdateBookControllerForUser() throws Exception {
        mockMvc.perform(patch("/library/books/1"))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @MethodSource("invalidBooks")
    @WithMockUser(value = "ltownes5@google.pl", username = "ltownes5@google.pl", roles = "LIBRARIAN")
    void shouldNotUpdateBookWithValidationError(Book invalidBook) throws Exception {
        when(libraryService.getBookById(any(long.class))).thenReturn(Optional.of(invalidBook));

        mockMvc.perform(patch("/library/books/1")
                        .flashAttr("updatedBook", invalidBook))
                .andExpect(status().isOk())
                .andExpect(view().name("book"));
    }

    private static Stream<Arguments> invalidBooks() {
        return Stream.of(
                Arguments.of(Book.builder().ISBN("225294039-5").title("").numberOfCopies(23).numberOfPages(50).language("RU").status(BookStatus.ACTIVE).publishDate(new Date()).archiveDate(null).author(new Author(1L, "John Doe")).build()),
                Arguments.of(Book.builder().ISBN("").title("Updated Book 1").numberOfCopies(23).numberOfPages(50).language("RU").status(BookStatus.ACTIVE).publishDate(new Date()).archiveDate(null).author(new Author(1L, "John Doe")).build()),
                Arguments.of(Book.builder().ISBN("225294039-5").title("Updated Book 1").numberOfCopies(0).numberOfPages(50).language("RU").status(BookStatus.ACTIVE).publishDate(new Date()).archiveDate(null).author(new Author(1L, "John Doe")).build()),
                Arguments.of(Book.builder().ISBN("225294039-5").title("Updated Book 1").numberOfCopies(23).numberOfPages(0).language("RU").status(BookStatus.ACTIVE).publishDate(new Date()).archiveDate(null).author(new Author(1L, "John Doe")).build()),
                Arguments.of(Book.builder().ISBN("225294039-5").title("Updated Book 1").numberOfCopies(23).numberOfPages(50).language("USA").status(BookStatus.ACTIVE).publishDate(new Date()).archiveDate(null).author(new Author(1L, "John Doe")).build()),
                Arguments.of(Book.builder().ISBN("225294039-5").title("Updated Book 1").numberOfCopies(1000).numberOfPages(50).language("RU").status(BookStatus.ACTIVE).publishDate(new Date()).archiveDate(null).author(new Author(1L, "John Doe")).build())
        );
    }

    @Test
    @WithMockUser(value = "ltownes5@google.pl", username = "ltownes5@google.pl", roles = "LIBRARIAN")
    void shouldAddNewBook() throws Exception {
        var newBook = Book.builder()
                .ISBN("225294039-5")
                .title("New Book")
                .numberOfCopies(23)
                .numberOfPages(50)
                .language("RU")
                .status(BookStatus.ACTIVE)
                .publishDate(new Date())
                .archiveDate(null)
                .author(new Author(1L, "John Doe"))
                .build();

        when(libraryService.addBook(any(Book.class))).thenReturn(newBook);

        mockMvc.perform(post("/library/books/add")
                        .flashAttr("book", newBook))
                .andExpect(view().name("redirect:/library/books"));
    }

    @Test
    @WithMockUser(value = "ltownes5@google.pl", username = "ltownes5@google.pl", roles = "LIBRARIAN")
    void shouldNotAddInvalidBook() throws Exception {
        var invalidBook = Book.builder()
                .ISBN("225294039-5")
                .title("")
                .numberOfCopies(23)
                .numberOfPages(50)
                .language("RU")
                .status(BookStatus.ACTIVE)
                .publishDate(new Date())
                .archiveDate(null)
                .author(new Author(1L, "John Doe"))
                .build();

        mockMvc.perform(post("/library/books/add")
                        .flashAttr("book", invalidBook))
                .andExpect(status().isOk())
                .andExpect(view().name("bookAdd"));
    }

    @Test
    void shouldNotAccessAddBookControllerForAnonymousUser() throws Exception {
        mockMvc.perform(post("/library/books/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(value = "ltownes5@google.pl", username = "ltownes5@google.pl", roles = "USER")
    void shouldNotAccessAddBookControllerForUser() throws Exception {
        mockMvc.perform(post("/library/books/add"))
                .andExpect(status().isForbidden());
    }
}