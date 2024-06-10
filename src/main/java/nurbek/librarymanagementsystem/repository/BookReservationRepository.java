package nurbek.librarymanagementsystem.repository;

import nurbek.librarymanagementsystem.entity.AccountEntity;
import nurbek.librarymanagementsystem.entity.BookEntity;
import nurbek.librarymanagementsystem.entity.BookReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BookReservationRepository extends JpaRepository<BookReservationEntity, Long> {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM BOOK_RESERVATION br WHERE br.book_id = :bookId", nativeQuery = true)
    void deleteByBookId(@Param("bookId") Long bookId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM BOOK_RESERVATION br WHERE br.account_id = :accountId", nativeQuery = true)
    void deleteByAccountId(@Param("accountId") Long accountId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM BOOK_RESERVATION br WHERE br.book_id = :bookId AND br.account_id = :accountId", nativeQuery = true)
    void deleteByBookIdAndAccountId(@Param("bookId") Long bookId, @Param("accountId") Long accountId);

    @Transactional
    @Modifying
    void deleteByBook(BookEntity book);

    @Transactional
    @Modifying
    void deleteByAccount(AccountEntity account);

    @Transactional
    @Modifying
    void deleteByBookAndAccount(BookEntity book, AccountEntity account);

    @Query(value = "SELECT * FROM BOOK_RESERVATION br WHERE br.account_id = :accountId", nativeQuery = true)
    List<BookReservationEntity> findByAccountId(@Param("accountId") Long accountId);

    List<BookReservationEntity> findByBook(BookEntity book);

    @Query(value = "SELECT * FROM BOOK_RESERVATION br WHERE br.status = 'RESERVED' AND br.due_date < :currentDate", nativeQuery = true)
    List<BookReservationEntity> getOverdueBookReservations(@Param("currentDate") Date currentDate);

    @Query(value = "SELECT * FROM BOOK_RESERVATION br WHERE br.book_id = :bookId AND br.account_id = :accountId", nativeQuery = true)
    Optional<BookReservationEntity> findByBookIdAndAccountId(@Param("bookId") Long bookId, @Param("accountId") Long accountId);

    boolean existsByAccount_IdAndBook_Id(Long accountId, Long bookId);
}