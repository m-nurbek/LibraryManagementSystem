package nurbek.librarymanagementsystem.repository;

import nurbek.librarymanagementsystem.entity.BookEntity;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<BookEntity, Long> {
}