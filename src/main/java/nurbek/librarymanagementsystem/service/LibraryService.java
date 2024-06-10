package nurbek.librarymanagementsystem.service;

import nurbek.librarymanagementsystem.dto.Book;
import nurbek.librarymanagementsystem.dto.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface LibraryService {
    Page<Book> getBookList(Pageable pageable);

    List<Book> getBooksToDelete();

    Page<Book> getBookListByStatus(BookStatus bookStatus, Pageable pageable);

    List<Book> getBookList();

    Optional<Book> getBookById(long id);

    Page<Book> searchBooksByKeyword(String keyword, Pageable pageable);

    /**
     * If the book exists, then increase the number of copies of this book. Otherwise, save the new book
     *
     * @param book book to be added
     * @return instance of the saved book
     */
    Book addBook(Book book);

    /**
     * If the book exists, Then returns Book with updated information (ISBN, title, numberOfCopies, language)
     * Otherwise, returns Optional.empty()
     *
     * @param book to be updated
     * @return an optional instance of a book that is updated, otherwise an empty optional instance
     */
    Optional<Book> updateBookInfo(Long id, Book book);

    Optional<Book> updateBookStatus(Long bookId, BookStatus status);

    /**
     * Updates the status of the book and sets it to be ARCHIVED
     * If the book exists, returns an optional instance of it.
     * Otherwise, returns an empty optional instance.
     *
     * @param id of the book that is to be archived
     * @return Optional instance of an archived book, otherwise an empty optional instance
     */
    Optional<Book> archiveBook(long id);

    Optional<Book> restoreBook(long id);

    Optional<Book> reserveBook(long bookId);

    Optional<Book> returnBook(long bookId);

    @Transactional
    void deleteBook(long id);
}