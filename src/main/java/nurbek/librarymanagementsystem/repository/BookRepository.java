package nurbek.librarymanagementsystem.repository;

import nurbek.librarymanagementsystem.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookRepository extends JpaRepository<BookEntity, Long>, PagingAndSortingRepository<BookEntity, Long> {
}