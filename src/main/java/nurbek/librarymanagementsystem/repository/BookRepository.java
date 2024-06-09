package nurbek.librarymanagementsystem.repository;

import nurbek.librarymanagementsystem.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long>, PagingAndSortingRepository<BookEntity, Long> {

    @Query(value = """
            SELECT * FROM BOOK b
                WHERE LOWER(b.TITLE) LIKE LOWER('%' || :keyword || '%') OR b.ISBN LIKE '%' || :keyword || '%' 
            """, nativeQuery = true)
    Page<BookEntity> searchByKeyword(@Param("keyword") String keyword, PageRequest pageRequest);

    @Query(value = """
            SELECT * FROM BOOK b
                WHERE LOWER(b.status) = LOWER(:status)
            """, nativeQuery = true)
    Page<BookEntity> getBookEntitiesByStatusIgnoreCase(@Param("status") String status, PageRequest pageRequest);

    @Query(value = """
            SELECT * FROM BOOK b
                WHERE LOWER(b.status) = LOWER(:status) AND b.ARCHIVE_DATE < :date
            """, nativeQuery = true)
    List<BookEntity> getBooksByStatusAndArchiveDateBefore(@Param("status") String status, @Param("date") Date date);

    boolean existsByISBN(String ISBN);

    Optional<BookEntity> findByISBN(String ISBN);
}