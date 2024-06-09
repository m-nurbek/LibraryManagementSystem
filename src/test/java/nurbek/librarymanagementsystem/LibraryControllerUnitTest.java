package nurbek.librarymanagementsystem;

import nurbek.librarymanagementsystem.controller.LibraryController;
import nurbek.librarymanagementsystem.dto.Book;
import nurbek.librarymanagementsystem.dto.BookStatus;
import nurbek.librarymanagementsystem.service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LibraryControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryService libraryService;

    private Page<Book> bookPage;
    private PageRequest pageRequestWithSorting;

    private static final int PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 4;
    private static final String SORT_BY = "title";


    @BeforeEach
    void setUp() {
        var bookList = List.of(new Book[]{
                new Book(1L, "0000-0000-0000-0001", "Book 1", "RU", 50, BookStatus.ACTIVE, new Date(), null, 23, null),
                new Book(2L, "0000-0000-0000-0002", "Book 2", "KZ", 100, BookStatus.ACTIVE, new Date(2004, Calendar.JANUARY, 1), null, 23, null),
                new Book(3L, "0000-0000-0000-0003", "Book 3", "EN", 200, BookStatus.ACTIVE, new Date(2004, Calendar.MARCH, 2), null, 23, null),
                new Book(4L, "0000-0000-0000-0004", "Book 4", "EN", 300, BookStatus.ACTIVE, new Date(2004, Calendar.JULY, 3), null, 23, null)
        });
        bookPage = new PageImpl<>(bookList);
        pageRequestWithSorting = PageRequest.of(PAGE_NUMBER, PAGE_SIZE, Sort.by(Sort.Direction.DESC, SORT_BY));
    }

    @Test
    void shouldReturnBooksPageWithSorting() throws Exception {
        when(libraryService.getBookList(pageRequestWithSorting)).thenReturn(bookPage);

        mockMvc.perform(get("/library/books?page=" + PAGE_NUMBER + "&size=" + PAGE_SIZE + "&sort=" + SORT_BY + ",desc"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", bookPage));
    }

    @Test
    void shouldReturnBookDetailView() throws Exception {
        var book = new Book(1L, "0000-0000-0000-0001", "Book 1", "RU", 50, BookStatus.ACTIVE, new Date(), null, 23, null);

        when(libraryService.getBookById(any(long.class))).thenReturn(Optional.of(book));

        mockMvc.perform(get("/library/books/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("book", book));
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
    void shouldUpdateBook() throws Exception {
        Book book = new Book(2L, "0000-0000-0000-0002", "Book 2", "KZ", 100, BookStatus.ACTIVE, new Date(2004, Calendar.JANUARY, 1), null, 23, null);
        when(libraryService.updateBook(any(Long.class), any(Book.class))).thenReturn(Optional.of(book));

        mockMvc.perform(put("/library/books/2")
                        .flashAttr("book", book))
                .andDo(log())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/library/books"));
    }

    @Test
    void shouldNotUpdateBookBecauseDoesNotExist() throws Exception {
        Book book = new Book(2L, "0000-0000-0000-0002", "Book 2", "KZ", 100, BookStatus.ACTIVE, new Date(2004, Calendar.JANUARY, 1), null, 23, null);
        when(libraryService.updateBook(any(Long.class), any(Book.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/library/books/1")
                        .flashAttr("book", book))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }
}