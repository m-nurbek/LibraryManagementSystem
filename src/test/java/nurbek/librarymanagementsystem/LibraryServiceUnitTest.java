package nurbek.librarymanagementsystem;

import nurbek.librarymanagementsystem.entity.AuthorEntity;
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
                new BookEntity(1L, "0001", "Book 1", "RU", "50", BookEntity.BookStatus.ACTIVE, new Date(), new Date(), 23, new AuthorEntity()),
                new BookEntity(2L, "0002", "Book 2", "KZ", "100", BookEntity.BookStatus.ACTIVE, new Date(2004, Calendar.JANUARY, 1), new Date(), 23, new AuthorEntity()),
                new BookEntity(3L, "0003", "Book 3", "EN", "200", BookEntity.BookStatus.ACTIVE, new Date(2004, Calendar.MARCH, 2), new Date(), 23, new AuthorEntity()),
                new BookEntity(4L, "0004", "Book 4", "EN", "300", BookEntity.BookStatus.ACTIVE, new Date(2004, Calendar.JULY, 3), new Date(), 23, new AuthorEntity())
        });
        bookPage = new PageImpl<>(bookList);
        pageRequestWithSorting = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "title"));
    }

    @Test
    void shouldBeAbleToReturnListOfBooks() throws Exception {
        when(bookRepository.findAll(pageRequestWithSorting)).thenReturn(bookPage);

        Page<BookEntity> actual = libraryService.getBookList(pageRequestWithSorting);
        assertThat(actual).isNotEmpty();
        assertThat(actual.getTotalElements()).isEqualTo(4);
        assertThat(actual).isEqualTo(bookPage);

        verify(bookRepository, times(1)).findAll(pageRequestWithSorting);
    }
}