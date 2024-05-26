package nurbek.librarymanagementsystem.repository;

import nurbek.librarymanagementsystem.entity.BookReservationEntity;
import org.springframework.data.repository.CrudRepository;

public interface BookReservationRepository extends CrudRepository<BookReservationEntity, Long> {
}