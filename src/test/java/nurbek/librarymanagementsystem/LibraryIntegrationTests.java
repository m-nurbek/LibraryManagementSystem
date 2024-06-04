package nurbek.librarymanagementsystem;

import nurbek.librarymanagementsystem.controller.LibraryController;
import nurbek.librarymanagementsystem.dto.Book;
import nurbek.librarymanagementsystem.dto.BookStatus;
import nurbek.librarymanagementsystem.entity.BookEntity;
import nurbek.librarymanagementsystem.repository.BookRepository;
import nurbek.librarymanagementsystem.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = LibraryManagementSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LibraryIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldUpdateBook() throws Exception {
        Book book = Book.builder()
                .ISBN("108357909-6")
                .title("Updated Book 1")
                .language("PH")
                .numberOfPages(1)
                .publishDate(new Date(2023, 2, 16))
                .numberOfCopies(1)
                .build();

        mockMvc.perform(put("/library/books/1")
                        .flashAttr("book", book))
                .andExpect(status().is3xxRedirection());

        // Verify the book is updated
        Map<String, Object> updatedBook = jdbcTemplate.queryForMap("SELECT * FROM BOOK WHERE ID = 1");
        assertThat(updatedBook.get("TITLE")).isEqualTo("Updated Book 1");
    }

    @Test
    void shouldNotUpdateBook() throws Exception {
        Book book = Book.builder()
                .ISBN("108357909-6")
                .title("Updated Book 1")
                .language("PH")
                .numberOfPages(1)
                .publishDate(new Date(2023, 2, 16))
                .numberOfCopies(1)
                .build();

        // Book id 100 does not exist
        mockMvc.perform(put("/library/books/100")
                        .flashAttr("book", book))
                .andExpect(view().name("error"));

        // Verify the number of books is still 7
        List<Map<String, Object>> books = jdbcTemplate.queryForList("SELECT * FROM BOOK");
        assertThat(books.size()).isEqualTo(10);
    }

    @Test
    void shouldNotUpdateBookWithEmptyTitle() throws Exception {
        Book book = Book.builder()
                .ISBN("108357909-6")
                .title("") // changed value from "Champagne for Caesar" to ""
                .language("EN")  // changed value from "PH" to "EN"
                .numberOfPages(1)
                .publishDate(new Date(2023, 2, 16))
                .numberOfCopies(1)
                .build();

        mockMvc.perform(put("/library/books/1")
                        .flashAttr("book", book))
                .andDo(print())
                .andExpect(view().name("error"));

        // Verify the title is not updated
        Map<String, Object> updatedBook = jdbcTemplate.queryForMap("SELECT * FROM BOOK WHERE ID = 1");
        assertThat(updatedBook.get("TITLE")).isEqualTo("Champagne for Caesar");
        assertThat(updatedBook.get("ISBN")).isEqualTo("108357909-6");
        assertThat(updatedBook.get("LANGUAGE")).isEqualTo("PH");
    }

    @Test
    void logBookTableContents() {
        jdbcTemplate.queryForList("SELECT * FROM BOOK").forEach(row -> System.out.println(row));
    }
}