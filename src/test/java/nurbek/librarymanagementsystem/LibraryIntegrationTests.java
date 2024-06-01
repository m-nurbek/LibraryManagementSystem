package nurbek.librarymanagementsystem;

import nurbek.librarymanagementsystem.controller.LibraryController;
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

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = LibraryManagementSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext
public class LibraryIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private LibraryController libraryController;
    @Autowired
    private LibraryService libraryService;
    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldUpdateBook() throws Exception {
        BookEntity book = new BookEntity(1L, "0001", "Updated Book 1", "RU", 50, BookEntity.BookStatus.ACTIVE, null, null, 23, null);

        mockMvc.perform(put("/library/books/1")
                        .flashAttr("book", book))
                .andExpect(status().is3xxRedirection());

        // Verify the book is updated
        Map<String, Object> updatedBook = jdbcTemplate.queryForMap("SELECT * FROM BOOK WHERE ID = 1");
        assertThat(updatedBook.get("TITLE")).isEqualTo("Updated Book 1");
    }

    @Test
    void shouldNotUpdateBook() throws Exception {
        BookEntity book = new BookEntity("0001", "Updated Book 1", "RU", 50, 23);

        // Book id 10 does not exist
        mockMvc.perform(put("/library/books/10")
                        .flashAttr("book", book))
                .andExpect(view().name("error"));

        // Verify the number of books is still 7
        List<Map<String, Object>> books = jdbcTemplate.queryForList("SELECT * FROM BOOK");
        assertThat(books.size()).isEqualTo(7);
    }

    @Test
    void shouldNotUpdateBookWithEmptyTitle() throws Exception {
        BookEntity book = new BookEntity("0001", "", "RU", 50, 23);

        mockMvc.perform(put("/library/books/1")
                        .flashAttr("book", book))
                .andExpect(view().name("redirect:/library/books"));

        // Verify the title is not updated
        Map<String, Object> updatedBook = jdbcTemplate.queryForMap("SELECT * FROM BOOK WHERE ID = 1");
        assertThat(updatedBook.get("TITLE")).isEqualTo("Book 1");
    }

    @Test
    void logBookTableContents() {
        jdbcTemplate.queryForList("SELECT * FROM BOOK").forEach(row -> System.out.println(row));
    }
}