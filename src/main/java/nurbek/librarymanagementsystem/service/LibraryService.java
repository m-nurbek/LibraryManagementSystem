package nurbek.librarymanagementsystem.service;

import nurbek.librarymanagementsystem.dto.Book;
import nurbek.librarymanagementsystem.dto.BookStatus;
import nurbek.librarymanagementsystem.entity.BookEntity;
import nurbek.librarymanagementsystem.repository.BookRepository;
import nurbek.librarymanagementsystem.repository.BookReservationRepository;
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
public class LibraryService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookReservationRepository reservationRepository;

    public Page<Book> getBookList(Pageable pageable) {
        Page<BookEntity> bookEntities = bookRepository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "title"))
        ));
        return bookEntities.map(BookEntity::toDto);
    }

    public List<Book> getBooksToDelete() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        Date date = calendar.getTime();

        List<BookEntity> booksToDelete = bookRepository.getBooksByStatusAndArchiveDateBefore(BookStatus.ARCHIVED.name(), date);
        return booksToDelete.stream().map(BookEntity::toDto).toList();
    }

    public Page<Book> getBookListByStatus(BookStatus bookStatus, Pageable pageable) {
        Page<BookEntity> bookEntities = bookRepository.getBookEntitiesByStatusIgnoreCase(bookStatus.name(), PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "title"))
        ));
        return bookEntities.map(BookEntity::toDto);
    }

    public List<Book> getBookList() {
        List<BookEntity> bookEntities = bookRepository.findAll();
        return bookEntities.stream().map(BookEntity::toDto).toList();
    }

    public Optional<Book> getBookById(long id) {
        BookEntity book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            return Optional.of(book.toDto());
        }
        return Optional.empty();
    }

    public Page<Book> searchBooksByKeyword(String keyword, Pageable pageable) {
        Page<BookEntity> bookEntities = bookRepository.searchByKeyword(keyword, PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "title"))
        ));
        return bookEntities.map(BookEntity::toDto);
    }

    public Optional<Book> addBook(Book book) {
        if (bookRepository.existsById(book.getId())) {
            BookEntity bookInDb = bookRepository.getReferenceById(book.getId());

            int copies = bookInDb.getNumberOfCopies();
            copies += book.getNumberOfCopies();
            bookInDb.setNumberOfCopies(copies);

            bookRepository.save(bookInDb);
            return Optional.of(bookInDb.toDto());
        }
        bookRepository.save(BookEntity.fromDto(book));
        return Optional.of(book);
    }

    /**
     * If the book exists, then returns Book with updated information. Otherwise, returns Optional.empty()
     *
     * @param id   of the book to be updated
     * @param book with updated information
     * @return an optional instance of a book that is updated, otherwise an empty optional instance
     */
    public Optional<Book> updateBook(Long id, Book book) {
        if (bookRepository.existsById(id)) {
            BookEntity bookEntity = BookEntity.fromDto(book);
            bookEntity.setId(id);
            bookRepository.save(bookEntity);
            return Optional.of(bookEntity.toDto());
        }
        return Optional.empty();
    }

    /**
     * If the book exists, Then returns Book with updated information (ISBN, title, numberOfCopies, language)
     * Otherwise, returns Optional.empty()
     *
     * @param book to be updated
     * @return an optional instance of a book that is updated, otherwise an empty optional instance
     */
    public Optional<Book> updateBookInfo(Long id, Book book) {
        if (bookRepository.existsById(id)) {
            BookEntity bookInDb = bookRepository.getReferenceById(id);
            bookInDb.setISBN(book.getISBN());
            bookInDb.setTitle(book.getTitle());
            bookInDb.setNumberOfCopies(book.getNumberOfCopies());
            bookInDb.setLanguage(book.getLanguage());
            bookRepository.save(bookInDb);
            return Optional.of(bookInDb.toDto());
        }
        return Optional.empty();
    }

    public Optional<Book> updateBookStatus(Long bookId, BookStatus status) {
        if (bookRepository.existsById(bookId)) {
            BookEntity bookInDb = bookRepository.getReferenceById(bookId);
            bookInDb.setStatus(status);
            bookRepository.save(bookInDb);
            return Optional.of(bookInDb.toDto());
        }
        return Optional.empty();
    }

    /**
     * Updates the status of the book and sets it to be ARCHIVED
     * If the book exists, returns an optional instance of it.
     * Otherwise, returns an empty optional instance.
     *
     * @param id of the book that is to be archived
     * @return Optional instance of an archived book, otherwise an empty optional instance
     */
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

    public Optional<Book> reserveBook(long bookId) {
        if (bookRepository.existsById(bookId)) {
            BookEntity bookInDb = bookRepository.getReferenceById(bookId);
            bookInDb.setNumberOfCopies(bookInDb.getNumberOfCopies() - 1);
            bookRepository.save(bookInDb);
            return Optional.of(bookInDb.toDto());
        }
        return Optional.empty();
    }

    public Optional<Book> returnBook(long bookId) {
        if (bookRepository.existsById(bookId)) {
            BookEntity bookInDb = bookRepository.getReferenceById(bookId);
            bookInDb.setNumberOfCopies(bookInDb.getNumberOfCopies() + 1);
            bookRepository.save(bookInDb);
            return Optional.of(bookInDb.toDto());
        }
        return Optional.empty();
    }

    @Transactional
    public void deleteBook(long id) {
        reservationRepository.deleteByBookId(id);
        bookRepository.deleteById(id);
    }
}