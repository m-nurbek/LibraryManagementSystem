package nurbek.librarymanagementsystem;

import nurbek.librarymanagementsystem.entity.AuthorEntity;
import nurbek.librarymanagementsystem.entity.BookEntity;
import nurbek.librarymanagementsystem.service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LibraryControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryService libraryService;

    private Page<BookEntity> bookPage;
    private PageRequest pageRequestWithSorting;

    private static final int PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 4;
    private static final String SORT_BY = "title";


    @BeforeEach
    void setUp() {
        var bookList = List.of(new BookEntity[]{
                new BookEntity(1L, "0001", "Book 1", "RU", "50", BookEntity.BookStatus.ACTIVE, new Date(), new Date(), 23, new AuthorEntity()),
                new BookEntity(2L, "0002", "Book 2", "KZ", "100", BookEntity.BookStatus.ACTIVE, new Date(2004, Calendar.JANUARY, 1), new Date(), 23, new AuthorEntity()),
                new BookEntity(3L, "0003", "Book 3", "EN", "200", BookEntity.BookStatus.ACTIVE, new Date(2004, Calendar.MARCH, 2), new Date(), 23, new AuthorEntity()),
                new BookEntity(4L, "0004", "Book 4", "EN", "300", BookEntity.BookStatus.ACTIVE, new Date(2004, Calendar.JULY, 3), new Date(), 23, new AuthorEntity())
        });
        bookPage = new PageImpl<>(bookList);
        pageRequestWithSorting = PageRequest.of(PAGE_NUMBER, PAGE_SIZE, Sort.by(Sort.Direction.DESC, SORT_BY));
    }

    @Test
    public void shouldReturnBooksPage() throws Exception {
        when(libraryService.getBookList(pageRequestWithSorting)).thenReturn(bookPage);

        mockMvc.perform(get("/library/books?page=" + PAGE_NUMBER + "&size=" + PAGE_SIZE + "&sort=" + SORT_BY + ",desc"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", bookPage));
    }
}