package nurbek.librarymanagementsystem.repository;

import nurbek.librarymanagementsystem.entity.BookEntity;
import nurbek.librarymanagementsystem.entity.BookReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BookReservationRepository extends JpaRepository<BookReservationEntity, Long> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM BOOK_RESERVATION br WHERE br.book_id = :bookId", nativeQuery = true)
    void deleteByBookId(@Param("bookId") Long bookId);

    @Transactional
    @Modifying
    void deleteByBook(BookEntity book);
}