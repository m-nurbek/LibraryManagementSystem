package nurbek.librarymanagementsystem.service;

import nurbek.librarymanagementsystem.dto.Book;
import nurbek.librarymanagementsystem.dto.BookStatus;
import nurbek.librarymanagementsystem.entity.BookEntity;
import nurbek.librarymanagementsystem.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {
    @Autowired
    private BookRepository bookRepository;

    public Page<Book> getBookList(Pageable pageable) {
        Page<BookEntity> bookEntities = bookRepository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "title"))
        ));
        return bookEntities.map(BookEntity::toDto);
    }

    /**
     * Returns a list of books corresponding to the book status that is given as a parameter.
     *
     * @param bookStatus
     * @param pageable
     * @return
     */
    public Page<Book> getBookListByStatus(BookStatus bookStatus, Pageable pageable) {
        Page<BookEntity> bookEntities = bookRepository.getBookEntitiesByStatusEqualsIgnoreCase(bookStatus, PageRequest.of(
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

    public Book getBookById(long id) {
        BookEntity book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            return book.toDto();
        }
        return null;
    }

    public Page<Book> searchBooksByKeyword(String keyword, Pageable pageable) {
        Page<BookEntity> bookEntities = bookRepository.searchByKeyword(keyword, PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "title"))
        ));
        return bookEntities.map(BookEntity::toDto);
    }

    public Book addBook(Book book) {
        if (bookRepository.existsById(book.getId())) {
            BookEntity bookInDb = bookRepository.getReferenceById(book.getId());
            int copies = bookInDb.getNumberOfCopies();
            copies += book.getNumberOfCopies();
            bookInDb.setNumberOfCopies(copies);
            bookRepository.save(bookInDb);
        } else {
            bookRepository.save(BookEntity.fromDto(book));
        }
        return book;
    }

    public Optional<Book> updateBook(Long id, Book book) {
        if (bookRepository.existsById(id)) {
            BookEntity bookEntity = BookEntity.fromDto(book);
            bookEntity.setId(id);
            bookRepository.save(bookEntity);
            return Optional.of(book);
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
            return Optional.of(book);
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
     * @return Optional instace of an archived book, otherwise an empty optional instance
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

    public Optional<Book> deleteBook(long id) {
        if (bookRepository.existsById(id)) {
            BookEntity bookInDb = bookRepository.getReferenceById(id);
            bookInDb.setStatus(BookStatus.DELETED);

            bookRepository.save(bookInDb);
            return Optional.of(bookInDb.toDto());
        }
        return Optional.empty();
    }
}