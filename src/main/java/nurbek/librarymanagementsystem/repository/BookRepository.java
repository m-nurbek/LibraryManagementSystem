package nurbek.librarymanagementsystem.repository;

import nurbek.librarymanagementsystem.dto.BookStatus;
import nurbek.librarymanagementsystem.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long>, PagingAndSortingRepository<BookEntity, Long> {

    @Query(value = "SELECT * FROM BOOK b " +
            "WHERE LOWER(b.TITLE) LIKE LOWER('%' || :keyword || '%') OR b.ISBN LIKE '%' || :keyword || '%'", nativeQuery = true)
    Page<BookEntity> searchByKeyword(@Param("keyword") String keyword, PageRequest pageRequest);

    Optional<BookEntity> findByISBN(String ISBN);

    Page<BookEntity> getBookEntitiesByStatusEqualsIgnoreCase(BookStatus status, Pageable pageable);
}