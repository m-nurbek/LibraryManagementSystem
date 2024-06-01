package nurbek.librarymanagementsystem;

import nurbek.librarymanagementsystem.dto.Book;
import nurbek.librarymanagementsystem.dto.BookStatus;
import nurbek.librarymanagementsystem.entity.BookEntity;
import nurbek.librarymanagementsystem.repository.BookRepository;
import nurbek.librarymanagementsystem.service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class LibraryServiceUnitTest {
    @InjectMocks
    private LibraryService libraryService;
    @Mock
    private BookRepository bookRepository;

    private Page<BookEntity> bookPage;
    private PageRequest pageRequestWithSorting;

    @BeforeEach
    void setUp() {
        var bookList = List.of(new BookEntity[]{
                new BookEntity(1L, "0000-0000-0000-0001", "Book 1", "RU", 50, BookStatus.ACTIVE, new Date(), null, 23, null),
                new BookEntity(2L, "0000-0000-0000-0002", "Book 2", "KZ", 100, BookStatus.ACTIVE, new Date(2004, Calendar.JANUARY, 1), null, 23, null),
                new BookEntity(3L, "0000-0000-0000-0003", "Book 3", "EN", 200, BookStatus.ACTIVE, new Date(2004, Calendar.MARCH, 2), null, 23, null),
                new BookEntity(4L, "0000-0000-0000-0004", "Book 4", "EN", 300, BookStatus.ACTIVE, new Date(2004, Calendar.JULY, 3), null, 23, null)
        });
        bookPage = new PageImpl<>(bookList);
        pageRequestWithSorting = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "title"));
    }

    @Test
    void shouldBeAbleToReturnListOfBooks() {
        when(bookRepository.findAll(pageRequestWithSorting)).thenReturn(bookPage);

        Page<Book> actual = libraryService.getBookList(pageRequestWithSorting);
        assertThat(actual).isNotEmpty();
        assertThat(actual.getTotalElements()).isEqualTo(4);

        verify(bookRepository, times(1)).findAll(pageRequestWithSorting);
    }

    @Test
    void shouldNotBeAbleToReturnListOfBooks() {
        when(bookRepository.findAll(pageRequestWithSorting)).thenReturn(new PageImpl<>(List.of(new BookEntity[] {})));

        Page<Book> actual = libraryService.getBookList(pageRequestWithSorting);
        assertThat(actual).isEmpty();
        assertThat(actual.getTotalElements()).isEqualTo(0);

        verify(bookRepository, times(1)).findAll(pageRequestWithSorting);
    }

}