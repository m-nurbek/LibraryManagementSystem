package nurbek.librarymanagementsystem.repository;

import nurbek.librarymanagementsystem.entity.AuthorEntity;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<AuthorEntity, Long> {
}