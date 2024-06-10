package nurbek.librarymanagementsystem.service.serviceImpl;

import nurbek.librarymanagementsystem.aop.Loggable;
import nurbek.librarymanagementsystem.dto.Book;
import nurbek.librarymanagementsystem.dto.BookStatus;
import nurbek.librarymanagementsystem.entity.BookEntity;
import nurbek.librarymanagementsystem.repository.BookRepository;
import nurbek.librarymanagementsystem.repository.BookReservationRepository;
import nurbek.librarymanagementsystem.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LibraryServiceImpl implements LibraryService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookReservationRepository reservationRepository;

    @Override
    public Page<Book> getBookList(Pageable pageable) {
        Page<BookEntity> bookEntities = bookRepository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "title"))
        ));
        return bookEntities.map(BookEntity::toDto);
    }

    @Override
    public List<Book> getBooksToDelete() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        Date date = calendar.getTime();

        List<BookEntity> booksToDelete = bookRepository.getBooksByStatusAndArchiveDateBefore(BookStatus.ARCHIVED.name(), date);
        return booksToDelete.stream().map(BookEntity::toDto).toList();
    }

    @Override
    public Page<Book> getBookListByStatus(BookStatus bookStatus, Pageable pageable) {
        Page<BookEntity> bookEntities = bookRepository.getBookEntitiesByStatusIgnoreCase(bookStatus.name(), PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "title"))
        ));
        return bookEntities.map(BookEntity::toDto);
    }

    @Override
    public List<Book> getBookList() {
        List<BookEntity> bookEntities = bookRepository.findAll();
        return bookEntities.stream().map(BookEntity::toDto).toList();
    }

    @Override
    public Optional<Book> getBookById(long id) {
        BookEntity book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            return Optional.of(book.toDto());
        }
        return Optional.empty();
    }

    @Override
    public Page<Book> searchBooksByKeyword(String keyword, Pageable pageable) {
        Page<BookEntity> bookEntities = bookRepository.searchByKeyword(keyword, PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "title"))
        ));
        return bookEntities.map(BookEntity::toDto);
    }

    @Override
    @Loggable
    public Book addBook(Book book) {
        Optional<BookEntity> optionalBookEntity = bookRepository.findByISBN(book.getISBN());
        if (optionalBookEntity.isPresent()) {
            var bookInDb = optionalBookEntity.get();
            int copies = bookInDb.getNumberOfCopies();
            copies += book.getNumberOfCopies();
            bookInDb.setNumberOfCopies(copies);

            bookRepository.save(bookInDb);
            return bookInDb.toDto();
        }
        BookEntity savedBookEntity = bookRepository.save(BookEntity.fromDto(book));
        return savedBookEntity.toDto();
    }

    @Override
    public Optional<Book> updateBookInfo(Long id, Book book) {
        if (bookRepository.existsById(id)) {
            BookEntity bookInDb = bookRepository.getReferenceById(id);
            bookInDb.setISBN(book.getISBN());
            bookInDb.setTitle(book.getTitle());
            bookInDb.setNumberOfCopies(book.getNumberOfCopies());
            bookInDb.setNumberOfPages(book.getNumberOfPages());
            bookInDb.setLanguage(book.getLanguage());
            bookInDb.setPublishDate(book.getPublishDate());
            bookRepository.save(bookInDb);
            return Optional.of(bookInDb.toDto());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Book> updateBookStatus(Long bookId, BookStatus status) {
        if (bookRepository.existsById(bookId)) {
            BookEntity bookInDb = bookRepository.getReferenceById(bookId);
            bookInDb.setStatus(status);
            bookRepository.save(bookInDb);
            return Optional.of(bookInDb.toDto());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Book> archiveBook(long id) {
        if (bookRepository.existsById(id)) {
            BookEntity bookInDb = bookRepository.getReferenceById(id);
            bookInDb.setStatus(BookStatus.ARCHIVED);
            bookInDb.setArchiveDate(new Date());

            bookRepository.save(bookInDb);
            return Optional.of(bookInDb.toDto());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Book> restoreBook(long id) {
        if (bookRepository.existsById(id)) {
            BookEntity bookInDb = bookRepository.getReferenceById(id);
            bookInDb.setStatus(BookStatus.ACTIVE);
            bookInDb.setArchiveDate(null);

            bookRepository.save(bookInDb);
            return Optional.of(bookInDb.toDto());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Book> reserveBook(long bookId) {
        if (bookRepository.existsById(bookId)) {
            BookEntity bookInDb = bookRepository.getReferenceById(bookId);
            bookInDb.setNumberOfCopies(bookInDb.getNumberOfCopies() - 1);
            bookRepository.save(bookInDb);
            return Optional.of(bookInDb.toDto());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Book> returnBook(long bookId) {
        if (bookRepository.existsById(bookId)) {
            BookEntity bookInDb = bookRepository.getReferenceById(bookId);
            bookInDb.setNumberOfCopies(bookInDb.getNumberOfCopies() + 1);
            bookRepository.save(bookInDb);
            return Optional.of(bookInDb.toDto());
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteBook(long id) {
        reservationRepository.deleteByBookId(id);
        bookRepository.deleteById(id);
    }
}